package com.lcb.Ezviz;

import com.lcb.R;

/**
 * Created by Administrator on 2017/6/23.
 */

public enum DeviceCategory {
    IP_CAMERA(R.string.add_device);
//    IP_CAMERA(R.string.ip_camera), DIGITAL_VIDEO_RECORDER(R.string.digital_video_recorder), VIDEO_BOX(
//            R.string.video_box), ALARM_BOX(R.string.alarm_box), NETWORK_VIDEO_RECORDER(R.string.network_video_recorder), ROUTER(
//            R.string.router);

    private int textResId;

    private DeviceCategory(int textResId) {
        this.textResId = textResId;
    }

    public int getTextResId() {
        return textResId;
    }
}