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
		
		
		scheduleAdapter.add( new ScheduleListItem("멤버십 ㄱㄱ", "월-금", "보령빌딩", "오전 09:00", "ON") );
		scheduleAdapter.add( new ScheduleListItem("학교 수업", "월,수,금", "국민대학교", "오전 10:00", "OFF") );
		scheduleAdapter.add( new ScheduleListItem("과외", "일", "압구정", "오후 07:00", "ON") );
		scheduleAdapter.add( new ScheduleListItem("동창회", "토", "홍대입구", "오후 09:00", "ON") );

	}
	
}
