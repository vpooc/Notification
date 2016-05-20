package com.vpooc.notification;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

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
    private Button btnNotify;
    private Button btnClean;

    private Button btcDownload;
    private Button btnProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnNotify = (Button) findViewById(R.id.btn_notifi);
        btnNotify.setOnClickListener(this);
        btnClean = (Button) findViewById(R.id.btn_clean_notify);
        btnClean.setOnClickListener(this);
        btcDownload = (Button) findViewById(R.id.btn_download);
        btcDownload.setOnClickListener(this);
        btnProgress = (Button) findViewById(R.id.btn_progress);
        btnProgress.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_notifi:
                sendNotify();
                break;
            case R.id.btn_clean_notify:
                clearNotify(NOTIFICATION_ID);
                clearNotify(NOTIFICATION_ID_PROGRESS);
                clearNotify(NOTIFICATION_ID_DOWNLOAD);
                break;
            case R.id.btn_download:
                dowmload();
                break;
            case R.id.btn_progress:
                progressNotify() ;
                break;
        }
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
        builder.setColor(Color.GREEN);
        builder.setContentTitle("标题");
        builder.setContentText("text");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.headimage03));
        builder.setContentInfo("ContentInfo");
        builder.setSubText("subtext");
        builder.setTicker("流动消息");
        //常驻方式一：
        builder.setOngoing(true);
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
        builder.setColor(Color.GREEN);
        builder.setContentTitle("下载");
        builder.setContentText("下载进度" + number + "%");
        builder.setSmallIcon(R.mipmap.headimage03);

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
            }
        }).start();


    }


}
