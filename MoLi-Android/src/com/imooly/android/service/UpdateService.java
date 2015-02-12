package com.imooly.android.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import com.imooly.android.R;
import com.imooly.android.entity.RspOrderProfile.logisticEty;
import com.imooly.android.ui.SettingsActivity;
import com.imooly.android.utils.FileUtils;
import com.imooly.android.view.UpdateDialog;

/***
 * 更新版本
 * 
 * @author
 * 
 */
public class UpdateService extends Service {
	private static final int TIMEOUT = 6 * 1000;// 超时

	private static final int DOWN_COMPLETED = 1;
	private static final int DOWN_ERROR = 0;
	private static final int DOWN_PROGRESS = 2;

	private String mDownloadUrl;
	private String mAppName;
	private String mAppInfo;
	private NotificationManager mNotificationManager;
	private Notification mNotification;

	private Intent mUpdateIntent;
	private PendingIntent mPendingIntent;

	private int mNotificationID = 0;

	private RemoteViews mContentView;

	private String mTotalSizeM;// 文件总大小

	private boolean isFirstStart = true;

	private int size = 0;

	/***
	 * 更新UI
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case DOWN_PROGRESS:
				mContentView.setTextViewText(R.id.notificationPercent, "下载进度：" + msg.arg1 + "%");
				String downloadSize = String.format("%.2f", (float) msg.arg2 / 1024.0f / 1024.0f);
				mContentView.setTextViewText(R.id.notificationSize, downloadSize + "M" + "/" + mTotalSizeM + "M");
				// contentView.setProgressBar(R.id.notificationProgress, 100,
				// updateCount, false);

				mNotificationManager.notify(mNotificationID, mNotification);

				break;
			case DOWN_COMPLETED:
				// 下载完成，点击安装
				Uri uri = Uri.fromFile(FileUtils.updateFile);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(uri, "application/vnd.android.package-archive");

				mPendingIntent = PendingIntent.getActivity(UpdateService.this, 0, intent, 0);

				mNotification.setLatestEventInfo(UpdateService.this, mAppName, "下载成功，点击安装", mPendingIntent);
				mNotification.flags = Notification.FLAG_AUTO_CANCEL;
				mNotificationManager.notify(mNotificationID, mNotification);
				// 关闭service
				stopService(mUpdateIntent);

				// 自动弹出安装界面
				installApk();

				break;

			case DOWN_ERROR:
				mNotification.setLatestEventInfo(UpdateService.this, mAppName, "下载失败", mPendingIntent);
				break;

			default:
				stopService(mUpdateIntent);
				break;
			}

		}

	};

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("xxx", "onCreate...");

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		flags = START_REDELIVER_INTENT;

		Log.d("xxx", "onStartCommand...");
		
			mAppName = intent.getStringExtra(UpdateDialog.APP_NAME);
			mAppInfo = intent.getStringExtra(UpdateDialog.APP_INFO);
			mDownloadUrl = intent.getStringExtra(UpdateDialog.APP_URL);
			// 创建文件
			FileUtils.getInstance().createAppFile(mAppName);
			createNotification();
			createThread();

		return super.onStartCommand(intent, flags, startId);

	}

	/***
	 * 开线程下载
	 */
	public void createThread() {

		new Thread(new Runnable() {

			Message message = new Message();

			@Override
			public void run() {

				try {
					long downloadSize = downloadUpdateFile(mDownloadUrl, FileUtils.updateFile.toString());
					if (downloadSize > 0) {
						// 下载成功
						message.what = DOWN_COMPLETED;
						handler.sendMessage(message);
					}

				} catch (Exception e) {
					e.printStackTrace();
					message.what = DOWN_ERROR;
					handler.sendMessage(message);
				}

			}
		}).start();
	}

	/***
	 * 创建通知栏
	 */

	public void createNotification() {

		Log.e("xxx", "createNotification...");

		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotification = new Notification();
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;

		/***
		 * 在这里我们用自定的view来显示Notification
		 */
		mNotification.icon = R.drawable.ic_launcher; // 不设置显示不了notification

		mContentView = new RemoteViews(getPackageName(), R.layout.notification_update);
		mContentView.setTextViewText(R.id.notificationTitle, mAppInfo);
		mContentView.setTextViewText(R.id.notificationPercent, "0%");

		mNotification.contentView = mContentView;

		mUpdateIntent = new Intent(this, SettingsActivity.class);
		mUpdateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		mPendingIntent = PendingIntent.getActivity(this, 0, mUpdateIntent, 0);

		mNotification.contentIntent = mPendingIntent;

		mNotificationManager.notify(mNotificationID, mNotification);

	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(FileUtils.updateFile.toString());
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		getApplication().getApplicationContext().startActivity(i);

	}

	/***
	 * 下载文件
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	public long downloadUpdateFile(String down_url, String file) throws Exception {

		int downloadCount = 0;// 已经下载好的大小
		int updateCount = 0;// 已经上传的文件大小
		InputStream inputStream;
		OutputStream outputStream;

		URL url = new URL(down_url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setConnectTimeout(TIMEOUT);
		httpURLConnection.setReadTimeout(TIMEOUT);
		// 获取下载文件的size
		int totalSize = httpURLConnection.getContentLength();

		mTotalSizeM = String.format("%.2f", (float) totalSize / 1024.0f / 1024.0f);

		if (httpURLConnection.getResponseCode() == 404) {
			throw new Exception("fail!");
		}
		inputStream = httpURLConnection.getInputStream();
		outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉
		byte buffer[] = new byte[1024];
		int readsize = 0;
		int progressPre = 0;
		while ((readsize = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, readsize);
			downloadCount += readsize;// 时时获取下载到的大小

			int progress = (int) (((float) downloadCount / totalSize) * 100);

			if (progress != progressPre) {

				Message message = new Message();
				message.what = DOWN_PROGRESS;
				message.arg1 = progress;
				message.arg2 = downloadCount;
				handler.sendMessage(message);
			}

			progressPre = progress;

		}

		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}

		inputStream.close();
		outputStream.close();

		return downloadCount;

	}

	@Override
	public void onDestroy() {
		Log.d("xxx", "onDestroy...");
		super.onDestroy();
	}

}
