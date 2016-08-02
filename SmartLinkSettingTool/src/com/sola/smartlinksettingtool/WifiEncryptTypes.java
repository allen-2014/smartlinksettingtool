package com.sola.smartlinksettingtool;

import java.util.ArrayList;
import java.util.List;

import android.R.string;
import android.net.wifi.WifiConfiguration;


public class WifiEncryptTypes {
	
	public final static short noneEncrypt = -1;
	public final static short WEP_0 = 0x0000;
	public final static short WPA_1 = 0x0001;
	public final static short WPA2_PSK_1 = 0x0001;
	public final static short EAP802_1_2 = 0x0002;
	public final static short WAPI_PSK_3 = 0x0003;
	public final static short WAPI_CERT_4 = 0x0004;
	public final static String NONE = "NONE";
	public final static String WEP_OPEN = "WEP_OPEN";//not sure
	public final static String WEP_SHARE = "WEP_SHARE";//not sure
	public final static String WPA_TKIP = "WPA_TKIP";
	public final static String WPA_AES = "WPA_AES";
	public final static String WPA2_TKIP = "WPA2_TKIP";
	public final static String WPA2_AES = "WPA2_AES";
	public final static String WPA_WPA2_TKIP = "WPA/WPA2_TKIP";//not sure
	public final static String WPA_WPA2_AES = "WPA/WPA2_AES";
	
	public enum SecurityType {
        OPEN, WEP64, WEP128, WPA_TKIP, WPA2_AES
    };
	
	
	//constructor
	public WifiEncryptTypes()
	{
		
	}
	
	public static List<String> getAllTypes()
	{
		List<String> typesList = new ArrayList<String>();
		if(SwitchModule.CKS01.equals(SwitchModule.curModuleType))
		{
			typesList.add(WifiEncryptTypes.NONE);
			typesList.add(WifiEncryptTypes.WEP_OPEN);
			typesList.add(WifiEncryptTypes.WEP_SHARE);
			typesList.add(WifiEncryptTypes.WPA_TKIP);
			typesList.add(WifiEncryptTypes.WPA_AES);
			typesList.add(WifiEncryptTypes.WPA2_TKIP);
			typesList.add(WifiEncryptTypes.WPA2_AES);
			typesList.add(WifiEncryptTypes.WPA_WPA2_TKIP);
			typesList.add(WifiEncryptTypes.WPA_WPA2_AES);
		}
		else if(SwitchModule.CKY03.equals(SwitchModule.curModuleType))
		{
			typesList.add(WifiEncryptTypes.NONE);
//			typesList.add("WEP_OPEN");
//			typesList.add("WEP_SHARE");
//			typesList.add("WPA_TKIP");
//			typesList.add("WPA/WPA2_TKIP");
			typesList.add(WifiEncryptTypes.WPA_WPA2_AES);
		}

		
		return typesList;
	}
	
	
	//for module configuration
	public static String getWiFiEncryptStrforConfig(String WiFiEncryptItem)
	{
		String str = null;
		switch (SwitchModule.curModuleType) {
		case SwitchModule.CKS01:
			if(WiFiEncryptItem.equals(WifiEncryptTypes.NONE))
			{
				str = "none";
			}
			else if(WiFiEncryptItem.equals(WifiEncryptTypes.WPA_WPA2_AES))
			{
				str = "wpawpa2_aes";
			}
			else if(WiFiEncryptItem.equals(WifiEncryptTypes.WEP_OPEN))
			{
				str = "wep_open";
			}
			else if(WiFiEncryptItem.equals(WifiEncryptTypes.WEP_SHARE))
			{
				str = "wep";
			}
			else if(WiFiEncryptItem.equals(WifiEncryptTypes.WPA_TKIP))
			{
				str = "wpa_tkip";
			}
			else if(WiFiEncryptItem.equals(WifiEncryptTypes.WPA_AES))
			{
				str = "wpa_aes";
			}
			else if(WiFiEncryptItem.equals(WifiEncryptTypes.WPA2_TKIP))
			{
				str = "wpa2_tkip";
			}
			else if(WiFiEncryptItem.equals(WifiEncryptTypes.WPA2_AES))
			{
				str = "wpa2_aes";
			}
			else if(WiFiEncryptItem.equals(WifiEncryptTypes.WPA_WPA2_TKIP))
			{
				str = "wpawpa2_tkip";
			}
			else if(WiFiEncryptItem.equals(WifiEncryptTypes.WPA_WPA2_AES))
			{
				str = "wpawpa2_aes";
			}
			break;
		case SwitchModule.CKY02:
			
			break;
		case SwitchModule.CKY03:
			if(WiFiEncryptItem.equals(WifiEncryptTypes.NONE))
			{
				str = "NONE";
			}
			else if(WiFiEncryptItem.equals(WifiEncryptTypes.WPA_WPA2_AES))
			{
				str = "WPA2PSK,AES";
			}
			break;
		default:
			if(WiFiEncryptItem.equals(WifiEncryptTypes.NONE))
			{
				str = "NONE";
			}
			else if(WiFiEncryptItem.equals(WifiEncryptTypes.WPA_WPA2_AES))
			{
				str = "WPA2PSK,AES";
			}
			break;
		}
		
		return str;
	}
	
	
	
	
	public static String wifiCapability2SecurityStr(String capabilities)
	{
		if(capabilities.toUpperCase().contains("WEP64") || capabilities.toUpperCase().contains("WEP128"))
		{
			return WifiEncryptTypes.NONE;
		}
		else if(capabilities.toUpperCase().contains("WEP"))
		{
			return WifiEncryptTypes.WEP_OPEN;
		}
		else if(capabilities.toUpperCase().contains("WPA-PSK") || capabilities.toUpperCase().contains("WPA_TKIP"))
		{
			return WifiEncryptTypes.WPA_TKIP;
		}
		else if(capabilities.toUpperCase().contains("WPA2-PSK") || capabilities.toUpperCase().contains("WPA2-AES"))
		{
			
			return WifiEncryptTypes.WPA2_AES;
		}
		
		return WifiEncryptTypes.NONE;
//		else if(capabilities.toLowerCase().contains(WifiEncryptTypes.WPA2_TKIP.toLowerCase()))
//		{
//			wifi.mWifiEncryptType = WifiEncryptTypes.WPA2_TKIP;
//		}
//		else if(capabilities.toLowerCase().contains(WifiEncryptTypes.WPA_AES.toLowerCase()))
//		{
//			wifi.mWifiEncryptType = WifiEncryptTypes.WPA_AES;
//		}
//
//		else if(capabilities.toLowerCase().contains(WifiEncryptTypes.WPA_WPA2_AES.toLowerCase()))	
//		{
//			wifi.mWifiEncryptType = WifiEncryptTypes.WPA_WPA2_AES;
//		}
//		else if(capabilities.toLowerCase().contains(WifiEncryptTypes.WPA_WPA2_TKIP.toLowerCase()))
//		{
//			wifi.mWifiEncryptType = WifiEncryptTypes.WPA_WPA2_TKIP;
//		}
	}
	
	public static SecurityType getSecurityType(String securityStr)
	{
		if(NONE.equals(securityStr))
		{
			return SecurityType.OPEN;
		}
		else if( securityStr.contains("WPA_TKIP"))
		{
			return SecurityType.WPA_TKIP;
		}
		else if(securityStr.contains("WPA2_AES"))
		{
			return SecurityType.WPA2_AES;
		}
		//
		return SecurityType.OPEN;
	}
	
}
