package com.imooly.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.imooly.android.Net.NetUtils;
import com.imooly.android.tool.Config;

/***
 * 广播接收
 * 
 * @author LSD
 * 
 */
public class MoLiBroadcastReceiver extends BroadcastReceiver {
	boolean isLoading = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivityManager.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {
				if(Config.connectivity){
					return;
				}
				Config.connectivity = true;
				Log.i("MoLiBroadcastReceiver", "isAvailable");
				NetUtils.reRequest();
			}else{
				Config.connectivity = false;
			}
		}
	}

}
