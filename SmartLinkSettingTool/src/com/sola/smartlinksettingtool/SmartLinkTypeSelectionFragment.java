/**
 * 
 */
package com.sola.smartlinksettingtool;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.hiflying.smartlink.ISmartLinker;
import com.hiflying.smartlink.OnSmartLinkListener;
import com.hiflying.smartlink.SmartLinkedModule;
import com.hiflying.smartlink.v3.SnifferSmartLinker;
import com.sola.smartlinksettingtool.Configuration1Activity.MessageHandler;
import com.sola.uicomponent.ProgressWheelDialog;

import android.R.bool;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnDismissListener;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.BoringLayout.Metrics;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * @author Administrator
 * 
 */
public class SmartLinkTypeSelectionFragment extends Fragment implements
		OnSmartLinkListener {

	private final static String TAG = "SmartLinkTypeSelectionFragment";
	// private Spinner mSpinner;
	private EditText mEditTextWIFIName;
	private EditText mEditTextWIFIPswd;
	private RadioGroup mRadioGroupSMType;
	private RadioButton mRadioBtnSmartLink;
	private RadioButton mRadioBtnAPLink;
	private ToggleButton mToggleButtonShowPass;
	private RelativeLayout mRelativeLayoutWifi;
	private TextView mTextViewSearchTip;
	private ImageView mImageViewSearchStatus;

	private TextView mTextViewWifiTitle;
	
	private ImageView mImageViewWifiWaitingStatus;

	private BroadcastReceiver mWifiChangedReceiver;
	private List<String> ssidList;

	protected ISmartLinker mSnifferSmartLinker;
	//protected ProgressDialog mWaitingDialog;
	protected ProgressWheelDialog mWaitingDialog;
	private boolean mIsConncting = false;
	private Button mBtnStartSearch;
	private String mSSID;// get by barcode
	private String mWifiPswd;//
	protected Handler mViewHandler = new Handler();
	private List<String> moduleIPList;
	private List<String> moduleMACList;
	private MessageHandler msgHandler = null;
	//
	private long heartCount = 0;
	private final static int HEART_MAX_COUNT = 180;// 180 times per 15min
	private MsgHeader header;
	private MsgHeader mHeaderforGather;
	//
	private View fragmentView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		// 生成frame视图,false:不直接添加给父视图
		fragmentView = inflater.inflate(
				R.layout.fragment_smartlink_type_selection, parent, false);
		//
		InitVariables();

		InitView(fragmentView);

		InitNetwork(fragmentView);

		InitSmartLink(fragmentView);

		return fragmentView;
	}

	private void InitVariables() {
		moduleIPList = new ArrayList<String>();
		moduleMACList = new ArrayList<String>();
	}

	private void InitView(final View v) {

		// onClick listen function in InitSmartLink
		mBtnStartSearch = (Button) v.findViewById(R.id.btn_start_search);
		mEditTextWIFIPswd = (EditText) v.findViewById(R.id.edittext_wifi_pass);
		mEditTextWIFIName = (EditText) v.findViewById(R.id.edittext_wifi_name);
		
		mToggleButtonShowPass = (ToggleButton) v
				.findViewById(R.id.checkbox_show_pass);

		mRelativeLayoutWifi = (RelativeLayout) v
				.findViewById(R.id.relativeLayout_wifi);

		mTextViewWifiTitle = (TextView) v
				.findViewById(R.id.textview_wifi_title);
		
		mTextViewSearchTip = (TextView)v.findViewById(R.id.textview_search_tip);
		
		mImageViewSearchStatus = (ImageView)v.findViewById(R.id.imageview_search_status);
		
		mImageViewWifiWaitingStatus = (ImageView)v.findViewById(R.id.imageview_wifi_waiting_status);
		//
		
		// mSpinner = (Spinner)v.findViewById(R.id.spinner_wifi_selection);
		// mSpinner.setOnItemSelectedListener(new
		// AdapterView.OnItemSelectedListener() {
		// @Override
		// public void onItemSelected(AdapterView<?> parent, View view, int
		// position, long id)
		// {
		//
		// }
		//
		// //
		// @Override
		// public void onNothingSelected(AdapterView<?> parent)
		// {
		//
		// }
		// });
		//

		mRadioBtnSmartLink = (RadioButton) v.findViewById(R.id.radio_smartlink);
		mRadioBtnAPLink = (RadioButton) v.findViewById(R.id.radio_aplink);

		mRadioGroupSMType = (RadioGroup) v
				.findViewById(R.id.radiogroup_smartlink_type_selection);
		mRadioGroupSMType
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@SuppressWarnings("deprecation")
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						if (checkedId == mRadioBtnSmartLink.getId()) {

							// mRadioBtnSmartLink.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
							// mRadioBtnAPLink.setBackgroundColor(getResources().getColor(android.R.color.background_light));
							//
							// mSpinner.setEnabled(true);
							mRelativeLayoutWifi.setVisibility(View.VISIBLE);
							mTextViewWifiTitle.setVisibility(View.VISIBLE);
							
							mTextViewSearchTip.setVisibility(View.GONE);
							mImageViewSearchStatus.setVisibility(View.GONE);
							
							mEditTextWIFIName.setEnabled(true);
							mEditTextWIFIPswd.setEnabled(true);

						} else if (checkedId == mRadioBtnAPLink.getId()) {
							// mRadioBtnAPLink.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
							// mRadioBtnSmartLink.setBackgroundColor(getResources().getColor(android.R.color.background_light));
							//
							// mSpinner.setEnabled(false);
							mRelativeLayoutWifi.setVisibility(View.GONE);
							mTextViewWifiTitle.setVisibility(View.INVISIBLE);
							//
							mTextViewSearchTip.setVisibility(View.VISIBLE);
							//
							SetLoadingImageViewInVisible(mImageViewSearchStatus);
							//
							mEditTextWIFIName.setEnabled(false);
							mEditTextWIFIPswd.setEnabled(false);
							//
						}
					}
				});

		mToggleButtonShowPass
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							mEditTextWIFIPswd
									.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
						} else {
							mEditTextWIFIPswd
									.setInputType(InputType.TYPE_CLASS_TEXT
											| InputType.TYPE_TEXT_VARIATION_PASSWORD);
						}
					}
				});

		if (SwitchModule.curModuleType.equals(SwitchModule.CKS01)) {
			mRadioBtnAPLink.setChecked(true);
			mRadioBtnAPLink.setEnabled(false);
			mRadioBtnSmartLink.setEnabled(false);
		}

		if (mRadioBtnSmartLink.isChecked()) {
			// mSpinner.setEnabled(true);
			mRelativeLayoutWifi.setVisibility(View.VISIBLE);
			mTextViewWifiTitle.setVisibility(View.VISIBLE);
			//
			mTextViewSearchTip.setVisibility(View.GONE);
			mImageViewSearchStatus.setVisibility(View.GONE);
			//
			mEditTextWIFIName.setEnabled(true);
			mEditTextWIFIPswd.setEnabled(true);
		} else if (mRadioBtnAPLink.isChecked()) {
			// mSpinner.setEnabled(false);
			mRelativeLayoutWifi.setVisibility(View.GONE);
			mTextViewWifiTitle.setVisibility(View.INVISIBLE);
			mEditTextWIFIName.setEnabled(false);
			mEditTextWIFIPswd.setEnabled(false);
			mBtnStartSearch.setEnabled(true);
			//
			mTextViewSearchTip.setVisibility(View.VISIBLE);
			SetLoadingImageViewInVisible(mImageViewSearchStatus);
		}

		mEditTextWIFIPswd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				//
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void InitNetwork(final View v) {

		msgHandler = new MessageHandler();

		ssidList = WifiWraper.getInstance(v).getSSIDlist();

		// ArrayAdapter<String> adapter = new
		// ArrayAdapter<String>(v.getContext(),
		// android.R.layout.simple_spinner_dropdown_item,ssidList);
		// mSpinner.setAdapter(adapter);

		// 网络侦听
		mWifiChangedReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				//
				SetLoadingImageViewVisible(mImageViewWifiWaitingStatus);
				//
				ConnectivityManager connectivityManager = (ConnectivityManager) v
						.getContext().getApplicationContext()
						.getSystemService(Context.CONNECTIVITY_SERVICE);

				NetworkInfo networkInfo = connectivityManager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if (networkInfo != null && networkInfo.isConnected()) {
					//
					int position = 0;
					WifiManager wm = (WifiManager) v.getContext()
							.getApplicationContext()
							.getSystemService(Context.WIFI_SERVICE);
					WifiInfo curWifiInfo = wm.getConnectionInfo();
					String curSSID = (curWifiInfo != null ? curWifiInfo
							.getSSID() : "");
					curSSID = curSSID.substring(1, curSSID.length() - 1);
					for (int j = 0; j < ssidList.size(); j++) {
						if (position == 0 && curSSID.equals(ssidList.get(j))) {
							position = j;
							break;
						}
					}
					//
					SetLoadingImageViewInVisible(mImageViewWifiWaitingStatus);
					//
					if(mRadioBtnAPLink.isChecked())
					{
						SetLoadingImageViewInVisible(mImageViewSearchStatus);
						
						boolean bModuleHifi = curSSID
								.equals(SwitchModule.curModuleHiFiName);
						
						if(bModuleHifi)
						{
							CommunicationClient.getInstance().Init(
									SwitchModule.getIP(SwitchModule.curModuleType),
									SwitchModule
											.getPort(SwitchModule.curModuleType),
									msgHandler);
							CommunicationClient.getInstance().Connect();

							return;
						}
					}
					
					// mSpinner.setSelection(position);
					mEditTextWIFIName.setText(ssidList.get(position));
					//mEditTextWIFIPswd.requestFocus();
					mBtnStartSearch.setEnabled(true);
					

				} else {

					//mEditTextWIFIPswd.requestFocus();
					mBtnStartSearch.setEnabled(false);
				}
			}
		};

		v.getContext()
				.getApplicationContext()
				.registerReceiver(
						mWifiChangedReceiver,
						new IntentFilter(
								ConnectivityManager.CONNECTIVITY_ACTION));
	}
	
	private void SetLoadingImageViewVisible(ImageView v)
	{
		v.setVisibility(View.VISIBLE);
		Utils.startLoading(getActivity(), v);
	}
	
	private void SetLoadingImageViewInVisible(ImageView v)
	{
		Utils.stopLoading(v);
		v.setVisibility(View.INVISIBLE);
	}

	// smartLink
	private void InitSmartLink(final View v) {
		mSnifferSmartLinker = SnifferSmartLinker.getInstance();

		
		//mWaitingDialog = new ProgressDialog(v.getContext());
		mWaitingDialog = Utils.createLoadingDialog(getActivity(), "正在连接，请稍后...");
		mWaitingDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {

				mSnifferSmartLinker.setOnSmartLinkListener(null);
				mSnifferSmartLinker.stop();
				mIsConncting = false;
			}
		});
