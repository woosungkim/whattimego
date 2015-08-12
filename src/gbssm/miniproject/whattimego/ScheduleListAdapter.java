package gbssm.miniproject.whattimego;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleListAdapter extends BaseAdapter {
	
	private DBManager dbManager;
	private ArrayList<ScheduleListItem> scheduleList;

	// ������
	public ScheduleListAdapter() {
		scheduleList = new ArrayList<ScheduleListItem>();
	}

	// ���� ������ �� ����
	public int getCount() {
		return scheduleList.size();
	}

	// pos ������ ����
	public Object getItem(int position) {
		return scheduleList.get(position);
	}

	// pos ����
	public long getItemId(int position) {
		return position;
	}

	// ��� �� ������ ����
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		final Context context = parent.getContext();
		
		ImageView btnDelete = null;

		TextView scheduleName = null;
		TextView dayOfWeek = null;
		TextView location = null;
		TextView time = null;

		RadioButton btnNo = null;
		RadioButton btnVib = null;
		RadioButton btnSound = null;
		

		ScheduleListHolder holder = null;
		
		dbManager = new DBManager( context, "ScheduleList.db", null, DBManager.DB_VERSION );

		// ����Ʈ�� ������鼭 ���� ȭ�鿡 ������ �ʴ� �������� convertView�� null�� ���·� ��� ��
		if (convertView == null) {
			// view�� null�� ��� Ŀ���� ���̾ƿ��� ��� ��
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.schedulelistitem, parent,
					false);

			// �� ��������
			btnDelete = (ImageView) convertView.findViewById( R.id.btnDelete );
			
			scheduleName = (TextView) convertView
					.findViewById(R.id.txtScheduleName);
			dayOfWeek = (TextView) convertView.findViewById(R.id.txtDayOfWeek);
			location = (TextView) convertView.findViewById(R.id.txtLocation);
			time = (TextView) convertView.findViewById(R.id.txtTime);

			btnNo = (RadioButton) convertView.findViewById( R.id.radioOff );
			btnVib = (RadioButton) convertView.findViewById( R.id.radioVib );
			btnSound = (RadioButton) convertView.findViewById( R.id.radioSound );

			// Ȧ�� ���� �� tag�� ���
			holder = new ScheduleListHolder();
			
			holder.m_BtnDelete = btnDelete;
			
			holder.m_ScheduleName = scheduleName;
			holder.m_DayOfWeek = dayOfWeek;
			holder.m_Location = location;
			holder.m_Time = time;
			
			holder.m_BtnNo = btnNo;
			holder.m_BtnVib = btnVib;
			holder.m_BtnSound = btnSound;
			
			convertView.setTag(holder);
		} else {
			holder = (ScheduleListHolder) convertView.getTag();
			btnDelete = holder.m_BtnDelete;
			
			scheduleName = holder.m_ScheduleName;
			dayOfWeek = holder.m_DayOfWeek;
			location = holder.m_Location;
			time = holder.m_Time;

			btnNo = holder.m_BtnNo;
			btnVib = holder.m_BtnVib;
			btnSound = holder.m_BtnSound;
			
		}

		scheduleName.setText(scheduleList.get(position).getScheduleName());
		dayOfWeek.setText(scheduleList.get(position).getDayOfWeek());
		location.setText(scheduleList.get(position).getLocation());
		time.setText(scheduleList.get(position).getTime());
		
		if ( scheduleList.get(position).getState().equals("sound") )
			btnSound.setChecked(true);
		else if ( scheduleList.get(position).getState().equals("vib") )
			btnVib.setChecked(true);
		else
			btnNo.setChecked(true);

		// delete ��ư ��ġ �̺�Ʈ
		btnDelete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				// �ش� ���� DB���� ����
				dbManager.delete( "DELETE FROM SCHEDULE_LIST WHERE s_id = " + ((ScheduleListItem)getItem(pos)).getsid() + ";");
				// ����Ʈ�信�� ����
				scheduleList.remove( pos );				
				
				notifyDataSetChanged();
				
				Toast.makeText(context,
						"������ �����Ǿ����ϴ�",
						Toast.LENGTH_SHORT).show();
			}
		});


		// ����Ʈ ������ ��ġ �� �̺�Ʈ
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(context, "����Ʈ Ŭ�� : " + pos + "  s_id : " + ((ScheduleListItem)getItem(pos)).getsid(),
						Toast.LENGTH_SHORT).show();
			}

		});

		return convertView;
	}

	// �ܺο��� ������ �߰� ��û �� ���
	public void add(ScheduleListItem item) {
		scheduleList.add(item);
	}

	// �ܺο��� ������ ���� ��û �� ���
	public void remove(int position) {
		scheduleList.remove(position);
	}

	// �� ����Ʈ ������
	static class ScheduleListItem {
		private int s_id;
		
		private String strScheduleName;
		private String strDayOfWeek;
		private String strLocation;
		private String strTime;

		private String strState;

		public ScheduleListItem(int id, String sn, String dow, String l, String t,
				String s) {
			
			s_id = id;
			
			strScheduleName = sn;
			strDayOfWeek = dow;
			strLocation = l;
			strTime = t;

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

		RadioButton m_BtnNo;
		RadioButton m_BtnVib;
		RadioButton m_BtnSound;
	}

}
