package com.lcb.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import com.lcb.R;
import com.lcb.constant.Constant;
import com.lcb.utils.DialogNotileUtil;
import com.lcb.utils.Logs;
import com.lcb.utils.NetConnectUtil;
import com.lcb.utils.SystemUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.concurrent.ScheduledExecutorService;

import okhttp3.Call;

/**
 * 下载的Service
 * 作者 Champion Dragon
 * created at 2017/7/8
 **/
public class DownloadService extends Service {
    Binder mBinder = new MyBinder();
    private Notification.Builder builder, msgBuilder;
    private NotificationManager manager;
    private Notification notification;
    private String mDownloadUrl;//APK的下载路径
    ScheduledExecutorService scheduledExecutorService;
    String tag = "DownloadService";
    int myprogress;
    int defferent;

    @Override
    public IBinder onBind(Intent intent) {
        Logs.d(tag + " 43    " + "onBind");
        return mBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Logs.d(tag + " 51    " + "onCreate()");
        initNotificationBuilder();
        initmsgBuilder();
    }

    /**
     * 初始化消息
     */
    private void initNotificationBuilder() {
        builder = new Notification.Builder(this);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        builder.setSmallIcon(R.drawable.ad3)
                .setTicker("软件正在更新")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ad2))
//                .setContentInfo("右下角的小字体信息")
                .setWhen(System.currentTimeMillis())
                //自定义通知的提示音
                //.setSound(Uri.parse("android.resource://com.lcb/" + R.raw.timeout))
                //设置系统默认声音
                .setDefaults(Notification.DEFAULT_SOUND)
                .setVibrate(new long[]{0, 900});

//        builder.setContentIntent(PendingIntent.getService(this,5,new Intent(),PendingIntent.FLAG_UPDATE_CURRENT));

//        Intent intents = new Intent(this, MainActivity.class);
//        intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //service跳转到activity一定要加这个
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, intents, PendingIntent.FLAG_CANCEL_CURRENT);
//        builder.setContentIntent(pendingIntent);
    }

    private void initmsgBuilder() {
        msgBuilder = new Notification.Builder(this);
        msgBuilder.setContentTitle("文件正在下载")
                .setSmallIcon(R.drawable.ad3)//小图标一定要设置不然Notification状态栏显示不出来
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ad2))
                .setContentText("下载进度");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获取下载APK的链接
//        mDownloadUrl = intent.getStringExtra("apkUrl");
        mDownloadUrl = Constant.apkUpdate;
        downloadFile(mDownloadUrl);//下载APKs

        Logs.d(tag + " 80    " + "onStartCommand()     " + mDownloadUrl);


//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                i++;
//                if (i == 100) {
//                    scheduledExecutorService.shutdownNow();
//                }
//                notifyMsg("版本过低" + i, "您真的需要升级了" + i, i);
//            }
//        };
//        scheduledExecutorService = Executors
//                .newSingleThreadScheduledExecutor();
//        scheduledExecutorService.scheduleWithFixedDelay(runnable, 1, 1,
//                TimeUnit.SECONDS);

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 下载文件
     */
    private void downloadFile(String downloadUrl) {
        OkHttpUtils.get().url(downloadUrl).build().execute(new FileCallBack(Constant.fileDir.getAbsolutePath(),
                SystemUtil.AppName() + "_updata.apk") {
            @Override
            public void onError(Call call, Exception e, int i) {
                if (NetConnectUtil.NetConnect(DownloadService.this)) {
//                    ToastUtil.showLong("服务器异常,文件下载失败");
                    DialogNotileUtil.show(DownloadService.this, "服务器异常,文件下载失败");
                } else {
//                    ToastUtil.showLong("未连接到网络,文件下载失败");
                    DialogNotileUtil.show(DownloadService.this, "未连接到网络,文件下载失败");
                }
                stopSelf();
                Logs.e(tag + " 111 " + e + "  " + i);
            }

            @Override
            public void onResponse(File file, int i) {
                notifyMsg("温馨提醒", "文件下载已完成", 100);
                stopSelf();
                Logs.e(tag + " 118 " + file.getAbsolutePath() + "  " + i);
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                //progress*100为当前文件下载进度，total为文件大小
                super.inProgress(progress, total, id);

                myprogress = (int) (progress * 100);

                if (myprogress == defferent) {
                    defferent = defferent + 20;
//                    Logs.e("" + myprogress);
                    if (myprogress > 0 && myprogress < 100) {
                        notifyMsg(myprogress);
                        Logs.d(tag + "  163 " + myprogress);
                    } else if (myprogress == 0) {
                        // 避免频繁刷新View，这里设置每下载10%提醒更新一次进度
                        notifyMsg("温馨提醒", "文件准备下载", myprogress);
                        Logs.e(tag + "  168 " + myprogress);
                    }
                }
            }
        });
    }

    /**
     * 设置下载开始和结束的消息
     */
    private void notifyMsg(String title, String msg, int progress) {
        builder.setContentTitle(title);
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentText(msg);
        if (progress >= 100) {
//            下载完成
            builder.setContentIntent(getInstallIntent());
        }
        notification = builder.build();
        manager.notify(0, notification);
    }


    /**
     * 下载中的消息
     */
    private void notifyMsg(int progress) {

        if (progress > 0 && progress < 100) {
//            下载进行中
            msgBuilder.setProgress(100, progress, false);
        } else {
            msgBuilder.setProgress(0, 0, false);
        }
        msgBuilder.setContentInfo("下载进度  " + progress + "%");
        notification = msgBuilder.build();
        manager.notify(0, notification);
    }


    /**
     * 设置安装的pendingIntent
     */
    private PendingIntent getInstallIntent() {
        File file = new File(Constant.fileDir.getAbsolutePath(), SystemUtil.AppName() + "_updata.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //系统自带安装程序
        //Uri uri = Uri.fromFile(file);和Uri.parse("file://" + file.getAbsolutePath())相同
        intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "application/vnd.android.package-archive");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        Logs.d(tag + " 178   " + "onDestroy");
//        Intent intents = new Intent(this, MainActivity.class);
//        intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //service跳转到activity一定要加这个
//        startActivity(intents);
    }


    class MyBinder extends Binder {
        public void startDownload() {
//            Logs.d(tag + " 46    " + "startDownload");
//            manager.notify(0, notification);
//            // 卸载APK,"package:"+包名
//            Uri packageURI = Uri.parse("package:" + SystemUtil.PackgeName());
//            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
//            uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(uninstallIntent);
        }
    }


}
