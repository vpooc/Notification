package com.vpooc.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

/**
 * 提取局部变量：Ctrl+Alt+V
 * <p/>
 * 提取全局变量：Ctrl+Alt+F
 * <p/>
 * 提取方法：Shit+Alt+M
 * 参数提示Ctrl+P
 * 向下复制Ctrl+d
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static int NOTIFICATION_ID = 1111;
    private static int NOTIFICATION_ID_DOWNLOAD = 2222;
    private static int NOTIFICATION_ID_PROGRESS = 3333;
    private static int NOTIFICATION_ID_SELF = 4444;
    private Button btnNotify;
    private Button btnClean;
    private Button btnDownload;
    private Button btnProgress;
    private Button btnSelf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnNotify = (Button) findViewById(R.id.btn_notify);
        btnClean = (Button) findViewById(R.id.btn_clean_notify);
        btnProgress = (Button) findViewById(R.id.btn_progress);
        btnDownload = (Button) findViewById(R.id.btn_download);
        btnSelf = (Button) findViewById(R.id.btn_self_notify);
        btnNotify.setOnClickListener(this);
        btnClean.setOnClickListener(this);
        btnProgress.setOnClickListener(this);
        btnDownload.setOnClickListener(this);
        btnSelf.setOnClickListener(this);

    }


    private void clearNotify(int id) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(id);
    }

    /**
     * 发送通知
     */
    private void sendNotify() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //构建Notification对象
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        //为了清晰不设置太多
        builder.setColor(Color.GREEN).setContentTitle("标题");
        //小图标必须，否则出错
        builder.setSmallIcon(R.mipmap.headimage03);

        //常驻方式一：
//        builder.setOngoing(true);
        Notification d = builder.build();

//        //常驻方式二：
//        d.flags=Notification.FLAG_NO_CLEAR;
        manager.notify(NOTIFICATION_ID, d);
    }


    int number = 0;

    private void dowmload() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //构建Notification对象
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setColor(Color.GREEN).
                setContentTitle("下载").
                setContentText("下载进度" + number + "%").
                setSmallIcon(R.mipmap.headimage03);

//        FLAG_CANCEL_CURRENT:如果当前系统中已经存在一个相同的PendingIntent对象，
//                              那么就将先将已有的PendingIntent取消，然后重新生成一个PendingIntent对象。
//        FLAG_NO_CREATE:如果当前系统中不存在相同的PendingIntent对象，
//                       系统将不会创建该PendingIntent对象而是直接返回null。
//        FLAG_ONE_SHOT:该PendingIntent只作用一次。在该PendingIntent对象通过send()方法触发过后，
//                      PendingIntent将自动调用cancel()进行销毁，那么如果你再调用send()方法的话，
//                      系统将会返回一个SendIntentException。
//        FLAG_UPDATE_CURRENT:如果系统中有一个和你描述的PendingIntent对等的PendingInent，
//                           那么系统将使用该PendingIntent对象，但是会使用新的Intent来更新之前PendingIntent中的Intent对象数据，
//                               例如更新Intent中的Extras。
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        builder.setContentIntent(pendingIntent);
        Notification d = builder.build();


        manager.cancel(NOTIFICATION_ID_DOWNLOAD);
        manager.notify(NOTIFICATION_ID_DOWNLOAD, d);
        number += 10;


    }


    private void progressNotify() {

        final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //构建Notification对象
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.headimage03);
        builder.setColor(Color.GREEN);
        builder.setContentTitle("下载进度");
        builder.setTicker("音乐开始上载");
        builder.setProgress(100, 0, true);
        Notification d = builder.build();

        manager.notify(NOTIFICATION_ID_PROGRESS, d);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    builder.setProgress(100, i * 10, false);
                    Notification c = builder.build();
                    manager.notify(NOTIFICATION_ID_PROGRESS, c);

                }
                builder.setProgress(0, 0, false);
                builder.setContentText("下载完成");
                Notification n = builder.build();
                manager.notify(NOTIFICATION_ID_PROGRESS, n);

            }
        }).start();


    }


    @Override
    public void onClick(View v) {
        Log.d("dd  ", "dddddddddd");
        switch (v.getId()) {
            case R.id.btn_notify:
                sendNotify();
                break;
            case R.id.btn_clean_notify:
                clearNotify(NOTIFICATION_ID);
                clearNotify(NOTIFICATION_ID_PROGRESS);
                clearNotify(NOTIFICATION_ID_DOWNLOAD);
                clearNotify(NOTIFICATION_ID_SELF);
                break;
            case R.id.btn_download:
                dowmload();
                break;
            case R.id.btn_progress:
                progressNotify();
                break;
            case R.id.btn_self_notify:
                Log.d("wwww ", "ffff1");
                Log.d("wwww ", "ffff1");
                selfNotify();
                break;
        }
    }

    /**
     * 自定义notify视图
     */
    private void selfNotify() {
        NotificationManager m = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        RemoteViews views = new RemoteViews(getPackageName(), R.layout.notify_layout);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent p = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.button, p);
        builder.setContent(views);

        builder.setSmallIcon(R.mipmap.headimage03);
        Notification b = builder.build();
        m.notify(NOTIFICATION_ID_SELF, b);

    }
}
