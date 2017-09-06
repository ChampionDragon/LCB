package com.lcb.activity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;

import com.lcb.MainActivity;
import com.lcb.R;
import com.lcb.base.BaseActivity;
import com.lcb.utils.Logs;

public class NotificationActivity extends BaseActivity implements View.OnClickListener {
    private Intent intentService;
    private DownloadService.MyBinder binder;
    String tag="Notification";


    private ServiceConnection connnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logs.d(tag+ " 29    " + "onServiceConnected");
            binder = (DownloadService.MyBinder) service;
            binder.startDownload();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//onServiceDisconnected()正常情况下是不被调用的，它的调用时机是当Service服务被异外销毁时，例如内存的资源不足时这个方法才被自动调用。
            Logs.d(tag+ " 36    " + "onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initView();
        intentService = new Intent(this, DownloadService.class);
        intentService.putExtra("apkUrl","APK下载地址");
    }

    /**
     * 发送一个消息
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initNotification() {
        //实例化通知栏构造器Notification.Builder
        Notification.Builder builder1 = new Notification.Builder(this);
        builder1.setSmallIcon(R.drawable.jx); //设置图标
        builder1.setTicker("显示第二个通知"); //设置通知首次出现在通知栏显示的内容，例如：您有一条短信请查收。
        builder1.setContentTitle("通知"); //设置标题
        builder1.setContentText("点击查看详细内容"); //设置接收消息后，将状态栏下拉后的消息内容
        builder1.setWhen(System.currentTimeMillis()); //发送时间

        /*设置振动效果：延迟0ms，然后振动900ms，在延迟1500ms，接着在振动1700ms。*/
        builder1.setVibrate(new long[]{0, 900, 1500, 1700});
        /*调用系统多媒体库内的铃声*/
//        builder1.setSound(Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "2"));
         /*调用自己提供的铃声，位于 /res/values/raw 目录下  com.lcb是程序的包名*/
        builder1.setSound(Uri.parse("android.resource://com.lcb/" + R.raw.wifi));
        //设置系统默认声音
//        builder1.setDefaults(Notification.DEFAULT_SOUND);
        /*设置呼吸灯 参数表示灯光颜色、 ledOnMS 亮持续时间、ledOffMS 暗的时间*/
        builder1.setLights(0xff0000, 3000, 3000);

        //设置默认的提示音，振动方式，灯光。设置了这个，上面三个功能就无效
//        builder1.setDefaults(Notification.DEFAULT_ALL);

//        builder1.setAutoCancel(true);//打开程序后图标消失
        Intent intent = new Intent(this, MainActivity.class);//设置意图，这里是跳转到一个界面。

        // 封装自定义的布局
        RemoteViews mRemoteViews = new RemoteViews(this.getPackageName(),
                R.layout.notification_view);
        // 如果是动态设置值的时候，需要这些操作
        mRemoteViews.setImageViewResource(R.id.img_user, R.drawable.ad2);
        mRemoteViews.setTextViewText(R.id.tv, "XXX");
        mRemoteViews.setProgressBar(R.id.progressBar, 100, 50, false);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        // 点击ImageView时跳转
        mRemoteViews.setOnClickPendingIntent(R.id.img_user, pendingIntent);
        builder1.setContent(mRemoteViews);
        builder1.setContentIntent(pendingIntent);//设置你点击消息后的意图
        Notification notification1 = builder1.build();//得到一个 notification
        //只有在设置了标志符Flags为Notification.FLAG_SHOW_LIGHTS的时候，才支持呼吸灯提醒。
//        notification1.flags = Notification.FLAG_SHOW_LIGHTS;
        //让声音、振动无限循环，直到用户响应 （取消或者打开）
        notification1.flags = Notification.FLAG_INSISTENT;
        //用户单击通知后自动消失
//        notification1.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager Manager = (NotificationManager) this.
                getSystemService(Context.NOTIFICATION_SERVICE);
        //获取通知栏管理者
        Manager.notify(1, notification1); // 通过通知管理器发送通知
    }

    private void initView() {
        findViewById(R.id.back_notification).setOnClickListener(this);
        findViewById(R.id.notification).setOnClickListener(this);
        findViewById(R.id.notification_service).setOnClickListener(this);
        findViewById(R.id.notification_service_stop).setOnClickListener(this);
        findViewById(R.id.notification_service_bind).setOnClickListener(this);
        findViewById(R.id.notification_service_unbind).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_notification:
                finish();
                break;
            case R.id.notification_service:
                startService(intentService);
                break;
            case R.id.notification_service_stop:
                stopService(intentService);
                break;
            case R.id.notification_service_bind:
                bindService(intentService, connnection, BIND_AUTO_CREATE);
                break;
            case R.id.notification_service_unbind:
                unbindService(connnection);
                break;
            case R.id.notification:
                initNotification();
                break;
        }

    }


}
