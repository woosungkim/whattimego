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

		TextView scheduleName = null;
		TextView dayOfWeek = null;
		TextView location = null;
		TextView time = null;

		Button btnOnOff = null;

		ScheduleListHolder holder = null;

		// ����Ʈ�� ������鼭 ���� ȭ�鿡 ������ �ʴ� �������� convertView�� null�� ���·� ��� ��
		if (convertView == null) {
			// view�� null�� ��� Ŀ���� ���̾ƿ��� ��� ��
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.schedulelistitem, parent,
					false);

			// �� ��������
			scheduleName = (TextView) convertView
					.findViewById(R.id.txtScheduleName);
			dayOfWeek = (TextView) convertView.findViewById(R.id.txtDayOfWeek);
			location = (TextView) convertView.findViewById(R.id.txtLocation);
			time = (TextView) convertView.findViewById(R.id.txtTime);

			btnOnOff = (Button) convertView.findViewById(R.id.btnMode);

			// Ȧ�� ���� �� tag�� ���
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

		// ��ư ��ġ �̺�Ʈ
		btnOnOff.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// ��ġ �� ������ �̸� ���
				Toast.makeText(context,
						scheduleList.get(pos).getScheduleName(),
						Toast.LENGTH_SHORT).show();
			}
		});

		// ����Ʈ ������ ��ġ �� �̺�Ʈ
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(context, "����Ʈ Ŭ�� : " + scheduleList.get(pos),
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
