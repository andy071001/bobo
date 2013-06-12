package com.svo.love.model;

import com.svo.love.MainActivity;
import com.svo.love.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * 发送通知的类
 * @author Administrator
 *
 */
public class MNotify {
	private NotificationManager manager;
	private Context context;
	public MNotify(Context context){
		manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		this.context = context;
		
	}
	public void notifyLove(String subHead) {
		int icon = R.drawable.logo;
		CharSequence tickerText = "有人在广播里提到了你，快去看看吧";
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		notification.defaults = Notification.DEFAULT_ALL;
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent ,0);
		notification.contentIntent = contentIntent;
		notification.setLatestEventInfo(context, tickerText, subHead, contentIntent);
		manager.notify(subHead, 0, notification);
	}
}
