package com.lcb.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lcb.R;
import com.lcb.bean.dataObject;
import com.lcb.constant.Constant;
import com.lcb.utils.TimeUtil;
import com.lcb.view.PPChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.R.id.list;

public class chartViewActivity extends AppCompatActivity {
    private PPChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_view);
        chart = (PPChart) findViewById(R.id.PPChart);
        initChart();
    }

    /**
     * 初始化圆点变化趋势曲线图
     */
    private void initChart() {
        List<dataObject> list = new ArrayList<>();
        long systemTime = System.currentTimeMillis();
        for (int i = 10; i > 0; i--) {
            //测试结果传过去的数字是一百以内的数字
            int random = new Random().nextInt(10000);
//因为当i=30时24*3600*1000*i等于2592000000超过int取值范围2147483647变成了负数越减越大所以让它的取值范围变大成long型
            long time = systemTime - (long) 24 * 3600 * 1000 * i;
            String timestr = TimeUtil.long2time(time, Constant.formatday);

//            Logs.w(time + "    " + timestr);

            dataObject data = new dataObject(timestr, (float) random /2);
            list.add(data);
        }
        chart.setMAX(5000);
        chart.setData(list);
    }


}
