package com.application.yamba;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class UpdaterService extends IntentService 
{
	private static final String TAG = UpdaterService.class.getSimpleName();
	public static final String NEW_STATUS_INTENT = "com.application.yamba.NEW_STATUS";
	public static final String NEW_STATUS_EXTRA_COUNT = "NEW_STATUS_EXTRA_COUNT";
	
	private NotificationManager notificationManager;
	private Notification notification;
	
	public UpdaterService()
	{
		super("Updater service");
		Log.d(TAG, "UpdaterService constructed");
	}

	@Override
	protected void onHandleIntent(Intent arg0) 
	{
	    this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	    this.notification = new Notification(android.R.drawable.stat_notify_chat, "", 0);
	    Log.d(TAG, "onHandleIntenting");
	    YambaApplication yamba = (YambaApplication) getApplication();
	    int newUpdates = yamba.fetchStatusUpdates();
	    
	    if (newUpdates > 0) 
	    {
	      Log.d(TAG, "We have a new status");
	      yamba.getContentResolver().notifyChange(Uri.parse("content://com.application.yamba.statusprovider"), null);
	      sendTimelineNotification(newUpdates);
	    }
	}
	
	private void sendTimelineNotification(int timelineUpdateCount) 
	{
	    Log.d(TAG, "sendTimelineNotification");
	    PendingIntent pendingIntent = PendingIntent.getActivity(this, -1, new Intent(this, TimelineActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
	    this.notification.when = System.currentTimeMillis();
	    this.notification.flags |= Notification.FLAG_AUTO_CANCEL;
	    CharSequence notificationTitle = this.getText(R.string.msgNotificationTitle);
	    CharSequence notificationSummary = this.getString(R.string.msgNotificationMessage, timelineUpdateCount);
	    this.notification.setLatestEventInfo(this, notificationTitle, notificationSummary, pendingIntent);
	    this.notificationManager.notify(0, this.notification);
	    Log.d(TAG, "sendTimelineNotificationed");
	  }
}