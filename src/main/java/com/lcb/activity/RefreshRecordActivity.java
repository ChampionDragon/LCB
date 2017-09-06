package com.lcb.activity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.lcb.R;
import com.lcb.adapter.RefreshRecordAdapter;
import com.lcb.base.BaseActivity;
import com.lcb.bean.DeviceBean;
import com.lcb.bean.DeviceManagerBean;
import com.lcb.constant.Constant;
import com.lcb.http.HttpByGet;
import com.lcb.utils.DialogLoadingUtil;
import com.lcb.utils.DialogNotileUtil;
import com.lcb.utils.Logs;
import com.lcb.utils.TimeUtil;
import com.lcb.view.OnRefreshListener;
import com.lcb.view.RefreshListView;

public class RefreshRecordActivity extends BaseActivity implements
		OnRefreshListener {
	private RefreshListView lv;
	private boolean isGet = true;// 判断后台是否还有可读数据
	private boolean isDialog;
	private RefreshRecordAdapter adapter;
	private int page = 0;// 查询后台的页码
	private List<DeviceBean> list;
	private List<DeviceManagerBean> deviceManages;
	public static final int INITLV = 0;
	public static final int Dialog = 1;
//	public static final int HIDEHEAD = 1;
	public static final int HIDEFOOT = 2;
	String tag = "";// 判断是上拉还是下拉
	private Dialog dialog;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case INITLV:
				deviceManages = getData(list);
				initLV();
				break;
//			case HIDEHEAD:
//				lv.hideHeaderView();
//				break;
			case HIDEFOOT:
				lv.hideFooterView();
				break;
			case Dialog:
				dialog.dismiss();
				DialogNotileUtil.show(RefreshRecordActivity.this, "未连接到网络\n或者服务器异常");
				lv.hideFooterView();
				break;
			}
		}

	};

	/**
	 * 刷新数据
	 */
	private void initLV() {
		if (lv.getAdapter() == null) {
			adapter = new RefreshRecordAdapter(this, deviceManages);
			lv.setAdapter(adapter);
			dialog.dismiss();
		} else {
			adapter.setData(deviceManages);
			adapter.notifyDataSetChanged();
		}
		page += 1;
//		if (tag.equals("pull")) {
//			handler.sendEmptyMessage(HIDEHEAD);
//		} else 
			if (tag.equals("load")) {
			handler.sendEmptyMessage(HIDEFOOT);
		}
	}

	/**
	 * 过滤数据
	 */
	private List<DeviceManagerBean> getData(List<DeviceBean> list) {
		Map<String, List<DeviceBean>> map = new LinkedHashMap<String, List<DeviceBean>>();
		String key = "";
		List<DeviceBean> deviceBeans = null;
		List<DeviceManagerBean> deviceManagerBeans = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			DeviceBean bean = list.get(i);
			String data = TimeUtil.long2time(bean.getCreattime(),
					Constant.cformatD);
			boolean b = key.equals(data);
			if (!b) {
				deviceBeans = new ArrayList<DeviceBean>();
				key = data;
			}
			deviceBeans.add(bean);
			map.put(key, deviceBeans);
		}
		Set<Entry<String, List<DeviceBean>>> entrySet = map.entrySet();
		for (Entry<String, List<DeviceBean>> entry : entrySet) {
			deviceManagerBeans.add(new DeviceManagerBean(entry.getKey(), entry
					.getValue()));
		}
		return deviceManagerBeans;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refreshrecord);
		initView();
		list = new ArrayList<>();
		initGetRecord();
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
			if(executeHttpGet.equals(HttpByGet.error)){
				handler.sendEmptyMessage(Dialog);
				return;
			}
			if (length < 10)
				isGet = false;
				executeHttpGet = executeHttpGet.substring(indexOf, length);
				JSONArray jsonArray = new JSONArray(executeHttpGet);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jo = (JSONObject) jsonArray.get(i);
					long time2long = TimeUtil.time2long(jo.getString("intime"),
							Constant.formatsecond);
					String typeStr=jo.getInt("type")+"" ;//判断类型相编号对应的中文
					switch (typeStr) {
					case "1":
						typeStr = "道闸门1";
						break;
					case "2":
						typeStr = "道闸门2";
						break;
					case "3":
						typeStr = "平开门";
						break;
					case "4":
						typeStr = "室内平开门";
						break;
					case "5":
						typeStr = "伸缩门";
						break;
					}
					DeviceBean bean = new DeviceBean(time2long,
							jo.getString("title"),typeStr);
					list.add(bean);
				}
				handler.sendEmptyMessage(INITLV);
		} catch (JSONException e) {
			e.printStackTrace();
			Logs.d(e.toString());
		}
	}

	private void initView() {
		findViewById(R.id.back_refreshrecord).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		lv = (RefreshListView) findViewById(R.id.refreshrecord_lv);
		lv.setOnRefreshListener(this);
		lv.removeHeaderView();
		dialog = DialogLoadingUtil.CreatDialog(this);
		dialog.show();
	}

	@Override
	public void onDownPullRefresh() {
//		if (!isDialog) {
//			initGetRecord();
//			tag = "pull";
//			Logs.d("111111111111");
//		} else {
//			SystemClock.sleep(666);
//			lv.hideHeaderView();
//			Logs.d("222222222222");
//		}
	}

	@Override
	public void onLoadingMore() {
		Logs.d("dadadadasd");
		if (!isDialog) {
			initGetRecord();
			tag = "load";
			Logs.d("66666666");
		}
	}
}
