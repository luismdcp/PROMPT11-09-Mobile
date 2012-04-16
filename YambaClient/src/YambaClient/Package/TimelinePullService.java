package YambaClient.Package;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class TimelinePullService extends Service
{
	private YambaClientApplication app;
	static final int DELAY = 60000;
	private Thread worker;
	
	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		app = (YambaClientApplication) getApplication();
		worker = new Thread(new Runnable() {
			
			public void run() 
			{
				try 
				{
					Twitter tw = app.GetTwitter();
					List<Status> statusList = tw.getPublicTimeline();
					
					for (Twitter.Status status : statusList) 
					{
						Log.d("TESTE", String.format("%s: %s", status.user.name, status.text));
					}
					
					Thread.sleep(DELAY);
				} 
				catch (InterruptedException e) 
				{
					
				}		
			}
		});
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		super.onStartCommand(intent, flags, startId);
		worker.start();
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		worker.interrupt();
	}
}