package com.lcb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.lcb.Ezviz.act.EZCameraListActivity;
import com.lcb.R;
import com.lcb.base.BaseApplication;
import com.lcb.socket.SocketActivity;
import com.lcb.utils.Logs;
import com.lcb.utils.SmallUtil;
import com.lcb.wifi.WifiApActivity;
import com.lcb.wifi.WifiListActivity;

public class OneFragment extends Fragment implements OnClickListener {
    private View view;
    private Button Scanf, HotSpots, socket;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_one, container, false);
        initView();
        return view;
    }

    private void initView() {
        Scanf = (Button) view.findViewById(R.id.bt_scan_wifi);
        HotSpots = (Button) view.findViewById(R.id.bt_start_ap);
        Scanf.setOnClickListener(this);
        HotSpots.setOnClickListener(this);
        socket = (Button) view.findViewById(R.id.bt_socket);
        socket.setOnClickListener(this);
        view.findViewById(R.id.bt_ezviz).setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_scan_wifi:
                SmallUtil.getActivity(getActivity(), WifiListActivity.class);
                break;

            case R.id.bt_start_ap:
                SmallUtil.getActivity(getActivity(), WifiApActivity.class);
                break;
            case R.id.bt_socket:
                SmallUtil.getActivity(getActivity(), SocketActivity.class);
                break;
            case R.id.bt_ezviz:
                ezviz();
                break;
        }
    }

    private void ezviz() {
        String token = BaseApplication.sp.getString("萤石摄像头的秘钥");
        Logs.d("onefrgment 74  " + token);
        BaseApplication.getOpenSDK().setAccessToken(token);
        Intent toIntent = new Intent(getActivity(), EZCameraListActivity.class);
        toIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(toIntent);
    }

}