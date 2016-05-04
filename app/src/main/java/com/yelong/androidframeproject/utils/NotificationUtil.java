package com.yelong.androidframeproject.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.InboxStyle;
import android.widget.RemoteViews;

/**
 * 消息工具类 3.0以前构建Notification对象用的是Notification.Builder
 * 3.0之后构建Notification对象用的是NotificationCompat.Builder
 * FLAG_CANCEL_CURRENT：如果构建的PendingIntent已经存在，则取消前一个，重新构建一个。
 * FLAG_NO_CREATE：如果前一个PendingIntent已经不存在了，将不再构建它。
 * FLAG_ONE_SHOT：表明这里构建的PendingIntent只能使用一次。
 * FLAG_UPDATE_CURRENT：如果构建的PendingIntent已经存在，则替换它，常用。
 * 
 * @author 800hr：yelong
 * 
 *         2015-5-19
 */
public class NotificationUtil {

	/**
	 * 构建标准消息
	 * 
	 * @param context
	 * @param title
	 *            消息的标题
	 * @param content
	 *            消息的内容
	 * @param ticker
	 *            第一次出现在屏幕上方的通知提示
	 * @param num
	 *            消息数量
	 * @param sIcon
	 *            消息的小图标
	 * @param bIcon
	 *            消息的大图标
	 * @param cls
	 *            启动的界面
	 */
	public static void setNormalNotification(Context context, String title,
			String content, String ticker, int num, int sIcon, Bitmap bIcon,
			Class<?> cls) {
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context);
		mBuilder.setContentTitle(title);
		mBuilder.setContentText(content);
		mBuilder.setNumber(num);
		mBuilder.setSmallIcon(sIcon);
		mBuilder.setLargeIcon(bIcon);
		// 第一次提示消息的时候显示在通知栏上
		mBuilder.setTicker(ticker);
		// 自己维护通知的消失
		mBuilder.setAutoCancel(true);

		Intent intent = new Intent(context, cls);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setContentIntent(pendingIntent);
		manager.notify(0, mBuilder.build());
	}

	/**
	 * 构建大视图消息
	 * 
	 * @param context
	 * @param title
	 *            消息的标题
	 * @param content
	 *            消息的内容
	 * @param lines
	 *            大视图显示的行文本
	 * @param ticker
	 *            第一次出现在屏幕上方的通知提示
	 * @param num
	 *            消息数量
	 * @param sIcon
	 *            消息的小图标
	 * @param bIcon
	 *            消息的大图标
	 * @param cls
	 *            启动的界面
	 */
	public static void setBigNotification(Context context, String title,
			String content, String[] lines, String ticker, int num, int sIcon,
			Bitmap bIcon, Class<?> cls) {
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context);
		mBuilder.setNumber(num);
		mBuilder.setSmallIcon(sIcon);
		mBuilder.setLargeIcon(bIcon);
		// 第一次提示消息的时候显示在通知栏上
		mBuilder.setTicker(ticker);
		// 自己维护通知的消失
		mBuilder.setAutoCancel(true);

		/**
		 * NotificationCompat.BigPictureStyle, 在细节部分显示一个256dp高度的位图。
		 * NotificationCompat.BigTextStyle，在细节部分显示一个大的文本块。
		 * NotificationCompat.InboxStyle，在细节部分显示一段行文本。
		 * 
		 * 如果仅仅显示一个图片，使用BigPictureStyle是最方便的； 如果需要显示一个富文本信息，则可以使用BigTextStyle；
		 * 如果仅仅用于显示一个文本的信息，那么使用InboxStyle即可。
		 */
		InboxStyle style = new InboxStyle();

		for (String line : lines) {
			style.addLine(line);
		}
		style.setBigContentTitle(title);
		style.setSummaryText(content);
		mBuilder.setStyle(style);

		Intent intent = new Intent(context, cls);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setContentIntent(pendingIntent);
		manager.notify(0, mBuilder.build());
	}

	/**
	 * 构建自定义通知视图，不能用
	 * 
	 * @param context
	 * @param layoutId
	 *            自定义视图ID
	 * @param viewId
	 *            自定义视图控件ID
	 * @param title
	 * @param content
	 * @param ticker
	 * @param sIcon
	 * @param cls
	 */
	public static void setViewNotification(Context context, int layoutId,
			int viewId, String title, String content, String ticker, int sIcon,
			Class<?> cls) {
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context);
		RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);
		views.setImageViewResource(viewId, sIcon);
		views.setTextViewText(viewId, title);
		views.setTextViewText(viewId, content);

		mBuilder.setContent(views);
		// 第一次提示消息的时候显示在通知栏上
		mBuilder.setTicker(ticker);
		// 自己维护通知的消失
		mBuilder.setAutoCancel(true);

		Intent intent = new Intent(context, cls);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setContentIntent(pendingIntent);
		manager.notify(0, mBuilder.build());
	}

}
