package com.sola.smartlinksettingtool;


import com.hiflying.smartlink.ISmartLinker;
import com.hiflying.smartlink.OnSmartLinkListener;
import com.hiflying.smartlink.SmartLinkedModule;

import com.hiflying.smartlink.v3.SnifferSmartLinker;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.Util;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.ConnectCallback;
import com.koushikdutta.async.callback.DataCallback;
//import com.sola.smartlinksettingtool.Configuration1Activity.MessageHandler;
//import com.sola.smartlinksettingtool.Configuration1Activity.MyHandler;
//import com.sola.smartlinksettingtool.Configuration1Activity.MyReceiverRunnable;
//import com.hiflying.smartlink.v7.MulticastSmartLinker;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.R.integer;
import android.annotation.SuppressLint;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ArrayAdapter;

import android.widget.AdapterView;
//import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;



/**
 * 
 */

/**
 * @author Administrator
 *
 */
public class Configuration1Activity extends AppCompatActivity implements OnSmartLinkListener{
	//varibles
	private static final String TAG = "MainActivity";

	protected EditText mSsidEditText;
	protected EditText mPasswordEditText;
	protected Button mStartButton;
	
	protected ISmartLinker mSnifferSmartLinker;
	private boolean mIsConncting = false;
	protected Handler mViewHandler = new Handler();
	protected ProgressDialog mWaitingDialog;
	private BroadcastReceiver mWifiChangedReceiver;
	
	private List<String> serverIPList;
	private List<String> serverMACList;
	
	//private ListView listView;
	private Spinner mSpinner;
	
	//end of varibles
	
	//network communication
	private InputStream in;
	private PrintWriter printWriter = null;
	private BufferedReader reader;
	private Socket mSocket = null;
	private boolean isConnected = false;
	//private MyHandler myHandler;
	private Thread receiverThread;
	private List<String> msgList;
	private String sendMsg;
	private String toIP;
	private String toPort = "8899";//默认端口
	
	
	
	private AsyncSocket clientSocket = null;
	private MessageHandler msgHandler = null;
    private String host;
    private int port;
	//end of network communication
	
	//setting variable
	private EditText mSettingWifiNameEditText;
	private EditText mSettingWifiPwdEditText;
	private EditText mSettingFarIPEditText;
	private EditText mSettingFarPortEditText;
	private EditText mSettingNearIPEditText;
	private EditText mSettingSubmaskEditText;
	private EditText mSettingGatewayEditText;
	private EditText mSettingDNSEditText;
	
	private RadioGroup mSettingRadioGroup;
	private RadioButton mSettingDHCPRadioBtn;
	private RadioButton mSettingStaticRadioBtn;
	private Button mSettingSubmitButtion;
	
	private String mWifiEncryptType = WifiEncryptTypes.NONE;
	private String mIPType = "";
	//
	//end of setting variable

