/**
 * 
 */
package com.sola.smartlinksettingtool;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sola.uicomponent.ProgressWheel;
import com.sola.uicomponent.ProgressWheelDialog;

import android.app.Dialog;
import android.content.Context;
import android.media.audiofx.LoudnessEnhancer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;


/**
 * @author Administrator
 *
 */
public class Utils {
	

	public static boolean bIp(String ipAddress)  
	{  
	       String ip = "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
	    		   		+ "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
	    		   		+ "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
	    		   		+ "|[1-9][0-9]|[0-9]))";   
	       Pattern pattern = Pattern.compile(ip);   
	       Matcher matcher = pattern.matcher(ipAddress);   
	       return matcher.matches();   
	}
	
	public static void showInfo(Context context, String msg) {
		
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

	}
	
	//
	public static void startLoading(Context context, final ImageView iv)
	{
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.sola_rotate_loading);

		anim.setInterpolator(new Interpolator() {
			private final int frameCount = 12;

			@Override
			public float getInterpolation(float input) {
				return (float) Math.floor(input * frameCount) / frameCount;
			}
		});
		
		iv.startAnimation(anim);
		
	}
	
	public static void stopLoading(ImageView iv)
	{
		iv.clearAnimation();
	}
	
	
	/** 
     * 得到自定义的progressDialog 
     * @param context 
     * @param msg 
     * @return 
     */  
    public static ProgressWheelDialog createLoadingDialog(Context context, String msg) {  
  
        LayoutInflater inflater = LayoutInflater.from(context);  
        View v = inflater.inflate(R.layout.sola_loading_dialog, null);// 得到加载view  
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.linearlayout_loading_dialog);// 加载布局  

        //Dialog loadingDialog = new Dialog(context, R.style.sola_loading_dialog);// 创建自定义样式dialog  
        ProgressWheel pw = (ProgressWheel)v.findViewById(R.id.progresswheel_smartlink_search);
        ProgressWheelDialog loadingDialog = new ProgressWheelDialog(context, R.style.sola_loading_dialog, pw);
        
        
        //loadingDialog.getProgressWheel().resetCount();
        
        loadingDialog.setCancelable(false);// 不可以用“返回键”取消  
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(  
                LinearLayout.LayoutParams.MATCH_PARENT,  
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局  
        return loadingDialog;
  
    }
    
    
    /**
     * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精确的浮点数运算，包括加减乘除和四舍五入。
     */
    public static class Arith { // 默认除法运算精度
    	private static final int DEF_DIV_SCALE = 10; // 这个类不能实例化

    	private Arith() {
    	}

    	/**
    	 * 提供精确的加法运算。
    	 * 
    	 * @param v1 被加数
    	 * @param v2 加数
    	 * @return 两个参数的和
    	 */
    	public static double add(double v1, double v2) {
    		BigDecimal b1 = new BigDecimal(Double.toString(v1));
    		BigDecimal b2 = new BigDecimal(Double.toString(v2));
    		return b1.add(b2).doubleValue();
    	}

    	/**
    	 * 提供精确的减法运算。
    	 * 
    	 * @param v1 被减数
    	 * @param v2 减数
    	 * @return 两个参数的差
    	 */
    	public static double sub(double v1, double v2) {
    		BigDecimal b1 = new BigDecimal(Double.toString(v1));
    		BigDecimal b2 = new BigDecimal(Double.toString(v2));
    		return b1.subtract(b2).doubleValue();
    	}

    	/**
    	 * 提供精确的乘法运算。
    	 * 
    	 * @param v1 被乘数
    	 * @param v2 乘数
    	 * @return 两个参数的积
    	 */
    	public static double mul(double v1, double v2) {
    		BigDecimal b1 = new BigDecimal(Double.toString(v1));
    		BigDecimal b2 = new BigDecimal(Double.toString(v2));
    		return b1.multiply(b2).doubleValue();
    	}

    	/**
    	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
    	 * 
    	 * @param v1 被除数
    	 * @param v2 除数
    	 * @return 两个参数的商
    	 */
    	public static double div(double v1, double v2) {
    		return div(v1, v2, DEF_DIV_SCALE);
    	}

    	/**
    	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
    	 * 
    	 * @param v1 被除数
    	 * @param v2 除数
    	 * @param scale 表示表示需要精确到小数点以后几位。
    	 * @return 两个参数的商
    	 */
    	public static double div(double v1, double v2, int scale) {
    		if (scale < 0) {
    			throw new IllegalArgumentException(
    					"The scale must be a positive integer or zero");
    		}
    		BigDecimal b1 = new BigDecimal(Double.toString(v1));
    		BigDecimal b2 = new BigDecimal(Double.toString(v2));
    		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    	}

    	/**
    	 * 提供精确的小数位四舍五入处理。
    	 * 
    	 * @param v 需要四舍五入的数字
    	 * @param scale 小数点后保留几位
    	 * @return 四舍五入后的结果
    	 */
    	public static double round(double v, int scale) {
    		if (scale < 0) {
    			throw new IllegalArgumentException(
    					"The scale must be a positive integer or zero");
    		}
    		BigDecimal b = new BigDecimal(Double.toString(v));
    		BigDecimal one = new BigDecimal("1");
    		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    	}
    };
	
	
}
