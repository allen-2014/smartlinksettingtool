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
     * �õ��Զ����progressDialog 
     * @param context 
     * @param msg 
     * @return 
     */  
    public static ProgressWheelDialog createLoadingDialog(Context context, String msg) {  
  
        LayoutInflater inflater = LayoutInflater.from(context);  
        View v = inflater.inflate(R.layout.sola_loading_dialog, null);// �õ�����view  
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.linearlayout_loading_dialog);// ���ز���  

        //Dialog loadingDialog = new Dialog(context, R.style.sola_loading_dialog);// �����Զ�����ʽdialog  
        ProgressWheel pw = (ProgressWheel)v.findViewById(R.id.progresswheel_smartlink_search);
        ProgressWheelDialog loadingDialog = new ProgressWheelDialog(context, R.style.sola_loading_dialog, pw);
        
        
        //loadingDialog.getProgressWheel().resetCount();
        
        loadingDialog.setCancelable(false);// �������á����ؼ���ȡ��  
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(  
                LinearLayout.LayoutParams.MATCH_PARENT,  
                LinearLayout.LayoutParams.MATCH_PARENT));// ���ò���  
        return loadingDialog;
  
    }
    
    
    /**
     * ����Java�ļ����Ͳ��ܹ���ȷ�ĶԸ������������㣬����������ṩ��ȷ�ĸ��������㣬�����Ӽ��˳����������롣
     */
    public static class Arith { // Ĭ�ϳ������㾫��
    	private static final int DEF_DIV_SCALE = 10; // ����಻��ʵ����

    	private Arith() {
    	}

    	/**
    	 * �ṩ��ȷ�ļӷ����㡣
    	 * 
    	 * @param v1 ������
    	 * @param v2 ����
    	 * @return ���������ĺ�
    	 */
    	public static double add(double v1, double v2) {
    		BigDecimal b1 = new BigDecimal(Double.toString(v1));
    		BigDecimal b2 = new BigDecimal(Double.toString(v2));
    		return b1.add(b2).doubleValue();
    	}

    	/**
    	 * �ṩ��ȷ�ļ������㡣
    	 * 
    	 * @param v1 ������
    	 * @param v2 ����
    	 * @return ���������Ĳ�
    	 */
    	public static double sub(double v1, double v2) {
    		BigDecimal b1 = new BigDecimal(Double.toString(v1));
    		BigDecimal b2 = new BigDecimal(Double.toString(v2));
    		return b1.subtract(b2).doubleValue();
    	}

    	/**
    	 * �ṩ��ȷ�ĳ˷����㡣
    	 * 
    	 * @param v1 ������
    	 * @param v2 ����
    	 * @return ���������Ļ�
    	 */
    	public static double mul(double v1, double v2) {
    		BigDecimal b1 = new BigDecimal(Double.toString(v1));
    		BigDecimal b2 = new BigDecimal(Double.toString(v2));
    		return b1.multiply(b2).doubleValue();
    	}

    	/**
    	 * �ṩ����ԣ���ȷ�ĳ������㣬�����������������ʱ����ȷ�� С�����Ժ�10λ���Ժ�������������롣
    	 * 
    	 * @param v1 ������
    	 * @param v2 ����
    	 * @return ������������
    	 */
    	public static double div(double v1, double v2) {
    		return div(v1, v2, DEF_DIV_SCALE);
    	}

    	/**
    	 * �ṩ����ԣ���ȷ�ĳ������㡣�����������������ʱ����scale����ָ �����ȣ��Ժ�������������롣
    	 * 
    	 * @param v1 ������
    	 * @param v2 ����
    	 * @param scale ��ʾ��ʾ��Ҫ��ȷ��С�����Ժ�λ��
    	 * @return ������������
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
    	 * �ṩ��ȷ��С��λ�������봦��
    	 * 
    	 * @param v ��Ҫ�������������
    	 * @param scale С���������λ
    	 * @return ���������Ľ��
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
