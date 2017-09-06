package com.lcb.wifi;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lcb.R;
import com.lcb.base.BaseActivity;
import com.lcb.utils.DialogNotileUtil;
import com.lcb.utils.SpUtil;
import com.lcb.utils.ToastUtil;

public class WifiListActivity extends BaseActivity implements OnClickListener {
	public static final String TAG = "lcb";
	private Button check_wifi, open_wifi, close_wifi, scan_wifi, jump_next;
	private ImageView iv;
	private ListView mlistView;
	protected WifiAdmin mWifiAdmin;
	private List<ScanResult> mWifiList;
	public int level;
	protected String ssid;
	private TextView tv_ssid;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_list);
		mWifiAdmin = new WifiAdmin(WifiListActivity.this);
		initViews();
		IntentFilter filter = new IntentFilter(
				WifiManager.NETWORK_STATE_CHANGED_ACTION);
		// ="android.net.wifi.STATE_CHANGE"
		registerReceiver(mReceiver, filter);
		mlistView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AlertDialog.Builder alert = new AlertDialog.Builder(
						WifiListActivity.this);
				ssid = mWifiList.get(position).SSID;
				alert.setTitle(ssid);
				alert.setMessage("输入密码");
				final EditText et_password = new EditText(WifiListActivity.this);
				et_password.setBackgroundResource(R.drawable.wifi_bg);
				// final SharedPreferences preferences = getSharedPreferences(
				// "wifi_password", Context.MODE_PRIVATE);
				// et_password.setText(preferences.getString(ssid, ""));
				// 保留上次ssid的密码
				final SpUtil sp = SpUtil.getInstance("wifi_password",
						Context.MODE_PRIVATE);
				et_password.setText(sp.getString(ssid));

				alert.setView(et_password);
				alert.setPositiveButton("连接",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String pw = et_password.getText().toString();
								if (TextUtils.isEmpty(pw)) {
									Toast.makeText(WifiListActivity.this,
											"密码不能为空", Toast.LENGTH_SHORT)
											.show();
									return;
								}
								// Editor editor = preferences.edit();
								// editor.putString(ssid, pw);
								// editor.commit();
								sp.putString(ssid, pw);
								mWifiAdmin.addNetwork(mWifiAdmin
										.CreateWifiInfo(ssid, et_password
												.getText().toString(), 3));
								checkWifiConfiguration(ssid);
							}
						});
				alert.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// mWifiAdmin.removeWifi(mWifiAdmin.getNetworkId());
							}
						});
				alert.create();
				alert.show();

			}
		});
	}

	/*
	 * 控件初始化
	 */
	private void initViews() {
		tv_ssid = (TextView) findViewById(R.id.tv_wifi_ssid);
		iv = (ImageView) findViewById(R.id.back_wifi_list);
		iv.setOnClickListener(this);
		check_wifi = (Button) findViewById(R.id.check_wifi);
		open_wifi = (Button) findViewById(R.id.open_wifi);
		close_wifi = (Button) findViewById(R.id.close_wifi);
		scan_wifi = (Button) findViewById(R.id.scan_wifi);
		jump_next = (Button) findViewById(R.id.jump_next);
		mlistView = (ListView) findViewById(R.id.wifi_list);
		check_wifi.setOnClickListener(WifiListActivity.this);
		open_wifi.setOnClickListener(WifiListActivity.this);
		close_wifi.setOnClickListener(WifiListActivity.this);
		scan_wifi.setOnClickListener(WifiListActivity.this);
		jump_next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_wifi_list:
			finish();
			break;
		case R.id.check_wifi:
			mWifiAdmin.checkState(WifiListActivity.this);
			break;
		case R.id.open_wifi:
			mWifiAdmin.openWifi(WifiListActivity.this);
			break;
		case R.id.close_wifi:
			mWifiAdmin.closeWifi(WifiListActivity.this);
			break;
		case R.id.scan_wifi:
			mWifiAdmin.startScan(WifiListActivity.this);
			mWifiList = mWifiAdmin.getWifiList();
			if (mWifiList != null) {
				mlistView.setAdapter(new MyAdapter(this, mWifiList));
				new Utility().setListViewHeightBasedOnChildren(mlistView);
			}
			break;
		case R.id.jump_next:
			Intent it = new Intent(WifiListActivity.this,
					WifiInfoActivity.class);
			startActivity(it);
			break;
		default:
			break;
		}
	}

	public class MyAdapter extends BaseAdapter {
		LayoutInflater inflater;
		List<ScanResult> list;

		public MyAdapter(Context context, List<ScanResult> list) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint({ "ViewHolder", "InflateParams" })
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			view = inflater.inflate(R.layout.wifi_listitem, null);
			ScanResult scanResult = list.get(position);
			TextView wifi_ssid = (TextView) view.findViewById(R.id.ssid);
			ImageView wifi_level = (ImageView) view
					.findViewById(R.id.wifi_level);
			wifi_ssid.setText(scanResult.SSID);
			// Log.i(TAG, "scanResult.SSID=" + scanResult);//遍历所有热点数据
			// 设置接收wifi的等级信号
			level = WifiManager.calculateSignalLevel(scanResult.level, 5);
			if (scanResult.capabilities.contains("WEP")
					|| scanResult.capabilities.contains("PSK")
					|| scanResult.capabilities.contains("EAP")) {
				wifi_level.setImageResource(R.drawable.wifi_signal_lock);
			} else {
				wifi_level.setImageResource(R.drawable.wifi_signal_open);
			}
			wifi_level.setImageLevel(level);
			// 判断信号强度，显示对应的指示图标  
			return view;
		}
	}

	/* 设置listview的高度 */
	public class Utility {
		public void setListViewHeightBasedOnChildren(ListView listView) {
			ListAdapter listAdapter = listView.getAdapter();
			if (listAdapter == null) {
				return;
			}
			int totalHeight = 0;
			for (int i = 0; i < listAdapter.getCount(); i++) {
				View listItem = listAdapter.getView(i, null, listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = totalHeight
					+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
			listView.setLayoutParams(params);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
		unregisterReceiver(mReceiver);
	}

	/**
	 * 判断连接的ssid是否连上网
	 */
	private void checkWifiConfiguration(final String SSID) {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = manager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if (!networkInfo.isConnected()) {
					DialogNotileUtil.show(WifiListActivity.this, "密码错误，请重新再尝试");
				}
			}
		}, 8888);
	}

	// 监听wifi状态变化
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo networkInfo = manager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			State wifi = networkInfo.getState();// 得到当前WiFi的状态
			DetailedState detailedState = networkInfo.getDetailedState();// 得到网络状态的详细内容
			Log.d(TAG, "netstate detail   " + detailedState.toString()
					+ " list260 netstate " + wifi);
			if (State.DISCONNECTED == wifi) {
				tv_ssid.setText("网络断开");
				// DialogNotileUtil.show(WifiListActivity.this,
				// "未连接到网络list264");
				ToastUtil.showLong("网络断开");
			}
			if (networkInfo.isConnected()) {
				WifiManager wifiManager = (WifiManager) context
						.getSystemService(Context.WIFI_SERVICE);
				String wifiSSID = wifiManager.getConnectionInfo().getSSID();
				tv_ssid.setText("连接到的网络名 ：" + wifiSSID);
				DialogNotileUtil.show(WifiListActivity.this, wifiSSID + "连接成功");
			}
		}

	};

}
