/**
 * 
 */
package com.sola.smartlinksettingtool;

import com.sola.smartlinksettingtool.WifiEncryptTypes;
import com.sola.smartlinksettingtool.WifiEncryptTypes.SecurityType;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.R.integer;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public class WifiWraper {
	

    
    public enum IpAssignment {
        /* Use statically configured IP settings. Configuration can be accessed
         * with staticIpConfiguration */
        STATIC,
        /* Use dynamically configured IP settigns */
        DHCP,
        /* no IP details are assigned, this is used to indicate
         * that any existing IP settings should be retained */
        UNASSIGNED
    };
    
    public enum ProxySettings {
        /* No proxy is to be used. Any existing proxy settings
         * should be cleared. */
        NONE,
        /* Use statically configured proxy. Configuration can be accessed
         * with httpProxy. */
        STATIC,
        /* no proxy details are assigned, this is used to indicate
         * that any existing proxy settings should be retained */
        UNASSIGNED,
        /* Use a Pac based proxy.
         */
        PAC
    };
	
	public static final String sDATA_EXTRA_ID = "com.sola.smartlinksettingtool.wifiwraper.wifiinfo";
	
	
    
	private static WifiWraper wifiInstance;
	private WifiManager wm;
	private Context context;
	
	private Handler msgHandler = null;
	private Timer mTimer = null;
	
	public static WifiWraper getInstance(View v) {
		// TODO Auto-generated method stub
		if(wifiInstance == null)
		{
			wifiInstance = new WifiWraper(v);
			return wifiInstance;
		}
		return wifiInstance;
	}
	
	public static WifiWraper getInstance(Context appContext)
	{
		if(wifiInstance == null)
		{
			wifiInstance = new WifiWraper(appContext);
			return wifiInstance;
		}
		return wifiInstance;
	}
	
	protected WifiWraper(View v)
	{
		context = v.getContext().getApplicationContext();
		wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
	}
	
	protected WifiWraper(Context appContext)
	{
		context = appContext;
		wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
	}
	
	//context: app context
	public ArrayList<String> getSSIDlist()
	{
		ArrayList<String> ssidList = new ArrayList<String>();
		//
		wm.startScan();
		List<ScanResult> wifiList = wm.getScanResults();
		if(wifiList != null)
		{
			for (int i = 0; i < wifiList.size(); i++) {
				ssidList.add(wifiList.get(i).SSID);
				
			}
		}
		
		return ssidList;
	}
	
	
	public ArrayList<WifiStruct> getWifiStructList()
	{
		ArrayList<WifiStruct> wifiStructList = new ArrayList<WifiStruct>();
		WifiStruct wifi = null;
		//
		
//		WifiStruct header = new WifiStruct();
//		header.mWifiName = sWIFI_NAME;
//		wifiStructList.add(header);
		//
		wm.startScan();
		List<ScanResult> wifiList = wm.getScanResults();
		if(wifiList != null)
		{
			for (int i = 0; i < wifiList.size(); i++) {
				
				wifi = new WifiStruct();
				wifi.mWifiName = wifiList.get(i).SSID;
				
				wifi.mWifiEncryptType = WifiEncryptTypes.wifiCapability2SecurityStr(wifiList.get(i).capabilities);
				
		    	/**
		    	 * 0到-100的区间值，是一个int型数据，
		    	 * 其中0到-50表示信号最好，
		    	 * -50到-70表示信号偏差，
		    	 * 小于-70表示最差，有可能连接不上或者掉线，
		    	 * 一般Wifi已断则值为-200
		    	 */
				if(wifiList.get(i).level > -50 && wifiList.get(i).level <= 0 )
				{
					wifi.mSignalLevel = 2;
				}
				else if(wifiList.get(i).level > -70 && wifiList.get(i).level <= -50 )
				{
					wifi.mSignalLevel = 1;
				}
				else if(wifiList.get(i).level > -100 && wifiList.get(i).level <= -70 )
				{
					wifi.mSignalLevel = 0;
				}
				
				wifiStructList.add(wifi);
				
			}
		}
		
		return wifiStructList;
	}
	
	
	public void init(Handler handler)
	{
		msgHandler = handler;
	} 
	
	public void unInit()
	{
		msgHandler = null;
		if (mTimer != null) {
			mTimer.cancel();
			mTimer.purge();
			mTimer = null;
		}
	}
	
	/**
	 * 一经启动，就不停止
	 */
	public void periodWifiStructList()
	{
		if(mTimer != null)
		{
			return;
		}
		mTimer = new Timer();
		mTimer.scheduleAtFixedRate(new TimerTask() {  
            @Override  
            public void run() {
            	try {
            		
            		if(msgHandler == null)
                	{
                		return;
                	}
                	
                	ArrayList<WifiStruct> wifiStructList = getWifiStructList();
            		Message msg = new Message();
        			msg.what = 1;
        			Bundle data = new Bundle();
        			ArrayList list = new ArrayList();
        			list.add(wifiStructList);
        			data.putParcelableArrayList(sDATA_EXTRA_ID, list);
        			
        			msg.setData(data);
        			
        			msgHandler.sendMessage(msg);
					
				} catch (Exception e) {
					// TODO: handle exception
				}
            	
            	
            	
            }  
  
        }, 1000, 5000);  
	}
	

	public int access2Wifi(String ssid, String pwd, String securityType)
	{
		// 无线未打开时，开启无线  
        if (!wm.isWifiEnabled() && WifiManager.WIFI_STATE_ENABLING != wm.getWifiState())  
        {  
            wm.setWifiEnabled(true);  
        }
        
        // 获取当前的wifi连接  
        WifiInfo curConnection = wm.getConnectionInfo();  
        String curSSID = curConnection.getSSID();
        curSSID = curSSID.substring(1, curSSID.length()-1);
        if (curConnection != null && ssid.equals(curSSID))  
        {  
            //已经是当前连接  
            return 0;  
        }
        
       
        List<WifiConfiguration> configurations = wm.getConfiguredNetworks();  
        WifiConfiguration config = null;
        int networkId = -1;  
        for (int i = configurations.size() - 1; i >= 0; i--)  
        {  
            config = configurations.get(i);  
            if (config.SSID.equals(ssid))  
            {  
                networkId = config.networkId;  
                break;  
            }
        }
        //
        //not exist
        if (networkId == -1)
        {  
            // 安全类型 无、WEP(128)、WPA(TKIP)、WPA2(AES)
        	SecurityType sType = WifiEncryptTypes.getSecurityType(securityType);
            config = getConfig(ssid, sType, pwd);
//            // 名称  
//            config.SSID = "\"" + SSID + "\"";  
//            config.allowedKeyManagement.set(encryptionType);  
//            if (encryptionType != 0)
//            {  
//                // 密码  
//                config.preSharedKey = pswd;  
//            }
//            config.hiddenSSID = false;
//              
//            config.priority = 30;  
//            config.status = WifiConfiguration.Status.ENABLED;  
//            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);  
//              
//            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);  
//            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);  
//            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);  
//            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);  
//              
//            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);  
//            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);  
//            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.NONE);  
//              
//            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);  
//            // 必须添加，否则无线路由无法连接  
//            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);  
            
            networkId = wm.addNetwork(config);  
            if (networkId != -1)
            {  
                wm.saveConfiguration();
            }
        }
        else  
        {
//            config.allowedKeyManagement.set(encryptionType); 
//            //有密码改为无密码了，key不用改吗
//            if (encryptionType != 0)  
//            {  
//                // 密码  
//                config.preSharedKey = pswd;  
//            }
        	SecurityType sType = WifiEncryptTypes.getSecurityType(securityType);
        	config = updateConfig(config, sType, pwd);
            wm.updateNetwork(config);  
        }  
          
        if (networkId != -1)
        {  
            wm.disconnect();  
            wm.enableNetwork(networkId, true);
            wm.reconnect();
            return 1;
        }
       
        return -1;
	}
	
	   /** 
     * 获取加密类型 
     * @param type 加密类型 
     * @return 加密类型 
     * @see [类、类#方法、类#成员] 
     */  
    private int getKeyMgmtType(String type)  
    {  
        if (type == null)  
        {  
            return WifiConfiguration.KeyMgmt.NONE;  
        }
        
        if(type.contains("WEP64") || type.contains("WEP128"))
        {
        	return WifiConfiguration.KeyMgmt.NONE;
        }
        else if ("WEP".equals(type))  
        {  
            return WifiConfiguration.KeyMgmt.IEEE8021X;  
        }
        else if (type.contains("WPA-PSK") || type.contains("WPA_TKIP"))  
        {  
            return WifiConfiguration.KeyMgmt.WPA_PSK;  
        }
        else if (type.contains("WPA2-PSK") || type.contains("WPA2-AES"))  
        {  
            return WifiConfiguration.KeyMgmt.WPA_PSK;  
        }
        return WifiConfiguration.KeyMgmt.NONE;  
    }
	
	
	
	public boolean bExist(String SSID)
	{
		List<String> ssidList = getSSIDlist();

		for (int j = 0; j < ssidList.size(); j++) {
			if(SSID.equals(ssidList.get(j)))
			{
				return true;
			}
		}

        return false;
	}
	
	
	private WifiConfiguration updateConfig(WifiConfiguration config, SecurityType securityType, String password)
	{
	        switch (securityType) {
	            case OPEN:
	            	config.allowedKeyManagement.set(KeyMgmt.NONE);
	                break;
	            case WEP64:
	                //assertNotNull("password is empty", password);
	                // always use hex pair for WEP-40
	                //assertTrue(isHex(password, 10));
	                config = updateWepConfig(config, password);
	                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
	                break;
	            case WEP128:
	                //assertNotNull("password is empty", password);
	                // always use hex pair for WEP-104
	                //assertTrue(isHex(password, 26));
	                config = updateWepConfig(config, password);
	                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
	                break;
	            case WPA_TKIP:
	                //assertNotNull("password is empty", password);
	                config = updatePskConfig(config, password);
	                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
	                config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
	                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
	                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
	                break;
	            case WPA2_AES:
	                //assertNotNull("password is empty", password);
	                config = updatePskConfig(config, password);
	                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
	                config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
	                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
	                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
	                break;
	            default:
	                //fail("Not a valid security type: " + securityType);
	                break;
	        }
	        
	        return config;
	                
	}
	//
	private WifiConfiguration getConfig(String ssid, SecurityType securityType, String password) {
        //logv("Security type is %s", securityType.toString());

        WifiConfiguration config = null;
        switch (securityType) {
            case OPEN:
                config = createOpenConfig(ssid);
                break;
            case WEP64:
                //assertNotNull("password is empty", password);
                // always use hex pair for WEP-40
                //assertTrue(isHex(password, 10));
                config = createWepConfig(ssid, password);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                break;
            case WEP128:
                //assertNotNull("password is empty", password);
                // always use hex pair for WEP-104
                //assertTrue(isHex(password, 26));
                config = createWepConfig(ssid, password);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                break;
            case WPA_TKIP:
                //assertNotNull("password is empty", password);
                config = createPskConfig(ssid, password);
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                break;
            case WPA2_AES:
                //assertNotNull("password is empty", password);
                config = createPskConfig(ssid, password);
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                break;
            default:
                //fail("Not a valid security type: " + securityType);
                break;
        }
        return config;
	}
	
	/**
     * Create a {@link WifiConfiguration} for an open network
     *
     * @param ssid The SSID of the wifi network
     * @return The {@link WifiConfiguration}
     */
    public static WifiConfiguration createOpenConfig(String ssid) {
        WifiConfiguration config = createGenericConfig(ssid);

        config.allowedKeyManagement.set(KeyMgmt.NONE);
        return config;
    }
	
	
	public static WifiConfiguration createPskConfig(String ssid, String password) {
        WifiConfiguration config = createGenericConfig(ssid);

        if (isHex(password, 64)) {
            config.preSharedKey = password;
        } else {
            config.preSharedKey = quotedString(password);
        }
        config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
        return config;
    }
	
	
	
	public static WifiConfiguration updatePskConfig(WifiConfiguration config, String password)
	{
		if (isHex(password, 64)) {
            config.preSharedKey = password;
        } else {
            config.preSharedKey = quotedString(password);
        }
        config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
        return config;
	}
	
	
	 /**
     * Create a {@link WifiConfiguration} for a WEP secured network
     *
     * @param ssid The SSID of the wifi network
     * @param password Either a 10, 26, or 58 character hex string or the plain text password
     * @return The {@link WifiConfiguration}
     */
    public static WifiConfiguration createWepConfig(String ssid, String password) {
        WifiConfiguration config = createGenericConfig(ssid);

        if (isHex(password, 10) || isHex(password, 26) || isHex(password, 58)) {
            config.wepKeys[0] = password;
        } else {
            config.wepKeys[0] = quotedString(password);
        }

        config.allowedKeyManagement.set(KeyMgmt.NONE);
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
        return config;
    }
    
    private static WifiConfiguration updateWepConfig(WifiConfiguration config,String password)
    {
    	if (isHex(password, 10) || isHex(password, 26) || isHex(password, 58)) {
            config.wepKeys[0] = password;
        } else {
            config.wepKeys[0] = quotedString(password);
        }

        config.allowedKeyManagement.set(KeyMgmt.NONE);
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
        return config;
    }
    
	private static WifiConfiguration createGenericConfig(String ssid) {
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = quotedString(ssid);
 
        return config;
    }
	
	private static String quotedString(String s) {
        return String.format("\"%s\"", s);
    }
	
	/**
     * Utility method to check if a given string is a hexadecimal string of given length
     */
    public static boolean isHex(String input, int length) {
        if (input == null || length < 0) {
            return false;
        }
        return input.matches(String.format("[0-9A-Fa-f]{%d}", length));
    }
    
    /**
     * 
     * @author Administrator
     *
     */
    public static class WifiStruct
    {
    	public String mWifiName = null;
    	public String mWifiEncryptType = null;
    	/**
		 *mSignalLevel:2,最强信号;1,中等信号;0,最弱信号
    	 */
    	public int mSignalLevel = 0;
    }
	
}
