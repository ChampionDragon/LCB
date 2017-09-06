package com.lcb.wifi;

import com.lcb.R;
import com.lcb.bean.WifiBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WifiAdapter extends ArrayAdapter<WifiBean> {
	private int resourceId;
	private Context context;

	public WifiAdapter(Context context, int resource) {
		super(context, resource);
		resourceId = resource;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		WifiBean wifiInfo = getItem(position);
		View view = LayoutInflater.from(context).inflate(resourceId, null);
		TextView key = (TextView) view.findViewById(R.id.wifiKey);
		TextView value = (TextView) view.findViewById(R.id.wifiValue);
		key.setText(wifiInfo.getKey());
		value.setText(wifiInfo.getValue());
		return view;
	}
}
