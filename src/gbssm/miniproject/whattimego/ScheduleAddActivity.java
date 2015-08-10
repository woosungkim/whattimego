package gbssm.miniproject.whattimego;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class ScheduleAddActivity extends Activity {
	private DBManager dbManager;

	private EditText editName;
	private EditText editDest;
	private CheckBox checkRepeat;
	private CheckBox btnMon, btnTues, btnWedn, btnThur, btnFri, btnSat, btnSun;
	private TextView txtTime;
	
	private Button btnOk;

	private int hour, minute;
	private String scheduleName = "";
	private String destination = "";
	private String destAddress = "";
	private String dayOfWeek = "";

	static final int TIME_DIALOG_ID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scheduleadd);
		
		// db ����
		dbManager = new DBManager( getApplicationContext(), "ScheduleList.db", null, 2 );
		
		// ���� �̸�
		editName = (EditText) findViewById( R.id.editScheduleName );
		// ������ 
		editDest = (EditText) findViewById( R.id.editDestination );
		// �ݺ�����
		checkRepeat = (CheckBox) findViewById( R.id.checkboxRepeat );
		// ���� ����
		btnMon = (CheckBox) findViewById( R.id.checkMon );
		btnTues = (CheckBox) findViewById( R.id.checkTues );
		btnWedn = (CheckBox) findViewById( R.id.checkWedn );
		btnThur = (CheckBox) findViewById( R.id.checkThur );
		btnFri = (CheckBox) findViewById( R.id.checkFri );
		btnSat = (CheckBox) findViewById( R.id.checkSat );
		btnSun = (CheckBox) findViewById( R.id.checkSun );
		// �ð�
		txtTime = (TextView) findViewById(R.id.txtTime);

		txtTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(TIME_DIALOG_ID);

			}
		});
		
		btnOk = (Button) findViewById( R.id.btnOk );
		
		btnOk.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String s_name = editName.getText().toString();
				String s_dest = editDest.getText().toString();
				String s_addr = "�ּ� �Ⱦ˷���!";
				String s_time = txtTime.getText().toString();
				String s_day = "";
				if ( btnMon.isChecked() ) s_day = s_day+"1";
				if ( btnTues.isChecked() ) s_day = s_day+"2";
				if ( btnWedn.isChecked() ) s_day = s_day+"3";
				if ( btnThur.isChecked() ) s_day = s_day+"4";
				if ( btnFri.isChecked() ) s_day = s_day+"5";
				if ( btnSat.isChecked() ) s_day = s_day+"6";
				if ( btnSun.isChecked() ) s_day = s_day+"7";
				String repeat = "";
				if ( checkRepeat.isChecked() ) repeat = repeat+"yes";
				else repeat = repeat+"no";

				// DB�� �����ϴ� ���� ����
				dbManager.insert( "INSERT INTO SCHEDULE_LIST VALUES(null, '" + s_name + "', '" + s_dest + "', '" + s_addr + "', '" + s_time + "', '" + s_day + "', '" + repeat + "', '" + "on" + "');");
				
				finish();
			}
		});
	}

	// �ð� ������Ʈ
	private void updateDisplay() {
		// TODO Auto-generated method stub
		txtTime.setText(new StringBuilder().append(pad(hour)).append(":").append(pad(minute)));
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
	
	protected Dialog onCreateDialog( int id ) {
		switch ( id ) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog( this, mTimeSetListener, hour, minute, false );
		}
		
		return null;
	}
}
