package gbssm.miniproject.whattimego;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleListAdapter extends BaseAdapter {
	
	private DBManager dbManager;
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
		
		ImageView btnDelete = null;

		TextView scheduleName = null;
		TextView location = null;
		TextView time = null;

		RadioButton btnOff = null;
		RadioButton btnVib = null;
		RadioButton btnSound = null;
		
		CheckBox checkRepeat = null;
		CheckBox checkSun = null;
		CheckBox checkMon = null;
		CheckBox checkTues = null;
		CheckBox checkWedn = null;
		CheckBox checkThur = null;
		CheckBox checkFri = null;
		CheckBox checkSat = null;
		

		ScheduleListHolder holder = null;
		
		dbManager = new DBManager( context, "ScheduleList.db", null, DBManager.DB_VERSION );

		// 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 convertView가 null인 상태로 들어 옴
		if (convertView == null) {
			// view가 null일 경우 커스텀 레이아웃을 얻어 옴
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.schedulelistitem, parent,
					false);

			// 뷰 가져오기
			btnDelete = (ImageView) convertView.findViewById( R.id.btnDelete );
			
			scheduleName = (TextView) convertView
					.findViewById(R.id.txtScheduleName);
			location = (TextView) convertView.findViewById(R.id.txtLocation);
			time = (TextView) convertView.findViewById(R.id.txtTime);

			btnOff = (RadioButton) convertView.findViewById( R.id.radioOff );
			btnVib = (RadioButton) convertView.findViewById( R.id.radioVib );
			btnSound = (RadioButton) convertView.findViewById( R.id.radioSound );

			checkRepeat = (CheckBox) convertView.findViewById ( R.id.checkRepeat );
			checkSun = (CheckBox) convertView.findViewById(R.id.checkSun);
			checkMon = (CheckBox) convertView.findViewById(R.id.checkMon);
			checkTues = (CheckBox) convertView.findViewById(R.id.checkTues);
			checkWedn = (CheckBox) convertView.findViewById(R.id.checkWedn);
			checkThur = (CheckBox) convertView.findViewById(R.id.checkThur);
			checkFri = (CheckBox) convertView.findViewById(R.id.checkFri);
			checkSat = (CheckBox) convertView.findViewById(R.id.checkSat);
			
			
			// 홀더 생성 및 tag로 등록
			holder = new ScheduleListHolder();
			
			holder.m_BtnDelete = btnDelete;
			
			holder.m_ScheduleName = scheduleName;
			holder.m_Location = location;
			holder.m_Time = time;
			
			holder.m_BtnOff = btnOff;
			holder.m_BtnVib = btnVib;
			holder.m_BtnSound = btnSound;
			
			holder.m_checkRepeat = checkRepeat;
			holder.m_checkSun = checkSun;
			holder.m_checkMon = checkMon;
			holder.m_checkTues = checkTues;
			holder.m_checkWedn = checkWedn;
			holder.m_checkThur = checkThur;
			holder.m_checkFri = checkFri;
			holder.m_checkSat = checkSat;
			
			convertView.setTag(holder);
		} else {
			holder = (ScheduleListHolder) convertView.getTag();
			btnDelete = holder.m_BtnDelete;
			
			scheduleName = holder.m_ScheduleName;
			location = holder.m_Location;
			time = holder.m_Time;

			btnOff = holder.m_BtnOff;
			btnVib = holder.m_BtnVib;
			btnSound = holder.m_BtnSound;
			
			checkRepeat = holder.m_checkRepeat;
			checkSun = holder.m_checkSun;
			checkMon = holder.m_checkMon;
			checkTues = holder.m_checkTues;
			checkWedn = holder.m_checkWedn;
			checkThur = holder.m_checkThur;
			checkFri = holder.m_checkFri;
			checkSat = holder.m_checkSat;
			
		}
		
		if ( scheduleList.get(position).getState().equals("sound") )
			btnSound.setChecked(true);
		else if ( scheduleList.get(position).getState().equals("vib") )
			btnVib.setChecked(true);
		else
			btnOff.setChecked(true);
		
		if ( scheduleList.get(position).getRepeat().equals("yes"))
			checkRepeat.setChecked( true );
		
		if (scheduleList.get(position).getDayOfWeek().contains("1"))
			checkSun.setChecked(true);
		if (scheduleList.get(position).getDayOfWeek().contains("2"))
			checkMon.setChecked(true);
		if (scheduleList.get(position).getDayOfWeek().contains("3"))
			checkTues.setChecked(true);
		if (scheduleList.get(position).getDayOfWeek().contains("4"))
			checkWedn.setChecked(true);
		if (scheduleList.get(position).getDayOfWeek().contains("5"))
			checkThur.setChecked(true);
		if (scheduleList.get(position).getDayOfWeek().contains("6"))
			checkFri.setChecked(true);
		if (scheduleList.get(position).getDayOfWeek().contains("7"))
			checkSat.setChecked(true);

		
		scheduleName.setText(scheduleList.get(position).getScheduleName());
		location.setText(scheduleList.get(position).getLocation());
		time.setText(scheduleList.get(position).getTime());
		
		

		// delete 버튼 터치 이벤트
		btnDelete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				// 해당 일정 DB에서 삭제
				dbManager.delete( "DELETE FROM SCHEDULE_LIST WHERE s_id = " + ((ScheduleListItem)getItem(pos)).getsid() + ";");
				// 리스트뷰에서 삭제
				scheduleList.remove( pos );				
				
				notifyDataSetChanged();
				
				Toast.makeText(context,
						"일정이 삭제되었습니다",
						Toast.LENGTH_SHORT).show();
			}
		});
		
		
		checkRepeat.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int s_id = scheduleList.get(pos).getsid();
				 
				 if ( scheduleList.get(pos).getRepeat().equals("yes") )
				 {
					 scheduleList.get(pos).setRepeat("no");
					 dbManager.update( "UPDATE SCHEDULE_LIST SET repeat = '" + "no" + "' WHERE s_id = " + s_id + ";");
				 }
				 else
				 {
					 scheduleList.get(pos).setRepeat("yes");
					 dbManager.update( "UPDATE SCHEDULE_LIST SET repeat = '" + "yes" + "' WHERE s_id = " + s_id + ";");
				 }
			}
		});
		
		btnOff.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int s_id = scheduleList.get(pos).getsid();
				
				scheduleList.get(pos).setState("off");
				dbManager.update( "UPDATE SCHEDULE_LIST SET onoff = '" + "off" +"' " + "WHERE s_id = " + s_id + ";");
			}
		});
		btnVib.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int s_id = scheduleList.get(pos).getsid();
				
				scheduleList.get(pos).setState("vib");
				dbManager.update( "UPDATE SCHEDULE_LIST SET onoff = '" + "vib" +"' " + "WHERE s_id = " + s_id + ";");
			}
		});
		btnSound.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int s_id = scheduleList.get(pos).getsid();
				
				scheduleList.get(pos).setState("sound");
				dbManager.update( "UPDATE SCHEDULE_LIST SET onoff = '" + "sound" +"' " + "WHERE s_id = " + s_id + ";");
			}
		});
		
		
		// 요일 클릭 이벤트
		checkSun.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				String yoil = scheduleList.get(pos).getDayOfWeek();
				int s_id = scheduleList.get(pos).getsid();
				if (yoil.contains("1"))
				{
					yoil = yoil.replace("1", "");
					scheduleList.get(pos).setDayOfWeek(yoil);

					dbManager.update( "UPDATE SCHEDULE_LIST SET s_day = '" + yoil +"' " + "WHERE s_id = " + s_id + ";");
				}
				else
				{
					yoil += "1";
					scheduleList.get(pos).setDayOfWeek(yoil);

					dbManager.update( "UPDATE SCHEDULE_LIST SET s_day = '" + yoil +"' " + "WHERE s_id = " + s_id + ";");
				}
			}
		});
		checkMon.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				String yoil = scheduleList.get(pos).getDayOfWeek();
				int s_id = scheduleList.get(pos).getsid();
				if (yoil.contains("2"))
				{
					yoil = yoil.replace("2", "");
					scheduleList.get(pos).setDayOfWeek(yoil);

					dbManager.update( "UPDATE SCHEDULE_LIST SET s_day = '" + yoil +"' " + "WHERE s_id = " + s_id + ";");
				}
				else
				{
					yoil += "2";
					scheduleList.get(pos).setDayOfWeek(yoil);

					dbManager.update( "UPDATE SCHEDULE_LIST SET s_day = '" + yoil +"' " + "WHERE s_id = " + s_id + ";");
				}
			}
		});
		checkTues.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				String yoil = scheduleList.get(pos).getDayOfWeek();
				int s_id = scheduleList.get(pos).getsid();
				if (yoil.contains("3"))
				{
					yoil = yoil.replace("3", "");
					scheduleList.get(pos).setDayOfWeek(yoil);

					dbManager.update( "UPDATE SCHEDULE_LIST SET s_day = '" + yoil +"' " + "WHERE s_id = " + s_id + ";");
				}
				else
				{
					yoil += "3";
					scheduleList.get(pos).setDayOfWeek(yoil);

					dbManager.update( "UPDATE SCHEDULE_LIST SET s_day = '" + yoil +"' " + "WHERE s_id = " + s_id + ";");
				}
			}
		});
		checkWedn.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				Log.d("checkSun", "Click");
				String yoil = scheduleList.get(pos).getDayOfWeek();
				int s_id = scheduleList.get(pos).getsid();
				if (yoil.contains("4"))
				{
					yoil = yoil.replace("4", "");
					scheduleList.get(pos).setDayOfWeek(yoil);

					dbManager.update( "UPDATE SCHEDULE_LIST SET s_day = '" + yoil +"' " + "WHERE s_id = " + s_id + ";");
				}
				else
				{
					yoil += "4";
					scheduleList.get(pos).setDayOfWeek(yoil);

					dbManager.update( "UPDATE SCHEDULE_LIST SET s_day = '" + yoil +"' " + "WHERE s_id = " + s_id + ";");
				}
			}
		});
		checkThur.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				String yoil = scheduleList.get(pos).getDayOfWeek();
				int s_id = scheduleList.get(pos).getsid();
				if (yoil.contains("5"))
				{
					yoil = yoil.replace("5", "");
					scheduleList.get(pos).setDayOfWeek(yoil);

					dbManager.update( "UPDATE SCHEDULE_LIST SET s_day = '" + yoil +"' " + "WHERE s_id = " + s_id + ";");
				}
				else
				{
					yoil += "5";
					scheduleList.get(pos).setDayOfWeek(yoil);

					dbManager.update( "UPDATE SCHEDULE_LIST SET s_day = '" + yoil +"' " + "WHERE s_id = " + s_id + ";");
				}
			}
		});
		checkFri.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				String yoil = scheduleList.get(pos).getDayOfWeek();
				int s_id = scheduleList.get(pos).getsid();
				if (yoil.contains("6"))
				{
					yoil = yoil.replace("6", "");
					scheduleList.get(pos).setDayOfWeek(yoil);

					dbManager.update( "UPDATE SCHEDULE_LIST SET s_day = '" + yoil +"' " + "WHERE s_id = " + s_id + ";");
				}
				else
				{
					yoil += "6";
					scheduleList.get(pos).setDayOfWeek(yoil);

					dbManager.update( "UPDATE SCHEDULE_LIST SET s_day = '" + yoil +"' " + "WHERE s_id = " + s_id + ";");
				}
			}
		});
		checkSat.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				String yoil = scheduleList.get(pos).getDayOfWeek();
				int s_id = scheduleList.get(pos).getsid();
				if (yoil.contains("7"))
				{
					yoil = yoil.replace("7", "");
					scheduleList.get(pos).setDayOfWeek(yoil);

					dbManager.update( "UPDATE SCHEDULE_LIST SET s_day = '" + yoil +"' " + "WHERE s_id = " + s_id + ";");
				}
				else
				{
					yoil += "7";
					scheduleList.get(pos).setDayOfWeek(yoil);

					dbManager.update( "UPDATE SCHEDULE_LIST SET s_day = '" + yoil +"' " + "WHERE s_id = " + s_id + ";");
				}
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
		private int s_id;
		
		private String strScheduleName;
		private String strDayOfWeek;
		private String strLocation;
		private String strTime;
		private String strRepeat;

		private String strState;

		public ScheduleListItem(int id, String sn, String dow, String l, String t, String r,
				String s) {
			
			s_id = id;
			
			strScheduleName = sn;
			strDayOfWeek = dow;
			strLocation = l;
			strTime = t;

			strRepeat = r;
			strState = s;
		}
		
		public int getsid() {
			return s_id;
		}

		public void getsid(int id) {
			s_id = id;
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

		public String getRepeat() {
			return strRepeat;
		}

		public void setRepeat(String r) {
			strRepeat = r;
		}
		
		public String getState() {
			return strState;
		}

		public void setState(String s) {
			strState = s;
		}

	}

	private class ScheduleListHolder {
		
		ImageView m_BtnDelete;
		
		TextView m_ScheduleName;
		TextView m_DayOfWeek;
		TextView m_Location;
		TextView m_Time;

		RadioButton m_BtnOff;
		RadioButton m_BtnVib;
		RadioButton m_BtnSound;
		
		CheckBox m_checkRepeat;
		CheckBox m_checkSun;
		CheckBox m_checkMon;
		CheckBox m_checkTues;
		CheckBox m_checkWedn;
		CheckBox m_checkThur;
		CheckBox m_checkFri;
		CheckBox m_checkSat;
	}

}