	private long heartCount = 0;
	private final static int  HEART_MAX_COUNT = 180;//180 times per 15min
	private MsgHeader header;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_type_1);
        
        
        
        
        InitView();
        
        InitSmartLink();
        
        InitNetwork();
        
        InitSettings();
        
        InitTestConnect();
    }
    
    private Button mTestConnectSubmitButtion;
    private void InitTestConnect()
    {
    	mTestConnectSubmitButtion = (Button)findViewById(R.id.connect_setting_button);
    	mTestConnectSubmitButtion.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				toIP = "192.168.1.97";
//				toPort = "8899";
//				connectThread();
				CommunicationClient.getInstance().Init("192.168.1.97", 9003, msgHandler);
				CommunicationClient.getInstance().Connect();
			}
		});
    	
    }

    private void InitView() {
    	//listView = (ListView)findViewById(R.id.wifi_encrypt_types_listView);
    	//listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.item_list, WiFiEncryptTypes.getAllTypes()));
    	mSpinner = (Spinner)findViewById(R.id.wifi_encrypt_types_spinner);
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(Configuration1Activity.this, android.R.layout.simple_spinner_item, WifiEncryptTypes.getAllTypes());
    	mSpinner.setAdapter(adapter);
    	//wifi加密方式
    	mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    		@Override
    		public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    		{
    			switch (position) {
				case 0:
					mWifiEncryptType = WifiEncryptTypes.NONE;
					break;
				case 1:
					mWifiEncryptType = WifiEncryptTypes.WEP_OPEN;
					break;
				case 2:
					mWifiEncryptType = WifiEncryptTypes.WEP_SHARE;
					break;
				case 3:
					mWifiEncryptType = WifiEncryptTypes.WPA_TKIP;
					break;
				case 4:
					mWifiEncryptType = WifiEncryptTypes.WPA_WPA2_TKIP;
					break;
				case 5:
					mWifiEncryptType = WifiEncryptTypes.WPA_WPA2_AES;
					break;
				default:
					break;
				}
    		}
    		
    		//
    		@Override
    		public void onNothingSelected(AdapterView<?> parent)
    		{
    			
    		}
		});
    	
	}
    
    private void InitSettings() {

    	//提交事件
    	mIPType = "DHCP";
    	mSettingSubmitButtion = (Button)findViewById(R.id.submit_setting_button);
    	//mSettingSubmitButtion.setEnabled(false);
    	mSettingSubmitButtion.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String wifiName = ((EditText)findViewById(R.id.wifiName_setting_editText)).getText().toString();
				String wifiPwd = ((EditText)findViewById(R.id.wifiPwd_setting_editText)).getText().toString();
				String farIP = ((EditText)findViewById(R.id.farIP_setting_editText)).getText().toString();
				String farPort = ((EditText)findViewById(R.id.farPort_setting_editText)).getText().toString();
				String nearIP = ((EditText)findViewById(R.id.nearIP_setting_editText)).getText().toString();
				String subMask = ((EditText)findViewById(R.id.subMask_setting_editText)).getText().toString();
				String gateway = ((EditText)findViewById(R.id.gateway_setting_editText)).getText().toString();
				//String DNS = ((EditText)findViewById(R.id.dns_setting_editText)).getText().toString();
				
				String instructionContent = "";
				//instructionContent += head;
				instructionContent += "config:";
				//instructionContent += "AT+WSSSID=" + wifiName + "\n";
				//instructionContent += "AT+WSKEY" + mWifiEncryptType + "," + wifiPwd + "\n";
				instructionContent += "AT+NETP=TCP,Client," + farPort + "," + farIP + "\r\n";
				
				if(mIPType.equalsIgnoreCase("static"))
				{
					instructionContent += "AT+WANN=static," + nearIP + "," + subMask + "," + gateway + "\r\n"; 
				}
				else if(mIPType.equalsIgnoreCase("dhcp"))
				{
					instructionContent += "AT+WANN=DHCP,0.0.0.0,0.0.0.0,0.0.0.0\r\n";
				}
				header = new MsgHeader();
				header.sLen = (short)instructionContent.getBytes().length;
				
				String head = new String(header.getBytes());
				
				sendMsg = head + instructionContent;
				
//				BigInteger bigInteger = new BigInteger(1, sendMsg.getBytes());
//				
//				String temp = bigInteger.toString(16);
				
				CommunicationClient.getInstance().SendData(sendMsg);
				//sendData();
				//for test
//				host = "192.168.1.97";
//				port = 8899;
//				Connect();
				
//				toIP = "192.168.1.97";
//				toPort = "8899";
//				connectThread();
			}
		});
    	
    	//DHCP或STATIC事件
    	mSettingDHCPRadioBtn = (RadioButton)findViewById(R.id.dhcp_setting_radioButton);
    	mSettingStaticRadioBtn = (RadioButton)findViewById(R.id.static_setting_radioButton);
    	
    	mSettingRadioGroup = (RadioGroup)findViewById(R.id.radioGroup_setting);
    	mSettingRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId == mSettingDHCPRadioBtn.getId())
				{
					//
					mIPType = "DHCP";
					
				}
				else if(checkedId == mSettingStaticRadioBtn.getId())
				{
					//
					mIPType = "static";
				}
			}
		});
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
    	
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
    
    //smartLink
    private void InitSmartLink()
    {
    	serverIPList = new ArrayList<String>();
    	serverMACList = new ArrayList<String>();
    	
    	mSnifferSmartLinker = SnifferSmartLinker.getInstance();		
		
		mWaitingDialog = new ProgressDialog(this);
		mWaitingDialog.setMessage(getString(R.string.hiflying_smartlinker_waiting));
		mWaitingDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		mWaitingDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {

				mSnifferSmartLinker.setOnSmartLinkListener(null);
				mSnifferSmartLinker.stop();
				mIsConncting = false;
			}
		});
		
		//setContentView(R.layout.activity_customized);
		mSsidEditText = (EditText) findViewById(R.id.ssid_editText);
		mPasswordEditText = (EditText) findViewById(R.id.ssid_pass_editText);
		mStartButton = (Button) findViewById(R.id.search_ip_button);
		mSsidEditText.setText(getSSid());
		
		
		//监听查找事件
		mStartButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!mIsConncting){

					//璁剧疆瑕侀厤缃殑ssid 鍜宲swd
					try {
						mSnifferSmartLinker.setOnSmartLinkListener(Configuration1Activity.this);
						//寮�濮� smartLink
						mSnifferSmartLinker.start(getApplicationContext(), mPasswordEditText.getText().toString().trim(), 
								mSsidEditText.getText().toString().trim());
						mIsConncting = true;
						mWaitingDialog.show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		mWifiChangedReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if (networkInfo != null && networkInfo.isConnected()) {
					mSsidEditText.setText(getSSid());
					mPasswordEditText.requestFocus();
					mStartButton.setEnabled(true);
				}else {
					mSsidEditText.setText(getString(R.string.hiflying_smartlinker_no_wifi_connectivity));
					mSsidEditText.requestFocus();
					mStartButton.setEnabled(false);
					if (mWaitingDialog.isShowing()) {
						mWaitingDialog.dismiss();
					}
				}
			}
		};
		registerReceiver(mWifiChangedReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//free smartLink
		mSnifferSmartLinker.setOnSmartLinkListener(null);
		//end of free smartLink
		try {
			unregisterReceiver(mWifiChangedReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onLinked(final SmartLinkedModule module) {
		// TODO Auto-generated method stub
		
		Log.w(TAG, "onLinked");
		
		
		mViewHandler.post(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), getString(R.string.hiflying_smartlinker_new_module_found, module.getMac(), module.getIp()), 
						Toast.LENGTH_SHORT).show();
				serverIPList.add(module.getIp());
				serverMACList.add(module.getMac());
				//toIP = serverIPList.get(0);
				//toPort = "8899";
				//connect to server module
				//connectThread();
				//设置ip显示
				mSettingNearIPEditText = (EditText)findViewById(R.id.nearIP_setting_editText);
				mSettingNearIPEditText.setText(host);
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
				Toast.makeText(getApplicationContext(), getString(R.string.hiflying_smartlinker_completed), 
						Toast.LENGTH_SHORT).show();
				mWaitingDialog.dismiss();
				
				mIsConncting = false;
				
				//完成之后再连，稳定性更高
				CommunicationClient.getInstance().Init(serverIPList.get(0), 8899, msgHandler);
				CommunicationClient.getInstance().Connect();
//				host = serverIPList.get(0);
//				port = 8899;
//				Connect();
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
				Toast.makeText(getApplicationContext(), getString(R.string.hiflying_smartlinker_timeout), 
						Toast.LENGTH_SHORT).show();
				mWaitingDialog.dismiss();
				mIsConncting = false;
			}
		});
	}	

	private String getSSid(){

		WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
		if(wm != null){
			WifiInfo wi = wm.getConnectionInfo();
			if(wi != null){
				String ssid = wi.getSSID();
				if(ssid.length()>2 && ssid.startsWith("\"") && ssid.endsWith("\"")){
					return ssid.substring(1,ssid.length()-1);
				}else{
					return ssid;
				}
			}
		}

		return "";
	}
    //end of smartLink

	
	//network commucation
	private void InitNetwork()
	{
	
		msgList = new ArrayList<String>();
		
		//myHandler = new MyHandler();//暂时没释放
		
		msgHandler = new MessageHandler();
	}
	//
	

