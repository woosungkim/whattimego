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

	private MapView mapView; // mapview는 리스트view 클릭에 따라 변경 될 수 있으므로 public

	// 검색어, 검색
	private EditText editDestWord;
	private Button btnSearch;

	// 선택 장소
	private TextView txtSelectedLoc;

	// 리스트
	private ListView resultListView;
	private ResultListAdapter resultAdapter;

	// 선택 완료
	private ImageView btnSelect;

	private String location = "";
	private String address = "";
	private String lati = "";
	private String longi = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_destsearch);

		// 레이아웃
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

		// 메인 핸들러 생성
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

		// 위치 이동
		mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(
				((ResultListItem) resultAdapter.getItem(position))
						.getLatitude(), ((ResultListItem) resultAdapter
						.getItem(position)).getLongitude()), true);
		// 선택
		mapView.selectPOIItem(mapView.findPOIItemByTag(position), true);

		// 선택 된 장소 텍스트 교체
		txtSelectedLoc.setText(((ResultListItem) resultAdapter
				.getItem(position)).getLocationName()
				+ " ("
				+ ((ResultListItem) resultAdapter.getItem(position))
						.getAddress() + ")");
		// 선택 된 장소로 변수 교체
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
				// ui 작업
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
					Toast.makeText(getApplicationContext(), "검색 결과가 없습니다.",
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

						// 리스트에 추가
						resultAdapter.add(new ResultListItem(item.latitude,
								item.longitude, item.title, item.address,
								item.newAddress));

						// map에 마커 추가
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
			Toast.makeText(getApplicationContext(), "검색어를 입력하세요.",
					Toast.LENGTH_SHORT).show();
			return;
		}
		// hide keyboard
		InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mInputMethodManager.hideSoftInputFromWindow(
				editDestWord.getWindowToken(), 0);

		GeoCoordinate geoCoordinate = mapView.getMapCenterPoint()
				.getMapPointGeoCoord();

		double latitude = geoCoordinate.latitude; // 위도
		double longitude = geoCoordinate.longitude; // 경도
		Log.d("lati longi", latitude + "   " + longitude);

		int radius = 10000; // 중심 좌표부터의 반경거리, 특정 지역을 중심으로 검색 하려고 할 경우 // 사용,
							// m단위, (0~10000)
		int page = 1; // 페이지 번호 ( 1~ 3 ), 한 페이지에 15개

		String apikey = API_KEY;

		Searcher searcher = new Searcher();
		searcher.searchKeyword(getApplicationContext(), query, latitude,
				longitude, radius, page, apikey, new OnFinishSearchListener() {

					@Override
					public void onSuccess(List<MapItem> itemList) {
						mapView.removeAllPOIItems(); // 기존 검색 결과 삭제

						Log.d("search", "onSuccess");

						// 메시지 얻어오기
						Message msg = m_MainHandler.obtainMessage();

						// 메시지 id 설정
						msg.what = SEND_THREAD_SEARCH;

						// 메시지 정보 생성
						msg.obj = itemList;

						// 메이시 보내기
						m_MainHandler.sendMessage(msg);

					}

					@Override
					public void onFail() {
						Toast.makeText(getApplicationContext(),
								"API_KEY의 제한 트래픽이 초과되었습니다.", Toast.LENGTH_SHORT)
								.show();
					}

				});

	}

	// 마커 추가
	/*
	 * MapPOIItem marker = new MapPOIItem();
	 * marker.setItemName("Default Marker"); marker.setTag(0);
	 * marker.setMapPoint();
	 * marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin
	 * 마커 모양. marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를
	 * 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
	 * 
	 * mapView.addPOIItem(marker);
	 */

}
