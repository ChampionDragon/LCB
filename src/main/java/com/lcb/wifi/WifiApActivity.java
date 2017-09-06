package com.lcb.wifi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lcb.R;
import com.lcb.base.BaseActivity;
import com.lcb.wifi.WifiApUtil.WifiSecurityType;

public class WifiApActivity extends BaseActivity {
	private String TAG = "lcb";
	public final static boolean DEBUG = true;
	private Button mBtStartWifiAp, mBtStopWifiAp;
	private EditText mWifiSsid, mWifiPassword;
	private RadioGroup mRgWifiSerurity;
	private RadioButton mRdNo, mRdWpa, mRdWpa2;
	private TextView mWifiApState;
	private WifiSecurityType mWifiType = WifiSecurityType.WIFICIPHER_NOPASS;
	private ImageView back;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (DEBUG)
				Log.i(TAG, "WifiApActivity message.what=" + msg.what);
			switch (msg.what) {
			case WifiApUtil.MESSAGE_AP_STATE_ENABLED:
				String ssid = WifiApUtil.getInstance(WifiApActivity.this)
						.getValidApSsid();
				String pw = WifiApUtil.getInstance(WifiApActivity.this)
						.getValidPassword();
				int security = WifiApUtil.getInstance(WifiApActivity.this)
						.getValidSecurity();
				mWifiApState.setText("wifi热点开启成功" + "\n" + "SSID = " + ssid
						+ "\n" + "Password = " + pw + "\n" + "Security = "
						+ security);
				break;
			case WifiApUtil.MESSAGE_AP_STATE_FAILED:
				mWifiApState.setText("wifi热点关闭");
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_ap_test);
		WifiApUtil.getInstance(getApplicationContext());
		WifiApUtil.getInstance(this).regitsterHandler(mHandler);
		mBtStartWifiAp = (Button) findViewById(R.id.bt_start_wifiap);
		mWifiSsid = (EditText) findViewById(R.id.et_ssid);
		mWifiPassword = (EditText) findViewById(R.id.et_password);
		mRgWifiSerurity = (RadioGroup) findViewById(R.id.rg_security);
		mRdNo = (RadioButton) findViewById(R.id.rd_no);
		mRdWpa = (RadioButton) findViewById(R.id.rd_wpa);
		mRdWpa2 = (RadioButton) findViewById(R.id.rd_wpa2);
		mWifiApState = (TextView) findViewById(R.id.tv_state);
		mBtStopWifiAp = (Button) findViewById(R.id.bt_stop_wifiap);
		initback();
	}

	private void initback() {
		back = (ImageView) findViewById(R.id.back_wifi_ap);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		mRgWifiSerurity
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {

						if (arg1 == mRdNo.getId()) {
							mWifiType = WifiSecurityType.WIFICIPHER_NOPASS;
						} else if (arg1 == mRdWpa.getId()) {
							mWifiType = WifiSecurityType.WIFICIPHER_WPA;
						} else if (arg1 == mRdWpa2.getId()) {
							mWifiType = WifiSecurityType.WIFICIPHER_WPA2;
						}
						if (DEBUG)
							Log.i(TAG, "radio check mWifiType = " + mWifiType);
					}
				});
		mBtStartWifiAp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String ssid = mWifiSsid.getText().toString();
				String password = mWifiPassword.getText().toString();
				if (DEBUG)
					Log.d(TAG, "ssid = " + ssid + "password = " + password);
				if (null == ssid || "".equals(ssid)) {
					Toast.makeText(WifiApActivity.this, "请输入ssid",
							Toast.LENGTH_SHORT).show();
					return;
				}
				mWifiApState.setText("正在开启");
				WifiApUtil.getInstance(WifiApActivity.this).turnOnWifiAp(ssid,
						password, mWifiType);

			}
		});

		mBtStopWifiAp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				WifiApUtil.getInstance(WifiApActivity.this).closeWifiAp();
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (DEBUG)
			Log.i(TAG, "WifiApActivity onBackPressed");
		finish();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (DEBUG)
			Log.i(TAG, "WifiApActivity onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (DEBUG)
			Log.i(TAG, "WifiApActivity onDestroy");
		WifiApUtil.getInstance(this).unregitsterHandler();
	}
}