//	private class MyReceiverRunnable implements Runnable {
//
//		public void run() {
//
//			while (true) {
//
//				Log.i(TAG, "---->>client receive....");
//				if(!isConnected)
//				{
//					break;
//				}
//				
//				if (mSocket != null && mSocket.isConnected()) {
//
//
//					byte[] result = readBytesFromInputStream(in);
//
//					try {
//						// String str = "";
//						//
//						// while ((str = reader.readLine()) != null) {
//						// Log.i(tag, "---->> read data:" + str);
//						// result += str;
//						// }
//						if (!result.equals(null) && result.length > 0) {
//
//							Message msg = new Message();
//							msg.what = 1;
//							Bundle data = new Bundle();
//							data.putByteArray("msg", result);
//							//data.putString("msg", result);
//							msg.setData(data);
//							//使用同步消息机制对消息进行处理
//							myHandler.sendMessage(msg);
//							//msgList.add(result);
//						}
//
//					} catch (Exception e) {
//						Log.e(TAG, "--->>read failure!" + e.toString());
//					}
//				}
//			}
//			try {
//				Thread.sleep(100L);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//
//		}
//	}
//
//	//</editor-fold>	
//	
//	@SuppressLint("HandlerLeak") private class MyHandler extends Handler {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//
//			receiverData(msg.what);
//			if (msg.what == 1) {
//				byte[] result = msg.getData().getByteArray("msg");
//				
//				if(result[0] + InstructionSet.GetClientOffset() != result.length)
//				{
//					System.out.println("[Client] Received Message All" + new String(result));
//					return;
//				}
//				System.out.println("[Client] Received Message " + new String(result));
//				//
//				//解析从服务器收到的指令
//				String instructString = InstructionSet.GetInstructionStr(1,result);
//				//edtReceiver.append(result);
//				//showInfo(result);
//				if(instructString.equalsIgnoreCase(InstructionSet.heartStr))
//				{
//					if(heartCount > HEART_MAX_COUNT)//one time per 15min
//					{
//						heartCount = 0;
//						sendMsg = new String(InstructionSet.instruction_heart_1);
//						sendData();
//					}
//					
//					heartCount++;
//					
//					if(!mSettingSubmitButtion.isEnabled())
//					{
//						mSettingSubmitButtion.setEnabled(true);
//					}
//					
//					showInfo("heart0");
//				}
//				else if(instructString.equalsIgnoreCase(InstructionSet.registerStr))
//				{
//					sendMsg = new String(InstructionSet.instruction_register_1);
//					//InstructionSet.instruction_register_1
//					sendData();
//				}
//				else if(instructString.equalsIgnoreCase(InstructionSet.configStr))
//				{
//					
//					BigInteger bigInteger = new BigInteger(1, result);
//					
//					String temp = bigInteger.toString(16);
//					
//					sendMsg = "config:OK";
//					header = new MsgHeader();
//					header.sLen = (short)sendMsg.getBytes().length;
//					byte[] headBytes = header.getBytes();
//					sendMsg = new String(headBytes) + sendMsg;
//					sendData();
//				}
//				else if(instructString.equalsIgnoreCase(InstructionSet.doneStr))
//				{
//					//
//					showInfo(InstructionSet.tipDoneSetting);
//				}
//				else if(instructString.equalsIgnoreCase(InstructionSet.errorStr))
//				{
//					showInfo(InstructionSet.tipErrorSetting);
//				}
//			
//			}
//		}
//	}

	/******************************************************************************/
