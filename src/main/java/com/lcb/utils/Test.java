package com.lcb.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.util.Log;

import com.lcb.bean.DeviceBean;
import com.lcb.bean.DeviceManagerBean;
import com.lcb.constant.Constant;
import com.lcb.db.DbManager;

public class Test {
	String tag = "lcb";
	DbManager manager;
	List<DeviceBean> list2;

	private void text2(List<DeviceBean> list) {
		Map<String, List<DeviceBean>> map = new HashMap<>();
		List<DeviceBean> deviceBeans = null;
		List<DeviceManagerBean> deviceManagerBeans = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			DeviceBean bean = list.get(i);
			String key = "";
			String data = TimeUtil.long2time(bean.getCreattime(),
					Constant.cformatD);
			boolean b = key.equals(data);
			if (!b) {
				deviceBeans = new ArrayList<DeviceBean>();
				key = data;
			}
			deviceBeans.add(bean);
			map.put(key, deviceBeans);
		}
		Set<Entry<String, List<DeviceBean>>> entrySet = map.entrySet();
		for (Entry<String, List<DeviceBean>> entry : entrySet) {
			Log.d(tag, entry.getKey() + "   " + entry.getValue());
			deviceManagerBeans.add(new DeviceManagerBean(entry.getKey(), entry
					.getValue()));
		}
	}

	private void text1() {
		// manager.cleanError(DbHelper.DEVICE_ERROR_ID + " in(2,3)", null);//
		// 同事删除两个id
		for (DeviceBean bean : list2) {
			Log.d(tag,
					bean.getId()
							+ "   "
							+ bean.getDeviceName()
							+ "  "
							+ bean.getDeviceControl()
							+ "   "
							+ TimeUtil.long2time(bean.getCreattime(),
									Constant.formatsecond));
		}

	}

	private void test() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("lcb", "haha");
		map.put("cyp", "lala");
		map.put("lxp", "huhu");
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			String value = map.get(key);
			Log.d(tag, key + "  " + value);
		}
		Set<Entry<String, String>> entrySet = map.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String key = entry.getKey();
			String value = entry.getValue();
			Log.e(tag, key + "  " + value);
		}

	}
}
