package com.lcb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库帮助类
 * 
 * @author lcb
 * @date 2017-5-8
 */
public class DbHelper extends SQLiteOpenHelper {
	public final static String TABLE_DEVICE_CONTROL = "deviceControl";
    public final static String DEVICE_CONTROL_ID = "id";
    public final static String DEVICE_CONTROL_time = "createTime";
    public final static String DEVICE_CONTROL = "someThing";


    public static DbHelper mInstance;
    private SQLiteDatabase db;
    private String TAG = "lcb";

    public DbHelper(Context context, String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion);
    }

    public static DbHelper getInstance(Context context, String dbName, int dbVersion) {
        if (mInstance == null) {
            mInstance = new DbHelper(context, dbName, dbVersion);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_DEVICE_CONTROL
                + "(_id integer PRIMARY KEY AUTOINCREMENT,"
                + DEVICE_CONTROL_ID + " integer,"
                + DEVICE_CONTROL + " varchar,"
                + DEVICE_CONTROL_time + " long)";
        Log.i(TAG, "数据库 " + TABLE_DEVICE_CONTROL + " 创建成功");
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == newVersion) {
            return;
        }//版本变了重新建立数据库
        db.execSQL("drop table if exists " + TABLE_DEVICE_CONTROL);
        onCreate(db);
    }

    /**
     * 插入数据
     */
    public long insert(String tableName, ContentValues contentValues) {
        if (db == null) {
            db = getWritableDatabase();
        }
        return db.insert(tableName, null, contentValues);
    }

    /**
     * 删除数据
     */
    public int delete(String table, String whereClause, String[] whereArgs) {
        if (db == null) {
            db = getWritableDatabase();
        }
        return db.delete(table, whereClause, whereArgs);
    }

    /**
     * 更新数据
     */
    public int update(String table, ContentValues contentValues, String whereClause, String[] whereArgs) {
        if (db == null) {
            db = getWritableDatabase();
        }
        return db.update(table, contentValues, whereClause, whereArgs);
    }

    /**
     * 通过四个参数数据查询
     *
     * @param whereClause where子句，除去where关键字剩下的部分，其中可带？占位符。如没有子句，则为null。
     * @param whereArgs   用于替代whereClause参数中？占位符的参数。如不需传入参数，则为null。
     */
    public Cursor query(String table, String[] columns, String whereClause, String[] whereArgs) {
        if (db == null) {
            db = getWritableDatabase();
        }
        return db.query(table, columns, whereClause, whereArgs, null, null, null);
    }


    /**
     * 通过sql语句查询数据
     */
    public Cursor rawQuery(String sql, String[] whereArgs) {
        if (db == null) {
            db = getWritableDatabase();
        }
        return db.rawQuery(sql, whereArgs);
    }

    /**
     * 向数据库写语句
     */
    public void execSql(String sql) {
        if (db == null) {
            db = getWritableDatabase();
        }
        db.execSQL(sql);
    }


    /**
     * 关闭数据库
     */
    public void close() {
        if (db == null) {
            db = getWritableDatabase();
        }
        db.close();
        db = null;
    }
}
