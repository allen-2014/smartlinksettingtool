package com.sola.smartlinksettingtool;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MsgHeader {
	
	public final static short length = 10;
	
	//totla bytes(data) not including length of head
    short sLen; // 长度 unsigned short,

    short sModel = 0x0002;//Ӳ������
    
    short sVer = 0x0010; // 版本 unsigned short

    short sSn = 0x0101; // 消息类型 unsigned short

    short sFlags = 0x0000; // 保留 unsigned short


	private ByteBuffer byteBuffer = ByteBuffer.allocate(10);

    public MsgHeader() {

    }

    public MsgHeader(short slen, short sVer, short smsgtype) {
//        this.sLen = slen;
//        this.sVer = sVer;
//        this.sMsgType = smsgtype;
//        this.sData = 0;
    }

    public MsgHeader(short slen, short smsgtype) {
//        this.sLen = slen;
//        this.sVer = 1;
//        this.sMsgType = smsgtype;
//        this.sData = 0;
    }

    public MsgHeader(short slen, short sver, short smsgtype, short sdata) {
//        this.sLen = slen;
//        this.sVer = sver;
//        this.sMsgType = smsgtype;
//        this.sData = sdata;
    }

    /**
     * �?16位的short转换成byte数组
     * 
     * @param s
     *            short
     * @return byte[] 长度�?2
     * */
    public static byte[] shortToByteArray(short s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    /**
     * 将short转为高字节在前，低字节在后的byte数组
     * 
     * @param n
     *            short
     * @return byte[]
     */
    public static byte[] shortToHH(short n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }

    /**
     * 头协议定�?
     * 
     * @return
     */
    public byte[] getBytes() {
        // byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        // byteBuffer.put(shortToByteArray(sLen));
        // // 新版采集端协议头
        // byteBuffer.put(shortToByteArray(sVer));
        // byteBuffer.put(shortToByteArray(sMsgType));
        // byteBuffer.put(shortToByteArray(sData));
        // return byteBuffer.array();
    	if(byteBuffer.position() > 0)
    	{
    		return byteBuffer.array();
    	}
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.put(shortToHH(sLen));
        byteBuffer.put(shortToHH(sModel));
        // 新版采集端协议头
        byteBuffer.put(shortToHH(sVer));
        byteBuffer.put(shortToHH(sSn));
        byteBuffer.put(shortToHH(sFlags));
        return byteBuffer.array();
    }

    public void putBytes(byte[] bytes) {
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        
        byteBuffer.put(bytes);
        this.sLen = byteBuffer.getShort(0);
        this.sModel = byteBuffer.getShort(2);
        this.sVer = byteBuffer.getShort(4);
        this.sSn = byteBuffer.getShort(6);
        this.sFlags = byteBuffer.getShort(8);
    }
    
    public void putBytes(byte[] bytes, int offset, int length) {
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.put(bytes, offset, length);
        this.sLen = byteBuffer.getShort(0);
        this.sModel = byteBuffer.getShort(2);
        this.sVer = byteBuffer.getShort(4);
        this.sSn = byteBuffer.getShort(6);
        this.sFlags = byteBuffer.getShort(8);
    }
    
    
    public short getsLen() {
        return sLen;
    }

    public void setsLen(short sLen) {
        this.sLen = sLen;
    }

    public short getsVer() {
        return sVer;
    }

    public void setsVer(short sVer) {
        this.sVer = sVer;
    }
    
    
    public short getsModel() {
		return sModel;
	}

	public void setsModel(short sModel) {
		this.sModel = sModel;
	}

	public short getsSn() {
		return sSn;
	}

	public void setsSn(short sSn) {
		this.sSn = sSn;
	}

	public short getsFlags() {
		return sFlags;
	}

	public void setsFlags(short sFlags) {
		this.sFlags = sFlags;
	}


    @Override
    public String toString() {
        return "MsgHeader [sLen=" + sLen + ", sVer=" + sVer + ", sModel="
                + sModel + ", sSn=" + sSn + ", sFlags=" + sFlags + ", byteBuffer=" + byteBuffer
                + "]";
    }

}
