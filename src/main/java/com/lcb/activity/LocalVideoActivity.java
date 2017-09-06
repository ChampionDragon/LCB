package com.lcb.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lcb.R;
import com.lcb.base.BaseActivity;
import com.lcb.utils.Logs;

public class LocalVideoActivity extends BaseActivity {
	private ListView lv;
	private List<String> list;
	private ArrayAdapter<String> adapter;
	public static String URL="videourl";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_localvideo);
		lv = (ListView) findViewById(R.id.localvideo_lv);
		list = new ArrayList<>();
		findViewById(R.id.back_localvideo).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		initFile(Environment.getExternalStorageDirectory().getAbsolutePath());
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, list);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(listener);
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent().putExtra(URL, list.get(position));

			setResult(-1, intent);
			finish();

		}
	};

	/**
	 * 初始化影片的路径
	 */
	private void initFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] fileArray = file.listFiles();
				for (File f : fileArray) {
					if (f.isDirectory()) {
						initFile(f.getPath());
					} else {
						if (f.getName().endsWith("wmv")
								|| f.getName().endsWith("mp4")
								|| f.getName().endsWith("mkv")
								|| f.getName().endsWith("rmvb")) {
							list.add(f.getPath());
						}
					}
				}
			}
		}
	}

}
