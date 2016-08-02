/**
 * 
 */
package com.sola.smartlinksettingtool;


import com.sola.smartlinksettingtool.R.color;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * @author Administrator
 *
 */
public class IntelligentSwitchInfoActivity extends SingleFragmentActivity {
	private TextView mTextView;
	
	
	@Override
	protected Fragment createFragment()
	{

		mTextView = (TextView)findViewById(R.id.toolbar_title);
		if(mTextView != null)
		{
			mTextView.setText("×´Ì¬ÐÅÏ¢");
		}
		
		ActionBar ab = getSupportActionBar();
		if(ab != null)
		{
			
			ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.sola_primary_color_inverse)));
			
			ab.setHomeAsUpIndicator(R.drawable.sola_navigation_icon_inverse_19);
			((TextView)findViewById(R.id.toolbar_title)).setTextColor(getResources().getColor(R.color.sola_text_color_inverse));
		}
		
		String value = getIntent().getStringExtra(IntelligentSwitchInfoFragment.sEXTRA_DATA_ID);
		
		return IntelligentSwitchInfoFragment.newInstance(value);
	}
	
	@Override
	protected Context getCurContext()
	{
		return IntelligentSwitchInfoActivity.this;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		
		
	}
	
	
}
