package com.lifeistech.android.notification_sample;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/***
 *
 * 1. Intent を受け取る
 * 2. Intent を受け取ったら, (ローカルの)通知を送る
 *
  */


public class AlarmReceiver extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notificationId";
    public static String NOTIFICATION_CONTENT = "content";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);

        String content = intent.getStringExtra(NOTIFICATION_CONTENT);

        notificationManager.notify(id, buildNotification(context, content));
    }

    private Notification buildNotification(Context context, String content) {
        Notification.Builder builder = new Notification.Builder(context);

        // 通知の設定
        builder.setContentTitle("Notification!!") // タイトル
                .setContentText(content) // テキスト
                .setSmallIcon(android.R.drawable.sym_def_app_icon); // アイコン

        return builder.build();
    }
}
