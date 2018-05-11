package com.lcb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lcb.R;
import com.lcb.base.BaseActivity;
import com.lcb.base.BaseApplication;
import com.lcb.constant.SpKey;
import com.lcb.pickerview.other.pickerViewUtil;
import com.lcb.utils.Logs;
import com.lcb.utils.SmallUtil;
import com.lcb.utils.SpUtil;

import java.util.ArrayList;
import java.util.Locale;

/**
 * 改变APP的语言
 */
public class ChangeLanguageActivity extends BaseActivity {
    TextView change, tv;
    private ArrayList<String> list = new ArrayList<>();
    public static final String EN = "ENGLISH";
    public static final String ZH = "中文";
    private SpUtil sp;
    private String state;
    private String stateLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);

        list.add("中文");
        list.add("ENGLISH");

        /*开机判断上次语言*/
        sp = BaseApplication.sp;
        judgeLanguage(sp.getString(SpKey.applanguage));

        initView();
        initTV();

    }

    /**
     * 初始化TextView
     */
    private void initTV() {
        tv.setText(getString(R.string.app_name) + "\n" + "\n"
                + getString(R.string.action_settings) + "\n" + "\n"
                + getString(R.string.check_wifi_state) + "\n" + "\n"
                + getString(R.string.realplay_loading) + "\n" + "\n"
                + getString(R.string.check_all_message)
        );
    }


    private void initView() {
        tv = (TextView) findViewById(R.id.changelanguage_tv);
        change = (TextView) findViewById(R.id.changelanguage_change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomWheelOption();
            }
        });
    }

    /**
     * 点击出弹框
     */
    private void BottomWheelOption() {
        pickerViewUtil.alertBottomWheelOption(ChangeLanguageActivity.this, "请选择系统语言", list, new pickerViewUtil.OnWheelViewClick() {
            @Override
            public void onClick(View view, int postion) {
                judgeLanguage(list.get(postion));
            }
        });
    }

    /**
     * 判断选中的语言
     */
    private void judgeLanguage(String s) {
        stateLast = sp.getString(SpKey.applanguage);
        if (s.equals(EN)) {
            SmallUtil.switchLanguage(this, Locale.ENGLISH);
            sp.putString(SpKey.applanguage, EN);
            state = EN;
        } else if (s.equals(ZH)) {
            SmallUtil.switchLanguage(this, Locale.CHINESE);
            sp.putString(SpKey.applanguage, ZH);
            state = ZH;
        }
        if (state == null) return;

        if (!stateLast.equals(s)) {
            cleanTop();
        }
    }

    /**
     * 清除顶端
     */
    private void cleanTop() {
        Logs.d(getResources().getConfiguration().locale.getLanguage());

        Intent i =
              /*  重启此APP(跳转到指定app)*/
//                getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
              /* 跳转到主界面*/
//                new Intent(this, MainActivity.class);
               /*保持本类不变*/
                new Intent(this, ChangeLanguageActivity.class);

        /*设置为该flag后，如果要启动ivity在栈中已经存在，那么不会创建新的activity而是将栈中
        该activity上方的所有activity出栈。这样做的目的是使所有的activity都显示为当前设定的语言。*/
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        /*设置此状态，记住以下原则，首先会查找是否存在和被启动的Activity具有相同的亲和性的任务栈（即taskAffinity，
        注意同一个应用程序中的activity的亲和性一样，所以下面的a情况会在同一个栈中），如果有，刚直接把这个栈
        整体移动到前台，并保持栈中的状态不变，即栈中的activity顺序不变，如果没有，则新建一个栈来存放被启动的activity*/
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(i);

    }


}
