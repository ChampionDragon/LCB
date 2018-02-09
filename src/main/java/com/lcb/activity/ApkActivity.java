package com.lcb.activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lcb.R;
import com.lcb.base.BaseActivity;
import com.lcb.utils.Logs;
import com.lcb.utils.SmallUtil;
import com.lcb.utils.StrUtil;
import com.lcb.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ApkActivity extends BaseActivity {
    private ListView lv;
    private List<String> listName, listUrl;
    private ArrayAdapter<String> adapter;
    private TextView tv;
    public static String apkURL = "apkurl";
    public static String apkName = "apkname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apk);
        tv = (TextView) findViewById(R.id.apk_tv);
        lv = (ListView) findViewById(R.id.apk_lv);
        listName = new ArrayList<>();
        listUrl = new ArrayList<>();
        findViewById(R.id.back_apk).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        initFile(Environment.getExternalStorageDirectory().getAbsolutePath());
        for(String a:listUrl){
            Logs.w(a);
        }
        if (listName.size() == 0) {
            ToastUtil.showLong("没搜索到APK");
            tv.setText("根目录未遍历到APK");
        } else {
            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_expandable_list_item_1, listName);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(listener);
        }
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Bundle bundle=new Bundle();
            bundle.putString(apkName,listName.get(position));
            bundle.putString(apkURL,listUrl.get(position));
            SmallUtil.getActivity(ApkActivity.this,ApkInfoActivity.class,bundle);
        }
    };





    /*初始化APK的路径*/
    private void initFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] fileArray = file.listFiles();
                for (File f : fileArray) {
                    if (f.isDirectory()) {
                        initFile(f.getPath());
                    } else {
                        if (f.getName().endsWith("apk")) {
                            listUrl.add(f.getPath());
                            //由于有些文件名的路径太长了，显示在listView太丑,所以我创建两个list一个保留名字，一个保留路径.
                            listName.add(StrUtil.getLastindexStr(f.getPath(), "/", false));
                        }
                    }
                }
            }
        }
    }


}
