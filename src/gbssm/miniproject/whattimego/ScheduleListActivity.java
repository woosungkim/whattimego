package gbssm.miniproject.whattimego;

import gbssm.miniproject.whattimego.ScheduleListAdapter.ScheduleListItem;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class ScheduleListActivity extends Activity {
	private DBManager dbManager;
	
	private ListView scheduleListView;
	private ScheduleListAdapter scheduleAdapter;
	
	private ImageView btnPlus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedulelist);   
		
		dbManager = new DBManager( getApplicationContext(), "ScheduleList.db", null, 3 );

		scheduleListView = (ListView) findViewById( R.id.schedulelistview );
		
		btnPlus = (ImageView) findViewById( R.id.btnAdd );
		
		btnPlus.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentScheduleAddActivity = new Intent(ScheduleListActivity.this, ScheduleAddActivity.class );
				
				startActivity(intentScheduleAddActivity);
			}
		});
		//dbManager.insert( "INSERT INTO SCHEDULE_LIST VALUES(null, '" + "�б� ����3" + "', '" + "���δ��б�" + "', '" + "���ϱ� ���" + "', '" + "10:30" + "', '" + "1234" + "', '" + "yes" + "', '" + "on" + "');");	
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		scheduleAdapter = new ScheduleListAdapter();
		
		scheduleListView.setAdapter( scheduleAdapter );
		
		Cursor cursor = dbManager.select( "SELECT * from SCHEDULE_LIST" );
		
		while(cursor.moveToNext()) {
			Log.d("DBList", cursor.getInt(0) + " " +
							cursor.getString(1) + " " + 
							cursor.getString(2) + " " + 
							cursor.getString(3) + " " + 
							cursor.getString(4) + " " + 
							cursor.getString(5) + " " + 
							cursor.getString(6) + " " + 
							cursor.getString(7) + " " + 
							cursor.getString(8) + " " + 
							cursor.getString(9) );
			;
			String yoil = "";
			String dbYoil = cursor.getString(5);
			if ( cursor.getString(6).equals("yes") ) yoil += "�ݺ�) ";
			
			if ( dbYoil.equals("1234567") )
				yoil += "�� ��";
			else
			{
				if ( dbYoil.contains("1") ) yoil += "�� ";
				if ( dbYoil.contains("2") ) yoil += "ȭ ";
				if ( dbYoil.contains("3") ) yoil += "�� ";
				if ( dbYoil.contains("4") ) yoil += "�� ";
				if ( dbYoil.contains("5") ) yoil += "�� ";
				if ( dbYoil.contains("6") ) yoil += "�� ";
				if ( dbYoil.contains("7") ) yoil += "�� ";
			}
			scheduleAdapter.add( new ScheduleListItem( cursor.getInt(0), cursor.getString(1), yoil, cursor.getString(2), cursor.getString(4), cursor.getString(7)));
        }
		
	}
	
}
