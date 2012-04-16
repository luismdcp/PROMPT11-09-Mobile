package YambaClient.Package;

import winterwell.jtwitter.Twitter;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public final class UpdateStatusService extends IntentService
{
	private YambaClientApplication app;
	//private Handler worker;
	
	public static String MessageKey = "TweetText";
	
	public UpdateStatusService() 
	{
		super("Updater service worker");
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		app = (YambaClientApplication) getApplication();
		
		/*// TODO para o regular Service
		HandlerThread workerThread = new HandlerThread("Updater service worker");
		workerThread.start();
		Looper looper = workerThread.getLooper();
		worker = new Handler(looper) {
			@Override
			public void handleMessage(Message msg) {
				
			}
		};*/
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	/*
	// TODO para o regular Service
	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) 
	{
		String message = intent.getStringExtra(getResources().getString(R.string.uploadIntentExtraText));
		Message msg = new Message();
		Bundle bundle = new Bundle();
		bundle.putString("TweetText", message);
		
		msg.setTarget(worker);
		msg.setData(bundle);
		worker.sendMessage(msg);
		
		return START_STICKY;
	}
	

	@Override
	public IBinder onBind(Intent arg0) 
	{
		// TODO para o regular Service
		return null;
	}
	*/

	@Override
	protected void onHandleIntent(Intent arg0) 
	{
		String message = arg0.getStringExtra(UpdateStatusService.MessageKey);
		Twitter tw = app.GetTwitter();
		tw.updateStatus(message);
	}
}