package com.lcb.Ezviz;

import android.text.TextPaint;
import android.text.style.StyleSpan;

/**
 * Created by Administrator on 2017/6/23.
 */

public class BoldSpan extends StyleSpan {

    public BoldSpan(int style) {
        super(style);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setFakeBoldText(true);
        super.updateDrawState(ds);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        paint.setFakeBoldText(true);
        super.updateMeasureState(paint);
    }

}
