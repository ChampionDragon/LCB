package com.lcb.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lcb.R;

public class MyToast {
	private Toast mToast;

	private MyToast(Context context, CharSequence text, int duration) {
		View v = LayoutInflater.from(context).inflate(R.layout.toast_ui, null);
		TextView textView = (TextView) v.findViewById(R.id.toast_tv);
		textView.setText(text);

		ImageView iv = (ImageView) v.findViewById(R.id.toast_iv);
		AnimationDrawable animationDrawable = (AnimationDrawable) iv
				.getBackground();
		animationDrawable.start();

		// DisplayMetrics dm = context.getResources().getDisplayMetrics();
		// LayoutParams params = new
		// FrameLayout.LayoutParams(dm.widthPixels*2/5,
		// LinearLayout.LayoutParams.WRAP_CONTENT);
		// textView.setLayoutParams(params);

		mToast = new Toast(context);
		mToast.setDuration(duration);
		mToast.setView(v);
	}

	public static MyToast makeText(Context context, CharSequence text,
			int duration) {
		return new MyToast(context, text, duration);
	}

	public void setDuration(int a) {
		if (mToast != null) {
			mToast.setDuration(a);
		}
	}

	// public void setText(String s) {
	// if (mToast != null) {
	// mToast.setText(s);//无法使用因为toast会调用默认的控件
	// }
	// }

	public void show() {
		if (mToast != null) {
			mToast.show();
		}
	}

	public void setGravity(int gravity, int xOffset, int yOffset) {
		if (mToast != null) {
			mToast.setGravity(gravity, xOffset, yOffset);
		}
	}

}
