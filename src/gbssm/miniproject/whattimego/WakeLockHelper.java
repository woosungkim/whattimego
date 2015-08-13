package gbssm.miniproject.whattimego;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

public class WakeLockHelper {     
	private static PowerManager.WakeLock sCpuWakeLock;    

	public static void acquireCpuWakeLock(Context context) {        
		Log.d("tag","Acquiring cpu wake lock");        
		Log.d("tag","wake sCpuWakeLock = " + sCpuWakeLock);        

		if (sCpuWakeLock != null) {            
			return;        
		}         
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);         
		sCpuWakeLock = pm.newWakeLock(                
				PowerManager.SCREEN_BRIGHT_WAKE_LOCK |                
				PowerManager.ACQUIRE_CAUSES_WAKEUP |                
				PowerManager.ON_AFTER_RELEASE, "WakeCall");        

		sCpuWakeLock.acquire();
	}

	public static void releaseCpuLock() {        
		Log.d("tag","Releasing cpu wake lock");
		Log.d("tag","relase sCpuWakeLock = " + sCpuWakeLock);

		if (sCpuWakeLock != null) {            
			sCpuWakeLock.release();            
			sCpuWakeLock = null;        
		}
	}
}