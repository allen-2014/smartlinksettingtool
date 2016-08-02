/**
 * 
 */
package com.sola.smartlinksettingtool;




import android.R.string;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class IntelligentSwitchInfoFragment extends Fragment {
	
	public static final String sEXTRA_DATA_ID = "com.sola.smartlinksetingtool.intelligentswitchinfofragment.data";
	
	private TextView mTextViewEquipmentType;
	private TextView mTextViewMAC;
	private Button mBtn2EquipmentSetting;
	
	//ssid afford by equipment,get by barcode
	private String mEquipmentSSID;
	
	public static Fragment newInstance(String value)
	{
		IntelligentSwitchInfoFragment fragment = new IntelligentSwitchInfoFragment();
		Bundle bundle = new Bundle();
		bundle.putString(IntelligentSwitchInfoFragment.sEXTRA_DATA_ID, value);
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
		View v = inflater.inflate(R.layout.fragment_intelligent_switch_info, parent, false);

		String[] data = getArguments().getString(sEXTRA_DATA_ID).split(":");
		
		mTextViewEquipmentType = (TextView)v.findViewById(R.id.textview_equipment_type);
		
		mTextViewMAC = (TextView)v.findViewById(R.id.textview_mac);
		if(data != null && data.length >= 3)
		{
			mTextViewEquipmentType.setText(data[0]);
			mTextViewMAC.setText(data[1]);
			SwitchModule.curModuleHiFiName = data[2];
			SwitchModule.curModuleType = data[0];
			//SwitchModule.moduleHiFiPswd = data[3];
		}
		
		//find view by id in this layer or below this layer
		mBtn2EquipmentSetting = (Button)v.findViewById(R.id.btn_to_quipment_setting);
		mBtn2EquipmentSetting.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				//goto setting activity
				Intent intent = new Intent(v.getContext(), SmartLinkTypeSelectionActivity.class);

				startActivity(intent);
			}
			
		});
		
		return v;
	}
	

	
}
