/**
 * 
 */
package com.sola.uicomponent;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.sola.smartlinksettingtool.Utils;
import com.sola.smartlinksettingtool.WifiWraper.WifiStruct;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;

/**
 * @author Administrator
 * 
 */
public class ProgressWheelDialog extends Dialog {

	private ProgressWheel progress;
	private Timer mTimer;
	private int intervalMS = 167;
	
	public ProgressWheelDialog(Context context, int themeResId,
			ProgressWheel progressWheel) {
		super(context, themeResId);
		// TODO Auto-generated constructor stub
		progress = progressWheel;

	}

	public ProgressWheelDialog(Context context, int themeResId) {
		super(context, themeResId);
		// TODO Auto-generated constructor stub
	}

	public ProgressWheelDialog(Context context) {
		super(context);
	}

	public ProgressWheel getProgressWheel() {
		return progress;
	}

	@Override
	public void show() {
		super.show();
		progress.resetCount();
		onStartTimer();
	}

	@Override
	public void dismiss() {
		super.dismiss();
		onStopTimer();
	}
	
	public void setPeriodMS(int ms)
	{
		intervalMS = ms / 360;
	}

	private void onStartTimer() {
		if (mTimer != null) {
			return;
		}
		mTimer = new Timer();
		mTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
						progress.incrementProgress();
						int res = (int)Utils.Arith.div(progress.getProgress() * 100, 360.0, 0);
						progress.setText(res + "%");
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		}, 0, intervalMS);
	}

	private void onStopTimer() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}

}