//	private String readFromInputStream(InputStream in) {
//		int count = 0;
//		byte[] inDatas = null;
//		try {
//			while (count == 0) {
//				count = in.available();
//			}
//			inDatas = new byte[count];
//			in.read(inDatas);
//			return new String(inDatas, "gb2312");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//	
//	private byte[] readBytesFromInputStream(InputStream in) {
//		int count = 0;
//		byte[] inDatas = null;
//		try {
//			while (count == 0) {
//				count = in.available();
//			}
//			inDatas = new byte[count];
//			in.read(inDatas);
//			return inDatas;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	/******************************************************************************/
	/*
	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		// 鍚姩2涓伐浣滅嚎绋�:鍙戦�併�佹帴鏀躲��
		case R.id.id_btn_connClose:

			connectThread();
			break;

		case R.id.id_btn_send:

			sendData();
			break;
		}
	}
	 */
	/**
	 * 褰撹繛鎺ュ埌鏈嶅姟鍣ㄦ椂,鍙互瑙﹀彂鎺ユ敹浜嬩欢.
	 */
//	private void receiverData(int flag) {
//
//		if (flag == 2) {
//			isConnected = true;
//			// mTask = new ReceiverTask();
//			receiverThread = new Thread(new MyReceiverRunnable());
//			receiverThread.start();
//
//			Log.i(TAG, "--->>socket start receive data");
//			//btnConn.setText("鏂紑");
//
//			//
//			// mTask.execute(null);
//			
//			//mSettingSubmitButtion.setText("connected");
//			
//		}
//
//	}

	/**
	 * send msg
	 */
//	public void sendData() {
//
//		// sendThread.start();
//		try {
//			
//			String context = sendMsg;//edtSend.getText().toString();
//
//			if (printWriter == null || context == null) {
//
//				if (printWriter == null) {
//					showInfo("send fail(printWriter == null)!");
//					return;
//				}
//				if (context == null) {
//					showInfo("send fail(context == null)!");
//					return;
//				}
//			}
//
//			printWriter.print(context);
//			printWriter.flush();
//			Log.i(TAG, "--->> client send data!");
//		} catch (Exception e) {
//			Log.e(TAG, "--->> send failure!" + e.toString());
//
//		}
//	}

	/**
	 * start to connect
	 */
