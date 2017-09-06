package com.lcb.guide;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.lcb.MainActivity;
import com.lcb.R;
import com.lcb.account.Login;
import com.lcb.base.BaseActivity;
import com.lcb.base.BaseApplication;
import com.lcb.constant.SpKey;
import com.lcb.utils.SmallUtil;
import com.lcb.utils.SystemUtil;

public class Welcome extends BaseActivity {
    public int DELAYTIME = 2000;
    public static final int MAIN = 0;
    public static final int LOGIN = 1;
    public static final int GUIDE = 2;
    boolean isFisrt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_welcome);
        super.onCreate(savedInstanceState);
        isFisrt = BaseApplication.sp.getBoolean(SpKey.isFirst, true);
        int curVer = SystemUtil.VersionCode();
        int preVer = BaseApplication.sp.getInt(SpKey.preVer);
        boolean isLogin = BaseApplication.sp.getBoolean(SpKey.isLogin);
        if (isFisrt || curVer > preVer) {
            BaseApplication.sp.putInt(SpKey.preVer, curVer);
            handler.sendEmptyMessageDelayed(GUIDE, DELAYTIME);
        } else if (!isLogin) {
            handler.sendEmptyMessageDelayed(LOGIN, DELAYTIME);
        } else {
            handler.sendEmptyMessageDelayed(MAIN, DELAYTIME);
        }

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MAIN:
				SmallUtil.getActivity(Welcome.this, MainActivity.class);


//                    String token = BaseApplication.sp.getString("萤石摄像头的秘钥");
//                    Logs.d("welcome 53  " + token);
//                    BaseApplication.getOpenSDK().setAccessToken(token);
//                    Intent toIntent = new Intent(Welcome.this, EZCameraListActivity.class);
//                    toIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(toIntent);


                    finish();
                    break;
                case LOGIN:
                    SmallUtil.getActivity(Welcome.this, Login.class);
                    finish();
                    break;
                case GUIDE:
                    SmallUtil.getActivity(Welcome.this, Guide.class);
                    finish();
                    break;

            }

        }
    };

}
