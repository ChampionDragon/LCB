package com.lcb.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lcb.R;
import com.lcb.base.BaseActivity;
import com.lcb.constant.Constant;
import com.lcb.socket.UDPThread;
import com.lcb.utils.Logs;
import com.lcb.utils.TimeUtil;

import static com.lcb.socket.UDPThread.UDPSuccess;

/**
 * Description: UDP测试类
 * AUTHOR: Champion Dragon
 * created at 2017/12/13
 **/
public class UdpActivity extends BaseActivity implements View.OnClickListener {
    private EditText et;
    private TextView tv;
    private UDPThread udpThread;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UDPThread.UDPReceive:
                    StringBuffer sb = new StringBuffer();
                    Bundle data = msg.getData();
                    String ip = (String) data.get(UDPThread.KEYUDPRECIP);
                    Integer port = (Integer) data.get(UDPThread.KEYUDPRECPORT);

                    String receiver = data.getString(UDPThread.KEYUDPRECEIVE);

                    sb.append("ip: " + ip).append("\n").append("port: " + port).append("\n")
                            .append("返回的数据:").append("\n")
                            .append(receiver).append("\n");
                    Logs.d(sb.toString());//后台返回的字符串
                    tv.setText(tv.getText().toString() + sb);
                    break;
                case UDPSuccess:
                    StringBuffer success = new StringBuffer();
                    String str = (String) msg.obj;
                    success.append(str).append("\n").append(TimeUtil.getSystem(Constant.cformatsecond)).append("\n");
                    tv.setText(tv.getText().toString() + success);
                    break;
                case UDPThread.UDPFail:
                    StringBuffer fail = new StringBuffer();
                    String s = (String) msg.obj;
                    fail.append(s).append("\n").append(TimeUtil.getSystem(Constant.cformatsecond)).append("\n");
                    tv.setText(tv.getText().toString() + fail);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udp);
        initView();
        udpThread = new UDPThread(handler);
    }

    private void initView() {
        et = (EditText) findViewById(R.id.udpEt);
        tv = (TextView) findViewById(R.id.udpTv);
        findViewById(R.id.udpreceive).setOnClickListener(this);
        findViewById(R.id.udpsend).setOnClickListener(this);
        findViewById(R.id.udpstop).setOnClickListener(this);
        findViewById(R.id.back_udp).setOnClickListener(this);
    }

    String send;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.udpreceive:
                udpThread.startReceive();
                break;
            case R.id.udpsend:
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        send= udpThread.send(et.getText().toString());
                        Message msg = handler.obtainMessage(UDPSuccess);
                        msg.obj =send;
                        handler.sendMessage(msg);
                    }
                });
                break;
            case R.id.udpstop:
                String s = udpThread.stopReceive();
                tv.setText(tv.getText().toString() + s + "\n" + TimeUtil.getSystem(Constant.cformatsecond) + "\n");
                break;
            case R.id.back_udp:
                finish();
                break;
        }
    }


}
