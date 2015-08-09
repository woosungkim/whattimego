package gbssm.miniproject.whattimego;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class TitleActivity extends Activity {

	RelativeLayout page;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title);   
		
		page = (RelativeLayout) findViewById(R.id.layout_page);
		
		page.setOnClickListener( new OnClickListener() {
			public void onClick( View v )
			{
				Intent intentScheduleListActivity = new Intent(TitleActivity.this, ScheduleListActivity.class );
				
				startActivity(intentScheduleListActivity);
				
				finish();
			}

		});
		
	}
}
