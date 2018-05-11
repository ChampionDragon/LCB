package com.lcb.activity;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.lcb.R;
import com.lcb.base.BaseActivity;
import com.lcb.constant.Constant;
import com.lcb.utils.DialogCustomUtil;
import com.lcb.utils.Logs;
import com.lcb.utils.ToastUtil;

import java.io.File;

public class VideoActivity extends BaseActivity implements OnClickListener {
    private Uri url;
    private String videoUrl = Environment.getExternalStorageDirectory().toString() + "/";
    private String netUrl = Constant.viedoUrl;
    private String localUrl;
    private VideoView videoView;
    private ImageView menu;
    private RelativeLayout rl;//标题的view
    String tag = "VideoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initView();
        initVideo();
        Logs.e(videoUrl+"   "+ File.separator);
    }

    private void initVideo() {
        videoView = (VideoView) findViewById(R.id.videoView);

        // 播放完成回调
        videoView.setOnCompletionListener(completion);
        videoView.setOnErrorListener(errorListener);
        setVideoViewLayoutParams(1);

        /*因为只有当oncreate执行完才能得到空间的宽高所以这里我延时300ms*/
        videoView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 设置视频控制器
                initMediaController();
            }
        }, 300);
    }

    /*播放错误的回调函数*/
    MediaPlayer.OnErrorListener errorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
//            Logs.e(tag + " 65 " + what + "    " + extra);
            Dialog dialog = DialogCustomUtil.create("警告", "播放文件不存在是否从云端下载", VideoActivity.this, null);
            dialog.show();
//            设置成true就不会有播放视频"错误的弹框"
            return true;
        }
    };

    /*初始化播放器的控制器*/
    private void initMediaController() {
        MediaController mediaController = new MediaController(this);
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int videoViewHeight = videoView.getHeight() * 1 / 3;
        int height = screenHeight - videoViewHeight - rl.getHeight() - getStatusBarHeight();
//        Logs.d(tag + " 69  " + screenHeight + " " + videoViewHeight);
//        Logs.i(rl.getHeight() + "");
//        Logs.i(getStatusBarHeight() + "");
//        Logs.i(height + "");
        //设置控制器的位置
        mediaController.setPadding(0, 0, 0, 1);
        videoView.setMediaController(mediaController);
    }

    /*获得状态栏的高度*/
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void initView() {
        rl = (RelativeLayout) findViewById(R.id.video_rl);
        findViewById(R.id.back_video).setOnClickListener(this);
        menu = (ImageView) findViewById(R.id.menu_videos);
        menu.setOnClickListener(this);
    }

    /*视频播放完成的监听*/
    OnCompletionListener completion = new OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            videoURL(videoUrl + "1.mp4");
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_videos:
                showMenu(menu);
                break;
            case R.id.back_video:
                finish();
                break;
        }
    }

    /*显示menu*/
    private void showMenu(ImageView menu) {
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
                        Intent intent = new Intent();
                        intent.setClass(VideoActivity.this, LocalVideoActivity.class);
                        startActivityForResult(intent, 1);
//                        videoURL(videoUrl + "1.mp4");
                        break;
                    case R.id.videos_intent:
                        videoIntent();
                        break;
                    case R.id.videos_full:
                        setVideoViewLayoutParams(1);
                        break;
                    case R.id.videos_half:
                        setVideoViewLayoutParams(2);
                        break;
                }
                return false;
            }
        });
        popmenu.show();
    }


    /*利用Intent调用系统自带的播放器或者安装的第三方播放器*/
    private void videoIntent() {
        Uri uri = Uri.parse(netUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "video/*");
        startActivity(intent);
    }


    /*通过URL播放视频*/

    private void videoURL(String netUrl) {
        // 设置视频路径
        url = Uri.parse(netUrl);
        videoView.setVideoURI(url);
        // 开始播放视频
        videoView.start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null)
            localUrl = data.getStringExtra(LocalVideoActivity.URL);
        if (localUrl == null)
            ToastUtil.showShort("播放路径不存在");
        else {
            url = Uri.parse(localUrl);
            videoView.setVideoURI(url);
            videoView.start();
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
            //设置窗口模式距离边框50  -rl.getHeight() * 2
            int videoHeight = DisplayMetrics.heightPixels * 2 / 3;//标题的view的高
            int videoWidth = DisplayMetrics.widthPixels;
            RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(videoWidth, videoHeight);
            //设置居中
            LayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            //为VideoView添加属性
            videoView.setLayoutParams(LayoutParams);
        }
    }


    /**
     * 设置videoView占整个屏幕的高的比例
     *
     * @param numberator  比例分子
     * @param denominator 比例分母
     */
    private void setVideoViewLayoutParams(int numberator, int denominator) {
        //获取整个屏幕的宽高
        DisplayMetrics DisplayMetrics = new DisplayMetrics();
//            getResources().getDisplayMetrics().heightPixels
        this.getWindowManager().getDefaultDisplay().getMetrics(DisplayMetrics);
        int videoHeight = DisplayMetrics.heightPixels * numberator / denominator;//标题的view的高
        int videoWidth = DisplayMetrics.widthPixels;
        RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(videoWidth, videoHeight);
        //设置居中
        LayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        //为VideoView添加属性
        videoView.setLayoutParams(LayoutParams);

    }


}
