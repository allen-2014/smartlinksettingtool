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
public class SmartLinkTypeSelectionActivity extends SingleFragmentActivity {
	
	private TextView mTextView;

	@Override
	protected Fragment createFragment()
	{
		
		mTextView = (TextView)findViewById(R.id.toolbar_title);
		if(mTextView != null)
		{
			mTextView.setText("设备连接");
			
		}
		
		ActionBar ab = getSupportActionBar();
		if(ab != null)
		{
			
			ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.sola_primary_color_inverse)));
			
			ab.setHomeAsUpIndicator(R.drawable.sola_navigation_icon_inverse_19);
			((TextView)findViewById(R.id.toolbar_title)).setTextColor(getResources().getColor(R.color.sola_text_color_inverse));
		}
		
		
		return new SmartLinkTypeSelectionFragment();
	}
	
	@Override
	protected Context getCurContext()
	{
		return SmartLinkTypeSelectionActivity.this;
	}

	
}
