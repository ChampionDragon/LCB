package com.lcb.socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.lcb.base.BaseApplication;
import com.lcb.utils.Logs;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Description: UDP连接的线程
 * AUTHOR: Champion Dragon
 * created at 2017/12/8
 **/

public class UDPThread {
    private String tag = "UDPThread";
    private int port = 6789;//自己作为服务器监听的端口
    private DatagramSocket dSocket;
    private Handler handler = null;
    private boolean hasStartReceive = false;
    public static final int UDPFail = 23;
    public static final int UDPReceive = 24;
    public static final int UDPSuccess = 25;
    public static final String KEYUDPRECIP = "KEY_STRING_UDPRECIP";
    public static final String KEYUDPRECPORT = "KEY_INT_UDPRECPORT";
    public static final String KEYUDPRECEIVE = "KEY_STRING_UDPRECEIVE";
    private ReceiveThread receiveThread = null;
    private int recvPort = 110;//客户端的端口
    private InetAddress addr;//客户端ip地址

    /*判断是否断开接收*/
    public boolean isReceive() {
        return hasStartReceive;
    }

    public UDPThread(Handler handler) {
        this.handler = handler;
    }

    public String send(String str) {
        Logs.v(addr + "  " + recvPort+" 数据："+str);
        String result = "发送失败";
        if (!hasStartReceive) {
            result = "请先接收,再发送";
        } else if (str.getBytes().length <= 0) {
            result = "发送数据不能为空";
        } else if (addr == null || recvPort == 110) {
            result = "未接收到客户端的ip和端口";
        } else {
            byte[] sendBuf = str.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, addr, recvPort);
            try {
                dSocket.send(sendPacket);
                result = "发送成功";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public String stopReceive() {
        if (!hasStartReceive) {
            return "已经断开，不用频繁操作";
        }

        hasStartReceive = false;
        dSocket.close();

                /*关闭线程*/
        if (receiveThread != null && receiveThread.isAlive()) {
            receiveThread.interrupt();
            receiveThread = null;
        }

        return "断开成功";
    }


    /*开始接收*/
    public void startReceive() {
        if (isReceive()) {
            return;
        }

        if (receiveThread == null) {
            receiveThread = new ReceiveThread();
            receiveThread.start();
        }
        hasStartReceive = true;
        /*返回开始接收成功的信号*/
        Message msg = handler.obtainMessage(UDPSuccess);
        String sussess = "开始接收";
        msg.obj = sussess;
        handler.sendMessage(msg);
    }


    class ReceiveThread extends Thread {
        @Override
        public void run() {
            try {
                dSocket = new DatagramSocket(port);
            } catch (SocketException e) {
                e.printStackTrace();
                Logs.e(dSocket+" dSocket对象 "+e);
            }
            byte[] recvBuf = new byte[128];
            final DatagramPacket dPacket = new DatagramPacket(recvBuf, recvBuf.length);

            try {
                while (hasStartReceive) {
                    BaseApplication.lock.acquire();

                    Logs.d(dSocket+"  "+receiveThread);

                    dSocket.receive(dPacket);//接收数据包
                    BaseApplication.lock.release();
                    /*开始做回应*/
                    recvPort = dPacket.getPort();//客户端的端口
                    addr = dPacket.getAddress();//客户端的ip地址
                    String strRecv = new String(dPacket.getData(), 0, dPacket.getLength());
                    Logs.d("udpthread111 端口  " + recvPort + " ip  " + addr);

                    /*返回接收的数据*/
                    Message msg = handler.obtainMessage(UDPReceive);
                    Bundle bundle = new Bundle();
                    bundle.putString(KEYUDPRECIP, addr.getHostAddress());
                    bundle.putInt(KEYUDPRECPORT, recvPort);
                    bundle.putString(KEYUDPRECEIVE, strRecv);
                    msg.setData(bundle);
                    handler.sendMessage(msg);


                    /*发送数据*/
                    byte[] sendBuf = "getData  lcb".getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, addr, recvPort);
                    dSocket.send(sendPacket);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Logs.e(tag + "  147");
            }

        }
    }


}
