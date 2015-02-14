package com.imooly.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.imooly.android.tool.HeartbeatAlarm;
import com.imooly.android.tool.TicketUtil;


/***
 * 广播接收
 * 
 * @author LSD
 * 
 */
public class MoLiBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i("HeartbeatAlarm", "beat");
		String action = intent.getAction();
		if (HeartbeatAlarm.action.equals(action)) {
			TicketUtil.instance().refrashTicket();
		}
	}

}
