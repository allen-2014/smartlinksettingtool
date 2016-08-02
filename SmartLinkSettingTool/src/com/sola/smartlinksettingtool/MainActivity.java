package com.sola.smartlinksettingtool;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


import android.content.Intent;
import android.content.res.Resources;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements OnItemClickListener{
	//for test
	private Button mbtnNext;
	private ImageView mImageViewToolbarActionButton;
	private ListPopupWindow listPopupWindow;
	//
	private TextView mTextViewTitle;
	//varibles
	private static final String TAG = "MainActivity";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
       
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("");
	    setSupportActionBar(myToolbar);
		
	    myToolbar.setNavigationIcon(R.drawable.sola_navigation_icon_19);
	    
	    // Get a support ActionBar corresponding to this toolbar
	    ActionBar ab = getSupportActionBar();
	    
	    if(ab != null)
	    {
	    	ab.setDisplayHomeAsUpEnabled(false);
	    }
	    mTextViewTitle = (TextView)findViewById(R.id.toolbar_title);
	    if(mTextViewTitle != null)
	    {
	    	mTextViewTitle.setText("开关列表");
	    }
        
        
//        int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
//        if(titleId == 0)
//        	titleId = com.actionbarsherlock.R.id.abs__action_bar_title;
       
        mbtnNext = (Button)findViewById(R.id.btnNext);
        mbtnNext.setOnClickListener(new View.OnClickListener(){
        	@Override
			public void onClick(View v) {
        		Intent  i = new Intent(MainActivity.this, SmartLinkTypeSelectionActivity.class);
        		startActivity(i);
        	}
        });
        
        
        mImageViewToolbarActionButton = (ImageView)findViewById(R.id.toolbar_action_button);
        mImageViewToolbarActionButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showListPopupMenu(v);
			}
		});
        
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.abs_textview);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	
        //getMenuInflater().inflate(R.menu.main, menu);
        
        return true;
    }
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    	
        int id = item.getItemId();
        switch (id) {
//		case R.id.menu_item_bar_code:
//			Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
//			startActivityForResult(openCameraIntent, 0);
//
//			break;

		default:
			break;
		}
 
        return super.onOptionsItemSelected(item);
    }
    
    
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
            if (menu != null) {
                if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                    try {
                        Method m = menu.getClass().getDeclaredMethod(
                                "setOptionalIconsVisible", Boolean.TYPE);
                        m.setAccessible(true);
                        m.invoke(menu, true);
                    } catch (Exception e) {
                        Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
                    }
                }
            }
        return super.onPrepareOptionsPanel(view, menu);
    }
    
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            //
            Intent intent = new Intent(MainActivity.this, IntelligentSwitchInfoActivity.class);
            intent.putExtra(IntelligentSwitchInfoFragment.sEXTRA_DATA_ID, scanResult);
            startActivity(intent);
        }
        
    }
    
    //list pupup window
    @Override
    public void onItemClick(AdapterView<?> parent, View view,
        int position, long id) {
    	// TODO Auto-generated method stub
		switch (position) {
		case 0:
			Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
			startActivityForResult(openCameraIntent, 0);
			break;

		default:
			break;
		}
		
        
    }
    
	private void showListPopupMenu(View v) {
		if(listPopupWindow != null)
		{
			listPopupWindow.show();
			return;
		}
		ListPopupWindow listPopupWindow = new ListPopupWindow(this);
		listPopupWindow.setOnItemClickListener(MainActivity.this);
		

		listPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.sola_list_popup_window_bg));
		// Converts 250 dip into its equivalent px
		Resources r = getResources();
		float pxWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 135, r.getDisplayMetrics());
		
		listPopupWindow.setWidth((int)pxWidth);
		listPopupWindow.setModal(true);

		listPopupWindow.setAnchorView(v);
		
        List<PopMenuItem> ListMenu = new  ArrayList<PopMenuItem>();
        PopMenuItem menu = new PopMenuItem();
        menu.setIconId(R.drawable.sola_item_barcode_icon_35);
        menu.setContent("扫一扫");
        ListMenu.add(menu);
        //
        menu = new PopMenuItem();
        menu.setIconId(R.drawable.sola_item_time_icon_35);
        menu.setContent("定时功能");
        ListMenu.add(menu);
        //
        listPopupWindow.setAdapter(new PopMenuAdapter(this, R.layout.v7_apt_list_popup_window, ListMenu));
        listPopupWindow.show();
	}
    
}
