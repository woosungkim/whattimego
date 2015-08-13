package gbssm.miniproject.whattimego;

import gbssm.miniproject.whattimego.ScheduleListAdapter.ScheduleListItem;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ScheduleListActivity extends Activity implements OnClickListener {
	private DBManager dbManager;

	private ListView scheduleListView;
	private ScheduleListAdapter scheduleAdapter;

	private ImageView btnPlus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedulelist);

		dbManager = new DBManager(getApplicationContext(), "ScheduleList.db",
				null, DBManager.DB_VERSION);

		scheduleListView = (ListView) findViewById(R.id.schedulelistview);

		btnPlus = (ImageView) findViewById(R.id.btnAdd);
		btnPlus.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		scheduleAdapter = new ScheduleListAdapter();

		scheduleListView.setAdapter(scheduleAdapter);

		Cursor cursor = dbManager.select("SELECT * from SCHEDULE_LIST");

		while (cursor.moveToNext()) {
			Log.d("DBList", cursor.getInt(0) + " " + cursor.getString(1) + " "
					+ cursor.getString(2) + " " + cursor.getString(3) + " "
					+ cursor.getString(4) + " " + cursor.getString(5) + " "
					+ cursor.getString(6) + " " + cursor.getString(7) + " "
					+ cursor.getString(8) + " " + cursor.getString(9));

			int nTime = Integer.parseInt(cursor.getString(4));
			int hour = nTime / 60;
			int minute = nTime % 60;

			String scheduleTime = "";
			if (hour < 12) {
				scheduleTime += "오전 ";
			} else {
				scheduleTime += "오후 ";
				if (hour > 12)
					hour -= 12;
			}
			if ( hour < 10 )
				scheduleTime += "0";
			scheduleTime += hour + ":";
			if ( minute < 10 )
				scheduleTime += "0";
			scheduleTime += (minute+"");

			scheduleAdapter.add(new ScheduleListItem(cursor.getInt(0), cursor
					.getString(1), cursor.getString(5), cursor.getString(2),
					scheduleTime, cursor.getString(6), cursor.getString(7)));
		}

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAdd:
			Intent intentScheduleAddActivity = new Intent(
					ScheduleListActivity.this, ScheduleAddActivity.class);

			startActivity(intentScheduleAddActivity);
			break;
		}
	}

}
