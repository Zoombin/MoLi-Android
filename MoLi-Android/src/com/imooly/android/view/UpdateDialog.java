package com.imooly.android.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
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
import com.imooly.android.widget.CustomDialog;
import com.imooly.android.widget.CustomDialog.CGravity;
import com.imooly.android.widget.Toast;

/***
 * 更新
 * @author
 * 
 */
public class UpdateDialog {
	
	public static final String APP_NAME = "app_name";
	public static final String APP_INFO = "app_info";
	public static final String APP_URL = "app_url";
	
	private Context mContext =  null;
	
	
	public UpdateDialog(Context context) {
		this.mContext = context;
	}
	
	public void checkUpdate() {

		//final int clinetVersion = getClientVersion();
		final int clinetVersion = 0;
		
		Api.getAppnewversion(mContext, 1.0 + "", new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspAppNewVersion rsp = (RspAppNewVersion) rspData;
				RspAppNewVersion.Data data = rsp.data;

				String serverVersion = data.getLatestversion();
				String appInfo = data.getDescription();
				String url = data.getDownloadaddress();
				
				if (Float.valueOf(serverVersion) > clinetVersion) {
					showUpdateDialog(mContext, appInfo, url);
				} else {
					Toast.show(mContext, "没有更新的版本");
				}

				

			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub

			}
		}, RspAppNewVersion.class);
	}

	private int getClientVersion() {

		PackageManager manager = mContext.getPackageManager();

		try {
			PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);

			// String appVersion = info.versionName; // 版本名

			return info.versionCode; // 版本号

		} catch (NameNotFoundException e) {

			e.printStackTrace();
		}

		return 0;
	}

	private void showUpdateDialog(Context context, final String appInfo, final String url) {

		final View view = LayoutInflater.from(context).inflate(R.layout.dialog_check_update, null);
		TextView textView = (TextView) view.findViewById(R.id.text_version_info);
		textView.setText(appInfo);
		
		final CustomDialog dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentVw(view).setGravity(CGravity.CENTER).show();

		view.findViewById(R.id.button_cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		view.findViewById(R.id.button_sure).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent updateIntent = new Intent(mContext, UpdateService.class);
				updateIntent.putExtra(UpdateDialog.APP_NAME, mContext.getResources().getString(R.string.app_name));
				updateIntent.putExtra(UpdateDialog.APP_INFO, appInfo);
				updateIntent.putExtra(UpdateDialog.APP_URL, url);
				
				mContext.startService(updateIntent);
			}
		});
	}

}
