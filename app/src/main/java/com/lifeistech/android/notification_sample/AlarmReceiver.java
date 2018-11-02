package com.lifeistech.android.notification_sample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

/***
 *
 * 1. Intent を受け取る
 * 2. Intent を受け取ったら, (ローカルの)通知を送る
 *
 *
 * 参考になったサイト
 * https://tech.pjin.jp/blog/2017/05/31/android_schedule_notification/
 * ・MainActivity の方の処理はこのサイトのまま
 * 　・onRecieve が実行されるところまではこのサイトのままでできる
 * 　・ただ, Notification周りの仕様変更により, AlarmReceiver周りは変更が必要
 *
 *
 * https://developer.android.com/training/notify-user/build-notification#java
 * ・日本語サイトはだめ. 古い. 言語設定を English にしなければいけない.
 * ・ここのコードはだいたい 2018/11/2 時点の英語記事のコードのまま.
 *
 * https://qiita.com/kawmra/items/9d80f15ea906f703d0d3
 * ・System UI has stopped のエラー対応
 * 　・Adaptive Icon を使用すると Android 8.0 のバグでシステムUIがクラッシュする
 */


public class AlarmReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_ID = "notificationId";
    public static final String NOTIFICATION_CONTENT = "content";
    private static final String CHANNEL_ID = "sample_notification_channel";
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        String content = intent.getStringExtra(NOTIFICATION_CONTENT);
        Log.d("debug", "got notification " + id + " " + content);

        createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(id, buildNotification(context, content));
    }

    private Notification buildNotification(Context context, String content) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_launcher)
// Adaptive icon を使うと 8.0 で System UI がクラッシュする
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentTitle("Notification")
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        return mBuilder.build();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
