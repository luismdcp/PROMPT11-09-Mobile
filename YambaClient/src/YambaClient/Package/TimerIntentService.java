package YambaClient.Package;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class TimerIntentService extends IntentService
{
	private YambaClientApplication app;
	private Timer timer;
	private TimerTask task;
	private Handler handler;
	
	static final int DELAY = 60000;
	public static String MessageKey = "TimerMessage";
	public static String StartMessage = "start";
	public static String StopMessage = "stop";
	public static String NowMessage = "now";

	public TimerIntentService() 
	{
		super("Timer service worker");
	}

	@Override
	protected void onHandleIntent(final Intent intent) 
	{
		String message = intent.getStringExtra(TimerIntentService.MessageKey);
		
		task = new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						Twitter tw = app.GetTwitter();
						List<Status> statusList = tw.getPublicTimeline();
					}
				});
			}
		};
		
		if (message.equalsIgnoreCase(StartMessage))
		{
			timer.schedule(task, DELAY);
			Log.d("MyTag", "Start");
			return;
		}
		
		if (message.equalsIgnoreCase(StopMessage))
		{
			Log.d("MyTag", "Stop");
			timer.cancel();
			return;
		}
		
		if (message.equalsIgnoreCase(NowMessage))
		{
			Log.d("MyTag", "Now");
			Twitter tw = app.GetTwitter();
			List<Status> statusList = tw.getPublicTimeline();
			app.tweets = new String[statusList.size()];
			
			for ( int i = 0; i < statusList.size(); i++ ) 
			{
				app.tweets[i] = statusList.get(i).getText();
			}
			
			Log.d("MyTag", "Before ListView");
			TweetsListView listView = new TweetsListView();
			Log.d("MyTag", "After ListView");
			
			return;
		}
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		app = (YambaClientApplication) getApplication();
		timer = new Timer();
		handler = new Handler();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		timer.cancel();
	}
}