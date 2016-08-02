/**
 * 
 */
package com.sola.smartlinksettingtool;


/**
 * @author Administrator
 *
 */
public class SwitchModule {
	
	public final static String defaultNearIP = "192.168.0.100";
	public final static String defaultSubMask = "255.255.255.0";
	public final static String defaultGateway = "192.168.0.1";
	public final static String defaultDns = "192.168.0.1";

	public final static String CKS01 = "CK-S01";
	public final static String CKY02 = "CK-Y02";
	public final static String CKY03 = "CK-Y03";
	
	public final static String WPA_WPA2_AES_VALUE = "wpawpa2_aes";
	
	public static String curModuleHiFiName="";
	public static String curModuleType="";
	
	public static ConfigInfo configInfo = new ConfigInfo();
	
	public static String getIP(String type)
	{
		String ip;
		switch (type) {
		case CKS01:
			ip = "192.168.16.254";
			break;
		case CKY02:
			ip = "192.168.1.254";
			break;
		case CKY03:
			ip = "10.10.100.254";
			break;
		default:
			ip = "10.10.100.254";
			break;
		}
		return ip;
	}
	
	public static int getPort(String type)
	{
		int port;
		switch (type) {
		case CKS01:
			port = 5555;
			break;
		case CKY02:
			port = 8899;
			break;
		case CKY03:
			port = 8899;
			break;
		default:
			port = 8899;
			break;
		}
		return port;
	}
	
	public static String getHiFiPswd(String type)
	{
		String pswd;
		switch (type) {
		case CKS01:
			pswd = "12345678";
			break;
		case CKY02:
			pswd = "";
			break;
		case CKY03:
			pswd = null;
			break;
		default:
			pswd = null;
			break;
		}
		return pswd;
	}
	
	//for AP mode
	public static String getHiFiEncryptTypeStr(String moduleType)
	{
		String encryptType;
		switch (moduleType) {
		case CKS01:
			encryptType = WifiEncryptTypes.WPA_WPA2_AES;
			break;
		case CKY02:
			encryptType = WifiEncryptTypes.NONE;
			break;
		case CKY03:
			encryptType = WifiEncryptTypes.NONE;
			break;
		default:
			encryptType = WifiEncryptTypes.NONE;
			break;
		}
		return encryptType;
	}

	
	//nested class
	public static class ConfigInfo
	{
		public String mWifiName = null;
		public String mWifiEncryptType = null;
		public String mWifiPwd = null;
		public String mFarIP = null;
		public String mFarPort = null;
		public String mNearIP = null;
		public String mSubMask = null;
		public String mGateway = null;
		public String mDNS = null;
		public String mIPType = null;
		
		public ConfigInfo()
		{
			
		}
		
	}

}
