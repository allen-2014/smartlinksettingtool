/**
 * 
 */
package com.sola.smartlinksettingtool;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class ModuleConfigWifiListActivity extends SingleFragmentActivity {

	private static final String TAG ="ModuleConfigWifiListActivity"; 
	public static final String sEXTRA_DATA_ID = "com.sola.smartlinksettingtool.moduleconfigipfragment.data";
	/* (non-Javadoc)
	 * @see com.sola.smartlinksettingtool.SingleFragmentActivity#createFragment()
	 */
	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		TextView mTextView = (TextView)findViewById(R.id.toolbar_title);
		if(mTextView != null)
		{
			mTextView.setText("—°‘ÒWifi");
		}
		
		ActionBar ab = getSupportActionBar();
		if(ab != null)
		{
			
			ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.sola_primary_color_inverse)));
			
			ab.setHomeAsUpIndicator(R.drawable.sola_navigation_icon_inverse_19);
			((TextView)findViewById(R.id.toolbar_title)).setTextColor(getResources().getColor(R.color.sola_text_color_inverse));
		}
		
		String data = getIntent().getStringExtra(ModuleConfigWifiListFragment.sEXTRA_DATA_ID);
		
		return ModuleConfigWifiListFragment.newInstance(data);
	}

	/* (non-Javadoc)
	 * @see com.sola.smartlinksettingtool.SingleFragmentActivity#getCurContext()
	 */
	@Override
	protected Context getCurContext() {
		// TODO Auto-generated method stub
		return null;
	}


}
