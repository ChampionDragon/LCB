package com.lcb;

import android.app.ActivityManager;
import android.content.Context;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lcb.adapter.MyFragmentPagerAdapter;
import com.lcb.base.AsyncTaskExecutor;
import com.lcb.base.BaseApplication;
import com.lcb.constant.Constant;
import com.lcb.fragment.FourFragment;
import com.lcb.fragment.OneFragment;
import com.lcb.fragment.ThreeFragment;
import com.lcb.fragment.TwoFragment;
import com.lcb.http.HttpByGet;
import com.lcb.utils.DialogLoading;
import com.lcb.utils.Logs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends FragmentActivity {
    private String tag = "lcb";
    private ViewPager pager;
    List<Fragment> fragmentList;// 页面集合
    private RadioGroup radioGroup;
    private RadioButton tab1;
    private RadioButton tab2;
    private RadioButton tab3;
    private RadioButton tab4;
    private AsyncTaskExecutor executor;
    String result;
    private SoundPool mSoundPool;//声音控件

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initWindow();// 初始化界面窗口
        executor = AsyncTaskExecutor.getinstance();
        test();

    }

    private void test() {


    }


    /*获得语言*/
    private void Langugae() {
        /*获取系统语言*/
        String systemLanguage = Locale.getDefault().getLanguage();
        /*获取应用语言*/
        String APPlanguage = getResources().getConfiguration().locale.getLanguage();
        Logs.i(systemLanguage + "\n" + APPlanguage);
    }


    /**
     * 但前APP的可用内存
     */
    private void MemorySize() {
        int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();//单位M(兆)
        int maxMemory = (int) Runtime.getRuntime().maxMemory();//单位B(字节）
//        maxMemory=memClass*1024KB*1024B
        Logs.d(memClass + "              " + maxMemory);
    }

    /**
     * 加载Dialog
     */
    private void DialogLoad() {
        final DialogLoading dialogLoading = new DialogLoading(this);
        dialogLoading.show();
        new Handler().postAtTime(new Runnable() {
            @Override
            public void run() {
                dialogLoading.close();
            }
        }, SystemClock.uptimeMillis() + 6666);
    }

    /**
     * 获取设备的id
     */
    private void initGetRecord() {
        final String url = Constant.urlGetRecord
                + HttpByGet.get("daozaid", "33ffd8054d52373729632351", "page",
                "1", "devtype", "5");
        Runnable getRecordRunnable = new Runnable() {
            @Override
            public void run() {
                String executeHttpGet = HttpByGet.executeHttpGet(url);
                Logs.d(executeHttpGet);
            }
        };
        executor.submit(getRecordRunnable);
    }

    /**
     * 线程循环
     */
    private void initScheduled() {
        ScheduledExecutorService scheduledExecutorService = Executors
                .newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(runnable, 1, 9,
                TimeUnit.SECONDS);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

        }
    };

    /**
     * volley的get方法
     */
    private void VolleyGet(String urlloginget) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                urlloginget, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logs.i("volleyget success  " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logs.d("volleyget fail  " + error);
            }
        });
        stringRequest.setTag("volleyget");
        BaseApplication.queue.add(stringRequest);
    }

    /**
     * volley的post方法
     */
    private void VolleyPost(String urlloginget) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                urlloginget, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logs.i("volleypost  success " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logs.d("volleypost fail  " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // 要传递的参数
                HashMap<String, String> map = new HashMap<>();
                map.put("key", "bb97bfce9edee938aeac99cb503b76db");
                map.put("cardno", "43052419910615");
                return map;
            }
        };
        stringRequest.setTag("volleypost");
        BaseApplication.queue.add(stringRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity时队列里面注销
        BaseApplication.queue.cancelAll("volleyget");
        BaseApplication.queue.cancelAll("volleypost");
    }

    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 下面两个功能只有在版本19或以上才能使用
            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            // 设置窗口全屏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void initViews() {
        pager = (ViewPager) findViewById(R.id.id_viewPager);
        fragmentList = getData();
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(
                getSupportFragmentManager(), fragmentList);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new TabOnPageChangeListener());
        // pager.setOnTouchListener(touchListener);// 设置这个可以是pager不滑动
        radioGroup = (RadioGroup) findViewById(R.id.id_radioGroup);
        tab1 = (RadioButton) findViewById(R.id.id_tab1);
        tab2 = (RadioButton) findViewById(R.id.id_tab2);
        tab3 = (RadioButton) findViewById(R.id.id_tab3);
        tab4 = (RadioButton) findViewById(R.id.id_tab4);
        // tab4.performClick();// 设置默认打开
        tab3.setChecked(true);// 设置默认打开方式二
        pager.setCurrentItem(2);
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);
    }

    /**
     * 添加页卡
     */
    private List<Fragment> getData() {
        fragmentList = new ArrayList<Fragment>();
        OneFragment oneFragment = new OneFragment();
        TwoFragment twoFragment = new TwoFragment();
        ThreeFragment threeFragment = new ThreeFragment();
        FourFragment fourFragment = new FourFragment();
        fragmentList.add(oneFragment);
        fragmentList.add(twoFragment);
        fragmentList.add(threeFragment);
        fragmentList.add(fourFragment);
        return fragmentList;
    }

    /**
     * 页卡改变事件
     */
    public class TabOnPageChangeListener implements OnPageChangeListener {
        // 当滑动状态改变时调用
        public void onPageScrollStateChanged(int state) {
        }

        // 当前页面被滑动时调用
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
        }

        // 当新的页面被选中时调用
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    tab1.setChecked(true);
                    break;
                case 1:
                    tab2.setChecked(true);
                    break;
                case 2:
                    tab3.setChecked(true);
                    break;
                case 3:
                    tab4.setChecked(true);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        BaseApplication.getInstance().exitApp();
    }


    OnTouchListener touchListener = new OnTouchListener() {
        // return true告诉事件分发系统，事件被我拦截了，后面的事件给我消费吧，其他监听事件变得无效return false就是不拦截啦
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            boolean b = v.performClick();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.d(tag, "0000000   " + b);
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.e(tag, "1111111   " + b);
                    break;
                case MotionEvent.ACTION_UP:
                    Log.v(tag, "2222222   " + b);
                    break;
            }
            return true;
        }
    };

    OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.id_tab1:
                    pager.setCurrentItem(0);// 选择某一页
                    break;
                case R.id.id_tab2:
                    pager.setCurrentItem(1);
                    break;
                case R.id.id_tab3:
                    pager.setCurrentItem(2);
                    break;
                case R.id.id_tab4:
                    pager.setCurrentItem(3);
                    break;
            }
        }
    };

}
