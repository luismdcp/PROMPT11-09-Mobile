package YambaClient.Package;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.util.Log;

public class YambaClientApplication extends Application implements OnSharedPreferenceChangeListener
{
	private volatile Twitter twitter;
	private SharedPreferences prefs;
	private PendingIntent operation;
	public static final String TAG = "YambaClientApplication";
	public volatile String[] tweets;

	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) 
	{
		twitter = null;
		
		/*
		boolean timelinePullServicechecked = prefs.getBoolean("activateTimelinePullService", false);
		
		if (timelinePullServicechecked)
		{
			Intent msg = new Intent(this, TimerIntentService.class);
	    	msg.putExtra(TimerIntentService.MessageKey, TimerIntentService.StartMessage);
	    	startService(msg);
			//startService(new Intent(this, TimelinePullService.class));
		}
		else
		{
			Intent msg = new Intent(this, TimerIntentService.class);
	    	msg.putExtra(TimerIntentService.MessageKey, TimerIntentService.StopMessage);
	    	startService(msg);
			//stopService(new Intent(this, TimelinePullService.class));
		}*/
	}

	@Override
	public void onCreate() 
	{
		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		twitter = null;
	}
	
	public synchronized Twitter GetTwitter()
	{
		if(twitter == null)
		{
			twitter = new Twitter(prefs.getString("username", ""), prefs.getString("password", ""));
	        twitter.setAPIRootUrl(prefs.getString("site_url", ""));
		}
		
		return twitter;
	}
	
	public void connectivityChanged(boolean isNetworkUp)
	{
		if (isNetworkUp)
		{
			registerPeriodicUpdate();
		}
		else
		{
			cancelPeriodicUpdate();
		}
	}
	
	private void registerPeriodicUpdate()
	{
		boolean timelinePullServicechecked = prefs.getBoolean("activateTimelinePullService", false);
		int timeInterval = Integer.parseInt(prefs.getString("update_interval", "10000"));
		
		if (timelinePullServicechecked)
		{
			AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			
			Intent pullIntent = new Intent(this, TimerIntentService.class);
			pullIntent.putExtra(TimerIntentService.MessageKey, TimerIntentService.NowMessage);
			
			operation = PendingIntent.getService(this, -1, pullIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			manager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), timeInterval, operation);
		}
	}
	
	private void cancelPeriodicUpdate()
	{
		AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		
		Intent pullIntent = new Intent(this, TimerIntentService.class);
		pullIntent.putExtra(TimerIntentService.MessageKey, TimerIntentService.NowMessage);
		
		operation = PendingIntent.getService(this, -1, pullIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		manager.cancel(operation);		
	}
}