package gbssm.miniproject.whattimego;

import java.util.ArrayList;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ResultListAdapter extends BaseAdapter {

	private ArrayList<ResultListItem> resultList;
	public MapView mapView;
	
	int befoPos = 0;
	int nowPos = 0;

	// ������
	public ResultListAdapter() {
		resultList = new ArrayList<ResultListItem>();
	}
	public ResultListAdapter(MapView map) {
		resultList = new ArrayList<ResultListItem>();
		mapView = map;
	}

	// ���� ������ �� ����
	public int getCount() {
		return resultList.size();
	}

	// pos ������ ����
	public Object getItem(int position) {
		return resultList.get(position);
	}

	// pos ����
	public long getItemId(int position) {
		return position;
	}

	// ��� �� ������ ����
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		final Context context = parent.getContext();
		
		TextView locationName = null;
		TextView address = null;
		TextView newAddress = null;

		ResultListHolder holder = null;

		// ����Ʈ�� ������鼭 ���� ȭ�鿡 ������ �ʴ� �������� convertView�� null�� ���·� ��� ��
		if (convertView == null) {
			// view�� null�� ��� Ŀ���� ���̾ƿ��� ��� ��
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.resultlistitem, parent,
					false);

			// �� ��������
			locationName = (TextView) convertView.findViewById( R.id.txtLocationName );
			address = (TextView) convertView.findViewById( R.id.txtLocationAddress );
			newAddress = (TextView) convertView.findViewById( R.id.txtLocationNewAddress );

			// Ȧ�� ���� �� tag�� ���
			holder = new ResultListHolder();
			
			holder.m_LocationName = locationName;
			holder.m_Address = address;
			holder.m_NewAddress = newAddress;
		
			convertView.setTag(holder);
		} else {
			holder = (ResultListHolder) convertView.getTag();
			
			locationName = holder.m_LocationName;
			address = holder.m_Address;
			newAddress = holder.m_NewAddress;

		}
		
		locationName.setText(resultList.get(position).getLocationName());
		address.setText(resultList.get(position).getAddress());
		newAddress.setText( resultList.get(position).getNewAddress());

		return convertView;
	}

	// �ܺο��� ������ �߰� ��û �� ���
	public void add ( ResultListItem item ) {
		resultList.add(item);
	}

	// �ܺο��� ������ ���� ��û �� ���
	public void remove(int position) {
		resultList.remove(position);
	}

	// �� ����Ʈ ������
	static class ResultListItem {
		private double latitude;
		private double longitude;
		
		private String strLocationName;
		private String strAddress;
		private String strNewAddress;

		public ResultListItem( double lati, double longi, String ln, String addr, String naddr ) {

			latitude = lati;
			longitude = longi;
			
			strLocationName = ln;
			strAddress = addr;
			strNewAddress = naddr;
		}
		
		public double getLatitude() {
			return latitude;
		}
		public void setLatitude( double lati ) {
			latitude = lati;
		}
		
		public double getLongitude () {
			return longitude;
		}
		public void setLongitude( double longi ) {
			longitude = longi;
		}

		public String getLocationName() {
			return strLocationName;
		}

		public void setLocationName(String ln) {
			strLocationName = ln;
		}

		public String getAddress() {
			return strAddress;
		}

		public void setAddress(String addr) {
			strAddress = addr;
		}
		
		public String getNewAddress() {
			return strNewAddress;
		}

		public void setNewAddress(String naddr) {
			strNewAddress = naddr;
		}

	}

	private class ResultListHolder {
		
		TextView m_LocationName;
		TextView m_Address;
		TextView m_NewAddress;
	}

}
