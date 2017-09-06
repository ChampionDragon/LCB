package com.lcb.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lcb.R;
import com.lcb.bean.DeviceBean;
import com.lcb.constant.Constant;
import com.lcb.utils.TimeUtil;
import com.lcb.utils.ViewHolderUtil;

public class LogItemAdapter extends BaseAdapter {
	private Context context;
	public List<DeviceBean> list;

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_loglv_item, null);
		}
		TextView time = ViewHolderUtil.get(convertView, R.id.log_time);
		TextView control = ViewHolderUtil.get(convertView, R.id.log_control);
		TextView name = ViewHolderUtil.get(convertView, R.id.log_name);
		DeviceBean deviceBean = list.get(position);
		String Timestr = TimeUtil.long2time(deviceBean.getCreattime(),
				Constant.formatminute);
		time.setText(Timestr);
		control.setText(deviceBean.getDeviceControl());
		name.setText(deviceBean.getDeviceName());

		return convertView;
	}

	public LogItemAdapter(Context context, List<DeviceBean> list) {
		super();
		this.context = context;
		this.list = list;
	}

	/**
	 * 添加刷新的数据
	 */
	public void setData(List<DeviceBean> scanResults) {
		list = scanResults;
	}
}
