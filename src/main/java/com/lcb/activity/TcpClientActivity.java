package com.lcb.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lcb.R;
import com.lcb.base.BaseActivity;
import com.lcb.socket.tcpClientThread;
import com.lcb.utils.Logs;

import org.json.JSONException;
import org.json.JSONObject;

public class TcpClientActivity extends BaseActivity implements View.OnClickListener {
    private Button send, close, sendByTime;
    private EditText edtSvrIpAddress, edtSvrPort;
    private TextView txtResult;
    private tcpClientThread tcThread;
    private JSONObject jo;
    private JSONObject params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp_client);
        initView();
    }

    private void initView() {
        send = (Button) findViewById(R.id.btnOpenDevice);
        send.setOnClickListener(this);
        close = (Button) findViewById(R.id.btnCloseDevice);
        close.setOnClickListener(this);
        sendByTime = (Button) findViewById(R.id.btnTestRes);
        sendByTime.setOnClickListener(this);
        edtSvrIpAddress = (EditText) findViewById(R.id.edtSvrIpAddress);
        edtSvrPort = (EditText) findViewById(R.id.edtSvrPort);
        txtResult = (TextView) findViewById(R.id.txtResult);
        findViewById(R.id.back_clientcontrol).setOnClickListener(this);
        findViewById(R.id.btnConnect).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_clientcontrol:
                finish();
                break;
            case R.id.btnOpenDevice:
                jo = new JSONObject();
                try {
                    jo.put("signway", "diaCreate");
                    params = new JSONObject();
                    params.put("dialogCode", "弹框测试成功");
                    params.put("dialogString", "弹框测试成功");
                    jo.put("params", params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendMsg(jo.toString());
                jo = null;
                break;
            case R.id.btnCloseDevice:
                jo = new JSONObject();
                try {
                    jo.put("signway", "diaDis");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendMsg(jo.toString());
                jo = null;
                break;
            case R.id.btnTestRes:
                jo = new JSONObject();
                try {
                    jo.put("signway", "diaCreateByTime");
                    params = new JSONObject();
                    params.put("dialogCode", "定时弹框测试成功");
                    params.put("dialogString", "定时弹框测试成功");
                    params.put("dialogDismissTime", "5");
                    jo.put("params", params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendMsg(jo.toString());
                jo = null;
                break;
            case R.id.btnConnect:
                connect();
                break;


        }
    }

    /* 向服务器发消息*/
    private void sendMsg(String ss) {
        if (tcThread == null) {
            txtResult.setText(txtResult.getText().toString() + "线程未开启" + "\n"+ "\n");
        } else if (!tcThread.isConnected()) {
            txtResult.setText(txtResult.getText().toString() + "线程未连接" + "\n"+ "\n");
        } else {
            tcThread.send(ss);
            txtResult.setText(txtResult.getText().toString() + "发送的数据：" + ss + "\n"+ "\n");
        }
    }

    /*连接服务器*/
    private void connect() {
        String strIpAddress = edtSvrIpAddress.getText().toString();
        String strPort = edtSvrPort.getText().toString();
        if (strIpAddress.isEmpty()) {
            Toast.makeText(TcpClientActivity.this, "请输入服务端IP地址",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (strPort.isEmpty()) {
            Toast.makeText(TcpClientActivity.this, "请输入服务端端口号",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (tcThread == null) {
            Logs.d("11111111111111111");
            tcThread = new tcpClientThread(strIpAddress, handler, Integer.valueOf(strPort));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    tcThread.connect();
                }
            }).start();

        } else if (tcThread.isConnected()) {
            Logs.d("2222222222222222");
            String s = tcThread.disConnect();
            txtResult.setText(txtResult.getText().toString() + s + "\n");
            tcThread = new tcpClientThread(strIpAddress, handler, Integer.valueOf(strPort));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    tcThread.connect();
                }
            }).start();
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case tcpClientThread.MSG_REC:
                    txtResult.setText(txtResult.getText().toString() + msg.obj + "\n");
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String s = tcThread.disConnect();
        Logs.e(s);
    }
}
