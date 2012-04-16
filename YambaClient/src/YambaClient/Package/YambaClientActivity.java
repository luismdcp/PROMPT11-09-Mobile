package YambaClient.Package;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class YambaClientActivity extends Activity implements OnClickListener
{
	private int ok, noSpace, warning, maxChars, warningThreshold, noSpaceThreshold;
	private EditText editText;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		
		if(item.getItemId() == R.id.prefs_menu)
		{
			startActivity(new Intent(this, UserPreferencesActivity.class));
		}
		
		if(item.getItemId() == R.id.pull_now_menu)
		{
			Intent msg = new Intent(this, TimerIntentService.class);
	    	msg.putExtra(TimerIntentService.MessageKey, TimerIntentService.NowMessage);
	    	startService(msg);
		}
		
		return true;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        Init();
        setContentView(R.layout.main);
        
        editText = (EditText) findViewById(R.id.editText);
        
        final TextView remainingChars = (TextView) findViewById(R.id.remainingCharsNumber);
        remainingChars.setText("" + maxChars);
        
        Button submitButton = (Button) findViewById(R.id.buttonSubmit);
        
        submitButton.setOnClickListener(this);
        
        editText.addTextChangedListener(new TextWatcher() 
        {
			public void afterTextChanged(Editable arg0) 
			{
				int len = arg0.length();
				
				if (maxChars - len >= 0)
				{
					remainingChars.setText("" + (maxChars - len));
				}
				
				if ((maxChars - len) < noSpaceThreshold)
        		{
					remainingChars.setTextColor(noSpace);
        		}
        		else if ((maxChars - len) < warningThreshold)
        		{
        			remainingChars.setTextColor(warning);
        		}
        		else
        		{
        			remainingChars.setTextColor(ok);
        		}
			}

			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) 
			{

			}

			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) 
			{

			}
        });
    }
    
    private void Init()
    {
    	Resources res = getResources();
        ok = res.getColor(R.color.ok);
        noSpace = res.getColor(R.color.noSpace);
        warning = res.getColor(R.color.warning);
        
        maxChars = res.getInteger(R.integer.maxChars);
        warningThreshold = res.getInteger(R.integer.warningThreshold);
        noSpaceThreshold = res.getInteger(R.integer.noSpaceThreshold);
    }
    
    public void onClick(View v) 
    {
    	Log.d("YambaClient", "onClicked called");
    	
    	Intent msg = new Intent(this, UpdateStatusService.class);
    	msg.putExtra(UpdateStatusService.MessageKey, editText.getText().toString());
    	startService(msg);
    	finish();
    	
    	/*
    	new AsyncTask<String, Void, Void>() 
    	{
			@Override
			protected Void doInBackground(String... params) 
			{
				if(twitter == null)
				{
					twitter = new Twitter(prefs.getString("username", ""), prefs.getString("password", ""));
			        twitter.setAPIRootUrl(prefs.getString("site_url", ""));
				}
				
				twitter.updateStatus(params[0]);
				return null;
			}
    		
		}.execute(editText.getText().toString());
		*/
    }
}