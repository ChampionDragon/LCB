package com.lcb.Ezviz.act;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lcb.R;
import com.lcb.base.BaseActivity;
import com.lcb.base.BaseApplication;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.exception.BaseException;
import com.videogo.openapi.bean.EZDeviceUpgradeStatus;
import com.videogo.openapi.bean.EZDeviceVersion;
import com.videogo.util.LogUtil;
import com.videogo.widget.TitleBar;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/6/23.
 */

public class EZUpgradeDeviceActivity extends BaseActivity{
    private final static String TAG = "lcb";
    private final static int TIMER_PERIODS = 3*1000;
    private TitleBar mTitleBar;
    LinearLayout mUpgradeLL = null;
    LinearLayout mUpgradeProgressLL = null;
    Button mUpgradeButton = null;
    TextView mProgressTextView = null;
    TextView mVersionDescTextView = null;

    String mDeviceSerial;
    private EZDeviceVersion mVersion = null;
    private EZDeviceUpgradeStatus mUpgradeStatus = null;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ezupgrade_device);
        initTitleBar();
        find_views();
        init_views();
    }

    private void find_views() {
        mUpgradeLL = (LinearLayout) findViewById(R.id.ezupgrade_ll_btn);
        mUpgradeProgressLL = (LinearLayout) findViewById(R.id.ezupgrade_ll_progress);
        mUpgradeButton = (Button) findViewById(R.id.ezupgrade_button);
        mProgressTextView = (TextView) findViewById(R.id.ezupgrade_progress_text);
        mVersionDescTextView = (TextView) findViewById(R.id.ezupgrade_text_version_desc);
    }

    private void init_views() {
        mUpgradeButton.setEnabled(false);
        mDeviceSerial = getIntent().getStringExtra("deviceSerial");
        LogUtil.i(TAG, "init_views: serial:" + mDeviceSerial);
        mUpgradeButton.setVisibility(View.VISIBLE);
        mProgressTextView.setVisibility(View.VISIBLE);
//        checkUpgradeStatusPeriodical();
        showVersionViewOnce();
        mProgressTextView.setText("升级进度: 0" + "%");

        mUpgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUpgrade();
            }
        });
    }

    private void initTitleBar() {
        mTitleBar = (TitleBar) findViewById(R.id.title_bar);
        mTitleBar.addBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitleBar.setTitle(R.string.ez_device_upgrade);
    }

    private void showButtonTab() {
        mUpgradeLL.setVisibility(View.VISIBLE);
        mUpgradeButton.setEnabled(true);
        mUpgradeProgressLL.setVisibility(View.GONE);
    }
    private void showProgressTab() {
        mUpgradeLL.setVisibility(View.GONE);
        mUpgradeProgressLL.setVisibility(View.VISIBLE);
    }
    private void showUpgradeSuccess() {
        showButtonTab();
        mUpgradeButton.setEnabled(false);
        mUpgradeButton.setText("升级成功");
    }
    private void showUpgradeFailure() {
        showButtonTab();
        mUpgradeButton.setEnabled(false);
        mUpgradeButton.setText("升级失败");
    }
    private void showIsLatestVersion() {
        showButtonTab();
        mUpgradeButton.setEnabled(true);
        mUpgradeButton.setText("升级");
    }
    private void showIsLatestVersion2() {
        showButtonTab();
        mUpgradeButton.setEnabled(true);
        mUpgradeButton.setText("已是最新版本");
    }

    private int mStatus = -2;
    private void checkUpgradeStatusPeriodical() {
        final Runnable runit = new Runnable() {
            @Override
            public void run() {

                LogUtil.i(TAG, "checkUpgradeStatusPeriodical: status: " + mStatus);
                switch (mStatus) {
                    case 0://正在升级
                        showProgressTab();
                        mProgressTextView.setText("升级进度: " + mUpgradeStatus.getUpgradeProgress() + "%");
                        mTimer = new Timer();
                        mTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                checkUpgradeStatusPeriodical();
                            }
                        }, TIMER_PERIODS);
                        break;
                    case 1://设备重启
                        if(mUpgradeStatus != null && mUpgradeStatus.getUpgradeProgress() == 100) {
                            Toast.makeText(EZUpgradeDeviceActivity.this, "升级成功，设备正在重启", Toast.LENGTH_SHORT).show();
                            mProgressTextView.setText("升级成功，设备正在重启: " + mUpgradeStatus.getUpgradeProgress() + "%");
                        }
                        showProgressTab();
                        break;
                    case 2://升级成功
//                            showUpgradeSuccess();
                        showButtonTab();
                        break;
                    case 3://升级失败
                        showUpgradeFailure();
                        break;
                    case -1://升级失败
                        showIsLatestVersion();
                        break;
                    default:
                        showIsLatestVersion();
                        break;
                }
            }
        };

        Thread thr = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mUpgradeStatus = BaseApplication.getOpenSDK().getDeviceUpgradeStatus(mDeviceSerial);
                    mStatus = mUpgradeStatus.getUpgradeStatus();
                    LogUtil.i(TAG, "checkUpgradeStatusPeriodical: status: " + mStatus);
                } catch (BaseException e) {
                    e.printStackTrace();

                    ErrorInfo errorInfo = (ErrorInfo) e.getObject();
                    LogUtil.debugLog(TAG, errorInfo.toString());

                    return;
                }
                runOnUiThread(runit);
            }
        });
        thr.start();
    }

    private void startUpgrade() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        Thread thr = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BaseApplication.getOpenSDK().upgradeDevice(mDeviceSerial);
                    mTimer = new Timer();
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            checkUpgradeStatusPeriodical();
                        }
                    }, TIMER_PERIODS);
                } catch (BaseException e) {
                    e.printStackTrace();

                    ErrorInfo errorInfo = (ErrorInfo) e.getObject();
                    LogUtil.debugLog(TAG, errorInfo.toString());
                }
            }
        });
        thr.start();
    }

    private void showVersionViewOnce() {
        LogUtil.i(TAG, "Enter showVersionViewOnce: ");
        Thread thr = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mVersion = BaseApplication.getOpenSDK().getDeviceVersion(mDeviceSerial);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 版本描述
                            if (mVersion != null) {
                                mVersionDescTextView.setText(mVersion.getUpgradeDesc());
                                checkUpgradeStatusPeriodical();
                            }

                        }
                    });
                } catch (BaseException e) {
                    e.printStackTrace();
                    ErrorInfo errorInfo = (ErrorInfo) e.getObject();
                    LogUtil.debugLog(TAG, errorInfo.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EZUpgradeDeviceActivity.this, "获取设备版本信息失败", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });
        thr.start();
    }
}
