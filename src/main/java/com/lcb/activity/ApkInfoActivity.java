package com.lcb.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.lcb.R;
import com.lcb.adapter.InfoAdapter;
import com.lcb.base.BaseActivity;
import com.lcb.bean.InfoBean;
import com.lcb.utils.ApkUtil;

public class ApkInfoActivity extends BaseActivity {
    private ListView lv;
    private TextView tv;
    private String name, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apk_info);
        findViewById(R.id.back_apkinfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv = (TextView) findViewById(R.id.apkinfo_tv);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString(ApkActivity.apkName);
            url = bundle.getString(ApkActivity.apkURL);
            tv.setText(name);
            initData();
        }
    }

    /*初始化数据*/
    private void initData() {
        InfoAdapter adapter = new InfoAdapter(this, R.layout.item_info);
        lv = (ListView) findViewById(R.id.apkinfo_lv);
        adapter.add(new InfoBean("apk名字", ApkUtil.ApkName(this, url)));
//        adapter.add(new InfoBean("apk签名", ApkUtil.ApkSignature(this, url)));
//        adapter.add(new InfoBean("apk权限", Arrays.toString(ApkUtil.ApkPremission(this, url))));
        adapter.add(new InfoBean("apk版本名", ApkUtil.VersionName(this, url)));
        adapter.add(new InfoBean("apk版本号", "" + ApkUtil.VersionCode(this, url)));
        adapter.add(new InfoBean("apk包名", "" + ApkUtil.PackgeName(this, url)));
        lv.setAdapter(adapter);
    }


}
