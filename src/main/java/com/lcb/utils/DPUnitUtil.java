package com.lcb.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * 长度单位转化工具类
 * 作者 Champion Dragon
 * created at 2017/7/24
 **/

public class DPUnitUtil {
    public static float dp2px(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
//        float px = dp * (metrics.densityDpi / 160f)+0.5f; //或者
        float px = dp * (metrics.density)+0.5f;
        return px;
    }


    public static float px2dp(Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
//        float dp = px / (metrics.densityDpi / 160f)+0.5f;
        float dp = px / (metrics.density)+0.5f;
        return dp;
    }


    public static float px2sp(Context context, Float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity+0.5f;
    }


    public static float sp2px(Context context, Float sp) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scaledDensity+0.5f;
    }

    public static float sp2dp(Context context, Float sp) {
        float px = sp2px(context, sp);
        return px2dp(context, px)+0.5f;
    }

    public static float dp2sp(Context context, Float dp) {
        float px = dp2px(context, dp);
        return px2sp(context, px)+0.5f;

    }


}
