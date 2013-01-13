package com.application.yamba;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver 
{
	@Override
	public void onReceive(Context context, Intent callingIntent) 
	{
	    int interval = ((YambaApplication) context.getApplicationContext()).getInterval();
	    
	    if (interval == YambaApplication.INTERVAL_NEVER)
	    {
	      return;
	    }

	    Intent msg = new Intent();
		msg.setClass(context, UpdaterService.class);
		msg.setAction(UpdaterService.NEW_STATUS_INTENT);
	    PendingIntent pendingIntent = PendingIntent.getService(context, -1, msg, PendingIntent.FLAG_UPDATE_CURRENT);

	    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	    alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), interval, pendingIntent);
	    Log.d("BootReceiver", "onReceive");
	}
}