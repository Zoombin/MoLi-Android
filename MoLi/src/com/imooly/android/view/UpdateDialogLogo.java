package com.imooly.android.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.entity.RspAppNewVersion;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.service.UpdateService;
import com.imooly.android.ui.LogoActivity;
import com.imooly.android.utils.Utils;
import com.imooly.android.widget.CustomDialog;
import com.imooly.android.widget.Toast;
import com.imooly.android.widget.CustomDialog.CGravity;
/***
 * 更新
 * @author
 * 
 */
public class UpdateDialogLogo {
	public static final String APP_NAME = "app_name";
	public static final String APP_INFO = "app_info";
	public static final String APP_URL = "app_url";
	
	private Context mContext =  null;
	private onUpdateCallBack callBack = null;
	private String mForceUpdate = null;
	
	
	public interface onUpdateCallBack {
		public void onForceUpdate();
		public void onDialogCancel();
		public void onDialogSure();
	}
	
	public UpdateDialogLogo(Context context, onUpdateCallBack callBack) {
		this.mContext = context;
		this.callBack = callBack;
		checkUpdate();
	}
	
	/***
	 * 检查更新
	 * 
	 * @param auto
	 */
	public void checkUpdate() {
		//获取客户端配置的版本 格式：v1.0 
		/**如果修改版本显示，记得更改这里*/
		String clinetVersion = Utils.getClientVersionName(mContext);
		clinetVersion = clinetVersion.replace("v", "");
		
		Api.getAppnewversion(mContext, clinetVersion, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspAppNewVersion rsp = (RspAppNewVersion) rspData;
				RspAppNewVersion.Data data = rsp.data;
				
				String hasnewversion = data.getHasnewversion();
				String serverVersion = data.getLatestversion();
				String appInfo = data.getDescription();
				String url = data.getDownloadaddress();
				mForceUpdate = data.getForceupdate();
				
				if("0".equals(hasnewversion)){
					callBack.onDialogSure();
					return;
				}
				
				if("1".equals(mForceUpdate)){
					//强制更新
					showUpdateDialog(mContext, true, appInfo, url);
					return;
				}
				
				if (TextUtils.equals("1", hasnewversion)) {
					showUpdateDialog(mContext, false,appInfo, url);
				}
			}

			@Override
			public void failed(String msg) {
				Log.d("xxx", msg);
				callBack.onDialogCancel();
			}
		}, RspAppNewVersion.class);
	}
	
	private void showUpdateDialog(Context context, final boolean forceupdate,final String appInfo, final String url) {
		final View view = LayoutInflater.from(context).inflate(R.layout.dialog_check_update, null);
		TextView textView = (TextView) view.findViewById(R.id.text_version_info);
		textView.setText(appInfo);
		
		final CustomDialog dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setContentVw(view).setGravity(CGravity.CENTER).show();

		if(forceupdate){
			//强制更新不能取消
			view.findViewById(R.id.button_cancel).setVisibility(View.GONE);
			
			//同时提示用户
			Toast.show(context, "必须更新后才能使用！");
		}
		
		view.findViewById(R.id.button_cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				callBack.onDialogCancel();
			}
		});

		view.findViewById(R.id.button_sure).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if("1".equals(mForceUpdate)){
					//强制更新
					callBack.onForceUpdate();
				}else {
					callBack.onDialogSure();
				}
				
				Intent updateIntent = new Intent(mContext, UpdateService.class);
				updateIntent.putExtra(UpdateDialogLogo.APP_NAME, mContext.getResources().getString(R.string.app_name));
				updateIntent.putExtra(UpdateDialogLogo.APP_INFO, appInfo);
				updateIntent.putExtra(UpdateDialogLogo.APP_URL, url);
				
				mContext.startService(updateIntent);
			}
		});
	}

}
