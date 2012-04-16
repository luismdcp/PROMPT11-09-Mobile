package YambaClient.Package;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class ConnectivityReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		boolean isNetworkDown = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
		YambaClientApplication app = (YambaClientApplication) context.getApplicationContext();
		app.connectivityChanged(!isNetworkDown);
		Log.d("ConnectivityReceiver", "onReceive");
	}
}