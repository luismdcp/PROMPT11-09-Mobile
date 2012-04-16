package YambaClient.Package;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TweetsListView extends ListActivity
{
	private YambaClientApplication app;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	  super.onCreate(savedInstanceState);
	  app = (YambaClientApplication) getApplication();
	  Log.d("TweetsListView", "onCreate");
	  
	  setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, app.tweets));

	  ListView lv = getListView();
	  lv.setTextFilterEnabled(true);

	  lv.setOnItemClickListener(new OnItemClickListener() 
	  {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
	  });
	}
}
