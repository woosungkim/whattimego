package gbssm.miniproject.whattimego;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class AlarmActivity extends Activity {
	private MediaPlayer mMediaPlayer; 
	
	private TextView txtTime;
	private TextView txtPath;
	
	private DBManager dbManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		try { 
			WakeLockHelper.acquireCpuWakeLock(this);
			   //to ignore password screen
	        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
	              | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
	              | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

			setContentView(R.layout.activity_alarm);
			
			// DB열고,
			dbManager = new DBManager(getApplicationContext(), "ScheduleList.db",
					null, DBManager.DB_VERSION);
			 

			// 받은 정보 액티비티에 띄워주기
			Intent intent = getIntent();
			
			int index = intent.getExtras().getInt("index");
			int s_id = intent.getExtras().getInt("s_id");
			String today = intent.getExtras().getString("yoil");
			txtTime = (TextView) findViewById( R.id.txtTime );
			txtPath = (TextView) findViewById( R.id.txtPath );
			txtTime.setText( intent.getExtras().getString( "nth" ) );
			txtPath.setText( intent.getExtras().getString( "path" ) );
			
			Log.d( "dataTest", index + "   " + s_id + "  " + today );
			
			Cursor cursor = dbManager.select("SELECT * from SCHEDULE_LIST WHERE s_id = " + s_id + ";");
			cursor.moveToNext();
			
			Log.d( "dbTest", cursor.getString(4)+ "   " + cursor.getString(5) + "  " + cursor.getString(6))	;
			
			// 반복 아닌 알람이 마지막 알람이 울리면 꺼준다.
			/*if ( index == 2 && cursor.getString(6).equals("no") )
			{
				String yoil = cursor.getString(4);
				Log.d("oldyoil", yoil);
				yoil = yoil.replace( today, "");
				Log.d("newyoil", yoil);
				dbManager.update( "UPDATE SCHEDULE_LIST SET s_day = '" + yoil + "' WHERE s_id = " + s_id + ";");
			}*/
			
			Button btnStop=(Button)findViewById(R.id.btnAlarmOff);
			
			btnStop.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					stopBell();
					WakeLockHelper.releaseCpuLock();
					
					finish();
				}
			});
			
			// 벨 울리기
			if ( cursor.getString(7).equals("vib") )
				startBell(1);
			else if ( cursor.getString(7).equals("sound") )
				startBell(0);
		} catch ( Exception e ) {
			Log.d("Alram Activity", "Error");
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopBell();
		WakeLockHelper.releaseCpuLock();
	}
	/*
	protected void onPause() 
	{
		super.onPause();
		stopBell();
	}*/


	private MediaPlayer bellPlayer = null;
	private Vibrator mVibrator = null;

	private void startBell(int mode)
	{
		switch(mode) {
		case 1: //vibrate
			Log.d("bell", "Vibrate on");
			if(mVibrator == null) {
				mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);   
			}

			long pattern[]={0,1000};
			mVibrator.vibrate(pattern, 0);
			break;

		case 0: //ring
			Log.d("bell", "Ring on");
			
			
			
			if(bellPlayer == null)
			{
				try
				{
					bellPlayer = MediaPlayer.create(this, R.raw.alarmsong);
					/*final AudioManager mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
					final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

					Uri defaultRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE);

					bellPlayer = new MediaPlayer();
					bellPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					bellPlayer.setDataSource(this, defaultRintoneUri);
					bellPlayer.setVolume(1f, 1f);
					bellPlayer.setLooping(true);
					bellPlayer.prepare();//
					bellPlayer.setOnCompletionListener(new OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							// TODO Auto-generated method stub
							mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
						}
					});*/
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}

			if(!bellPlayer.isPlaying()) {
				bellPlayer.start();
			}
			break;
		}
	}

	private void stopBell()
	{
		if(mVibrator != null) {
			Log.d("bell", "Vibrate off");
			mVibrator.cancel();   
			mVibrator = null;
		}

		if(bellPlayer != null) {
			if(bellPlayer.isPlaying()) {
				Log.d("bell", "Ring off");
				bellPlayer.stop();
				bellPlayer = null;
			}
		}
	}
	
}
