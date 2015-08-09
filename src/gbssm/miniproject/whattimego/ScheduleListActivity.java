package gbssm.miniproject.whattimego;

import gbssm.miniproject.whattimego.ScheduleListAdapter.ScheduleListItem;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ScheduleListActivity extends Activity {

	private ListView scheduleListView;
	private ScheduleListAdapter scheduleAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedulelist);   
		
		scheduleListView = (ListView) findViewById( R.id.schedulelistview );
		
		scheduleAdapter = new ScheduleListAdapter();
		
		scheduleListView.setAdapter( scheduleAdapter );
		
		
		scheduleAdapter.add( new ScheduleListItem("����� ����", "��-��", "���ɺ���", "���� 09:00", "ON") );
		scheduleAdapter.add( new ScheduleListItem("�б� ����", "��,��,��", "���δ��б�", "���� 10:00", "OFF") );
		scheduleAdapter.add( new ScheduleListItem("����", "��", "�б���", "���� 07:00", "ON") );
		scheduleAdapter.add( new ScheduleListItem("��âȸ", "��", "ȫ���Ա�", "���� 09:00", "ON") );

	}
	
}
