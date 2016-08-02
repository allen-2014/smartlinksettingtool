/**
 * 
 */
package com.sola.smartlinksettingtool;


import android.R.anim;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
/**
 * @author Administrator
 *
 */
public abstract class SingleFragmentActivity extends AppCompatActivity 	implements CallbackListener{
	protected abstract Fragment createFragment();
	protected abstract Context getCurContext();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_fragment);

		Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
		myToolbar.setTitle("");
	    setSupportActionBar(myToolbar);
		

	    // Get a support ActionBar corresponding to this toolbar
	    ActionBar ab = getSupportActionBar();
	    
	    if(ab != null)
	    {
	    	ab.setDisplayHomeAsUpEnabled(true);
	    }
	    
		FragmentManager fm = getSupportFragmentManager();
		
		//
		//find view by id in this layer or below this layer
		Fragment fragment = fm.findFragmentById(R.id.fragment_container_single);
		if(fragment == null)
		{
			fragment = createFragment();
			
//			String str = getIntent().getStringExtra("hdModule");
//			if(str != null && !str.trim().equals(""))
//			{
//				if(savedInstanceState == null)
//				{
//					savedInstanceState = new Bundle();
//				}
//				savedInstanceState.putString("hdModule", str);
//				fragment.setArguments(savedInstanceState);
//			}

			fm.beginTransaction().add(R.id.fragment_container_single, fragment).commit();
		}
		
	}
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
//		 	MenuInflater inflater = getMenuInflater();  
//		    inflater.inflate(R.menu.main, menu);  
//		    return super.onCreateOptionsMenu(menu);
		 	//getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle action bar item clicks here. The action bar will
	        // automatically handle clicks on the Home/Up button, so long
	        // as you specify a parent activity in AndroidManifest.xml.
	        int id = item.getItemId();
//	        if (id == R.id.action_settings) {
//	            return true;
//	        }
	        switch (id) {
			case android.R.id.home:
				 finish();
				break;

			default:
				break;
			}
	        return super.onOptionsItemSelected(item);
	    }
	    
	    
//	    @Override
//	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//	        super.onActivityResult(requestCode, resultCode, data);
//	        switch (requestCode) {
//			case RequestCode.moduleConfigIPCode:
//				if(resultCode == RESULT_OK)
//				{
//					FragmentManager fm = getSupportFragmentManager();
//					Fragment fragment = fm.findFragmentById(R.id.fragment_container_single);
//					if(fragment != null)
//					{
//						fragment.onActivityResult(RequestCode.moduleConfigIPCode, RESULT_OK, data);
//					}
//				}
//				break;
//
//			default:
//				break;
//			}
//	        
//	    }
	    
		@Override
		public void indicateUp(String key,String value) {
			// TODO Auto-generated method stub
			Bundle args = new Bundle();
			args.putString(key, value);
			Intent resultIntent = new Intent();
	        resultIntent.putExtras(args);


	        this.setResult(RESULT_OK, resultIntent);

			finish();
		}

}
