package gbssm.miniproject.whattimego;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class TitleActivity extends Activity {

	RelativeLayout page;

	Button btnStart;
	Button btnEnd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title);   
		
		page = (RelativeLayout) findViewById(R.id.layout_page);
		
		btnStart = (Button) findViewById( R.id.btnStart );
		btnStart.setOnClickListener( m_StartListener );
		
		btnEnd = (Button) findViewById ( R.id.btnEnd );
		btnEnd.setOnClickListener( m_EndListener );
		
		page.setOnClickListener( new OnClickListener() {
			public void onClick( View v )
			{
				Intent intentScheduleListActivity = new Intent(TitleActivity.this, ScheduleListActivity.class );
				
				startActivity(intentScheduleListActivity);
				 
				finish();
				
			}

		});
		
	}

	private OnClickListener m_StartListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startService(new Intent(TitleActivity.this, ScheduleService.class));
		}
	};

	private OnClickListener m_EndListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			stopService(new Intent(TitleActivity.this, ScheduleService.class));
		}
	};

}
