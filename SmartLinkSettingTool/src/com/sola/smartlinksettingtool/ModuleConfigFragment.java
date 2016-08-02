/**
 * 
 */
package com.sola.smartlinksettingtool;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.sola.smartlinksettingtool.SwitchModule.ConfigInfo;
import com.sola.uicomponent.CustomSpinnerAdapter;

import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.FrameStats;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * @author Administrator
 * 
 */
public class ModuleConfigFragment extends Fragment {

	public static final String sEXTRA_DATA_ID = "com.sola.smartlinksettingtool.moduleconfigfragment.data";
	private String TAG = "ModuleConfigurationFragment";
	private View fragmentView;
	//
	private String mIPType = "";
	private Button mBtnSubmit;
	private MsgHeader header;
	//
	// private Spinner mSpinnerWifi;
	// private Spinner mSpinnerWifiEncryptType;
	// private CheckBox mCheckBoxShowPass;

	private LinearLayout mLinearLayoutWifi;
	// private LinearLayout mLinearLayoutEncryptType;
	private LinearLayout mLinearLayoutIP;
	private TextView mTextViewConfigModuleIp;
	private TextView mTextViewWifiName;
	private TextView mTextViewEncryptType;
	//
	private String mWifiEncryptItem;

	public static Fragment newInstance(String value) {
		
		ModuleConfigFragment fragment = new ModuleConfigFragment();
		Bundle bundle = new Bundle();
		bundle.putString(sEXTRA_DATA_ID, value);
		fragment.setArguments(bundle);
		return fragment;
	}

	//
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {

		// 生成frame视图,false:不直接添加给父视图
		fragmentView = inflater.inflate(R.layout.fragment_module_config,
				parent, false);

		initSettings();

		//
		return fragmentView;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		setNearIpInfo();
		
		setWifiInfo();
	}

/*	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RequestCode.moduleConfigIPCode:
			if(resultCode == Activity.RESULT_OK)
			{
				setNearIpInfo();
			}
			break;
		case RequestCode.moduleConfigWifiListCode:
			if(resultCode == Activity.RESULT_OK)
			{
				setWifiInfo();
			}
		default:
			break;
		}
		
	}*/
	
	
	private void setNearIpInfo()
	{
		if(SwitchModule.configInfo.mIPType == null)
		{
			return;
		}
		//
		if(SwitchModule.configInfo.mIPType.equalsIgnoreCase("DHCP"))
		{
			mTextViewConfigModuleIp.setText("DHCP");
		}
		else if(SwitchModule.configInfo.mIPType.equalsIgnoreCase("static"))
		{
			mTextViewConfigModuleIp.setText(SwitchModule.configInfo.mNearIP);
		}
	}
	
	private void setWifiInfo()
	{
		if(SwitchModule.configInfo.mWifiName == null || SwitchModule.configInfo.mWifiEncryptType == null)
		{
			return;
		}
		mTextViewWifiName.setText(SwitchModule.configInfo.mWifiName);
		mTextViewEncryptType.setText(SwitchModule.configInfo.mWifiEncryptType);
	}

