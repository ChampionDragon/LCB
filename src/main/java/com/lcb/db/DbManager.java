package com.lcb.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lcb.bean.DeviceBean;

/**
 * 数据库管理类
 * 
 * @author lcb
 * @date 2017-5-8
 */
public class DbManager {
	public static DbManager mInstance = null;
	private DbHelper mDbHelper = null;
	private Context mContext = null;
	private String tag = "lcb";

	public DbManager(Context context, String dbName, int dbVersion) {
		super();
		mContext = context;
		mDbHelper = DbHelper.getInstance(context, dbName, dbVersion);
	}

	public static DbManager getmInstance(Context context, String dbName,
			int dbVersion) {
		if (mInstance == null) {
			mInstance = new DbManager(context, dbName, dbVersion);
		}
		return mInstance;
	}

	/* 有关BSMK设备的相关数据库操作 */

	/**
	 * 设备的增加或更新
	 */
	public boolean addOrUpdateDevice(int id, String control, long time) {
		ContentValues values = new ContentValues();
		values.put(DbHelper.DEVICE_CONTROL, control);
		values.put(DbHelper.DEVICE_CONTROL_time, time);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		String sql = "select *from " + DbHelper.TABLE_DEVICE_CONTROL
				+ " where " + DbHelper.DEVICE_CONTROL_ID + "=?";
		Cursor cursor = mDbHelper.rawQuery(sql, new String[] { id + "" });
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				mDbHelper.update(DbHelper.TABLE_DEVICE_CONTROL, values,
						DbHelper.DEVICE_CONTROL_ID + "=?", new String[] { id
								+ "" });
				Log.w(tag, "dbm54设备更新成功");
			}

		} else {
			values.put(DbHelper.DEVICE_CONTROL_ID, id);
			mDbHelper.insert(DbHelper.TABLE_DEVICE_CONTROL, values);
			Log.w(tag, "dbm54设备添加成功");
		}
		cursor.close();
		return true;
	}

	/**
	 * 删除设备
	 */
	public boolean cleanDevice(String whereClause, String[] whereArgs) {
		mDbHelper.delete(DbHelper.TABLE_DEVICE_CONTROL, whereClause, whereArgs);
		Log.d(tag, "dbm71删除设备某行成功");
		return true;
	}

	/**
	 * 设备的数据集合
	 */
	public List<DeviceBean> getDeviceList() {
		List<DeviceBean> list = new ArrayList<>();
		Cursor cursor = mDbHelper.query(DbHelper.TABLE_DEVICE_CONTROL, null,
				null, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				DeviceBean device = new DeviceBean();
				device.setCreattime(cursor.getLong(cursor
						.getColumnIndex(DbHelper.DEVICE_CONTROL_time)));
				device.setDeviceControl(cursor.getString(cursor
						.getColumnIndex(DbHelper.DEVICE_CONTROL)));
				device.setId(cursor.getInt(cursor
						.getColumnIndex(DbHelper.DEVICE_CONTROL_ID)));
				list.add(device);
				Log.e(tag, "dbm88返回数据集合成功       cursor" + cursor.getCount());
			}
		}
		cursor.close();
		return list;
	}

	/**
	 * 指定的设备数据
	 */
	public DeviceBean getDevice(int id) {
		DeviceBean device = new DeviceBean();
		String sql = "select *from " + DbHelper.TABLE_DEVICE_CONTROL
				+ " where " + DbHelper.DEVICE_CONTROL_ID + "=?";
		Cursor cursor = mDbHelper.rawQuery(sql, new String[] { id + "" });
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				device.setCreattime(cursor.getLong(cursor
						.getColumnIndex(DbHelper.DEVICE_CONTROL_time)));
				device.setDeviceControl(cursor.getString(cursor
						.getColumnIndex(DbHelper.DEVICE_CONTROL)));
				device.setId(cursor.getInt(cursor
						.getColumnIndex(DbHelper.DEVICE_CONTROL_ID)));
				Log.i(tag, "dbm106返回指定数据成功");
			}
		}
		cursor.close();
		;
		return device;
	}

	/**
	 * 删除所有表
	 */
	public void cleanAll() {
		cleanDevice(null, null);
		Log.v(tag, "dbm120数据库所有表删除成功");
	}
}
