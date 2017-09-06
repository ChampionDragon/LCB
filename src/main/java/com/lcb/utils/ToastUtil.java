package com.lcb.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.lcb.base.BaseApplication;
import com.lcb.view.MyToast;

public class ToastUtil {
	private static MyToast toast;
	private static Context context = BaseApplication.getInstance();

	public static void showShort(int a) {
		String s = context.getResources().getString(a);
		showShort(s);
	}

	public static void showLong(int a) {
		String s = context.getResources().getString(a);
		showLong(s);
	}

	public static void showShort(CharSequence s) {
		toast = MyToast.makeText(BaseApplication.getInstance(), s,
				Toast.LENGTH_SHORT);
		int height = context.getResources().getDisplayMetrics().heightPixels * 3 / 10;
		toast.setGravity(Gravity.TOP, 0, height);// 起点位置,水平向右位移,垂直向下位移
		toast.show();
	}

	public static void showLong(CharSequence s) {
		int height = context.getResources().getDisplayMetrics().heightPixels * 1 / 10;// 偏移量
		toast = MyToast.makeText(BaseApplication.getInstance(), s,
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP, 0, height);
		toast.show();
	}

	// public static void showShort(String s) {
	// if (toast == null) {
	// toast = Toast.makeText(BaseApplication.getInstance(), s,
	// Toast.LENGTH_SHORT);
	// }
	// toast.setDuration(Toast.LENGTH_SHORT);
	// toast.setText(s);
	// toast.show();
	// }
	//
	// public static void showLong(String s) {
	// if (toast == null) {
	// toast = Toast.makeText(BaseApplication.getInstance(), s,
	// Toast.LENGTH_LONG);
	// }
	// toast.setDuration(Toast.LENGTH_LONG);
	// toast.setText(s);
	// toast.show();
	// }

}
