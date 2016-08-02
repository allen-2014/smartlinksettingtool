package com.sola.smartlinksettingtool;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


import com.koushikdutta.async.*;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.ConnectCallback;
import com.koushikdutta.async.callback.DataCallback;



import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by reweber on 12/20/14.
 */
public class CommunicationClient {
	
	private final static int MAX_SIZE = 1024;
	public byte[] ProcessedDataBuffer;
	
	private static CommunicationClient client;
	private ArrayList<byte[]> data;
	private Charset US_ASCII = Charset.forName("US-ASCII");
	
    private String host;
    private int port;
    private AsyncSocket clientSocket = null;
    private Handler msgHandler = null;
    //
    public static CommunicationClient getInstance()
    {
    	
    	if(client == null)
    	{
    		client = new CommunicationClient();
    		return client;
    	}
    	return client;
    }
    
    
    protected CommunicationClient(String host, int port) {
    	//server ip and port
        this.host = host;
        this.port = port;
        data = new ArrayList<byte[]>();
        ProcessedDataBuffer = new byte[MAX_SIZE];
        if (ProcessedDataBuffer == null) {
        	
        	throw new RuntimeException(new Exception(ConstString.ClientAllocateFailedString));
        	
		}
    }
    
    protected CommunicationClient()
    {
    	data = new ArrayList<byte[]>();
    	ProcessedDataBuffer = new byte[MAX_SIZE];
    	if (ProcessedDataBuffer == null) {
        	
        	throw new RuntimeException(new Exception(ConstString.ClientAllocateFailedString));
        	
		}
    }
    
    public ArrayList<byte[]> getCachedData()
    {
    	return data;
    }
    
    public Charset US_ASCII()
    {
    	return US_ASCII;
    }
    
    public void Init(String host, int port, Handler messageHandler)
    {
    	//server ip and port
        this.host = host;
        this.port = port;
        msgHandler = messageHandler;
    }
    
    public void SetMessageHandler(Handler messageHandler)
    {
    	msgHandler = messageHandler;
    }
    
    public void Connect()
    {
    	DisConnect();
    	setup();
    }
    
    
    public void DisConnect()
    {
    	if(clientSocket != null)
    	{
    		clientSocket.end();
    		clientSocket.close();
    		clientSocket = null;
    	}
    }
    
    public void SendData(String msg)
    {
    	if(msg == null || clientSocket == null)
    	{
    		return;
    	}
    	//
    	Util.writeAll(clientSocket, msg.getBytes(Charset.forName("US-ASCII")), new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
            	if (ex != null) //throw new RuntimeException(ex);
                {
                	System.out.println("[Client] Failed wrote message:" + ex.toString());
                }
                System.out.println("[Client] Successfully wrote message");
            }
        });
    }
    
    
    public void SendData(byte[] msgBytes)
    {
    	if(msgBytes == null || clientSocket == null)
    	{
    		return;
    	}
    	//
    	Util.writeAll(clientSocket, msgBytes, new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) //throw new RuntimeException(ex);
                {
                	System.out.println("[Client] Failed wrote message:" + ex.toString());
                }
                System.out.println("[Client] Successfully wrote message");
            }
        });
    }
   


    private void setup() {
    	//AsyncServer.getDefault().stop()
        AsyncServer.getDefault().connectSocket(new InetSocketAddress(host, port), new ConnectCallback() {
            @Override
            public void onConnectCompleted(Exception ex, final AsyncSocket socket) {
            	clientSocket = socket;
                handleConnectCompleted(ex, socket);
            }
        });
    }
    
    

    private void handleConnectCompleted(Exception ex, final AsyncSocket socket) {
    	
        //if(ex != null) throw new RuntimeException(ex);
    	if(ex != null)
    	{
    		System.out.println(ex.toString());
    		
    		sendData(null, -1);
    	
    		return;
    	}
    	
    	sendData(null, 0);
    	
//        Util.writeAll(socket, "Hello Server".getBytes(), new CompletedCallback() {
//            @Override
//            public void onCompleted(Exception ex) {
//                if (ex != null) throw new RuntimeException(ex);
//                System.out.println("[Client] Successfully wrote message");
//            }
//        });

        socket.setDataCallback(new DataCallback() {
            @Override
            public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                //System.out.println("[Client] Received Message " + new String(bb.getAllByteArray()));
               	byte[] res = bb.getAllByteArray();
               	
               	String tmp = new String(res,Charset.forName("US-ASCII"));
               	if(tmp.contains("config"))
               	{
               		System.out.println("[Client Normal] Received Message has config");
               	}
            	System.out.println("[Client Normal] Received Message " + tmp);

            	sendData(res, 1);
            }
        });

        socket.setClosedCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                //if(ex != null) throw new RuntimeException(ex);
            	if(ex != null)
            	{
            		System.out.println(ex.toString());
            	}
                System.out.println("[Client] Successfully closed connection");
            }
        });

        socket.setEndCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                //if(ex != null) throw new RuntimeException(ex);
            	if(ex != null)
            	{
            		System.out.println(ex.toString());
            	}
                System.out.println("[Client] Successfully end connection");
            }
        });
    }

    //
	private void sendData(byte[] res, int flag) {
		try {

			Message msg = new Message();
			msg.what = flag;// 1;
			Bundle data = new Bundle();
			data.putByteArray("msg", res);

			msg.setData(data);
			// 使用同步消息机制对消息进行处理
			msgHandler.sendMessage(msg);

		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}

