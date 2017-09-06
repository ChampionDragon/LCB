package com.lcb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lcb.R;
import com.lcb.activity.AnimActivity;
import com.lcb.activity.GetRecordActivity;
import com.lcb.activity.NotificationActivity;
import com.lcb.activity.RefreshRecordActivity;
import com.lcb.activity.SpannableStringActivity;
import com.lcb.activity.VideoActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreeFragment extends Fragment {
    private View view;
    private ListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_three, container, false);
        init();
        return view;
    }

    private void init() {
        lv = (ListView) view.findViewById(R.id.three_lv);
        lv.setAdapter(new SimpleAdapter(getActivity(), getData(),
                android.R.layout.simple_list_item_1, new String[]{"title"},
                new int[]{android.R.id.text1}));
        lv.setOnItemClickListener(itemClickListener);
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();
        addItem(myData, "获取云端备份", new Intent(getActivity(),
                GetRecordActivity.class));
        addItem(myData, "获取云端备份(分日期)", new Intent(getActivity(),
                RefreshRecordActivity.class));
        addItem(myData, "视频界面", new Intent(getActivity(), VideoActivity.class));
        addItem(myData, "动画DEMO", new Intent(getActivity(), AnimActivity.class));
        addItem(myData, "SpannableString", new Intent(getActivity(), SpannableStringActivity.class));
        addItem(myData, "通知DEMO", new Intent(getActivity(), NotificationActivity.class));
        return myData;
    }

    private void addItem(List<Map<String, Object>> data, String name,
                         Intent intent) {
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("title", name);
        temp.put("intent", intent);
        data.add(temp);
    }

    OnItemClickListener itemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Map<String, Object> map = (Map<String, Object>) lv
                    .getItemAtPosition(position);
//			Logs.d(map + "  " + position);
            Intent intent = (Intent) map.get("intent");
            startActivity(intent);
        }
    };

}
