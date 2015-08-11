package gbssm.miniproject.whattimego;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

public class ScheduleAddActivity extends Activity {
	private DBManager dbManager;

	private EditText editName;
	private TextView editDest;
	private CheckBox checkRepeat;
	private CheckBox btnMon, btnTues, btnWedn, btnThur, btnFri, btnSat, btnSun;
	private TextView txtTime;

	private ImageView btnOk;

	private int hour, minute;
	private String scheduleName = "";
	private String destination = "";
	private String destAddress = "";
	private String dayOfWeek = "";
	
	
	String selectedLoca = "";
	String selectedAddr = "";
	String selectedLati = "";
	String selectedLongi = "";

	static final int TIME_DIALOG_ID = 0;
	
	static final int DEST_SEARCH_CODE = 1111;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scheduleadd);

		// db 연결
		dbManager = new DBManager(getApplicationContext(), "ScheduleList.db",
				null, 3);

		// 일정 이름
		editName = (EditText) findViewById(R.id.editScheduleName);
		// 목적지
		editDest = (TextView) findViewById(R.id.editDestination);
		// 반복여부
		checkRepeat = (CheckBox) findViewById(R.id.checkboxRepeat);
		// 요일 선택
		btnMon = (CheckBox) findViewById(R.id.checkMon);
		btnTues = (CheckBox) findViewById(R.id.checkTues);
		btnWedn = (CheckBox) findViewById(R.id.checkWedn);
		btnThur = (CheckBox) findViewById(R.id.checkThur);
		btnFri = (CheckBox) findViewById(R.id.checkFri);
		btnSat = (CheckBox) findViewById(R.id.checkSat);
		btnSun = (CheckBox) findViewById(R.id.checkSun);
		// 시간
		txtTime = (TextView) findViewById(R.id.txtTime);

		editDest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentDestSearchActivity = new Intent(
						ScheduleAddActivity.this, DestSearchActivity.class);

				startActivityForResult(intentDestSearchActivity, DEST_SEARCH_CODE);
			}
		});

		txtTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(TIME_DIALOG_ID);

			}
		});

		btnOk = (ImageView) findViewById(R.id.btnOk);

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String s_name = editName.getText().toString();
				String s_time = txtTime.getText().toString();
				String s_day = "";
				if (btnMon.isChecked())
					s_day = s_day + "1";
				if (btnTues.isChecked())
					s_day = s_day + "2";
				if (btnWedn.isChecked())
					s_day = s_day + "3";
				if (btnThur.isChecked())
					s_day = s_day + "4";
				if (btnFri.isChecked())
					s_day = s_day + "5";
				if (btnSat.isChecked())
					s_day = s_day + "6";
				if (btnSun.isChecked())
					s_day = s_day + "7";
				String repeat = "";
				if (checkRepeat.isChecked())
					repeat = repeat + "yes";
				else
					repeat = repeat + "no";

				// DB에 저장하는 동작 수행
				dbManager.insert("INSERT INTO SCHEDULE_LIST VALUES(null, '"
						+ s_name + "', '" + selectedLoca + "', '" + selectedAddr + "', '"
						+ s_time + "', '" + s_day + "', '" + repeat + "', '"
						+ "sound" + "', '" + selectedLati + "', '" + selectedLongi + 
						"');");

				finish();
			}
		});
	}
	
	protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK)
		{
			if ( (requestCode == DEST_SEARCH_CODE) )
			{
				Log.d( "Receive Test", data.getStringExtra("locationName") + "  " + 
										data.getStringExtra("address") + "  " + 
										data.getStringExtra("latitude") + "  " + 
										data.getStringExtra("longitude") );
				
				selectedLoca = data.getStringExtra("locationName");
				selectedAddr = data.getStringExtra("address");
				selectedLati = data.getStringExtra("latitude");
				selectedLongi = data.getStringExtra("longitude");
				
				editDest.setText( selectedLoca + " (" + selectedAddr + ")" );
				
			}
		}
	}

	// 시간 업데이트
	private void updateDisplay() {
		// TODO Auto-generated method stub
		txtTime.setText(new StringBuilder().append(pad(hour)).append(":")
				.append(pad(minute)));
	}

	private static String pad(int c) {
		// TODO Auto-generated method stub
		if (c >= 10) {
			return String.valueOf(c);
		} else
			return "0" + String.valueOf(c);
	}

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int min) {
			// TODO Auto-generated method stub
			hour = hourOfDay;
			minute = min;
			
			updateDisplay();
		}
	};

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, hour, minute,
					false);
		}

		return null;
	}
}
