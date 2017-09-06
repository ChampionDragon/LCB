package com.lcb.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;

import com.lcb.R;
import com.lcb.adapter.RecordAdapter;
import com.lcb.base.BaseActivity;
import com.lcb.bean.RecordBean;
import com.lcb.constant.Constant;
import com.lcb.http.HttpByGet;
import com.lcb.utils.DialogLoadingUtil;
import com.lcb.utils.DialogNotileUtil;
import com.lcb.utils.Logs;
import com.lcb.view.OnRefreshListener;
import com.lcb.view.RefreshListView;

public class GetRecordActivity extends BaseActivity implements OnClickListener,
		OnRefreshListener {
	private RefreshListView lv;
	private RecordAdapter adapter;
	private boolean first = true;
	private boolean isGet = true;// 判断后台是否还有可读数据
	private boolean isDialog;
	private int page = 0;// 查询后台的页码
	public static final int INITLV = 0;
	public static final int HIDEHEAD = 1;
	public static final int HIDEFOOT = 2;
	public static final int Dialog = 3;
	String tag = "";// 判断是上拉还是下拉
	private List<RecordBean> list;
	private Dialog dialog;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case INITLV:
				adapter.addAll(list);
				initLv();
				break;
			case HIDEHEAD:
				lv.hideHeaderView();
				break;
			case HIDEFOOT:
				lv.hideFooterView();
				break;
			case Dialog:
				dialog.dismiss();
				DialogNotileUtil
						.show(GetRecordActivity.this, "未连接到网络\n或者服务器异常");
				lv.hideFooterView();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getrecord);
		initView();
		initGetRecord();
	}

	private void initView() {
		findViewById(R.id.back_getrecord).setOnClickListener(this);
		lv = (RefreshListView) findViewById(R.id.lv_getrecord);
		adapter = new RecordAdapter(this, R.layout.item_recordlv);
		lv.setOnRefreshListener(this);
		// lv.removeHeaderView();//屏蔽上拉刷新
		dialog = DialogLoadingUtil.CreatDialog(this);
		dialog.show();
	}

	/**
	 * 获取后台数据
	 */
	private void initGetRecord() {
		if (isGet) {
			final String url = Constant.urlGetRecord
					+ HttpByGet.get("daozaid", "33ffd8054d52373729632351",
							"page", page + "", "devtype", "5");
			Runnable getRecordRunnable = new Runnable() {
				@Override
				public void run() {
					String executeHttpGet = HttpByGet.executeHttpGet(url);
					Logs.d("----------------data------------------");
					parseData(executeHttpGet);
				}
			};
			executor.submit(getRecordRunnable);
		} else {
			if (!isDialog) {
				isDialog = true;
				DialogNotileUtil.show(this, "后台已经没有数据了");
				lv.removeHeaderView();
				lv.changeFooterView();
			}
		}

	}

	/**
	 * 解析返回的数据
	 */
	private void parseData(String executeHttpGet) {
		try {
			int indexOf = executeHttpGet.indexOf("[");
			int length = executeHttpGet.length();// 返回的数据长度
			Logs.d(executeHttpGet);
			if (executeHttpGet.equals(HttpByGet.error)) {
				handler.sendEmptyMessage(Dialog);
				return;
			}
			if (length < 10)
				isGet = false;
			executeHttpGet = executeHttpGet.substring(indexOf, length);
			JSONArray jsonArray = new JSONArray(executeHttpGet);
			list = new ArrayList<>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jo = (JSONObject) jsonArray.get(i);
				RecordBean recordBean = new RecordBean(jo.getString("title"),
						jo.getString("intime"), jo.getInt("type") + "");
				list.add(recordBean);
			}
			handler.sendEmptyMessage(INITLV);
		} catch (JSONException e) {
			e.printStackTrace();
			Logs.d(e.toString());
		}
	}

	private void initLv() {
		if (first) {
			lv.setAdapter(adapter);
			first = false;
			dialog.dismiss();
		} else {
			if (lv.getAdapter() == null) {
				adapter = new RecordAdapter(this, R.layout.item_recordlv);
				lv.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
		}
		page += 1;
		if (tag.equals("pull")) {
			handler.sendEmptyMessage(HIDEHEAD);
		} else if (tag.equals("load")) {
			handler.sendEmptyMessage(HIDEFOOT);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_getrecord:
			finish();
			break;

		}
	}

	@Override
	public void onDownPullRefresh() {
		if (!isDialog) {
			initGetRecord();
			tag = "pull";
			Logs.d("111111111111");
		} else {
			SystemClock.sleep(666);
			lv.hideHeaderView();
			Logs.d("222222222222");
		}
	}

	@Override
	public void onLoadingMore() {
		if (!isDialog) {
			initGetRecord();
			tag = "load";
			Logs.d("66666666");
		}
	}

}