	private void initSettings() {
		
		mTextViewWifiName = (TextView)fragmentView.findViewById(R.id.textview_config_wifi);
		
		mTextViewEncryptType = (TextView)fragmentView.findViewById(R.id.textview_config_encrypttype);
		
		mTextViewConfigModuleIp = (TextView)fragmentView.findViewById(R.id.textview_config_module_ip);
		
		mLinearLayoutWifi = (LinearLayout) fragmentView
				.findViewById(R.id.linearlayout_wifi_select);
		// mLinearLayoutEncryptType =
		// (LinearLayout)fragmentView.findViewById(R.id.linearlayout_encrypttype_select);
		mLinearLayoutIP = (LinearLayout) fragmentView
				.findViewById(R.id.linearlayout_ip_select);
		
		
		setNearIpInfo();
		
		setWifiInfo();

		mLinearLayoutWifi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(fragmentView.getContext(),
						ModuleConfigWifiListActivity.class);
				startActivity(intent);
			}
		});
		//
		mLinearLayoutIP.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						ModuleConfigIPActivity.class);
				
				startActivity(intent);
				
			}
		});
		// 提交事件

		// mSpinnerWifi =
		// (Spinner)fragmentView.findViewById(R.id.spinner_configuration_wifi_selection);
		// mSpinnerWifiEncryptType =
		// (Spinner)fragmentView.findViewById(R.id.spinner_configuration_wifi_encrypt_types);
		// mCheckBoxShowPass =
		// (CheckBox)fragmentView.findViewById(R.id.checkbox_setting_show_pass);

		mBtnSubmit = (Button) fragmentView.findViewById(R.id.btn_submit);
		// mSettingSubmitButtion.setEnabled(false);
		mBtnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				SwitchModule.configInfo.mWifiName = ((TextView) fragmentView
						.findViewById(R.id.textview_config_wifi))
						.getText().toString();
				SwitchModule.configInfo.mWifiEncryptType = ((TextView) fragmentView
						.findViewById(R.id.textview_config_encrypttype))
						.getText().toString();
				SwitchModule.configInfo.mWifiPwd = ((EditText) fragmentView
						.findViewById(R.id.editText_wifiPwd_setting)).getText()
						.toString();
				SwitchModule.configInfo.mFarIP = ((EditText) fragmentView
						.findViewById(R.id.editText_farIP_setting)).getText()
						.toString();
				SwitchModule.configInfo.mFarPort = ((EditText) fragmentView
						.findViewById(R.id.editText_farPort_setting)).getText()
						.toString();

				boolean bPortInt = true;
				try {
					Integer.parseInt(SwitchModule.configInfo.mFarPort, 10);
				} catch (Exception e) {
					// TODO: handle exception
					bPortInt = false;
				}
				if (!bPortInt) {
					Utils.showInfo(fragmentView.getContext(), "端口格式有误");
					return;
				}

				if (!Utils.bIp(SwitchModule.configInfo.mFarIP)) {
					Utils.showInfo(fragmentView.getContext(), "服务器IP格式有误");
					return;
				}
				if (!Utils.bIp(SwitchModule.configInfo.mNearIP)) {
					Utils.showInfo(fragmentView.getContext(), "本地IP格式有误");
					return;
				}
				if (!Utils.bIp(SwitchModule.configInfo.mSubMask)) {
					Utils.showInfo(fragmentView.getContext(), "子网掩码格式有误");
					return;
				}

				if (!Utils.bIp(SwitchModule.configInfo.mGateway)) {
					Utils.showInfo(fragmentView.getContext(), "网关格式有误");
					return;
				}

				if (!Utils.bIp(SwitchModule.configInfo.mDNS)) {
					Utils.showInfo(fragmentView.getContext(), "DNS格式有误");
					return;
				}

				// accroding to the module type:Y01,Y02,Y03
				if (SwitchModule.curModuleType.equals(SwitchModule.CKS01)) {
					ConfigCKS01(SwitchModule.configInfo);
				} else if (SwitchModule.curModuleType
						.equals(SwitchModule.CKY02)) {

				} else if (SwitchModule.curModuleType
						.equals(SwitchModule.CKY03)) {
					ConfigCKY03(SwitchModule.configInfo);
				}

			}
		});
		//
		// List<String> ssidList =
		// WifiWraper.getInstance(fragmentView).getSSIDlist();
		// simple_spinner_dropdown_item
		// CustomSpinnerAdapter customSpinnerAdapter = new
		// CustomSpinnerAdapter(fragmentView.getContext(),ssidList);
		// mSpinnerWifi.setAdapter(customSpinnerAdapter);

		// customSpinnerAdapter = new
		// CustomSpinnerAdapter(fragmentView.getContext(),WifiEncryptTypes.getAllTypes());
		// mSpinnerWifiEncryptType.setAdapter(customSpinnerAdapter);
		// ArrayAdapter<String> adapter = new
		// ArrayAdapter<String>(fragmentView.getContext(),
		// android.R.layout.simple_spinner_dropdown_item,ssidList);
		// mSpinnerWifi.setAdapter(adapter);

		// adapter = new ArrayAdapter<String>(fragmentView.getContext(),
		// android.R.layout.simple_spinner_dropdown_item,WifiEncryptTypes.getAllTypes());
		// mSpinnerWifiEncryptType.setAdapter(adapter);
		// mSpinnerWifiEncryptType.setOnItemSelectedListener(new
		// OnItemSelectedListener() {
		// @Override
		// public void onItemSelected(AdapterView<?> parent, View view, int
		// position, long id)
		// {
		//
		//
		//
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> parent)
		// {
		//
		// }
		//
		// });

		// //
		// mCheckBoxShowPass.setOnCheckedChangeListener(new
		// OnCheckedChangeListener(){
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView, boolean
		// isChecked)
		// {
		// if(isChecked)
		// {
		// mEditTextWiFiPswd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		// }
		// else
		// {
		// mEditTextWiFiPswd.setInputType(InputType.TYPE_CLASS_TEXT |
		// InputType.TYPE_TEXT_VARIATION_PASSWORD);
		// }
		// }
		// });

	}

	private void ConfigCKS01(ConfigInfo info) {
		/******
		 * example********* config:at+netmode=2
		 * at+wifi_conf=test,wpawpa2_aes,12345678 at+dhcpc=1
		 * at+net_ip=192.168.0.99,255.255.255.0,192.168.0.1
		 * at+net_dns=192.168.0.1,0.0.0.0 at+remotepro=tcp at+mode=client
		 * at+remoteip=192.168.0.108 at+remoteport=9003 end of example
		 ****/
		// wifi
		String instructionContent = "config:at+netmode=2\r\n";
		instructionContent += "at+wifi_conf=";
		instructionContent += info.mWifiName
				+ ","
				+ WifiEncryptTypes
						.getWiFiEncryptStrforConfig(info.mWifiEncryptType)
				+ ",";
		instructionContent += SwitchModule
				.getHiFiPswd(SwitchModule.curModuleType);
		instructionContent += "\r\n";
		//
		// ip etc.
		instructionContent += "at+dhcpc=";
		if (info.mIPType.equalsIgnoreCase("dhcp")) {
			instructionContent += "1\r\n";

			instructionContent += "at+net_ip=0.0.0.0,0.0.0.0,0.0.0.0\r\n";

			instructionContent += "at+net_dns=0.0.0.0,0.0.0.0\r\n";

		} else if (info.mIPType.equalsIgnoreCase("static")) {
			instructionContent += "0\r\n";

			instructionContent += "at+net_ip=" + info.mNearIP + ","
					+ info.mSubMask + "," + info.mGateway + "\r\n";

			instructionContent += "at+net_dns=" + info.mDNS + ","
					+ "0.0.0.0\r\n";

		}
		//
		instructionContent += "at+remotepro=tcp\r\n";
		instructionContent += "at+mode=client\r\n";
		instructionContent += "at+remoteip=" + info.mFarIP + "\r\n";
		instructionContent += "at+remoteport=" + info.mFarPort + "\r\n";
		//
		try {
			header = new MsgHeader();
			byte[] contentBytes = instructionContent
					.getBytes(CommunicationClient.getInstance().US_ASCII());
			header.sLen = (short) (contentBytes.length + 1);// + checksum num of
															// byte
			byte[] headBytes = header.getBytes();
			InstructionSet.sendData = new byte[contentBytes.length
					+ MsgHeader.length + 1];// + checksum num of byte

			byte sumVal = 0;
			// head
			for (int i = 0; i < MsgHeader.length; i++) {
				InstructionSet.sendData[i] = headBytes[i];
				sumVal += InstructionSet.sendData[i];
			}
			// head + content
			int ttLen = headBytes.length + contentBytes.length;
			for (int i = MsgHeader.length; i < ttLen; i++) {
				InstructionSet.sendData[i] = contentBytes[i - MsgHeader.length];
				sumVal += InstructionSet.sendData[i];
			}
			InstructionSet.sendData[InstructionSet.sendData.length - 1] = sumVal;
			InstructionSet.encrypt(InstructionSet.sendData, InstructionSet.GetClientOffset(), InstructionSet.sendData.length - 1);
			// send
			CommunicationClient.getInstance().SendData(InstructionSet.sendData);

		} catch (Exception e) {
			// TODO: handle exception
			// throw new RuntimeException(e);
			Log.d(TAG, e.toString());
		}

	}

	//
	private void ConfigCKY02(ConfigInfo info) {

	}

	//
	private void ConfigCKY03(ConfigInfo info) {
		// String wifiName =
		// ((EditText)fragmentView.findViewById(R.id.wifiName_setting_editText)).getText().toString();

		String instructionContent = "config:";
		// instructionContent += head;
		instructionContent += "AT+WSSSID=" + info.mWifiName + "\r\n";
		instructionContent += "AT+WSKEY="
				+ WifiEncryptTypes
						.getWiFiEncryptStrforConfig(info.mWifiEncryptType)
				+ "," + info.mWifiPwd + "\r\n";
		instructionContent += "AT+NETP=TCP,Client," + info.mFarPort + ","
				+ info.mFarIP + "\r\n";

		if (mIPType.equalsIgnoreCase("static")) {
			instructionContent += "AT+WANN=static," + info.mNearIP + ","
					+ info.mSubMask + "," + info.mGateway + "\r\n";
		} else if (mIPType.equalsIgnoreCase("dhcp")) {
			instructionContent += "AT+WANN=DHCP,0.0.0.0,0.0.0.0,0.0.0.0\r\n";
		}

		try {

			byte[] contentBytes = instructionContent
					.getBytes(CommunicationClient.getInstance().US_ASCII());

			header = new MsgHeader();
			header.sLen = (short) (contentBytes.length + 1);
			byte[] headBytes = header.getBytes();
			InstructionSet.sendData = new byte[contentBytes.length
					+ headBytes.length + 1];
			byte sumVal = 0;
			// head
			for (int i = 0; i < headBytes.length; i++) {
				InstructionSet.sendData[i] = headBytes[i];
				sumVal += InstructionSet.sendData[i];
			}
			// head + content
			int ttLen = headBytes.length + contentBytes.length;
			for (int i = headBytes.length; i < ttLen; i++) {
				InstructionSet.sendData[i] = contentBytes[i - headBytes.length];
				sumVal += InstructionSet.sendData[i];
			}
			InstructionSet.sendData[InstructionSet.sendData.length - 1] = sumVal;
			//
			InstructionSet.encrypt(InstructionSet.sendData, InstructionSet.GetClientOffset(), InstructionSet.sendData.length - 1);
			// send
			CommunicationClient.getInstance().SendData(InstructionSet.sendData);

			// BigInteger bigInteger = new BigInteger(1, sendMsg.getBytes());
			//
			// String temp = bigInteger.toString(16);

			// sendData();
			// for test
			// host = "192.168.1.97";
			// port = 8899;
			// Connect();

			// toIP = "192.168.1.97";
			// toPort = "8899";
			// connectThread();

		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}

	}

	// private void initCustomSpinner() {
	//
	// Spinner spinnerCustom= (Spinner) findViewById(R.id.spinnerCustom);
	// // Spinner Drop down elements
	// ArrayList<String> languages = new ArrayList<String>();
	// languages.add("Andorid");
	// languages.add("IOS");
	// languages.add("PHP");
	// languages.add("Java");
	// languages.add(".Net");
	// CustomSpinnerAdapter customSpinnerAdapter=new
	// CustomSpinnerAdapter(MainActivity.this,languages);
	// spinnerCustom.setAdapter(customSpinnerAdapter);
	// spinnerCustom.setOnItemSelectedListener(new OnItemSelectedListener() {
	// @Override
	// public void onItemSelected(AdapterView<?> parent, View view, int
	// position, long id) {
	//
	// String item = parent.getItemAtPosition(position).toString();
	//
	// Toast.makeText(parent.getContext(),
	// "Android Custom Spinner Example Output..." + item,
	// Toast.LENGTH_LONG).show();
	// }
	//
	// @Override
	// public void onNothingSelected(AdapterView<?> parent) {
	//
	// }
	// });
	// }

}
