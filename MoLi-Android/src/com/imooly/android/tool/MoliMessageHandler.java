package com.imooly.android.tool;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.imooly.android.R;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

/***
 * 推送自定义消息处理
 * 
 * @author LSD
 * 
 */
public class MoliMessageHandler extends UmengMessageHandler {
	public String TAG = "MoliMessageHandler";

	@Override
	public Notification getNotification(Context context, UMessage msg) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
		myNotificationView.setTextViewText(R.id.notification_title, msg.title);
		myNotificationView.setTextViewText(R.id.notification_text, msg.text);
		Bitmap largeBitmap = getLargeIcon(context, msg);
		if (largeBitmap == null) {
			largeBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon);
		}
		myNotificationView.setImageViewBitmap(R.id.notification_large_icon, largeBitmap);
		builder.setLargeIcon(largeBitmap);

		int smallIcon = getSmallIconId(context, msg);
		if (smallIcon == 0) {
			smallIcon = R.drawable.app_icon;
		}
		myNotificationView.setImageViewResource(R.id.notification_small_icon, smallIcon);
		builder.setSmallIcon(smallIcon);

		builder.setContent(myNotificationView);
		builder.setAutoCancel(true);
		Notification mNotification = builder.build();
		// 由于Android
		// v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
		mNotification.contentView = myNotificationView;
		return mNotification;
	}
}
