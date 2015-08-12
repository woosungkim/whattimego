package gbssm.miniproject.whattimego;

import gbssm.miniproject.whattimego.ResultListAdapter.ResultListItem;

import java.util.List;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPoint.GeoCoordinate;
import net.daum.mf.map.api.MapView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DestSearchActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private final String API_KEY = "a0c9f3a91b128f53541ab1367cec8e36";

	private static final int SEND_THREAD_SEARCH = 0;

	private SendMessageHandler m_MainHandler = null;

	private MapView mapView; // mapview�� ����Ʈview Ŭ���� ���� ���� �� �� �����Ƿ� public

	// �˻���, �˻�
	private EditText editDestWord;
	private Button btnSearch;

	// ���� ���
	private TextView txtSelectedLoc;

	// ����Ʈ
	private ListView resultListView;
	private ResultListAdapter resultAdapter;

	// ���� �Ϸ�
	private ImageView btnSelect;

	private String location = "";
	private String address = "";
	private String lati = "";
	private String longi = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_destsearch);

		// ���̾ƿ�
		mapView = new MapView(this);
		mapView.setDaumMapApiKey(API_KEY);

		ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
		mapViewContainer.addView(mapView);

		editDestWord = (EditText) findViewById(R.id.editDestWord);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(this);

		txtSelectedLoc = (TextView) findViewById(R.id.txtSelectedLocation);

		resultListView = (ListView) findViewById(R.id.resultlistview);
		resultListView.setOnItemClickListener(this);

		btnSelect = (ImageView) findViewById(R.id.btnSelect);
		btnSelect.setOnClickListener(this);

		// ���� �ڵ鷯 ����
		m_MainHandler = new SendMessageHandler();

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSearch:

			searchLocation();

			break;

		case R.id.btnSelect:

			Intent intent = getIntent();

			intent.putExtra("locationName", location);
			intent.putExtra("address", address);
			intent.putExtra("latitude", lati);
			intent.putExtra("longitude", longi);

			setResult(RESULT_OK, intent);

			finish();

			break;
		}

	}

	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Log.d("position", "pos = " + position);

		// ��ġ �̵�
		mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(
				((ResultListItem) resultAdapter.getItem(position))
						.getLatitude(), ((ResultListItem) resultAdapter
						.getItem(position)).getLongitude()), true);
		// ����
		mapView.selectPOIItem(mapView.findPOIItemByTag(position), true);

		// ���� �� ��� �ؽ�Ʈ ��ü
		txtSelectedLoc.setText(((ResultListItem) resultAdapter
				.getItem(position)).getLocationName()
				+ " ("
				+ ((ResultListItem) resultAdapter.getItem(position))
						.getAddress() + ")");
		// ���� �� ��ҷ� ���� ��ü
		lati = ((ResultListItem) resultAdapter.getItem(position)).getLatitude()
				+ "";
		longi = ((ResultListItem) resultAdapter.getItem(position))
				.getLongitude() + "";
		location = ((ResultListItem) resultAdapter.getItem(position))
				.getLocationName();
		address = ((ResultListItem) resultAdapter.getItem(position))
				.getAddress();
	}

	class SendMessageHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case SEND_THREAD_SEARCH:
				// ui �۾�
				resultAdapter = new ResultListAdapter(mapView);

				resultListView.setAdapter(resultAdapter);
				resultListView.setSelector(R.drawable.listselector);

				List<MapItem> resultList = (List<MapItem>) msg.obj;

				mapView.setMapCenterPoint(
						MapPoint.mapPointWithGeoCoord(
								resultList.get(0).latitude,
								resultList.get(0).longitude), true);

				int nTag = 0;
				if (resultList.size() == 0) {
					Toast.makeText(getApplicationContext(), "�˻� ����� �����ϴ�.",
							Toast.LENGTH_SHORT).show();
				} else {
					for (MapItem item : resultList) { 
						/*
						 * Log.d("item", item.title + "  " + item.imageUrl +
						 * "  " + item.address + "  " + item.newAddress + "  " +
						 * item.zipcode + "  " + item.phone + "  " +
						 * item.longitude + "  " + item.latitude + "  " +
						 * item.distance + "  " + item.category + "  " + item.id
						 * + "  " + item.placeUrl + "  " + item.direction + "  "
						 * + item.addressBCode + "  ");
						 */

						// ����Ʈ�� �߰�
						resultAdapter.add(new ResultListItem(item.latitude,
								item.longitude, item.title, item.address,
								item.newAddress));

						// map�� ��Ŀ �߰�
						MapPOIItem marker = new MapPOIItem();
						marker.setItemName(item.title);
						marker.setTag(nTag);
						nTag = nTag + 1;
						marker.setMapPoint(MapPoint.mapPointWithGeoCoord(
								item.latitude, item.longitude));
						marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
						marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

						mapView.addPOIItem(marker);
					}
					// mapView.selectPOIItem(mapView.findPOIItemByTag(0), true);

					break;
				}

			}
		}

	}

	private void searchLocation() {

		String query = editDestWord.getText().toString();

		if (query == null || query.length() == 0) {
			Toast.makeText(getApplicationContext(), "�˻�� �Է��ϼ���.",
					Toast.LENGTH_SHORT).show();
			return;
		}
		// hide keyboard
		InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mInputMethodManager.hideSoftInputFromWindow(
				editDestWord.getWindowToken(), 0);

		GeoCoordinate geoCoordinate = mapView.getMapCenterPoint()
				.getMapPointGeoCoord();

		double latitude = geoCoordinate.latitude; // ����
		double longitude = geoCoordinate.longitude; // �浵
		Log.d("lati longi", latitude + "   " + longitude);

		int radius = 10000; // �߽� ��ǥ������ �ݰ�Ÿ�, Ư�� ������ �߽����� �˻� �Ϸ��� �� ��� // ���,
							// m����, (0~10000)
		int page = 1; // ������ ��ȣ ( 1~ 3 ), �� �������� 15��

		String apikey = API_KEY;

		Searcher searcher = new Searcher();
		searcher.searchKeyword(getApplicationContext(), query, latitude,
				longitude, radius, page, apikey, new OnFinishSearchListener() {

					@Override
					public void onSuccess(List<MapItem> itemList) {
						mapView.removeAllPOIItems(); // ���� �˻� ��� ����

						Log.d("search", "onSuccess");

						// �޽��� ������
						Message msg = m_MainHandler.obtainMessage();

						// �޽��� id ����
						msg.what = SEND_THREAD_SEARCH;

						// �޽��� ���� ����
						msg.obj = itemList;

						// ���̽� ������
						m_MainHandler.sendMessage(msg);

					}

					@Override
					public void onFail() {
						Toast.makeText(getApplicationContext(),
								"API_KEY�� ���� Ʈ������ �ʰ��Ǿ����ϴ�.", Toast.LENGTH_SHORT)
								.show();
					}

				});

	}

	// ��Ŀ �߰�
	/*
	 * MapPOIItem marker = new MapPOIItem();
	 * marker.setItemName("Default Marker"); marker.setTag(0);
	 * marker.setMapPoint();
	 * marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // �⺻���� �����ϴ� BluePin
	 * ��Ŀ ���. marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // ��Ŀ��
	 * Ŭ��������, �⺻���� �����ϴ� RedPin ��Ŀ ���.
	 * 
	 * mapView.addPOIItem(marker);
	 */

}
