package com.lcb.socket;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * 添加Socket服务
 */
public class SocketService extends Service {
	private SocketBinder socketBinder = new SocketBinder();
	private String TAG = "lcb";

	public class SocketBinder extends Binder {
		public SocketService getService() {
			Log.i(TAG, "getService");
			return SocketService.this;
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
