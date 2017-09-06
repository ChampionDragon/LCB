package com.lcb.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcb.R;

public class DialogLoadingUtil {
	public static Dialog CreatDialog(Context context, String str) {
		final Dialog dialog = new Dialog(context, R.style.dialog);
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_loading, null);
		TextView tv = ViewHolderUtil.get(view, R.id.load_tv);
		ImageView iv = ViewHolderUtil.get(view, R.id.load_iv);
		if (str != null) {
			tv.setText(str);
		}
		AnimationDrawable animationDrawable = (AnimationDrawable) iv
				.getBackground();
		animationDrawable.start();

		dialog.setContentView(view, new RelativeLayout.LayoutParams(
				dm.widthPixels, dm.heightPixels * 2 / 3));
		dialog.getWindow().setGravity(Gravity.TOP);

		return dialog;
	}

	public static Dialog CreatDialog(Context context) {
		return CreatDialog(context, null);
	}

}
