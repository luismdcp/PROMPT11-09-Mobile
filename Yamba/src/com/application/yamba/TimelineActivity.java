package com.application.yamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class TimelineActivity extends BaseActivity
{
	static final String SEND_TIMELINE_NOTIFICATIONS = "com.application.yamba.SEND_TIMELINE_NOTIFICATIONS";
	private Cursor cursor;
	private ListView listTimeline;
	private TimelineAdapter adapter;
	private TimelineReceiver receiver;
	private IntentFilter filter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);
		
		if (((YambaApplication) getApplication()).prefs.getString("username", "") == "") 
		{
			startActivity(new Intent(this, PrefsActivity.class));
			Toast.makeText(this, R.string.msgSetupPrefs, Toast.LENGTH_LONG).show();
		}
		
		listTimeline = (ListView) findViewById(R.id.listTimeline);
		receiver = new TimelineReceiver();
	    filter = new IntentFilter(UpdaterService.NEW_STATUS_INTENT);
	}
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		yamba.getStatusData().close();
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		setupList();
		registerReceiver(receiver, filter);
	}
	
	@Override
	protected void onPause() 
	{
	    super.onPause();
	    unregisterReceiver(receiver); 
	}
	
	private void setupList() 
	{
		cursor = yamba.getStatusData().getStatusUpdates();
		startManagingCursor(cursor);
		adapter = new TimelineAdapter(this, cursor);
		listTimeline.setAdapter(adapter);
	}
	
	class TimelineReceiver extends BroadcastReceiver 
	{
	    @Override
	    public void onReceive(Context context, Intent intent) 
	    {
	    	setupList();
	    	Log.d("TimelineReceiver", "onReceived");
	    }
	}
}