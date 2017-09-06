package com.lcb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcb.R;
import com.lcb.bean.RecordBean;
import com.lcb.constant.Constant;
import com.lcb.utils.Logs;
import com.lcb.utils.TimeUtil;
import com.lcb.utils.ViewHolderUtil;

public class RecordAdapter extends ArrayAdapter<RecordBean> {
	private int resourceId;
	private Context context;
	private String date;

	public RecordAdapter(Context context, int resource) {
		super(context, resource);
		resourceId = resource;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context)
					.inflate(resourceId, null);
		}
		TextView type = ViewHolderUtil.get(convertView, R.id.record_type);
		TextView msg = ViewHolderUtil.get(convertView, R.id.record_msg);
		TextView time = ViewHolderUtil.get(convertView, R.id.record_time);
		ImageView iv = ViewHolderUtil.get(convertView, R.id.record_iv);
		TextView tv = ViewHolderUtil.get(convertView, R.id.record_tv);

		RecordBean item = getItem(position);
		String typeStr = item.getType();
		switch (typeStr) {
		case "1":
			typeStr = "道闸门1";
			break;
		case "2":
			typeStr = "道闸门2";
			break;
		case "3":
			typeStr = "平开门";
			break;
		case "4":
			typeStr = "室内平开门";
			break;
		case "5":
			typeStr = "伸缩门";
			break;
		}
		String timeStr = item.getTime();
		long time2long = TimeUtil.time2long(timeStr, Constant.formatsecond);
		
//		String dateStr = TimeUtil.long2time(time2long, Constant.cformatD);
//		long itemId = this.getItemId(position);
//		int count = this.getCount();
//		if (itemId == 0) {
//			tv.setVisibility(View.VISIBLE);
//			if (dateStr.equals(TimeUtil.long2time(System.currentTimeMillis(),
//					Constant.cformatD)))
//				tv.setText("今天");
//			else
//				tv.setText(dateStr);
//		} else {
//			if (!dateStr.equals(date)) {
//				tv.setVisibility(View.VISIBLE);
//				iv.setVisibility(View.VISIBLE);
//				tv.setText(dateStr);
//				date = dateStr;
//			}
//		}

		
		timeStr = TimeUtil.long2time(time2long, Constant.formatsecond);
		String msgStr = item.getTitle();
		type.setText(typeStr);
		msg.setText(msgStr);
		time.setText(timeStr);
		return convertView;
	}

}
