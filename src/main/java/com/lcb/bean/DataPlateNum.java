package com.lcb.bean;

import java.util.ArrayList;

/**
 * Description: 存储车牌号码的数据
 * AUTHOR: Champion Dragon
 * created at 2017/10/23
 **/

public class DataPlateNum {
    public static void initTwo(ArrayList<String> one, ArrayList<ArrayList<String>> two) {
        setPrivince(one);
        two.add(getcity());
        two.add(getPrivince());
    }

    /*设置省份缩写的集合*/
    public static void setPrivince(ArrayList<String> a) {
        a.add("赣");
        a.add("粤");
        a.add("京");
        a.add("沪");
        a.add("鄂");
        a.add("湘");
        a.add("川");
        a.add("渝");
        a.add("闽");
        a.add("晋");
        a.add("黑");
        a.add("津");
        a.add("浙");
        a.add("豫");
        a.add("贵");
        a.add("青");
        a.add("琼");
        a.add("宁");
        a.add("蒙");
        a.add("吉");
        a.add("冀");
        a.add("苏");
        a.add("皖");
        a.add("桂");
        a.add("云");
        a.add("陕");
        a.add("甘");
        a.add("藏");
        a.add("新");
        a.add("辽");
        a.add("鲁");
    }

    /*返回城市字母缩写*/
    public static  ArrayList<String> getcity() {
        ArrayList<String> c = new ArrayList();
        c.add("A");
        c.add("B");
        c.add("C");
        c.add("D");
        c.add("E");
        c.add("F");
        c.add("G");
        c.add("H");
        c.add("I");
        c.add("J");
        c.add("K");
        c.add("L");
        c.add("M");
        c.add("N");
        c.add("O");
        c.add("P");
        c.add("Q");
        c.add("R");
        c.add("S");
        c.add("T");
        c.add("U");
        c.add("V");
        c.add("W");
        c.add("X");
        c.add("Y");
        c.add("Z");
        return c;
    }

    /*返回省份缩写的集合*/
    public static ArrayList<String> getPrivince() {
        ArrayList<String> a = new ArrayList();
        a.add("赣");
        a.add("粤");
        a.add("京");
        a.add("沪");
        a.add("鄂");
        a.add("湘");
        a.add("川");
        a.add("渝");
        a.add("闽");
        a.add("晋");
        a.add("黑");
        a.add("津");
        a.add("浙");
        a.add("豫");
        a.add("贵");
        a.add("青");
        a.add("琼");
        a.add("宁");
        a.add("蒙");
        a.add("吉");
        a.add("冀");
        a.add("苏");
        a.add("皖");
        a.add("桂");
        a.add("云");
        a.add("陕");
        a.add("甘");
        a.add("藏");
        a.add("新");
        a.add("辽");
        a.add("鲁");
        return a;
    }


}
