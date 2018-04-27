package com.lcb.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lcb.R;
import com.lcb.activity.ChangeLanguageActivity;
import com.lcb.activity.chartViewActivity;
import com.lcb.base.AsyncTaskExecutor;
import com.lcb.constant.Constant;
import com.lcb.pickerview.PickviewActivity;
import com.lcb.utils.DialogAddId;
import com.lcb.utils.DialogLogin;
import com.lcb.utils.Logs;
import com.lcb.utils.SmallUtil;
import com.lcb.utils.TimeUtil;
import com.lcb.utils.ToastUtil;
import com.lcb.view.RoundProgressBar;

public class TwoFragment extends Fragment implements OnClickListener {
    private View view;
    private RoundProgressBar progressBar;
    private Button btn;
    private boolean open;
    private AsyncTaskExecutor executor;
    private int progress;
    private SeekBar mSeekBar;
    private TextView mTextView, menu;

    String tag = "twofragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executor = AsyncTaskExecutor.getinstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_two, container, false);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        btn.setText(getString(R.string.app_name));
    }

    private void initView() {
        progressBar = (RoundProgressBar) view.findViewById(R.id.prograssbar);
        btn = (Button) view.findViewById(R.id.two_btn);
        btn.setOnClickListener(this);
        mSeekBar = (SeekBar) view.findViewById(R.id.two_seekbar);
        mSeekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        mTextView = (TextView) view.findViewById(R.id.tv_seekbar);
        menu = (TextView) view.findViewById(R.id.two_menu);
        menu.setOnClickListener(this);
    }


    /**
     * seekbar的监听
     */
    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            seekBar 当前被修改进度的SeekBar
//            progress 当前的进度值。此值的取值范围为0到max之间。Max为用户通过setMax(int)设置的值，默认为100
//            fromUser 如果是用户触发的改变则返回True
            int max = seekBar.getMax();
            mTextView.setText(progress + "/" + max);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //通知用户已经开始一个触摸拖动手势。客户端可能需要使用这个来禁用seekbar的滑动功能。
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //通知用户触摸手势已经结束。户端可能需要使用这个来启用seekbar的滑动功能。
        }
    };


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.two_btn:
                if (open) {
                    executor.submit(closeRunnable);
                } else {
                    executor.submit(openRunnable);
                }
                ToastUtil.showLong(TimeUtil.long2time(System.currentTimeMillis(),
                        Constant.cformatsecond));
                break;
            case R.id.two_menu:
                showPopupMenu(menu);
                break;
        }
    }

    /**
     * 展示按钮
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void showPopupMenu(View menu) {
//        View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(getActivity(), menu);
//        menu布局
        popupMenu.getMenuInflater().inflate(R.menu.activitys, popupMenu.getMenu());
/*方法一：使用反射，强制显示菜单图标*/
//        try {
//            Field field = popupMenu.getClass().getDeclaredField("mPopup");
//            field.setAccessible(true);
//            MenuPopupHelper mHelper = (MenuPopupHelper) field.get(popupMenu);
//            mHelper.setForceShowIcon(true);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//            Logs.e(tag + "  137");
//        }
//        menu的item点击事件
        popupMenu.setOnMenuItemClickListener(menuItem);
//        PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(getActivity(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });
        popupMenu.show();
    }


    /**
     * popmenu的监听
     */
    PopupMenu.OnMenuItemClickListener menuItem = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_one:
                    SmallUtil.getActivity(getActivity(), chartViewActivity.class);
                    break;
                case R.id.menu_two:
                    SmallUtil.getActivity(getActivity(), PickviewActivity.class);
                    break;
                case R.id.menu_three:
                    SmallUtil.getActivity(getActivity(), ChangeLanguageActivity.class);
                    break;
                case R.id.menu_four:

                    DialogLogin dialogLogin = new DialogLogin(getActivity(), new DialogLogin.putDataListener() {
                        @Override
                        public void putData(String user, String psw) {
                            Logs.d("账号:" + user + " " + "密码:" + psw);
                        }
                    });

                    break;

                case R.id.menu_five:
                    DialogAddId idDialog=new DialogAddId(getActivity(), new DialogAddId.putIdListener() {
                        @Override
                        public void putId(String id) {
                            Logs.v("输入的设备ID为："+id);
                        }
                    });
                    break;
            }
            return false;
        }
    };


    Runnable openRunnable = new Runnable() {

        @Override
        public void run() {
            while (progress <= 100) {
                progress += 1;
                progressBar.setProgress(progress);
                SystemClock.sleep(20);
            }
            open = true;
        }
    };
    Runnable closeRunnable = new Runnable() {

        @Override
        public void run() {
            while (progress > 0) {
                progress -= 1;
                progressBar.setProgress(progress);
                SystemClock.sleep(20);
            }
            open = false;
        }
    };

}
