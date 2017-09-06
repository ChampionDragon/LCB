package com.lcb.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lcb.R;
import com.lcb.base.BaseActivity;
import com.lcb.bean.WifiBean;
import com.lcb.utils.SmallUtil;

public class WifiInfoActivity extends BaseActivity {
	private String TAG = "lcb";
	private WifiManager mWifiManager;
	private WifiInfo mWifiInfo;
	private ListView listView;
	private ConnectivityManager connectivityManager;
	private NetworkInfo networkInfo;
	private TextView tv;
	private ImageView iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_connected_info);
		listView = (ListView) findViewById(R.id.lv_wifi_info);
		tv = (TextView) findViewById(R.id.tv_wifi_info);
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mWifiInfo = mWifiManager.getConnectionInfo();
		connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		networkInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		initData();
		initback();
		Log.d(TAG, "当前wifi状态：" + mWifiManager.getWifiState() + "    当前网络状态："
				+ networkInfo.getState());
		Log.v(TAG, "当前wifi是否开启 " + mWifiManager.isWifiEnabled());
	}

	private void initback() {
		iv=(ImageView) findViewById(R.id.back_wifi_info);
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initData() {
		if (networkInfo.isConnected()) {
			tv.setVisibility(View.GONE);
			WifiAdapter adapter = new WifiAdapter(this, R.layout.wifiinfo_item);
			adapter.add(new WifiBean("热点名", mWifiInfo.getSSID()));
			adapter.add(new WifiBean("手机本身网卡的MAC地址", mWifiInfo.getMacAddress()));
			adapter.add(new WifiBean("连接的速度", mWifiInfo.getLinkSpeed() + ""));
			adapter.add(new WifiBean("SSID是否被隐藏", mWifiInfo.getHiddenSSID()
					+ ""));
			adapter.add(new WifiBean("BSSID属性（所连接的WIFI设备的MAC地址）", mWifiInfo
					.getBSSID()));
			adapter.add(new WifiBean("网络号", mWifiInfo.getNetworkId() + ""));
			adapter.add(new WifiBean("网络信号强度", mWifiInfo.getRssi() + ""));
			adapter.add(new WifiBean("获取具体客户端状态的信息", mWifiInfo
					.getSupplicantState() + ""));
//			Log.d(TAG, "ip地址   "+mWifiInfo.getIpAddress());
			
			adapter.add(new WifiBean("WifiInfo", mWifiInfo.toString()));
			mWifiInfo.getIpAddress();
			DetailedState detailedStateOf = WifiInfo
					.getDetailedStateOf(mWifiInfo.getSupplicantState());
			String s = detailedStateOf.toString();
			adapter.add(new WifiBean("获取客户端的连通性", s));

			// 得到DhcpInfo类的一些信息DHCP（Dynamic Host Configuration
			// Protocol，动态主机配置协议）是一个局域网的网络协议
			DhcpInfo dhcpInfo = mWifiManager.getDhcpInfo();
			String DhcpStr = dhcpInfo.toString();
			int dns1 = mWifiManager.getDhcpInfo().dns1;
			int dns2 = mWifiManager.getDhcpInfo().dns2;
			int leaseDuration = dhcpInfo.leaseDuration;
			int gateway = dhcpInfo.gateway;
			int ipAddresse = dhcpInfo.ipAddress;
			int describeContents = dhcpInfo.describeContents();
			int netmask = dhcpInfo.netmask;
			int serverAddress = dhcpInfo.serverAddress;
			adapter.add(new WifiBean("dns1地址", SmallUtil.intToString(dns1)));
			adapter.add(new WifiBean("dns2地址", SmallUtil.intToString(dns2)));
			adapter.add(new WifiBean("网关", SmallUtil.intToString(gateway)));
			adapter.add(new WifiBean("ip地址", SmallUtil.intToString(ipAddresse)));
			
			
//			Log.i(TAG, "ip地址   "+ipAddresse);
			
			
			adapter.add(new WifiBean("子网掩码", SmallUtil.intToString(netmask)));
			adapter.add(new WifiBean("服务端地址", SmallUtil
					.intToString(serverAddress)));
			adapter.add(new WifiBean("连接速度", leaseDuration + ""));
			adapter.add(new WifiBean("描述内容", describeContents + ""));
			adapter.add(new WifiBean("DhcpStr", DhcpStr));
			listView.setAdapter(adapter);
		} else {
			listView.setVisibility(View.GONE);
			tv.setText("　　  未连接网络"+"\n"+"　　无法获取信息");
		}
	}
}
