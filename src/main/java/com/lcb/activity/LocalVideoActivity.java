package com.lcb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lcb.R;
import com.lcb.base.BaseActivity;
import com.lcb.utils.StrUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LocalVideoActivity extends BaseActivity {
    private ListView lv;
    private List<String> listName, listUrl;
    private ArrayAdapter<String> adapter;
    private TextView tv;
    public static String URL = "videourl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localvideo);
        tv = (TextView) findViewById(R.id.localvideo_tv);
        /*显示根目录*/
        tv.setText(Environment.getExternalStorageDirectory().getAbsolutePath());

        lv = (ListView) findViewById(R.id.localvideo_lv);
        listName = new ArrayList<>();
        listUrl = new ArrayList<>();
        findViewById(R.id.back_localvideo).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        initFile(Environment.getExternalStorageDirectory().getAbsolutePath());
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1, listUrl);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(listener);
    }

    OnItemClickListener listener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Intent intent = new Intent().putExtra(URL, listUrl.get(position));
            setResult(-1, intent);
            finish();
        }
    };

    /**
     * 初始化影片的路径,原理遍历根目录下所有文件夹，将视频格式结尾的文件路径添加到list里
     */
    private void initFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] fileArray = file.listFiles();
                //Logs.e(Arrays.toString(fileArray)+"  这个可能会为空");
                if (fileArray != null) {
                    for (File f : fileArray) {
                        if (f.isDirectory()) {
                            initFile(f.getPath());
                        } else {
                            if (f.getName().endsWith("wmv")
                                    || f.getName().endsWith("mp4")
                                    || f.getName().endsWith("mkv")
                                    || f.getName().endsWith("rmvb")) {
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

}
