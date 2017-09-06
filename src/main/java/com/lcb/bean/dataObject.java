package com.lcb.bean;

/**
 * 圆点变化趋势曲线图的BEAN类
 * 作者 Champion Dragon
 * created at 2017/7/24
 **/

public class dataObject {
    String happenTime;
    float num;

    public dataObject(String happenTime, float num) {
        this.happenTime = happenTime;
        this.num = num;
    }

    public String getHappenTime() {
        return happenTime;
    }

    public void setHappenTime(String happenTime) {
        this.happenTime = happenTime;
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }
}
