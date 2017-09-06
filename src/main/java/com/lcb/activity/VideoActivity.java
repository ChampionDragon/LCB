package com.lcb.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.MediaController;
import android.widget.VideoView;

import com.lcb.R;
import com.lcb.base.BaseActivity;
import com.lcb.constant.Constant;
import com.lcb.utils.ToastUtil;

public class VideoActivity extends BaseActivity implements OnClickListener {
	private Uri url;
	private String localUrl;
	private String netUrl = Constant.viedoUrl;
	private VideoView videoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		initView();
		initVideo();
	}

	private void initVideo() {

		// 设置视频控制器
		videoView.setMediaController(new MediaController(this));

		// 播放完成回调
		// videoView.setOnCompletionListener(completion);
	}

	private void initView() {
		videoView = (VideoView) findViewById(R.id.videoView);
		findViewById(R.id.video_local).setOnClickListener(this);
		findViewById(R.id.video_net).setOnClickListener(this);
		findViewById(R.id.back_video).setOnClickListener(this);
	}

	OnCompletionListener completion = new OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			ToastUtil.showShort("视频播放完成了");
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.video_local:
			Intent intent = new Intent();
			intent.setClass(this, LocalVideoActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.video_net:
			// 设置视频路径
			url = Uri.parse(netUrl);
			videoView.setVideoURI(url);
			// 开始播放视频
			videoView.start();
			break;
		case R.id.back_video:
			finish();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null)
		localUrl = data.getStringExtra(LocalVideoActivity.URL);
		if (localUrl == null)
			ToastUtil.showShort("播放路径不存在");
		else {
			url = Uri.parse(localUrl);
			videoView.setVideoURI(url);
			videoView.start();
		}

	}

}
