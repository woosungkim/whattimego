package gbssm.miniproject.whattimego;

import gbssm.miniproject.whattimego.ScheduleService.ParserQuery.PathInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class ScheduleService extends Service {
	private static final int SEND_ASYNC_ROUTESEARCH = 0;

	private static final String INTENT_ACTION = "gbssm.miniproject.whattimego";

	// calendar
	Calendar cal;

	// db
	DBManager dbManager;

	LocationManager locationManager;
	MyLocationListener locationListener;

	RouteMessageHandler routeMessageHandler;

	@Override
	public void onCreate() {
		Log.d("Service", "start");
		dbManager = new DBManager(getApplicationContext(), "ScheduleList.db",
				null, DBManager.DB_VERSION);

		// �ڵ鷯 ����
		routeMessageHandler = new RouteMessageHandler();

		// ������ �ð����� GPS Ž��, ��� Ž��, ���, �˶� ����
		getMyLocation();
	}

	@Override
	public void onDestroy() {
		Log.d("Service", "end");

		locationManager.removeUpdates(locationListener);
	}

	private void getMyLocation() {
		Log.d("getMyLocation", "start!!");
		
		if (locationManager == null) {
			locationManager = (LocationManager) this
					.getSystemService(Context.LOCATION_SERVICE);
		}
		// provider ������||GPS �� ���ؼ� �������� �˷��ִ� Stirng ����
		// minTime �ּ��� �󸶸��� �ð��� �帥�� ��ġ������ �������� �ð������� ���� �����ϴ� ����
		// minDistance �󸶸��� �Ÿ��� �������� ��ġ������ �������� �����ϴ� ����
		// manager.requestLocationUpdates(provider, minTime, minDistance,
		// listener);

		long minTime = 60 * 1000; // 1��

		// �Ÿ��� 0���� ����, �׷��� �ð��� �Ÿ� ������ ���� �������� �ʰ� 10�ʵ� �ٽ� ��ġ ������ �޴´�.
		float minDistance = 0;

		Criteria criteria = new Criteria();

		criteria.setAccuracy(Criteria.ACCURACY_FINE);

		criteria.setPowerRequirement(Criteria.POWER_LOW);

		criteria.setAltitudeRequired(true);

		criteria.setBearingRequired(false);

		criteria.setSpeedRequired(false);

		criteria.setCostAllowed(true);

		String provider = locationManager.getBestProvider(criteria, true);

		locationListener = new MyLocationListener();

		if (!locationManager.isProviderEnabled(provider)
				&& locationManager.getLastKnownLocation(provider) != null) {

			locationManager.requestLocationUpdates(	provider, minTime, minDistance, locationListener );
		} else {

			criteria.setAccuracy(Criteria.ACCURACY_COARSE);
			provider = locationManager.getBestProvider(criteria, true);

			locationManager.requestLocationUpdates(	provider, minTime, minDistance, locationListener );
		}
	}

	// ���� �ð����� GPS �޾� ó���ϴ� ��ü
	class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			Log.d("LocaListener", "Location Changed");
			String start_lati = location.getLatitude() + "";
			String start_longi = location.getLongitude() + "";
			String dest_lati;
			String dest_longi;
			Log.d("LocaListener", "" + start_lati + "    " + start_longi);

			Cursor cursor = dbManager.select("SELECT * from SCHEDULE_LIST");

			while (cursor.moveToNext()) {
				/*
				 * Log.d("DBList", cursor.getInt(0) + " " + cursor.getString(1)
				 * + " " + cursor.getString(2) + " " + cursor.getString(3) + " "
				 * + cursor.getString(4) + " " + cursor.getString(5) + " " +
				 * cursor.getString(6) + " " + cursor.getString(7) + " " +
				 * cursor.getString(8) + " " + cursor.getString(9) );
				 */
				if ( cursor.getString(7).equals("off") )
					continue;
				
				// �켱, ��¥ �޾ƿͼ� �ش� ��¥�� ��ӿ��� ó���ϱ�
				cal = Calendar.getInstance();

				int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

				if (cursor.getString(5).contains(dayOfWeek + "")) {
					Log.d("Time Check", cursor.getString(1));

					dest_lati = cursor.getString(8);
					dest_longi = cursor.getString(9);

					// �����ġ -> ���� ��ġ������ ��� ���
					ParserQuery parser = new ParserQuery(start_longi,
							start_lati, dest_longi, dest_lati,
							cursor.getString(4), cursor.getInt(0));

					parser.execute();
				}
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
	}

	// ��� Ž�� Thread�� ���� �� message�� �޾� ó���ϴ� ��ü
	class RouteMessageHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SEND_ASYNC_ROUTESEARCH:
				Log.d("Handler", "catch!!");
				Toast.makeText(getApplicationContext(), "�� �� ���ϰ� �־�!",
						Toast.LENGTH_SHORT).show();

				List<PathInfo> routeList = (List<PathInfo>) msg.obj;
				
				// ��ΰ� ���� ��,
				if ( routeList.size() == 0 )
				{
					Log.d("handler", "��ΰ� ����!!");
					break;
				}
					
				
				// ���� �ð�
				int scheduleTime = Integer.parseInt((msg.getData()
						.getString("time")));

				// �ҿ� �ð�
				int requiredTime = Integer.parseInt(routeList.get(0).time);

				// ���� �ð�
				Date date = new Date(System.currentTimeMillis());
				SimpleDateFormat sdfNow = new SimpleDateFormat("HH");
				String strHour = sdfNow.format(date);
				sdfNow = new SimpleDateFormat("mm");
				String strMinute = sdfNow.format(date);
				int nHour = Integer.parseInt(strHour);
				int nMinute = Integer.parseInt(strMinute);

				int nowTime = nHour * 60 + nMinute;

				Log.d("times", nowTime + "  " + scheduleTime + "   "
						+ requiredTime);

				int firstAlarmTime = scheduleTime - requiredTime - 6;

				int startTime = firstAlarmTime - nowTime;
				Log.d("Start time", startTime + " ");
				Toast.makeText(getApplicationContext(), "ù �˶� �︱ �ð� " + startTime + "�� ��!",
						Toast.LENGTH_SHORT).show();

				// ù��° �˶� �ð����� ���� �ð��� ���� ���� �˶�, ���� �϶��� �˶� ���� ����
				if (startTime > 0 && startTime <= 5) {
					int s_id = msg.getData().getInt("s_id");

					AlarmManager alarmManager = (AlarmManager) getApplicationContext()
							.getSystemService(Context.ALARM_SERVICE);
					
					String pathMessage = "��� �ð� : " + scheduleTime + "\n";
					int nPath = 1;
					for (PathInfo info : routeList) {

						String time = info.time;
						String path = info.path;

						pathMessage += ("��� " + nPath + "\n" + "�ҿ�ð� : " + time + "\n"
								+ path + "\n\n");
						nPath++;
					}

					int interval[] = { 0, 1000 * 60, 1000 * 60 * 2 };
					for (int i = 0; i < 3; i++) {
						Intent intent = new Intent(INTENT_ACTION);
						// �ε��� 
						intent.putExtra("index", i);
						// s_id
						intent.putExtra("s_id", s_id);
						// ����
						intent.putExtra("yoil", Calendar.getInstance().get(Calendar.DAY_OF_WEEK)+"");
						// Ÿ��Ʋ
						if ( i == 0 )
							intent.putExtra( "nth", "��� 1�ð� �� �˸��Դϴ�!!!\n");
						else if ( i == 1 )
							intent.putExtra( "nth", "��� 30�� �� �˸��Դϴ�!!!\n���� ���� �غ� �ϼ���!");
						else if ( i == 2 )
							intent.putExtra( "nth", "��� 10�� �� �˸��Դϴ�!!!\n� ������ �ؿ�!!!!!");
						
						// �޽���
						intent.putExtra("path", pathMessage );
						
						// pendingIntent �� �� ������ s_id�� ���� ��(��û�ڵ�)�� �Ͽ� ȣ���Ѵ�.
						int request_id = s_id * 3 + i;
						PendingIntent pendingIntent = PendingIntent
								.getActivity(getApplicationContext(),
										request_id, intent, 0);
						alarmManager
								.set(AlarmManager.RTC_WAKEUP,
										System.currentTimeMillis()
												+ (startTime * 1000 * 60)
												+ interval[i], pendingIntent);

						Log.d("set Alarm", "" + request_id);
					}
				}
				break;
			default:
				break;
			}
		}
	};

	// �˻�, parsing �����ϴ� class
	class ParserQuery extends AsyncTask<String, String, String> {
		private static final int SEND_ASYNC_ROUTESEARCH = 0;

		private String s_lon;
		private String s_lat;
		private String e_lon;
		private String e_lat;
		private String time;
		private int s_id;

		private String json;

		public ParserQuery(String s_lon, String s_lat, String e_lon,
				String e_lat, String time, int s_id) {
			this.s_lon = s_lon;
			this.s_lat = s_lat;
			this.e_lon = e_lon;
			this.e_lat = e_lat;
			this.time = time;

			this.s_id = s_id;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			// query
			json = QuerySend();

			// parsing
			List<PathInfo> pathList = new ArrayList<PathInfo>();
			pathList = parsing(json);

			// send message
			Message msg = routeMessageHandler.obtainMessage();

			msg.what = SEND_ASYNC_ROUTESEARCH;
			msg.obj = pathList;
			Bundle data = new Bundle();
			data.putString("time", time);
			data.putInt("s_id", s_id);
			msg.setData(data);

			routeMessageHandler.sendMessage(msg);

			return null;
		}

		public String QuerySend() {
			String getjson = "";
			try {
				URL url = new URL(
						"http://map.naver.com/findroute2/searchPubtransPath.nhn?apiVersion=3&searchType=0"
								+ "&start="
								+ s_lon
								+ "%2C"
								+ s_lat
								+ "%2C%EB"
								+ "&destination="
								+ e_lon
								+ "%2C"
								+ e_lat
								+ "%2C%EB");

				// URL url = new
				// URL("http://map.naver.com/findroute2/searchPubtransPath.nhn?apiVersion=3&searchType=0&start=126.9979637%2C37.5742960%2C%EB%B3%B4%EB%A0%B9%EB%B9%8C%EB%94%A9&destination=127.0436500%2C37.2799920%2C%EC%95%84%EC%A3%BC%EB%8C%80%ED%95%99%EA%B5%90");

				// open connection
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setDoInput(true); // �Է½�Ʈ�� ��뿩��
				conn.setDoOutput(true); // ��½�Ʈ�� ��뿩��
				conn.setUseCaches(false); // ĳ�û�� ����
				conn.setReadTimeout(20000); // Ÿ�Ӿƿ� ���� ms����
				conn.setRequestMethod("GET"); // or GET

				// Response�ޱ�
				StringBuffer sb = new StringBuffer();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "UTF-8"));
				for (;;) {
					String line = br.readLine();
					if (line == null)
						break;
					sb.append(line + "\n");
				}
				br.close();
				conn.disconnect();
				getjson = sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return getjson;
		}

		class PathInfo {
			private String time;
			private String path;

			PathInfo(String time, String path) {
				this.time = time;
				this.path = path;
			}

			String getTime() {
				return this.time;
			}

			String getPath() {
				return this.path;
			}
		};

		public List<PathInfo> parsing(String json) {
			List<PathInfo> list = new ArrayList<PathInfo>();

			if (json == null)
				return list;

			try {
				// JSONParser jsonParser = new JSONParser();
				JSONObject JObject = new JSONObject(json);
				JSONObject result = JObject.getJSONObject("result");
				JSONArray path = result.getJSONArray("path");
				for (int i = 0; i < path.length() && i < 3; i++) {
					JSONObject path_i = path.getJSONObject(i);

					// total time
					JSONObject info = path_i.getJSONObject("info");
					String totalTime = info.getString("totalTime");

					// path
					JSONArray subPath = path_i.getJSONArray("subPath");
					String _path = "";
					boolean flag = true;
					JSONObject last = new JSONObject();
					for (int j = 0; j < subPath.length(); j++) {
						JSONObject subPath_j = subPath.getJSONObject(j);
						String trafficType = subPath_j.getString("trafficType");
						// System.out.println(trafficType);
						if (trafficType.equals("1")) {
							// subway
							flag = true;
							last = subPath_j;
							JSONObject lane = subPath_j.getJSONObject("lane");
							_path += (subPath_j.getString("startName") + "��("
									+ lane.getString("name") + ")" + " -> ");
						} else if (trafficType.equals("2")) {
							// bus
							flag = false;
							last = subPath_j;
							JSONArray lane = subPath_j.getJSONArray("lane");
							JSONObject lane_k = lane.getJSONObject(0);
							_path += (subPath_j.getString("startName") + "("
									+ lane_k.getString("busNo") + "��)" + " -> ");
						}
					}
					// last -> print destination
					if (flag) {
						// subway
						_path += last.getString("endName") + "������ ����";
					} else {
						// bus
						_path += last.getString("endName") + "�����忡�� ����";
					}

					// add to list
					list.add(new PathInfo(totalTime, _path));

				}
			} catch (Exception e) {
				try {
					JSONObject JObject = new JSONObject(json);
					JSONObject error = JObject.getJSONObject("error");
					String totalTime = "0";
					String _path = error.getString("msg");
					list.add(new PathInfo(totalTime, _path));
				} catch (Exception e2) {
					e.printStackTrace();
				}
			}
			return list;
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return m_Binder;
	}

	private final IBinder m_Binder = new Binder() {
		@Override
		protected boolean onTransact(int code, Parcel data, Parcel reply,
				int flags) throws RemoteException {
			return super.onTransact(code, data, reply, flags);
		}
	};

}
