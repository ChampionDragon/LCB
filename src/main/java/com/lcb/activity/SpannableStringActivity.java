package com.lcb.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.lcb.R;
import com.lcb.utils.ToastUtil;

public class SpannableStringActivity extends Activity implements View.OnClickListener {
    private Spinner choice;
    private TextView tv, auto;
    private String[] mItems;
    private String select, autoSting;
    private int position = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SpannableString spannableString = new SpannableString(autoSting);

            RelativeSizeSpan sizeSpan = new RelativeSizeSpan(1.5f);
            spannableString.setSpan(sizeSpan, position, position + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.red));
            spannableString.setSpan(colorSpan, position, position + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            auto.setText(spannableString);

            position++;
            if (position >= auto.getText().toString().length()) {
                position = 0;
            }
            handler.sendEmptyMessageDelayed(111, 333);
//            handler.obtainMessage().sendToTarget();
//            handler.sendMessageDelayed(handler.obtainMessage(),333);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spannable_string);
        initView();
        mItems = getResources().getStringArray(
                R.array.spannableStyle);
        initSpinner();
        handler.obtainMessage(111).sendToTarget();
    }

    private void initSpinner() {
        ArrayAdapter<String> adapter = getadapter();
        // = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
        // mItems);//系统默认的布局
        // 绑定 Adapter到控件
        choice.setAdapter(adapter);
        choice.setOnItemSelectedListener(listener);
    }

    private ArrayAdapter<String> getadapter() {
        final String[] mItems = this.mItems;
// 建立Adapter并且绑定数据源,默认系统布局
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_checked_text, mItems) {

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = LayoutInflater.from(SpannableStringActivity.this).inflate(
                        R.layout.spinner_item_layout, null);
                TextView label = (TextView) view
                        .findViewById(R.id.spinner_item_label);
                ImageView check = (ImageView) view
                        .findViewById(R.id.spinner_item_checked_image);
                label.setText(mItems[position]);
                if (choice.getSelectedItemPosition() == position) {
                    view.setBackgroundColor(getResources().getColor(
                            R.color.blue_deep));
                    check.setImageResource(R.drawable.dot_press);
                } else {
                    view.setBackgroundColor(getResources().getColor(
                            R.color.blueSky));
                    check.setImageResource(R.drawable.dot);
                }

                return view;
            }

        };

        return adapter;
    }


    private void initView() {
        tv = (TextView) findViewById(R.id.spannable_tv);
        findViewById(R.id.spannable_btn).setOnClickListener(this);
        findViewById(R.id.back_spannable).setOnClickListener(this);
        choice = (Spinner) findViewById(R.id.spannable_spinner);
        auto = (TextView) findViewById(R.id.spannable_auto);
        autoSting = auto.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_spannable:
                finish();
                break;
            case R.id.spannable_btn:
                choice();
                break;
        }
    }

    private void choice() {
        switch (select) {
            case "前景颜色":
                ForegroundColorSpan();
                break;
            case "后景颜色":
                BackgroundColorSpan();
                break;
            case "文字相对大小":
                RelativeSizeSpan();
                break;
            case "设置中划线":
                StrikethroughSpan();
                break;
            case "设置下划线":
                UnderlineSpan();
                break;
            case "设置上标":
                SuperscriptSpan();
                break;
            case "设置下标":
                SubscriptSpan();
                break;
            case "为文字设置风格":
                StyleSpan();
                break;
            case "设置文本图片":
                ImageSpan();
                break;
            case "设置可点击的文本":
                ClickableSpan();
                break;
            case "设置超链接文本":
                URLSpan();
                break;
            case "SpannableStringBuilder":
                SpannableStringBuilder();
                break;
        }

    }

    /**
     * SpannableString的一个拼接效果，同样是append()方法，可以实现各种风格效果的SpannableString拼接.
     */
    private void SpannableStringBuilder() {
        SpannableString one = new SpannableString("这是");
        RelativeSizeSpan sizeSpan02 = new RelativeSizeSpan(1.4f);
        one.setSpan(sizeSpan02, 0, one.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.red));
        one.setSpan(colorSpan, 0, one.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        SpannableString two = new SpannableString("我　　的");
        ToastUtil.showLong("长度   " + two.length());
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ad2);
        drawable.setBounds(22, 22, 299, 299);
        ImageSpan imageSpan = new ImageSpan(drawable);
        two.setSpan(imageSpan, 1, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        SpannableString three = new SpannableString("首页");
        URLSpan urlSpan = new URLSpan("https://github.com/ChampionDragon");
        three.setSpan(urlSpan, 0, three.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setHighlightColor(getResources().getColor(R.color.red));
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(one).append(two).append(three);
        tv.setText(builder);
    }

    /**
     * 设置超链接文本，URLSpan就是继承自ClickableSpan，也和想象中一样，就是重写了父类的onClick事件，用系统自带浏览器打开链接
     */
    private void URLSpan() {
        SpannableString spannableString = new SpannableString("为文字设置超链接");
        URLSpan urlSpan = new URLSpan("http://www.jianshu.com/users/dbae9ac95c78");
        spannableString.setSpan(urlSpan, 5, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setHighlightColor(getResources().getColor(R.color.red));
        tv.setText(spannableString);
    }

    /**
     * 设置可点击的文本，设置这个属性的文本可以相应用户点击事件，至于点击事件用户可以自定义，就像效果图显示一样，用户可以实现点击跳转页面的效果
     */
    private void ClickableSpan() {
        SpannableString spannableString = new SpannableString("为文字设置点击事件");
        MyClickableSpan clickableSpan = new MyClickableSpan("http://www.jianshu.com/users/dbae9ac95c78");
        spannableString.setSpan(clickableSpan, 5, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//使用ClickableSpan的文本如果想真正实现点击作用，必须为TextView设置setMovementMethod方法，否则没有点击相应
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        // setColor()设定的是span超链接的文本颜色，而不是点击后的颜色，点击后的背景颜色(HighLightColor)属于TextView的属性，
        // Android4.0以上默认是淡绿色，低版本的是黄色。setHighlightColor方法则是控制点击时的背景色。
        tv.setHighlightColor(getResources().getColor(R.color.blue));
        tv.setText(spannableString);
    }

    /**
     * 继承至ClickableSpan，并重写其中一些方法。ds.setUnderlineText()控制是否让可点击文本显示下划线
     * 重写ClickableSpan的onClick方法实现Activity的跳转效果，并传递跳转数据
     */
    class MyClickableSpan extends ClickableSpan {

        private String content;

        public MyClickableSpan(String content) {
            this.content = content;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(true);
            ds.setColor(getResources().getColor(R.color.green));
        }

        @Override
        public void onClick(View widget) {
//            Intent intent = new Intent(SpannableStringActivity.this, OtherActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("content", content);
//            intent.putExtra("bundle", bundle);
//            startActivity(intent);
            ToastUtil.showLong("点击成功");
        }
    }




    /*    SpannableString其实和String一样，都是一种字符串类型，同样TextView也可以直接设置SpannableString作为显示文本，
不同的是SpannableString可以通过使用其方法setSpan方法实现字符串各种形式风格的显示,重要的是可以指定设置的区间，
也就是为字符串指定下标区间内的子字符串设置格式。
setSpan(Object what, int start, int end, int flags)方法需要用户输入四个参数，what表示设置的格式是什么，
可以是前景色、背景色也可以是可点击的文本等等，start表示需要设置格式的子字符串的起始下标，同理end表示终了下标，flags属性就有意思了，共有四种属性：*/
//    Spanned.SPAN_INCLUSIVE_EXCLUSIVE 从起始下标到终了下标，包括起始下标
//    Spanned.SPAN_INCLUSIVE_INCLUSIVE 从起始下标到终了下标，同时包括起始下标和终了下标
//    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE 从起始下标到终了下标，但都不包括起始下标和终了下标
//    Spanned.SPAN_EXCLUSIVE_INCLUSIVE 从起始下标到终了下标，包括终了下标


    /**
     * 设置文本图片
     */
    private void ImageSpan() {
        SpannableString spannableString = new SpannableString("在文本中添加表情（表情）");
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ad2);
//                getResources().getDrawable(R.drawable.ad2);
        drawable.setBounds(5, 5, 99, 99);
        ImageSpan imageSpan = new ImageSpan(drawable);
        spannableString.setSpan(imageSpan, 6, 8, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setText(spannableString);
    }

    /**
     * 为文字设置风格（粗体、斜体），和TextView属性textStyle类似
     */
    private void StyleSpan() {
        SpannableString spannableString = new SpannableString("为文字设置粗体、斜体风格");
        StyleSpan styleSpan_B = new StyleSpan(Typeface.BOLD);
        /*如果斜体字没反应,在控件中添加属性android:typeface="monospace"*/
        StyleSpan styleSpan_I = new StyleSpan(Typeface.ITALIC);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0099EE"));

        spannableString.setSpan(styleSpan_B, 5, 7, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan, 5, 7, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(styleSpan_I, 8, 10, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(colorSpan, 8, 10, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        tv.setHighlightColor(Color.parseColor("#36969696"));
        tv.setText(spannableString);
    }

    /**
     * 设置下标
     */
    private void SubscriptSpan() {
        SpannableString spannableString = new SpannableString("为文字设置下标");
        SubscriptSpan subscriptSpan = new SubscriptSpan();
        spannableString.setSpan(subscriptSpan, 5, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setText(spannableString);
    }

    /**
     * 设置上标
     */
    private void SuperscriptSpan() {
        SpannableString spannableString = new SpannableString("为文字设置上标");
        SuperscriptSpan superscriptSpan = new SuperscriptSpan();
        spannableString.setSpan(superscriptSpan, 5, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setText(spannableString);
    }

    /**
     * 为文本设置下划线
     */
    private void UnderlineSpan() {
        SpannableString spannableString = new SpannableString("为文字设置下划线");
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableString.setSpan(underlineSpan, 5, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setText(spannableString);
    }

    /**
     * 为文本设置中划线，也就是常说的删除线
     */
    private void StrikethroughSpan() {
        SpannableString spannableString = new SpannableString("为文字设置删除线");
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        spannableString.setSpan(strikethroughSpan, 5, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setText(spannableString);
    }

    /**
     * 设置文字相对大小，在TextView原有的文字大小的基础上，相对设置文字大小
     */
    private void RelativeSizeSpan() {
        SpannableString spannableString = new SpannableString("万丈高楼平地起");

        RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(1.2f);
        RelativeSizeSpan sizeSpan02 = new RelativeSizeSpan(1.4f);
        RelativeSizeSpan sizeSpan03 = new RelativeSizeSpan(1.6f);
        RelativeSizeSpan sizeSpan04 = new RelativeSizeSpan(1.8f);
        RelativeSizeSpan sizeSpan05 = new RelativeSizeSpan(1.6f);
        RelativeSizeSpan sizeSpan06 = new RelativeSizeSpan(1.4f);
        RelativeSizeSpan sizeSpan07 = new RelativeSizeSpan(1.2f);

        spannableString.setSpan(sizeSpan01, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan02, 1, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan03, 2, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan04, 3, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan05, 4, 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan06, 5, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan07, 6, 7, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        tv.setText(spannableString);
    }

    /**
     * 设置后景颜色 为文本设置背景色，效果和TextView的setBackground()
     */
    private void BackgroundColorSpan() {
        SpannableString spannableString = new SpannableString("设置文字的背景色为淡绿色");
        BackgroundColorSpan colorSpan = new BackgroundColorSpan(Color.parseColor("#AC00FF30"));
        spannableString.setSpan(colorSpan, 9, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan, 3, 7, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setText(spannableString);
    }

    /**
     * 设置前景颜色 为文本设置前景色，效果和TextView的setTextColor()类似
     */
    private void ForegroundColorSpan() {
        SpannableString spannableString = new SpannableString("设置文字的前景色为淡蓝色");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0099EE"));
        spannableString.setSpan(colorSpan, 9, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan, 3, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        /*发现一个问题,设置颜色只能设置一个区域，如果设置多个只会取设置的最后一条语句的区域*/
        tv.setText(spannableString);
    }


    AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            select = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}
