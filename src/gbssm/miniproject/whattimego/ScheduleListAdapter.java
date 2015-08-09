package gbssm.miniproject.whattimego;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleListAdapter extends BaseAdapter {

	private ArrayList<ScheduleListItem> scheduleList;

	// 생성자
	public ScheduleListAdapter() {
		scheduleList = new ArrayList<ScheduleListItem>();
	}

	// 현재 아이템 수 리턴
	public int getCount() {
		return scheduleList.size();
	}

	// pos 아이템 리턴
	public Object getItem(int position) {
		return scheduleList.get(position);
	}

	// pos 리턴
	public long getItemId(int position) {
		return position;
	}

	// 출력 될 아이템 관리
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		final Context context = parent.getContext();

		TextView scheduleName = null;
		TextView dayOfWeek = null;
		TextView location = null;
		TextView time = null;

		Button btnOnOff = null;

		ScheduleListHolder holder = null;

		// 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 convertView가 null인 상태로 들어 옴
		if (convertView == null) {
			// view가 null일 경우 커스텀 레이아웃을 얻어 옴
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.schedulelistitem, parent,
					false);

			// 뷰 가져오기
			scheduleName = (TextView) convertView
					.findViewById(R.id.txtScheduleName);
			dayOfWeek = (TextView) convertView.findViewById(R.id.txtDayOfWeek);
			location = (TextView) convertView.findViewById(R.id.txtLocation);
			time = (TextView) convertView.findViewById(R.id.txtTime);

			btnOnOff = (Button) convertView.findViewById(R.id.btnMode);

			// 홀더 생성 및 tag로 등록
			holder = new ScheduleListHolder();
			holder.m_ScheduleName = scheduleName;
			holder.m_DayOfWeek = dayOfWeek;
			holder.m_Location = location;
			holder.m_Time = time;
			
			holder.m_Btn = btnOnOff;
			
			convertView.setTag(holder);
		} else {
			holder = (ScheduleListHolder) convertView.getTag();
			scheduleName = holder.m_ScheduleName;
			dayOfWeek = holder.m_DayOfWeek;
			location = holder.m_Location;
			time = holder.m_Time;

			btnOnOff = holder.m_Btn;
		}

		scheduleName.setText(scheduleList.get(position).getScheduleName());
		dayOfWeek.setText(scheduleList.get(position).getDayOfWeek());
		location.setText(scheduleList.get(position).getLocation());
		time.setText(scheduleList.get(position).getTime());
		
		if ( scheduleList.get(position).getState().equals("ON") )
			btnOnOff.setText( "ON" );
		else
			btnOnOff.setText( "OFF" );

		// 버튼 터치 이벤트
		btnOnOff.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 터치 시 아이템 이름 출력
				Toast.makeText(context,
						scheduleList.get(pos).getScheduleName(),
						Toast.LENGTH_SHORT).show();
			}
		});

		// 리스트 아이템 터치 시 이벤트
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(context, "리스트 클릭 : " + scheduleList.get(pos),
						Toast.LENGTH_SHORT).show();
			}

		});

		return convertView;
	}

	// 외부에서 아이템 추가 요청 시 사용
	public void add(ScheduleListItem item) {
		scheduleList.add(item);
	}

	// 외부에서 아이템 삭제 요청 시 사용
	public void remove(int position) {
		scheduleList.remove(position);
	}

	// 각 리스트 아이템
	static class ScheduleListItem {
		private String strScheduleName;
		private String strDayOfWeek;
		private String strLocation;
		private String strTime;

		private String strState;

		public ScheduleListItem(String sn, String dow, String l, String t,
				String s) {
			strScheduleName = sn;
			strDayOfWeek = dow;
			strLocation = l;
			strTime = t;

			strState = s;
		}

		public String getScheduleName() {
			return strScheduleName;
		}

		public void setScheduleName(String sn) {
			strScheduleName = sn;
		}

		public String getDayOfWeek() {
			return strDayOfWeek;
		}

		public void setDayOfWeek(String dow) {
			strDayOfWeek = dow;
		}

		public String getLocation() {
			return strLocation;
		}

		public void setLocation(String l) {
			strLocation = l;
		}

		public String getTime() {
			return strTime;
		}

		public void setTime(String t) {
			strTime = t;
		}

		public String getState() {
			return strState;
		}

		public void setState(String s) {
			strState = s;
		}

	}

	private class ScheduleListHolder {
		TextView m_ScheduleName;
		TextView m_DayOfWeek;
		TextView m_Location;
		TextView m_Time;
		
		Button m_Btn;
	}

}
