package com.lcb.socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lcb.R;
import com.lcb.base.BaseActivity;
import com.lcb.constant.Constant;
import com.lcb.utils.DialogNotileUtil;
import com.lcb.utils.SmallUtil;
import com.lcb.utils.SoundPoolUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class SocketActivity extends BaseActivity implements OnClickListener {
    private Button send, receive;
    private TextView tv_receive;
    private EditText et_send;
    private final int SUCCESS = 0;
    private SoundPoolUtil instance;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    Bundle data = msg.getData();
                    String s = data.getString("msg");
                    DialogNotileUtil.show(SocketActivity.this,
                            "接收到的数据：             " + s);
                    tv_receive.setText(s);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        initView();
        instance = SoundPoolUtil.getInstance(this);
    }

    private void initView() {
        send = (Button) findViewById(R.id.socket_send);
        send.setOnClickListener(this);
        receive = (Button) findViewById(R.id.socket_receive);
        receive.setOnClickListener(this);
        tv_receive = (TextView) findViewById(R.id.socket_tv_receive);
        et_send = (EditText) findViewById(R.id.socket_et_send);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.socket_send:

                String s = et_send.getText().toString();
                /*判断字符串是否是数字*/
                boolean isNum = SmallUtil.isNumer(s);
                if (isNum) {
                    int a = Integer.valueOf(s);
                    instance.play(a);
                }


//			new Thread(sendRb).start();
                break;
            case R.id.socket_receive:
                new Thread(receiveRb).start();
                break;

        }

    }

    /**
     * 网络操作相关的子线程
     */
    Runnable sendRb = new Runnable() {

        @Override
        public void run() {
            SocketUDP.Send(et_send.getText().toString());
//			new Thread(receiveRb).start();
        }
    };

    Runnable receiveRb = new Runnable() {

        @Override
        public void run() {
//			String s = SocketUDP.Recevice();
//			Bundle bundle = new Bundle();
//			bundle.putString("msg", s);
//			Message msg = mHandler.obtainMessage();
//			msg.setData(bundle);
//			msg.what = SUCCESS;
//			mHandler.sendMessage(msg);
//			receives();
        }

    };

    private void receives() {
        // 接收的字节大小，客户端发送的数据不能超过这个大小
        byte[] buf = new byte[1024];
        // 建立Socket连接
        DatagramSocket datagramSocket = null;
        if (datagramSocket == null) {
            try {
                datagramSocket = new DatagramSocket(null);
                datagramSocket.setReuseAddress(true);
                datagramSocket.bind(new InetSocketAddress(
                        Constant.udpServerPort));
                DatagramPacket datagramPacket = new DatagramPacket(buf,
                        buf.length);
                // 准备接收数据
                datagramSocket.receive(datagramPacket);
                String result = new String(datagramPacket.getData(),
                        datagramPacket.getOffset(), datagramPacket.getLength());
                Log.e("lcb", "socket101   " + result);
                tv_receive.setText(result);
                datagramSocket.close();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

}
