package com.lcb.utils;

import com.lcb.base.BaseApplication;

import android.content.SharedPreferences;

public class SpUtil {
	private static SpUtil mInstance;
	private static SharedPreferences sp;

	public SpUtil(String filekey, int mode) {
		sp = BaseApplication.context.getSharedPreferences(filekey, mode);
	}

	// 删除指定表直接调用SpUtil.getInstance(唯一文件名,Context.MODE_PRIVATE).clean()
	public static SpUtil getInstance(String filekey, int mode) {
		if (mInstance == null) {
			mInstance = new SpUtil(filekey, mode);
		} else {
			sp = BaseApplication.context.getSharedPreferences(filekey, mode);
		}
		return mInstance;
	}

	// 删除清空指定key的数据
	public void remove(String key) {
		sp.edit().remove(key).commit();
	}

	// 删除指定SharedPreferences表
	public void clean() {
		sp.edit().clear().commit();
	}

	public void putInt(String key, int value) {
		sp.edit().putInt(key, value).commit();
	}

	public void putBoolean(String key, boolean value) {
		sp.edit().putBoolean(key, value).commit();
	}

	public void putFloat(String key, Float value) {
		sp.edit().putFloat(key, value).commit();
	}

	public void putString(String key, String value) {
		sp.edit().putString(key, value).commit();
	}

	public void putLong(String key, long value) {
		sp.edit().putLong(key, value).commit();
	}

	public int getInt(String key) {
		return sp.getInt(key, 0);
	}

	public boolean getBoolean(String key) {
		return sp.getBoolean(key, false);
	}

	public boolean getBoolean(String key, boolean defValue) {
		return sp.getBoolean(key, defValue);
	}

	public float getFloat(String key) {
		return sp.getFloat(key, 0.0f);
	}

	public String getString(String key) {
		return sp.getString(key, "");
	}

	public long getLong(String key) {
		return sp.getLong(key, 0);
	}

}