//		mWaitingDialog
//				.setMessage(getString(R.string.hiflying_smartlinker_waiting));
//		mWaitingDialog.setButton(ProgressDialog.BUTTON_NEGATIVE,
//				getString(android.R.string.cancel),
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//					}
//				});

//		mWaitingDialog.setOnDismissListener(new OnDismissListener() {
//
//			@Override
//			public void onDismiss(DialogInterface dialog) {
//
//				mSnifferSmartLinker.setOnSmartLinkListener(null);
//				mSnifferSmartLinker.stop();
//				mIsConncting = false;
//			}
//		});

		// 监听查找事件
		mBtnStartSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// CommunicationClient.getInstance().Init("192.168.1.87", 9004,
				// msgHandler);
				// CommunicationClient.getInstance().Connect();
				// return;
				//
				
				//
				if (mRadioBtnSmartLink.isChecked()) {
					mSSID = mEditTextWIFIName.getText().toString();// mSpinner.getSelectedItem().toString();
					mWifiPswd = mEditTextWIFIPswd.getText().toString().trim();

					// TODO Auto-generated method stub
					if (!mIsConncting) {

						// 璁剧疆瑕侀厤缃殑ssid 鍜宲swd
						try {
							mSnifferSmartLinker
									.setOnSmartLinkListener(SmartLinkTypeSelectionFragment.this);
							// 寮�濮� smartLink
							mSnifferSmartLinker.start(v.getContext()
									.getApplicationContext(), mWifiPswd, mSSID);
							mIsConncting = true;
							mWaitingDialog.show();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				} else if (mRadioBtnAPLink.isChecked()) {
					
					SetLoadingImageViewVisible(mImageViewSearchStatus);
					
					mViewHandler.post(new Runnable() {

						@Override
						public void run() {
							if (!WifiWraper.getInstance(fragmentView).bExist(
									SwitchModule.curModuleHiFiName)) {
								SetLoadingImageViewInVisible(mImageViewSearchStatus);
								showInfo("module HiFi not exist!");
								return;
							}

							mBtnStartSearch.setEnabled(false);

							int res = WifiWraper
									.getInstance(fragmentView)
									.access2Wifi(
											SwitchModule.curModuleHiFiName,
											SwitchModule
													.getHiFiPswd(SwitchModule.curModuleType),
											SwitchModule
													.getHiFiEncryptTypeStr(SwitchModule.curModuleType));
							// mode1:has existed
							// mode2:change network to HIFI,handle this by
							// receivedNetwork
							if (res == 0) {
								CommunicationClient
										.getInstance()
										.Init(SwitchModule
												.getIP(SwitchModule.curModuleType),
												SwitchModule
														.getPort(SwitchModule.curModuleType),
												msgHandler);
								CommunicationClient.getInstance().Connect();
							}

						}
					});

				}

			}
		});

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// free smartLink
		mSnifferSmartLinker.setOnSmartLinkListener(null);
		// end of free smartLink
		try {
			fragmentView.getContext().unregisterReceiver(mWifiChangedReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//
		SetLoadingImageViewInVisible(mImageViewSearchStatus);
		SetLoadingImageViewInVisible(mImageViewWifiWaitingStatus);
	}

	@Override
	public void onLinked(final SmartLinkedModule module) {
		// TODO Auto-generated method stub

		Log.w(TAG, "onLinked");

		mViewHandler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(
						fragmentView.getContext(),
						getString(
								R.string.hiflying_smartlinker_new_module_found,
								module.getMac(), module.getIp()),
						Toast.LENGTH_SHORT).show();
				moduleIPList.add(module.getIp());
				moduleMACList.add(module.getMac());

			}
		});
	}

	@Override
	public void onCompleted() {

		Log.w(TAG, "onCompleted");

		mViewHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(fragmentView.getContext(),
						getString(R.string.hiflying_smartlinker_completed),
						Toast.LENGTH_SHORT).show();
				mWaitingDialog.dismiss();

				mIsConncting = false;

				// 完成之后再连，稳定性更高
				CommunicationClient.getInstance().Init(moduleIPList.get(0),
						SwitchModule.getPort(SwitchModule.curModuleType),
						msgHandler);
				// MainActivity.getClient().Init("192.168.1.97", 9003,
				// msgHandler);
				CommunicationClient.getInstance().Connect();
				// host = serverIPList.get(0);
				// port = 8899;
				// Connect();

				// start configuration activity
				// System.out.println("start activity ModuleConfigurationActivity");

			}
		});
	}

	@Override
	public void onTimeOut() {

		Log.w(TAG, "onTimeOut");
		mViewHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(fragmentView.getContext(),
						getString(R.string.hiflying_smartlinker_timeout),
						Toast.LENGTH_SHORT).show();
				mWaitingDialog.dismiss();
				mIsConncting = false;
			}
		});
	}

	@SuppressLint("HandlerLeak")
	public class MessageHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			// receiverData(msg.what);
			if (msg.what == 1) {
				byte[] result = msg.getData().getByteArray("msg");
				result = GatherContent(result);

				// if(result[0] + InstructionSet.GetClientOffset() !=
				// result.length)
				// {
				//
				// //System.out.println("[Client] Received Message All" + new
				// String(result));
				// return;
				// }
				if (result == null) {
					return;
				}
				//
				if (result[0] == 1 && InstructionSet.sendData != null) {
					CommunicationClient.getInstance().SendData(
							InstructionSet.sendData);
					return;
				}

				System.out.println("[Client] Received Message "
						+ new String(result));
				//
				// 解析从服务器收到的指令
				String instructString = InstructionSet.GetInstructionStr(1,
						result);
				// edtReceiver.append(result);
				// showInfo(result);
				if (instructString.equalsIgnoreCase(InstructionSet.heartStr)) {
					if (heartCount > HEART_MAX_COUNT)// one time per 15min
					{
						heartCount = 0;
						// sendMsg = new
						// String(InstructionSet.instruction_heart_1);
						// sendData();
						InstructionSet.encrypt(
								InstructionSet.instruction_heart_1,
								InstructionSet.GetClientOffset(),
								InstructionSet.instruction_heart_1.length - 1);
						CommunicationClient.getInstance().SendData(
								InstructionSet.instruction_heart_1);
					}

					heartCount++;

					//showInfo("heart0");
				} else if (instructString
						.equalsIgnoreCase(InstructionSet.registerStr)) {
					// sendMsg = new
					// String(InstructionSet.instruction_register_1);
					// InstructionSet.instruction_register_1
					// sendData();
					InstructionSet.encrypt(
							InstructionSet.instruction_register_1,
							InstructionSet.GetClientOffset(),
							InstructionSet.instruction_register_1.length - 1);
					CommunicationClient.getInstance().SendData(
							InstructionSet.instruction_register_1);
				} else if (instructString
						.equalsIgnoreCase(InstructionSet.configStr)) {

					// BigInteger bigInteger = new BigInteger(1, result);
					//
					// String temp = bigInteger.toString(16);

					byte[] contentBytes = "config:OK"
							.getBytes(CommunicationClient.getInstance()
									.US_ASCII());
					// make new head data
					header = new MsgHeader();
					header.sLen = (short) (contentBytes.length + 1);// plus
																	// checksum
																	// count of
																	// byte
					byte[] headBytes = header.getBytes();

					InstructionSet.sendData = new byte[contentBytes.length
							+ headBytes.length + 1];// plus checksum count of
													// byte
					if (InstructionSet.sendData == null || contentBytes == null
							|| headBytes == null) {
						Log.d(TAG,
								"sendData == null or data == null or headBytes == null");
						return;
					}
					byte sumVal = 0;
					// head
					for (int i = 0; i < headBytes.length; i++) {
						InstructionSet.sendData[i] = headBytes[i];
						sumVal += InstructionSet.sendData[i];
					}
					// head + content
					int ttLen = headBytes.length + contentBytes.length;
					for (int i = headBytes.length; i < ttLen; i++) {
						InstructionSet.sendData[i] = contentBytes[i
								- headBytes.length];
						sumVal += InstructionSet.sendData[i];
					}

					InstructionSet.sendData[InstructionSet.sendData.length - 1] = sumVal;
					InstructionSet.encrypt(InstructionSet.sendData,
							InstructionSet.GetClientOffset(),
							InstructionSet.sendData.length - 1);
					// send
					CommunicationClient.getInstance().SendData(
							InstructionSet.sendData);

				} else if (instructString
						.equalsIgnoreCase(InstructionSet.doneStr)) {
					//
					showInfo(InstructionSet.tipDoneSetting);
				} else if (instructString
						.equalsIgnoreCase(InstructionSet.errorStr)) {
					showInfo(InstructionSet.tipErrorSetting);
				}
			} else if (msg.what == 0) {
				if (isAdded()) {
					Intent i = new Intent(fragmentView.getContext(),
							ModuleConfigActivity.class);
					startActivity(i);
				}
				SetLoadingImageViewInVisible(mImageViewSearchStatus);
				
				mBtnStartSearch.setEnabled(true);
			} else if (msg.what == -1) {
				showInfo("连接失败,请重试");
				mBtnStartSearch.setEnabled(true);
			}
		}
	}

	// 返回:
	// 1. null:数据未全
	// 2. res[0]=1:实际长度已超出理论长度
	// 3. res全部:正常接收完毕
	private byte[] GatherContent(byte[] result) {
		if (result == null) {
			return null;
		}
		//
		InstructionSet.decrypt(result, InstructionSet.GetClientOffset(),
				result.length - 1);
		if (CommunicationClient.getInstance().getCachedData().size() <= 0
				&& isContainedStr(result, null)) {
			CommunicationClient.getInstance().getCachedData().add(result);
		} else if (CommunicationClient.getInstance().getCachedData().size() > 0
				&& !isContainedStr(result, null)) {
			CommunicationClient.getInstance().getCachedData().add(result);
		} else if (CommunicationClient.getInstance().getCachedData().size() > 0
				&& isContainedStr(result, null)) {
			// to make sure for "config" instruction
			if (isContainedStr(CommunicationClient.getInstance()
					.getCachedData().get(0), InstructionSet.configStr)) {
				CommunicationClient.getInstance().ProcessedDataBuffer[0] = 1;

				CommunicationClient.getInstance().getCachedData().clear();
				mHeaderforGather = null;
				return CommunicationClient.getInstance().ProcessedDataBuffer;
			}
			CommunicationClient.getInstance().getCachedData().clear();
			CommunicationClient.getInstance().getCachedData().add(result);
			mHeaderforGather = null;
		}
		//
		int totLen = 0;

		for (int i = 0; i < CommunicationClient.getInstance().getCachedData()
				.size(); i++) {
			totLen += CommunicationClient.getInstance().getCachedData().get(i).length;
		}
		Log.d(TAG, "totLen = " + totLen + "; CachedData size = "
				+ CommunicationClient.getInstance().getCachedData().size());
		//
		if (totLen <= 0) {
			return null;
		}
		if (mHeaderforGather == null) {
			mHeaderforGather = new MsgHeader();
			mHeaderforGather.putBytes(CommunicationClient.getInstance()
					.getCachedData().get(0), 0, MsgHeader.length);
		}

		Log.e(TAG, "totLen = " + totLen
				+ ";mHeaderforGather.sLen + MsgHeader.length = "
				+ (mHeaderforGather.sLen + MsgHeader.length));

		if (totLen < (mHeaderforGather.sLen + MsgHeader.length)) {
			return null;
		} else if (totLen > (mHeaderforGather.sLen + MsgHeader.length)) {

			// to make sure for "config" instruction
			if (isContainedStr(CommunicationClient.getInstance()
					.getCachedData().get(0), InstructionSet.configStr)) {
				CommunicationClient.getInstance().ProcessedDataBuffer[0] = 1;
				CommunicationClient.getInstance().getCachedData().clear();
				mHeaderforGather = null;
				return CommunicationClient.getInstance().ProcessedDataBuffer;
			}

			CommunicationClient.getInstance().getCachedData().clear();
			mHeaderforGather = null;
			return null;
		}
		//
		byte sumVal = 0;
		int idx = 0;
		CommunicationClient.getInstance().ProcessedDataBuffer = new byte[totLen];
		for (int i = 0; i < CommunicationClient.getInstance().getCachedData()
				.size(); i++) {
			for (int j = 0; j < CommunicationClient.getInstance()
					.getCachedData().get(i).length; j++) {
				if (i - 1 >= 0) {
					idx = j
							+ CommunicationClient.getInstance().getCachedData()
									.get(i - 1).length;
					CommunicationClient.getInstance().ProcessedDataBuffer[idx] = CommunicationClient
							.getInstance().getCachedData().get(i)[j];

					if (idx != totLen - 1) {
						sumVal += CommunicationClient.getInstance().ProcessedDataBuffer[idx];
					}

				} else {
					CommunicationClient.getInstance().ProcessedDataBuffer[j] = CommunicationClient
							.getInstance().getCachedData().get(i)[j];
					if (j != totLen - 1) {
						sumVal += CommunicationClient.getInstance().ProcessedDataBuffer[j];
					}

				}
			}
		}

		CommunicationClient.getInstance().getCachedData().clear();
		mHeaderforGather = null;

		Log.e(TAG,
				"sumVal = "
						+ sumVal
						+ ";lastByte = "
						+ CommunicationClient.getInstance().ProcessedDataBuffer[totLen - 1]);

		if (sumVal != CommunicationClient.getInstance().ProcessedDataBuffer[totLen - 1]) {
			CommunicationClient.getInstance().ProcessedDataBuffer = null;
		}

		return CommunicationClient.getInstance().ProcessedDataBuffer;
	}

	//
	private boolean isContainedStr(byte[] result, String keyStr) {
		if (keyStr == null) {
			String str = new String(result, CommunicationClient.getInstance()
					.US_ASCII());
			if (str.contains(InstructionSet.configStr)
					|| str.contains(InstructionSet.registerStr)
					|| str.contains(InstructionSet.doneStr)
					|| str.contains(InstructionSet.heartStr)
					|| str.contains(InstructionSet.errorStr)) {

				return true;

			}

			return false;
		} else {
			String str = new String(result, CommunicationClient.getInstance()
					.US_ASCII());
			if (str.contains(keyStr)) {
				return true;
			}
			return false;

		}

	}

	//

	private void showInfo(String msg) {
		Toast.makeText(fragmentView.getContext(), msg, Toast.LENGTH_SHORT)
				.show();

	}

}