//	public void connectThread() {
//		
//		disconnect();
//		
//		if (!isConnected) {
//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					Looper.prepare();
//					Log.i(TAG, "---->> connect/close server!");
//
//					//connectServer(edtIP.getText().toString(), edtPort.getText().toString());
//					//toIP = "192.168.1.97";
//					
//					connectServer(toIP, toPort);
//				}
//			}).start();
//		} else {
//			try {
//				if (mSocket != null) {
//					mSocket.close();
//					mSocket = null;
//					
//					Log.i(TAG, "--->>disconnect server.");
//					
//					// receiverThread.interrupt();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			//btnConn.setText("杩炴帴");
//			isConnected = false;
//			//mSettingSubmitButtion.setText("disconnected");
//		}
//	}
	
	/**
	 * start to disconnect
	 */
//	public void disconnect()
//	{
//		try {
//			if (mSocket != null) {
//				mSocket.close();
//				mSocket = null;
//				
//				Log.i(TAG, "--->>disconnect server.");
//				
//				// receiverThread.interrupt();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		//btnConn.setText("杩炴帴");
//		isConnected = false;
//	}

	// 杩炴帴鏈嶅姟鍣�.(缃戠粶璋冭瘯鍔╂墜鐨勬湇鍔″櫒绔紪鐮佹柟寮�:gb2312)
//	private void connectServer(String ip, String port) {
//		try {
//			Log.e(TAG, "--->>start connect  server !" + ip + "," + port);
//
//			mSocket = new Socket(ip, Integer.parseInt(port));
//			Log.e(TAG, "--->>end connect  server!");
//
//			OutputStream outputStream = mSocket.getOutputStream();
//
//			printWriter = new PrintWriter(new BufferedWriter(
//					new OutputStreamWriter(outputStream,
//							Charset.forName("gb2312"))));
//			//listener.setOutStream(printWriter);
//			// reader = new BufferedReader(new InputStreamReader(
//			// mSocket.getInputStream()));
//
//			in = mSocket.getInputStream();
//			
//			//receive data after connecting is succeed
//			myHandler.sendEmptyMessage(2);
//
//			showInfo("connect success!");
//		} catch (Exception e) {
//			isConnected = false;
//			showInfo("connect failed");
//			Log.e(TAG, "exception:" + e.toString());
//		}
//
//	}

	private void showInfo(String msg) {
		Toast.makeText(Configuration1Activity.this, msg, Toast.LENGTH_SHORT).show();

	}
	
	//end of network commucation

    
    
	@SuppressLint("HandlerLeak") public class MessageHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			//receiverData(msg.what);
			if (msg.what == 1) {
				byte[] result = msg.getData().getByteArray("msg");
				
				
				if(result[0] + InstructionSet.GetClientOffset() != result.length)
				{
					//System.out.println("[Client] Received Message All" + new String(result));
					return;
				}
				System.out.println("[Client] Received Message " + new String(result));
				//
				//解析从服务器收到的指令
				String instructString = InstructionSet.GetInstructionStr(1,result);
				//edtReceiver.append(result);
				//showInfo(result);
				if(instructString.equalsIgnoreCase(InstructionSet.heartStr))
				{
					if(heartCount > HEART_MAX_COUNT)//one time per 15min
					{
						heartCount = 0;
						//sendMsg = new String(InstructionSet.instruction_heart_1);
						//sendData();
						CommunicationClient.getInstance().SendData(InstructionSet.instruction_heart_1);
					}
					
					heartCount++;
					
					if(!mSettingSubmitButtion.isEnabled())
					{
						mSettingSubmitButtion.setEnabled(true);
					}
					
					showInfo("heart0");
				}
				else if(instructString.equalsIgnoreCase(InstructionSet.registerStr))
				{
					//sendMsg = new String(InstructionSet.instruction_register_1);
					//InstructionSet.instruction_register_1
					//sendData();
					CommunicationClient.getInstance().SendData(InstructionSet.instruction_register_1);
				}
				else if(instructString.equalsIgnoreCase(InstructionSet.configStr))
				{
					
					BigInteger bigInteger = new BigInteger(1, result);
					
					String temp = bigInteger.toString(16);
					
					sendMsg = "config:OK";
					header = new MsgHeader();
					header.sLen = (short)sendMsg.getBytes().length;
					byte[] headBytes = header.getBytes();
					sendMsg = new String(headBytes) + sendMsg;
					//sendData();
					CommunicationClient.getInstance().SendData(sendMsg);
				}
				else if(instructString.equalsIgnoreCase(InstructionSet.doneStr))
				{
					//
					showInfo(InstructionSet.tipDoneSetting);
				}
				else if(instructString.equalsIgnoreCase(InstructionSet.errorStr))
				{
					showInfo(InstructionSet.tipErrorSetting);
				}
			}
		}
	}
}
