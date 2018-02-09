package com.lcb.socket;

import android.os.Handler;
import android.os.Message;

import com.lcb.utils.Logs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Description: tcp客户端的线程
 * AUTHOR: Champion Dragon
 * created at 2018/1/16
 **/

public class tcpClientThread {
    private Handler handler = null;
    private boolean hasConnect = false;
    private recTcpThread receiveThread = null;
    private Socket socket = null;
    private String serverIpAddress;
    private String tag = "tcpClientThread";
    public static final int MSG_REC = 111;


    public tcpClientThread(String serverIpAddress, Handler handler, int serverPort) {
        this.serverIpAddress = serverIpAddress;
        this.handler = handler;
        this.serverPort = serverPort;
    }

    private int serverPort;


    /*判断是否和服务器连接*/
    public boolean isConnected() {
        return hasConnect;
    }

    public void connect() {
        StringBuilder sb = new StringBuilder();

        try {
            socket = new Socket(serverIpAddress, serverPort);
            hasConnect = true;
            sb.append("连接服务器成功").append("\n");

            Message msg = handler.obtainMessage(MSG_REC);
            msg.obj = sb.toString();//服务器返回的信息
            handler.sendMessage(msg);

        } catch (Exception e) {
            sb.append("服务器连接失败.").append("\n");
            sb.append("失败原因 " + e);

            Logs.e(sb.toString());
            Message msg = handler.obtainMessage(MSG_REC);
            msg.obj = sb.toString();//服务器返回的信息
            handler.sendMessage(msg);
        }
    }

    public String disConnect() {
        StringBuilder sb = new StringBuilder();

        if (hasConnect) {
            try {
                socket.getOutputStream().close();
                socket.close();
                socket=null;
                hasConnect = false;

                sb.append("断开服务器成功").append("\n");
            } catch (IOException e) {
                sb.append("断开服务器失败").append("\n");
                e.printStackTrace();
            }

        }
        return sb.toString();
    }


    public void send(final String msg) {
        if (isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        /*获取输出流，向服务器端发送信息*/
                        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                        byte[] bytes = msg.getBytes();
                        dos.write(bytes, 0, bytes.length);
//                        PrintWriter pw = new PrintWriter(socket.getOutputStream());//将输出流包装成打印流
//                        pw.write(msg);
//                        pw.flush();
//禁用此套接字的输出流。对于 TCP 套接字，任何以前写入的数据都将被发送，并且后跟 TCP 的正常连接终止序列。
// 如果在套接字上调用 shutdownOutput(),后写入套接字输出流，则该流将抛出 IOException。
//                        socket.shutdownOutput();

                        /*获取输入流，并读取服务器端的响应信息*/
//                        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
//                        BufferedReader br = new BufferedReader(isr);

//                        String s = null;
//                        StringBuffer strBuffer = new StringBuffer();
//                        while ((s = br.readLine()) != null) {
//                            strBuffer.append(s);
//                        }
                        String s = "连接断开";
                        DataInputStream in = new DataInputStream(socket.getInputStream());
                        byte[] b = new byte[1024];
                        int length = in.read(b);
                        if (length > 0) {
                            byte[] data = new byte[length];
                            System.arraycopy(b, 0, data, 0, length);
                            s = new String(data);
                            Logs.d(tag+"120 接收到的信息： " + s);
                            Logs.i("服务器端ip " + socket.getInetAddress());
                        }


                        Message msg = handler.obtainMessage(MSG_REC);
                        msg.obj = s;//服务器返回的信息
                        handler.sendMessage(msg);

//                        pw.close();
//                        isr.close();
//                        br.close();


                    } catch (Exception e) {
                        Logs.e(tag + "116 " + e);
                        Message msg = handler.obtainMessage(MSG_REC);
                        msg.obj = "错误原因：" + e;
                        handler.sendMessage(msg);
                    }
                }
            }).start();
        } else {
            Message msg1 = handler.obtainMessage(MSG_REC);
            msg1.obj = "未连接服务器";
            handler.sendMessage(msg1);
        }
    }


    class recTcpThread extends Thread {
        @Override
        public void run() {
            super.run();
        }
    }


}
