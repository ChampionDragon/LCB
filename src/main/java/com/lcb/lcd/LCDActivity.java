package com.lcb.lcd;

import android.app.Dialog;
import android.app.smdt.SmdtManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.lcb.R;
import com.lcb.base.BaseActivity;
import com.lcb.constant.Constant;
import com.lcb.utils.DialogCustomUtil;
import com.lcb.utils.DialogNotileUtil;
import com.lcb.utils.Logs;
import com.lcb.utils.SmallUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LCDActivity extends BaseActivity implements View.OnClickListener {
    private VideoView videoView;
    private LinearLayout menu, onoffMenu, checkMenu, setMenu;
    private Uri url;
    private String videoUrl = Environment.getExternalStorageDirectory().toString() + File.separator;
    private String netUrl = Constant.viedoUrl;
    private SmdtManager smdt;//smdt是控制板的jdk对象有厂家提供
    private String tag = "LCDActivity";
    //Environment.DIRECTORY_PICTURES  系统的照片根目录
    private String filePath = Environment.
            getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + File.separator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lcd);
        initView();
        initVideo();
        smdt = SmdtManager.create(getApplicationContext());
    }

    /*每次交互以后让它自动运行*/
    @Override
    protected void onResume() {
        super.onResume();
        videoURL(videoUrl + "1.mp4");
    }

    private void initVideo() {
        videoView = (VideoView) findViewById(R.id.lcdvideoView);

        // 播放完成回调
        videoView.setOnCompletionListener(completion);
        videoView.setOnErrorListener(errorListener);
        setVideoViewLayoutParams(1);
        videoView.setMediaController(new MediaController(this));
    }

    private void initView() {
        menu = (LinearLayout) findViewById(R.id.lcdVideo);
        menu.setOnClickListener(this);
        onoffMenu = (LinearLayout) findViewById(R.id.lcdback);
        onoffMenu.setOnClickListener(this);
        checkMenu = (LinearLayout) findViewById(R.id.lcdcheck);
        checkMenu.setOnClickListener(this);
        setMenu = (LinearLayout) findViewById(R.id.lcdSet);
        setMenu.setOnClickListener(this);
    }

    /*系统设置的menu*/
    private void setSetMenu(View menu) {
        PopupMenu popupMenu = new PopupMenu(this, menu);
        popupMenu.getMenuInflater().inflate(R.menu.set, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.set_onoff:
                        SmallUtil.getActivity(LCDActivity.this, OnOffSetActivity.class);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    /*查看信息的menu*/
    private void setCheckMenu(View menu) {
        PopupMenu popupMenu = new PopupMenu(this, menu);
        popupMenu.getMenuInflater().inflate(R.menu.check, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.check_app:
                        SmallUtil.getActivity(LCDActivity.this, AppInfoActivity.class);
                        break;
                    case R.id.check_sdk:
                        SmallUtil.getActivity(LCDActivity.this, SDKInfoActivity.class);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    /*显现设置开关机的menu*/
    private void setOnoffMenu(View menu) {
        PopupMenu popmenu = new PopupMenu(this, menu);
        popmenu.getMenuInflater().inflate(R.menu.onoff, popmenu.getMenu());
        popmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.onoff_reboot:
                        Dialog one = DialogCustomUtil.create("警告", "您确定要重启系统吗？",
                                LCDActivity.this, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        smdt.smdtReboot("reboot");
                                    }
                                });
                        one.show();
                        break;
                    case R.id.onoff_shutdown:
                        Dialog dialog = DialogCustomUtil.create("警告", "您确定要关闭系统吗？",
                                LCDActivity.this, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        smdt.shutDown();
                                    }
                                });
                        dialog.show();
                        break;
                }
                return false;
            }
        });
        popmenu.show();
    }

    /*显示视频播放的menu*/
    private void showMenu(View menu) {
        PopupMenu popmenu = new PopupMenu(this, menu);
        popmenu.getMenuInflater().inflate(R.menu.videos, popmenu.getMenu());
        popmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.videos_net:
                        videoURL(netUrl);
                        break;
                    case R.id.videos_local:
                        videoURL(videoUrl + "1.mp4");
                        break;
                    case R.id.videos_intent:
                        videoView.stopPlayback();
                        break;
                    case R.id.videos_full:
                        setVideoViewLayoutParams(1);
                        break;
                    case R.id.videos_half:
                        setVideoViewLayoutParams(2);
                        break;
                    case R.id.videos_screen:
                        videoScreen();
                        break;
                }
                return false;
            }
        });
        popmenu.show();
    }

    /*截屏  这个功能必须要有签名才有效*/
    private void videoScreen() {
//        smdt = SmdtManager.create(getApplicationContext());
        SimpleDateFormat sdformats = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒SSS毫秒");
        String fileNames = sdformats.format(new Date(System.currentTimeMillis())) + ".png";
        smdt.smdtTakeScreenshot(filePath, fileNames, getApplicationContext());
        DialogNotileUtil.show(this, "文件保留成功请在根目录Picture文件夹下查看");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lcdVideo:
                showMenu(menu);
                break;
            case R.id.lcdback:
                setOnoffMenu(onoffMenu);
                break;
            case R.id.lcdcheck:
                setCheckMenu(checkMenu);
                break;
            case R.id.lcdSet:
                setSetMenu(setMenu);
                break;
        }
    }


    /**
     * 设置videiview的全屏和窗口模式
     *
     * @param i 标识 1为全屏模式 2为窗口模式
     */
    private void setVideoViewLayoutParams(int i) {
        if (i == 1) {
        /*全屏模式*/
            //设置充满整个父布局
            RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            //设置相对于父布局四边对齐
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            //为VideoView添加属性
            videoView.setLayoutParams(LayoutParams);

        } else {
            /*窗口模式*/
            //获取整个屏幕的宽高
            DisplayMetrics DisplayMetrics = new DisplayMetrics();
//            getResources().getDisplayMetrics().heightPixels
            this.getWindowManager().getDefaultDisplay().getMetrics(DisplayMetrics);
            int videoHeight = DisplayMetrics.heightPixels * 1 / 3;//标题的view的高
            int videoWidth = DisplayMetrics.widthPixels;
            RelativeLayout.LayoutParams LayoutParams = new RelativeLayout
                    .LayoutParams(videoWidth, videoHeight);
            //设置居中
            LayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            //为VideoView添加属性
            videoView.setLayoutParams(LayoutParams);
        }
    }


    /*视频播放完成的监听*/
    MediaPlayer.OnCompletionListener completion = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            videoURL(videoUrl + "1.mp4");
        }
    };

    /*播放错误的回调函数*/
    MediaPlayer.OnErrorListener errorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Logs.d("lcdactivity 150   " + what + "   " + extra);
            Dialog dialog = DialogCustomUtil.create("警告", "播放文件不存在,是否从云端下载",
                    LCDActivity.this, null);
            dialog.show();
//            设置成true就不会有播放视频"错误的弹框"
            return true;
        }
    };
        /*通过URL播放视频*/

    private void videoURL(String netUrl) {
        // 设置视频路径
        url = Uri.parse(netUrl);
        videoView.setVideoURI(url);
        // 开始播放视频
        videoView.start();
    }

}
