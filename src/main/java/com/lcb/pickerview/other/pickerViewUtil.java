package com.lcb.pickerview.other;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lcb.R;
import com.lcb.pickerview.OptionsPickerView;
import com.lcb.pickerview.TimePickerView;
import com.lcb.pickerview.adapter.ArrayWheelAdapter;
import com.lcb.pickerview.lib.WheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.lcb.utils.SmallUtil.backgroundAlpaha;

public class pickerViewUtil<T> {

    /**
     * 时间选择回调
     */
    public interface TimerPickerCallBack {
        void onTimeSelect(String date);
    }

    /**
     * 弹出时间选择
     *
     * @param context
     * @param type     TimerPickerView 中定义的 选择时间类型
     * @param format   时间格式化
     * @param callBack 时间选择回调
     */
    public static void alertTimerPicker(Context context, TimePickerView.Type type, final String format, final TimerPickerCallBack callBack) {
        TimePickerView pvTime = new TimePickerView(context, type);
        //控制时间范围
        //        Calendar calendar = Calendar.getInstance();
        //        pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        /*点击其他部位弹框消失*/
        pvTime.setCancelable(true);
        /*设置顶部标题*/
        pvTime.setTitle("请选择时间");
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
//                        tvTime.setText(getTime(date));
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                callBack.onTimeSelect(sdf.format(date));
            }
        });

        /*时间选择器字体大小*/
        pvTime.setTextSize(16);
        //弹出时间选择器
        pvTime.show();
    }


   /* ----------------------------------------------------------------------------------------*/


    /**
     * 底部滚轮点击事件回调
     */
    public interface OnWheelViewClick {
        void onClick(View view, int postion);
    }

    /**
     * 弹出底部滚轮选择
     *
     * @param title   标题
     * @param context
     * @param list    传进来的数据
     * @param click
     */
    public static void alertBottomWheelOption(final Context context, String title, ArrayList<?> list, final OnWheelViewClick click) {

        final PopupWindow popupWindow = new PopupWindow();

        View view = LayoutInflater.from(context).inflate(R.layout.layout_bottom_wheel_option, null);
        TextView tv_confirm = (TextView) view.findViewById(R.id.btnSubmit);
        TextView tv_title = (TextView) view.findViewById(R.id.tvTitle);
        final WheelView wv_option = (WheelView) view.findViewById(R.id.wv_option);
        wv_option.setAdapter(new ArrayWheelAdapter(list));

        wv_option.setCyclic(false);

        /*设置字体大小*/
        wv_option.setTextSize(21);

        /*确认*/
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                backgroundAlpaha((Activity) context, 1);
                click.onClick(view, wv_option.getCurrentItem());
            }
        });

        /*设置标题*/
        tv_title.setText(title);

        /*取消*/
        view.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                backgroundAlpaha((Activity) context, 1);
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int top = view.findViewById(R.id.ll_container).getTop();
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    int y = (int) motionEvent.getY();
                    if (y < top) {
                        popupWindow.dismiss();
                        backgroundAlpaha((Activity) context, 1);
                    }
                }
                return true;
            }
        });
        popupWindow.setAnimationStyle(R.style.PopupAnimation); // 设置弹出动画
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        ColorDrawable colorDrawable = new ColorDrawable(context.getResources()
                .getColor(R.color.transparent));
        popupWindow.setBackgroundDrawable(colorDrawable);// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        backgroundAlpaha((Activity) context, 0.5f);
        popupWindow.showAtLocation(((ViewGroup) ((Activity) context).findViewById(android.R.id.content)).getChildAt(0), Gravity.CENTER, 0, 0);
    }

    /* ----------------------------------------------------------------------------------------*/


    /**
     * 三级别选择器
     */
    public void optionPicker(Context context, String title, OptionsPickerView.OnOptionsSelectListener listener,
                             ArrayList<T> options1Items, ArrayList<ArrayList<T>> options2Items,
                             ArrayList<ArrayList<ArrayList<T>>> options3Items) {
        //选项选择器
        OptionsPickerView pvOptions = new OptionsPickerView(context);
           /*点击其他部位弹框消失*/
        pvOptions.setCancelable(true);

        //三级联动效果 当为true的时候：次目录会随着上级目录的变动而变动
        pvOptions.setPicker(options1Items, options2Items, options3Items, true);

        /*设置选择的三级单位,就是在滚轮旁再添加的字体*/
//        pvOptions.setLabels("省", "市", "区");

            /*选择标题*/
        pvOptions.setTitle(title);
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(1, 1, 1);

        /*设置文字的大小  有个小bug：只有字体大于22第一个item的线才不会错位*/
        pvOptions.setTextSize(22);

        pvOptions.setOnoptionsSelectListener(listener);

        pvOptions.show();


    }


}