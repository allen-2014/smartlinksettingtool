/**
 * 
 */
package com.sola.smartlinksettingtool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ContentFrameLayout.OnAttachListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * @author Administrator
 *
 */
public class ModuleConfigIPFragment extends Fragment {
	
	private static final String TAG ="ModuleConfigIPFragment"; 
	
	public static final String sEXTRA_DATA_ID = "com.sola.smartlinksettingtool.moduleconfigipfragment.data";
	
	private RadioButton mRadioDHCP;
	private RadioButton mRadioStatic;
	private RadioGroup mRGroupIP;
	private EditText mEditTextGatewaySetting;
	private EditText mEditTextDnsSetting;
	private EditText mEditTextNearIPSetting;
	private EditText mEditTextSubMaskSetting;
	
	private Button mButtonOk;
	
	private View fragmentView;
	
	
	private CallbackListener mListenner;
	
	public static Fragment newInstance(String value) {
		
		ModuleConfigIPFragment fragment = new ModuleConfigIPFragment();
		Bundle bundle = new Bundle();
		bundle.putString(sEXTRA_DATA_ID, value);
		fragment.setArguments(bundle);
		return fragment;
		
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
	{
		
		//生成frame视图,false:不直接添加给父视图
		fragmentView = inflater.inflate(R.layout.fragment_module_config_ip, parent, false);
		//
		InitSettings();
		//
		SwitchModule.configInfo.mNearIP = ((EditText)fragmentView.findViewById(R.id.editText_nearIP_setting)).getText().toString();
		SwitchModule.configInfo.mSubMask = ((EditText)fragmentView.findViewById(R.id.editText_subMask_setting)).getText().toString();
		SwitchModule.configInfo.mGateway = ((EditText)fragmentView.findViewById(R.id.editText_gateway_setting)).getText().toString();
		SwitchModule.configInfo.mDNS = ((EditText)fragmentView.findViewById(R.id.editText_dns_setting)).getText().toString();
		//
		return fragmentView;
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		
		super.onAttach(activity);
        try {
        	mListenner = (CallbackListener) activity;
        }catch (ClassCastException e){
            Log.e(TAG, e.toString());
        }
	}
	

	private void InitSettings()
	{
		//mIPType = "DHCP";
		SwitchModule.configInfo.mIPType = "DHCP";
		//DHCP或STATIC事件
    	mRadioDHCP = (RadioButton)fragmentView.findViewById(R.id.radioButton_dhcp_setting);
    	mRadioStatic = (RadioButton)fragmentView.findViewById(R.id.radioButton_static_setting);
    	
    	mButtonOk = (Button)fragmentView.findViewById(R.id.btn_ok_setting);
    	
    	mRGroupIP = (RadioGroup)fragmentView.findViewById(R.id.radioGroup_setting);
    	mRGroupIP.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId == mRadioDHCP.getId())
				{
					//
					//mIPType = "DHCP";
					SwitchModule.configInfo.mIPType = "DHCP";
					setControlEnable(false);
				}
				else if(checkedId == mRadioStatic.getId())
				{
					//
					//mIPType = "static";
					SwitchModule.configInfo.mIPType = "static";
					setControlEnable(true);
				}
			}
		});
    	
    	//
    	mButtonOk.setOnClickListener(new View.OnClickListener()
    	{
    		public void onClick(View v)
    		{
    			if(mRadioDHCP.isChecked())
    			{
    				SwitchModule.configInfo.mNearIP = SwitchModule.defaultNearIP;
    				SwitchModule.configInfo.mSubMask = SwitchModule.defaultSubMask;
    				SwitchModule.configInfo.mDNS = SwitchModule.defaultDns;
    				SwitchModule.configInfo.mGateway = SwitchModule.defaultGateway;
    			}
    			else if(mRadioStatic.isChecked())
    			{
    				SwitchModule.configInfo.mNearIP = mEditTextNearIPSetting.getText().toString();
    				SwitchModule.configInfo.mSubMask = mEditTextSubMaskSetting.getText().toString();
    				SwitchModule.configInfo.mDNS = mEditTextDnsSetting.getText().toString();
    				SwitchModule.configInfo.mGateway = mEditTextGatewaySetting.getText().toString();
    			}
    			//
    			//mListenner.indicateUp(sEXTRA_DATA_ID, null);
    			Intent intent = new Intent(fragmentView.getContext(),
						ModuleConfigActivity.class);
    			startActivity(intent);
    			getActivity().finish();
    		}
    	});
    	//
    	
    	mEditTextNearIPSetting = (EditText)fragmentView.findViewById(R.id.editText_nearIP_setting);
    	mEditTextSubMaskSetting = (EditText)fragmentView.findViewById(R.id.editText_subMask_setting);
    	mEditTextGatewaySetting = (EditText)fragmentView.findViewById(R.id.editText_gateway_setting);
    	mEditTextDnsSetting = (EditText)fragmentView.findViewById(R.id.editText_dns_setting);
    	
    	mEditTextNearIPSetting.setText(SwitchModule.defaultNearIP);
    	mEditTextSubMaskSetting.setText(SwitchModule.defaultSubMask);
    	mEditTextDnsSetting.setText(SwitchModule.defaultDns);
    	mEditTextGatewaySetting.setText(SwitchModule.defaultGateway);
    	
    	if(mRadioDHCP.isChecked())
    	{
    		setControlEnable(false);
    	}
    	else 
    	{
    		setControlEnable(true);
		}
    	
	}
	//
	private void setControlEnable(boolean bTrue)
	{
		mEditTextNearIPSetting.setEnabled(bTrue);
		mEditTextSubMaskSetting.setEnabled(bTrue);
		mEditTextGatewaySetting.setEnabled(bTrue);
		mEditTextDnsSetting.setEnabled(bTrue);
	}
	
	
}
