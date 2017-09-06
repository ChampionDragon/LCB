package com.lcb.constant;

import android.os.Environment;

import com.lcb.utils.SystemUtil;

import java.io.File;

/**
 * 常量类
 */
public class Constant {
    /* 网站 */
    public final static String urlBaidu = "https://www.baidu.com/";
    public final static String urlSina = "http://www.sina.com.cn/";
    public final static String urlBaiSheng = "https://jxbsmk.1688.com/";

    /*更新APK的网址*/
    public final static String apkUpdate=
//"http://120.203.0.218:30000/img/1.png";
"http://releases.b0.upaiyun.com/hoolay.apk";


    /* 设备连接 */
    public final static String udpIP = "120.203.0.218";
    // "116.62.180.134";
    public final static int udpServerPort = 30066;
    public final static int udpClientPort = 8866;
    public final static int tcpServerPort = 8080;
    public final static int tcpClientPort = 0;

    public final static String idONE = "AE0067BD11";

    /* 文件夹 */
    // 整个项目的目录
    public final static File fileDir = new File(
            Environment.getExternalStorageDirectory(), SystemUtil.AppName());


    /* 数据库 */
    public final static String dbDiveceBsmk = "BSMK";
    public final static int dbVersion = 1;

    /* 时间格式 */
    public final static String cformatDay = "yyyy年MM月dd日";
    public final static String cformatD = "MM月dd日";
    public final static String cformatsecond = "yyyy年MM月dd日HH时mm分ss秒";
    public final static String formatminute = "HH:mm";
    public final static String formatsecond = "yyyy-MM-dd HH:mm:ss";
    public final static String formats = "mm:ss";
    public final static String formatday = "yyyy-MM-dd";

    /* 测试 */
    public final static String cmdOpen = "gate=1";
    public final static String cmdClose = "gate=0";
    public final static String cmdStop = "gate=2";

    /* 登录模块 */
    public final static String urlLogin =
            "http://www.bsznyun.com/wifi/ios/get_user_ios.php";
    //            "http://www.bsznyun.com/wifi/get_user_ios.php";
    public final static String urlGetRecord = "http://120.203.0.218:30000/wifi/get_records_ios.php";
    public final static String key = "5tZwmk3TIaJ4ELVHzN";

    /* 视频 */
    public final static String viedoUrl =
            "http://yxfile.idealsee.com/9f6f64aca98f90b91d260555d3b41b97_mp4.mp4";
//    "rtmp://live.hkstv.hk.lxdns.com/live/hks";


    /*某个声音所代表的值*/
    public final static int CLOSED = 1;//关门完成
    public final static int CLOSING = 2;//正在关门
    public final static int OPENED = 3;//开门完成
    public final static int OPENING = 4;//正在开门
    public final static int OPENING2 = 5;//正在开门,先按停止才能开门
    public final static int STOP = 6;//停止
    public final static int DOERROR = 7;//操作失败
    public final static int BEEP = 8;//铃声
    public final static int BINDCAMERA = 9;//请绑定摄像头到门控设备
    public final static int ID = 10;//请先绑定门控设备ID
    public final static int LOGIN = 11;//请登录
    public final static int LOGINED = 12;//登录成功
    public final static int LOGINFAIL = 13;//登录失败
    public final static int OFFLINE = 14;//设备离线
    public final static int PAIZHAO = 15;//拍照的声音
    public final static int TIMEOUT = 16;//接收超时
    public final static int WIFI = 17;//请添加设备按一键添加WIFI


}
