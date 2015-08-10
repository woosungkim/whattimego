package gbssm.miniproject.whattimego;

import gbssm.miniproject.whattimego.ScheduleListAdapter.ScheduleListItem;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class ScheduleListActivity extends Activity {
	private DBManager dbManager;
	
	private ListView scheduleListView;
	private ScheduleListAdapter scheduleAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedulelist);   
		
		dbManager = new DBManager( getApplicationContext(), "ScheduleList.db", null, 2 );

		scheduleListView = (ListView) findViewById( R.id.schedulelistview );
		//dbManager.insert( "INSERT INTO SCHEDULE_LIST VALUES(null, '" + "ÇÐ±³ ¼ö¾÷3" + "', '" + "±¹¹Î´ëÇÐ±³" + "', '" + "¼ººÏ±¸ ¾îµò°¡" + "', '" + "10:30" + "', '" + "1234" + "', '" + "yes" + "', '" + "on" + "');");	
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		scheduleAdapter = new ScheduleListAdapter();
		
		scheduleListView.setAdapter( scheduleAdapter );
		
		Cursor cursor = dbManager.select( "SELECT * from SCHEDULE_LIST" );
		
		while(cursor.moveToNext()) {
			scheduleAdapter.add( new ScheduleListItem( cursor.getInt(0), cursor.getString(1), cursor.getString(5), cursor.getString(2), cursor.getString(4), cursor.getString(7)));
        }
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {
			Intent intentScheduleAddActivity = new Intent(ScheduleListActivity.this, ScheduleAddActivity.class );
			
			startActivity(intentScheduleAddActivity);
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
