/**
 * 
 */
package com.sola.smartlinksettingtool;

import java.util.ArrayList;
import java.util.List;

import com.sola.smartlinksettingtool.WifiWraper.WifiStruct;

import android.R.anim;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Administrator
 * 
 */
public class ModuleConfigWifiListFragment extends ListFragment {

	public static final String sEXTRA_DATA_ID = "com.sola.smartlinksetting.moduleconfigwifilistfragment.data";

	private static final String TAG = "ModuleConfigWifiListFragment";

	ArrayList<WifiStruct> mWifiList;
	//private CallbackListener mListenner;
	private MessageHandler msgHandler;
	
	private ListView listViewWifi;
	
	private View fragmentView;

	public static Fragment newInstance(String value) {

		ModuleConfigWifiListFragment fragment = new ModuleConfigWifiListFragment();
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
		fragmentView = inflater.inflate(R.layout.fragment_module_config_wifi_list, parent, false);
		
		InitSettings(fragmentView);
		
		return fragmentView;
				
	}
	
	

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		try {
			//mListenner = (CallbackListener) activity;
		} catch (ClassCastException e) {
			Log.e(TAG, e.toString());
		}
	}
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//free msgHandler
		WifiWraper.getInstance(getContext().getApplicationContext()).unInit();
	}
	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup parent,
	// Bundle savedInstanceState)
	// {
	//
	// //生成frame视图,false:不直接添加给父视图
	// View fragmentView = inflater.inflate(R.layout.fragment_module_config_ip,
	// parent, false);
	//
	//
	// //
	// InitSettings();
	// //
	// SwitchModule.configInfo.mNearIP =
	// ((EditText)fragmentView.findViewById(R.id.editText_nearIP_setting)).getText().toString();
	// SwitchModule.configInfo.mSubMask =
	// ((EditText)fragmentView.findViewById(R.id.editText_subMask_setting)).getText().toString();
	// SwitchModule.configInfo.mGateway =
	// ((EditText)fragmentView.findViewById(R.id.editText_gateway_setting)).getText().toString();
	// SwitchModule.configInfo.mDNS =
	// ((EditText)fragmentView.findViewById(R.id.editText_dns_setting)).getText().toString();
	// //
	// return fragmentView;
	// }

	private void InitSettings(View v) {

//		LinearLayout layout = (LinearLayout) getActivity().findViewById(
//				R.id.linearlayout_config_wifi_status_tip);
//		layout.setVisibility(View.VISIBLE);

		mWifiList = WifiWraper
				.getInstance(v.getContext().getApplicationContext())
				.getWifiStructList();
		
		listViewWifi = (ListView)v.findViewById(android.R.id.list);
	
		WifiAdapter adapter = new WifiAdapter(R.layout.fragment_module_config_wifi_list_item, R.id.textview_config_wifi, mWifiList);

		listViewWifi.setAdapter(adapter);
		
		//setListAdapter(adapter);
		
		msgHandler = new MessageHandler();
		
		WifiWraper.getInstance(v.getContext().getApplicationContext()).init(msgHandler);
		
		WifiWraper.getInstance(v.getContext().getApplicationContext()).periodWifiStructList();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		// refresh listview for view
		WifiAdapter adapter = new WifiAdapter(R.layout.fragment_module_config_wifi_list_item, R.id.textview_config_wifi,mWifiList);

		listViewWifi.setAdapter(adapter);
		
	}
	


	// @Override
	// public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	// {
	// super.onCreateOptionsMenu(menu, inflater);
	// inflater.inflate(R.menu.fragment_crime_list, menu);
	//
	// }

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		// Log.d(TAG, c.getmTitle() + "was clicked");
		// connect to ssid,出个等待框
		SwitchModule.configInfo.mWifiName = ((WifiAdapter) listViewWifi.getAdapter())
				.getItem(position).mWifiName;
		SwitchModule.configInfo.mWifiEncryptType = ((WifiAdapter) listViewWifi.getAdapter())
				.getItem(position).mWifiEncryptType;
		
		//setListAdapter(adapter);
		//
		//mListenner.indicateUp(sEXTRA_DATA_ID, null);
		Intent intent = new Intent(getContext(),
				ModuleConfigActivity.class);
		startActivity(intent);
		getActivity().finish();

	}

	private class WifiAdapter extends ArrayAdapter<WifiStruct> {
		public WifiAdapter(int resource, int textViewId, ArrayList<WifiStruct> wifis) {
			
			super(getActivity(), resource, textViewId,wifis);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.fragment_module_config_wifi_list_item, null);
			}
			//
			TextView textViewWifiName = (TextView) convertView
					.findViewById(R.id.textview_config_wifi);
			
//			LinearLayout linearLayout = (LinearLayout) convertView
//					.findViewById(R.id.linearlayout_config_wifi_select);
					
			
			textViewWifiName.setText(getItem(position).mWifiName);
			ImageView signalIcon = (ImageView)convertView.findViewById(R.id.imageview_config_wifi_signal);
			//for list view header
//			if(getItem(position).mWifiName.equalsIgnoreCase(WifiWraper.sWIFI_NAME))
//			{
//				
//				linearLayout.setBackgroundColor(getResources().getColor(R.color.sola_window_background_color));
//				textViewWifiName.setTextColor(getResources().getColor(R.color.sola_section_title_color_inverse));
//				
///*				LayoutParams params = new LayoutParams(260, R.dimen.sola_section_title_height);
//				params.setMarginStart(R.dimen.sola_margin_left);
//				
//				textViewWifiName.setLayoutParams(params);*/
//
//				signalIcon.setVisibility(View.GONE);
//				return convertView;
//			}
			//
			// Configure the view for this crime
			
			if(getItem(position).mSignalLevel == 2)
			{
				signalIcon.setBackground(getResources().getDrawable(R.drawable.sola_3_signal_icon));
			}
			else if(getItem(position).mSignalLevel == 1)
			{
				signalIcon.setBackground(getResources().getDrawable(R.drawable.sola_2_signal_icon));
			}
			else if(getItem(position).mSignalLevel == 0)
			{
				signalIcon.setBackground(getResources().getDrawable(R.drawable.sola_1_signal_icon));
			}
			//set signal icon visible
			if (SwitchModule.configInfo.mWifiName != null) {
				if (SwitchModule.configInfo.mWifiName
						.equals(getItem(position).mWifiName)) {
					ImageView v = (ImageView) convertView
							.findViewById(R.id.imageview_config_wifi_select_ok);
					v.setVisibility(View.VISIBLE);
					//Utils.showInfo(fragmentView.getContext(), "" + position);
				}
				else {
					ImageView v = (ImageView) convertView
							.findViewById(R.id.imageview_config_wifi_select_ok);
					v.setVisibility(View.INVISIBLE);
				}
			}
			

			return convertView;

		}
	}

	@SuppressLint("HandlerLeak")
	private class MessageHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			// receiverData(msg.what);
			if (msg.what == 1) {
				
				//refresh list view
				ArrayList list = msg.getData().getParcelableArrayList(WifiWraper.sDATA_EXTRA_ID);
				
				mWifiList = (ArrayList<WifiStruct>)list.get(0);
				
				WifiAdapter adapter = new WifiAdapter(R.layout.fragment_module_config_wifi_list_item, R.id.textview_config_wifi, mWifiList);
				
				listViewWifi.setAdapter(adapter);
				
				//setListAdapter(adapter);
				
			}
		}
	}
}
