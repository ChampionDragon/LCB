package com.lcb.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.net.wifi.WifiManager.MulticastLock;
import android.util.Log;

import com.lcb.base.BaseApplication;
import com.lcb.constant.Constant;

public class SocketUDP {

	private static MulticastLock lock;
	static String tag = "lcb";
	static DatagramSocket socket = null;
	static Socket sockets = null;
	static int port = Constant.udpClientPort;
	static int portSvr = Constant.udpServerPort;
	static String ipSvr = Constant.udpIP;

	/**
	 * 通过UDP发送指令
	 */
	public static void Send(String s) {
		lock = BaseApplication.lock;
		try {
			lock.acquire();
			if (socket == null) {
				// 创建DatagramSocket对象并指定一个端口号，注意，如果客户端需要接收服务器的返回数据,
				// 还需要使用这个端口号来receive，所以一定要记住
				socket = new DatagramSocket(port);

			}
			// 使用InetAddress(或Inet4Address).getByName把IP地址转换为网络地址
			InetAddress serverAddress = InetAddress.getByName(Constant.udpIP);
			if (socket.isClosed()) {
				socket = new DatagramSocket(port);
				socket.connect(serverAddress, portSvr);
				// 第二种连接方式
				// SocketAddress socketAddress=new
				// InetSocketAddress(serverAddress, port);
				// socket.connect(socketAddress);
			}
			String str = s;// 设置要发送的报文
			byte data[] = str.getBytes();// 把字符串str字符串转换为字节数组
			// 创建一个DatagramPacket对象，用于发送数据。
			// 参数一：要发送的数据 参数二：数据的长度 参数三：服务端的网络地址 参数四：服务器端端口号
			DatagramPacket packet = new DatagramPacket(data, data.length,
					serverAddress, Constant.udpServerPort);
			socket.send(packet);// 把数据发送到服务端。
			Log.w("lcb",
					"udp42  " + "localport  " + socket.getLocalPort()
							+ "  port   " + socket.getPort()
							+ "  InetAddress  " + socket.getInetAddress()
							+ "  LocalAddress " + socket.getLocalAddress());
			Recevice();
			lock.release();
		} catch (SocketException e) {
			e.printStackTrace();
			Log.w("lcb", "udp44  " + e.getMessage());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			Log.w("lcb", "udp47   " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.w("lcb", "udp50   " + e.getMessage());
		}

	}

	/**
	 * 接收信息
	 */
	public static String Recevice() {
		String result = "";
		lock = BaseApplication.lock;
		byte data[] = new byte[1024];
		try {
			lock.acquire();

			// 解决bind failed: EADDRINUSE (Address already in use)
			if (socket == null) {
				socket = new DatagramSocket(null);
				socket.setReuseAddress(true);
				socket.bind(new InetSocketAddress(Constant.udpServerPort));
			}
			// socket.setBroadcast(true);

			// 参数一:要接受的data 参数二：data的长度
			DatagramPacket packet = new DatagramPacket(data, data.length);
			socket.receive(packet);
			// 把接收到的data转换为String字符串
			result = new String(packet.getData(), packet.getOffset(),
					packet.getLength());
			Log.w("lcb", "udp80  " + result + " port " + socket.getLocalPort()
					+ "   serverPORT  " + packet.getPort() + "     serverIP"
					+ packet.getAddress());
			// socket.disconnect();
			socket.close();
			socket = null;
			lock.release();
		} catch (SocketException e) {
			e.printStackTrace();
			Log.w("lcb", "udp72  " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.w("lcb", "udp75  " + e.getMessage());
		}
		return result;
	}

	public void colse() {
		socket.close();// 不使用了记得要关闭
	}

}
