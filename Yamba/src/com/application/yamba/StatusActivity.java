package com.application.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends BaseActivity implements OnClickListener 
{
	private int ok, noSpace, warning, maxChars, warningThreshold, noSpaceThreshold;
	private static final String TAG = StatusActivity.class.getSimpleName();
	private EditText editText;
	private Button updateButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Init();
		setContentView(R.layout.status);
		
		final TextView remainingChars = (TextView) findViewById(R.id.textCount);
        remainingChars.setText("" + maxChars);
		editText = (EditText) findViewById(R.id.editText);
		updateButton = (Button) findViewById(R.id.buttonUpdate);
		updateButton.setOnClickListener(this);
		
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
	
	public void onClick(View v) 
	{
		String status = editText.getText().toString();
		new PostToTwitter().execute(status);
		Log.d(TAG, "onClicked");
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
	
	class PostToTwitter extends AsyncTask<String, Integer, String> 
	{
		@Override
	    protected String doInBackground(String... statuses) 
	    {
			try 
			{
				YambaApplication yamba = ((YambaApplication) getApplication());
				Twitter.Status status = yamba.getTwitter().updateStatus(statuses[0]);
		        return status.text;
		    } 
			catch (TwitterException e) 
			{
		        Log.e(TAG, e.toString());
		        e.printStackTrace();
		        return "Failed to post";
		    }
	    }
		
		@Override
	    protected void onProgressUpdate(Integer... values) 
	    {
	      super.onProgressUpdate(values);
	    }
		
		@Override
	    protected void onPostExecute(String result) 
	    {
	      Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
	    }
	}
}