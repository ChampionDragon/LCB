package com.lcb.fragment;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.lcb.R;
import com.lcb.view.RoundImageView;

public class FourFragment extends Fragment implements OnClickListener {
    private View view;
    private RoundImageView head;
    private PopupWindow popupWindow;
    private TextView photo, look;
    private Button cancle;
    private FragmentActivity activity;
    String tag = "lcb";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_four, container, false);
        activity = getActivity();
        initView(view);
        return view;
    }

    private void initView(View view) {
        head = (RoundImageView) view.findViewById(R.id.head);
        head.setOnClickListener(this);
        initPopWindow();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head:
                popWindow();
                break;
            case R.id.btn_cancle:
                popupWindow.dismiss();
                break;

        }
    }

    /**
     * popwindow显示
     */
    private void popWindow() {
        View rootView = view.findViewById(R.id.fragment_four);// 设置当前根目录
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int y = dm.heightPixels * 1 / 12;//获得屏幕高度
        Log.e(tag, dm.heightPixels + "");
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, y);
        // popupWindow.update();//更新后显示，比如做了长宽缩小放大的处理
        backgroundAlpaha(activity, 0.6f);
    }

    /**
     * 初始化popwindow
     */
    private void initPopWindow() {
        View popView = View.inflate(getActivity(), R.layout.popowindow_photo,
                null);
        popupWindow = new PopupWindow(popView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.PopupAnimation); // 设置弹出动画
        ColorDrawable colorDrawable = new ColorDrawable(getResources()
                .getColor(R.color.transparent));
        popupWindow.setBackgroundDrawable(colorDrawable);// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // popupWindow.setBackgroundDrawable(new BitmapDrawable(
        // getApplicationContext().getResources(), Bitmap.createBitmap(1,
        // 1, Bitmap.Config.ARGB_8888)));

        popupWindow.setFocusable(true);// 设置PopupWindow可获得焦点
        popupWindow.setOutsideTouchable(true);// PopupWindow以外的区域是否可点击,点击后是否会消失。
        cancle = (Button) popView.findViewById(R.id.btn_cancle);
        cancle.setOnClickListener(this);
        photo = (TextView) popView.findViewById(R.id.photo_ing);
        photo.setOnClickListener(this);
        look = (TextView) popView.findViewById(R.id.photo_look);
        look.setOnClickListener(this);
        // popupWindow消失时监听
        popupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpaha(getActivity(), 1.0f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpaha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

}
