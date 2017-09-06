package com.lcb.activity;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcb.R;
import com.lcb.base.BaseActivity;

public class AnimActivity extends BaseActivity implements View.OnClickListener {
    private TextView state;
    private Animation mAnimation1;
    private Animation animOpen, animClose;
    private SoundPool mSoundPool;//声音控件
    private ImageView iv;
    private int CLOSED = 1;
    private int CLOSEING = 2;
    private int OPENED = 3;
    private int OPENING = 4;
    private int STOP = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        initView();
    }

    private void initView() {
        findViewById(R.id.anim_close).setOnClickListener(this);
        findViewById(R.id.anim_open).setOnClickListener(this);
        findViewById(R.id.back_anim).setOnClickListener(this);
        iv = (ImageView) findViewById(R.id.anima_iv);
        state = (TextView) findViewById(R.id.anim_state);
        initAnim();
        initSoundPool();
    }

    /**
     * 初始化声音
     */
    private void initSoundPool() {
        if (Build.VERSION.SDK_INT>21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            //传入音频数量
            builder.setMaxStreams(10);
            //AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适的属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            //加载一个AudioAttributes
            builder.setAudioAttributes(attrBuilder.build());
            mSoundPool = builder.build();
        } else {
            mSoundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        }
//        第一个参数为soundPool可以支持的声音数量，这决定了Android为其开设多大的缓冲区，第二个参数为声音类型，
//        在这里标识为系统声音，除此之外还有AudioManager.STREAM_RING以及AudioManager.STREAM_MUSIC等
//        系统会根据不同的声音为其标志不同的优先级和缓冲区，最后参数为声音品质，品质越高，声音效果越好，但耗费更多的系统资源
        mSoundPool.load(this, R.raw.closed, 1);
        //第三个参数为声音的优先级，当多个声音冲突而无法同时播放时，系统会优先播放优先级高的。
        mSoundPool.load(this, R.raw.closing, 1);
        mSoundPool.load(this, R.raw.opened, 1);
        mSoundPool.load(this, R.raw.opening, 1);
        mSoundPool.load(this, R.raw.stop, 1);
//        第一个参数为id，id即为放入到soundPool中的顺序，比如现在collide.wav是第一个，因此它的id就是1。
//        第二个和第三个参数为左右声道的音量控制。第四个参数为优先级，由于只有这一个声音，因此优先级在这里并不重要。
//        第五个参数为是否循环播放，n为n+1次即0为1次，-1为循环。最后一个参数为播放比率，从0.5到2，一般为1，表示正常播放。
        mSoundPool.play(CLOSEING, 1, 1, 0, 0, 1);
    }

    private void initAnim() {
        animOpen = AnimationUtils.loadAnimation(this, R.anim.circular_ring);
        animClose = AnimationUtils.loadAnimation(this, R.anim.circular_ring);
//        mAnimation1 = AnimationUtils.loadAnimation(this, R.anim.translate_demo);
        animOpen.setAnimationListener(mAnimationListener);
        animClose.setAnimationListener(mAnimationListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.anim_close:
                iv.startAnimation(animClose);
                break;
            case R.id.anim_open:
                iv.startAnimation(animOpen);
                break;
            case R.id.back_anim:
                finish();
                break;
        }
    }


    private Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            if (animation == animClose) {
                state.setText("正在关门");
                mSoundPool.play(CLOSEING, 1, 1, 0, 0, 1);
            } else {
                state.setText("正在开门");
                mSoundPool.play(OPENING, 1, 1, 0, 0, 1);
            }

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (animation == animClose) {
                state.setText("已关门");
                mSoundPool.play(CLOSED, 1, 1, 0, 0, 1);
            } else {
                state.setText("已开门");
                mSoundPool.play(OPENED, 1, 1, 0, 0, 1);
            }

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };


}
