package com.imooly.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.imooly.android.tool.TicketUtil;

public class MoLiService extends Service {
	private Handler mHandler;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("service", "start");
		mHandler = new Handler();
		startTimer(1);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("service", "destory");
		stopTimer();
		super.onDestroy();
	}

	/**************
	 * 定时器
	 */
	private void startTimer(int time) {
		mHandler.postDelayed(task, time * 60000);
	}

	private void stopTimer() {
		mHandler.removeCallbacks(task);
	}

	private Runnable task = new Runnable() {
		public void run() {
			stopTimer();
			
			TicketUtil.instance().refrashTicket();
			
			startTimer(10);
		}
	};
}
