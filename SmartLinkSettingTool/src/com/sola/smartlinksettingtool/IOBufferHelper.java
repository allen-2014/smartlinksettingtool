/**
 * 
 */
package com.sola.smartlinksettingtool;

/**
 * @author Administrator
 *
 */
public class IOBufferHelper {
    /// <summary>
    /// byte数组混淆加密
    /// </summary>
    /// <param name="data"></param>
    /// <returns></returns>
    public static byte[] BufferConvert(byte[] data)
    {                                   
        byte[] result = new byte[data.length];           
        for(int i=0; i<data.length; i++)
        {
            result[i] = ByteConvert(data[i]);
        }
        return result;
    }

    /// <summary>
    /// 混淆加密还原
    /// </summary>
    /// <param name="data"></param>
    /// <returns></returns>
    public static byte[] BufferReConvert(byte[] data)
    {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++)
        {
            result[i] = ByteReConvert(data[i]);
        }
        return result;
    }

    private static byte ByteConvert(byte d)
    {
        return (byte)(d << 4 | d >> 4);           
    }

    private static byte ByteReConvert(byte d)
    {
        return (byte)(d >> 4 | d << 4);
    }
}
