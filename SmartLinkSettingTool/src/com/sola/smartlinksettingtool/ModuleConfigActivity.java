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
public class ModuleConfigActivity extends SingleFragmentActivity {

	private TextView mTextView;

	/* (non-Javadoc)
	 * @see com.sola.smartlinksettingtool.SingleFragmentActivity#createFragment()
	 */
	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		
		mTextView = (TextView)findViewById(R.id.toolbar_title);
		if(mTextView != null)
		{
			mTextView.setText("–¥»Î≈‰÷√");
			
		}
		
		ActionBar ab = getSupportActionBar();
		if(ab != null)
		{
			
			ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.sola_primary_color_inverse)));
			
			ab.setHomeAsUpIndicator(R.drawable.sola_navigation_icon_inverse_19);
			
			((TextView)findViewById(R.id.toolbar_title)).setTextColor(getResources().getColor(R.color.sola_text_color_inverse));
		}
		
		String data = getIntent().getStringExtra(ModuleConfigFragment.sEXTRA_DATA_ID);
		
		return ModuleConfigFragment.newInstance(data);
	}
	
	
	
	@Override
	protected Context getCurContext()
	{
		return ModuleConfigActivity.this;
	}

}
