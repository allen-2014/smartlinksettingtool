/**
 * 
 */
package com.sola.smartlinksettingtool;

import java.nio.ByteBuffer;


/**
 * @author Administrator
 *
 */
public class InstructionSet {
	
	public static byte[] sendData = null;
	//
	//msgHead(10bytes,已小端) + "register:1"
	//old
//	public final static byte[] instruction_register_1 = new byte[] {0xA,0x00,0x02,0x00,0x10,0x00,0x01,0x01,0x00,0x00,
//												0x72,0x65,0x67,0x69,0x73,0x74,0x65,0x72,0x3A,0x31};
	public final static byte[] instruction_register_1 = new byte[] {0x0B,0x00,0x02,0x00,0x10,0x00,0x01,0x01,0x00,0x00,
																	0x72,0x65,0x67,0x69,0x73,0x74,0x65,0x72,0x3A,0x31,0x68};
	
	//已小端
	//old
//	public final static byte[] instruction_heart_1 = new byte[] {0x07,0x00,0x02,0x00,0x10,0x00,0x01,0x01,0x00,0x00,
//												0x68,0x65,0x61,0x72,0x74,0x3A,0x31};
	//new
	public final static byte[] instruction_heart_1 = new byte[] {0x08,0x00,0x02,0x00,0x10,0x00,0x01,0x01,0x00,0x00,
																 0x68,0x65,0x61,0x72,0x74,0x3A,0x31,(byte)0xE5};
	
	
	public final static String registerStr = "register";
	public final static String configStr = "config";
	public final static String doneStr = "done";
	//public final static String register1Str = "register";
	public final static String heartStr = "heart";
	public final static String errorStr = "error";
	
	//tip
	public final static String tipErrorSetting = "配置出错";
	public final static String tipDoneSetting = "配置成功";
	//end of tip
	
	
	
	private final static int  clientOffset = 10;
	public final static int GetClientOffset()
	{
		return clientOffset;
	}
	private final static int  serverOffset = 17;
	//
	//register0/register1
	//heart0/heart1
	//instruction:
	public static String GetInstructionStr(int svrClient, byte[] instruction)
	{
		if(instruction == null)
		{
			return null;
		}
		short len = (short) ((instruction[1] << 8) | instruction[0] & 0xff);

		//第1个字节为总字节数
		int totalBytesCnt = len + 10;//10字节头长度
		String istrctString = "";
		if(svrClient == 0)//client
		{
			for(int i = clientOffset; i < totalBytesCnt;++i)
			{
				if(instruction[i] == 0x3A)
				{
					istrctString = new String(instruction, clientOffset, i  - clientOffset);
					break;
				}
			}
			istrctString = new String(instruction, clientOffset, totalBytesCnt - clientOffset, CommunicationClient.getInstance().US_ASCII());
		}
		else if(svrClient == 1)//server
		{
			
			for(int i = serverOffset; i < totalBytesCnt;++i)
			{
				if(instruction[i] == 0x3A)
				{
					istrctString = new String(instruction, serverOffset, i - serverOffset, CommunicationClient.getInstance().US_ASCII());
					break;
				}
			}
			
		}

		return istrctString;
	}
	
	//
	public static void decrypt(byte[] encryptedData, int startIdx, int endIdx)
	{
		if(encryptedData == null || startIdx < 0 || endIdx >= encryptedData.length || endIdx < startIdx)
		{
			return;
		}
		for (int i = startIdx; i < endIdx; i++) {
			encryptedData[i] = (byte)(encryptedData[i] ^ 0x01);
		}
	}
	//
	public static void encrypt(byte[] oridData, int startIdx, int endIdx)
	{
		if(oridData == null || startIdx < 0 || endIdx >= oridData.length || endIdx < startIdx)
		{
			return;
		}
		for (int i = startIdx; i < endIdx; i++) {
			
			oridData[i] = (byte)(oridData[i] ^ 0x01);
		}
	}
	
}
