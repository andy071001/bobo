package com.svo.love.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static DBHelper dbHelper;
	private static int version = 3;
	public DBHelper(Context context) {
		super(context, "love.db", null, version);
	}
	
	public static DBHelper getInstance(Context context) {
		if (dbHelper == null) {
			dbHelper = new DBHelper(context);
		}
		return dbHelper;
	}
	
	String sql1 = "CREATE TABLE IF NOT EXISTS  send([_id] integer PRIMARY KEY AUTOINCREMENT,[send_num] text,[send_time] text,[title] text,[subhead] text,[content] text,[send_type] text,[deviceId] text)";
	
	String sql2 = "CREATE TABLE IF NOT EXISTS  rece([_id] integer PRIMARY KEY AUTOINCREMENT,[send_time] text,[rece_time] text,[title] text,[subhead] text,[content] text,[user_deviceId] text,[user_name] text,[age] text,[sex] text,[qianMing] text,[city] text,[icon_url] text,[send_type] text,[isRead] text)";
	//下面两张表通过设备ID相关联
	String sql3 = "CREATE TABLE IF NOT EXISTS  friend([_id] integer PRIMARY KEY AUTOINCREMENT,[user_deviceId] text unique,[user_name] text,[age] text,[sex] text,[qianMing] text,[city] text,[icon_url] text)";
	String sql4 = "CREATE TABLE IF NOT EXISTS  friend_msg([_id] integer PRIMARY KEY AUTOINCREMENT,[send_time] text,[rece_time] text,[title] text,[subhead] text,[content] text,[user_deviceId] text,[user_name] text,[age] text,[sex] text,[qianMing] text,[city] text,[icon_url] text,[send_type] text,[isRead] text)";
	String receSql = "Insert  Into [rece] ([_id],[send_time],[rece_time],[title],[subhead],[content],[user_deviceId],[user_name],[age],[sex],[qianMing],[city],[icon_url],[send_type],[isRead]) Values('1',?,?,'爱情宣言','小播欢迎您使用播播','非常感谢您使用播播产品,若您在使用过程中有什么建议和意见都可以与小播联系,直接回复即可,小播会尽快回复您','867064010609137','小播','1992-1-15','女','爱一个人好难','江苏 南京','http://dubinwei.web-149.com/bobo/xiaobo.png','12','false');";
	String friendSql = "Insert  Into [friend] ([_id],[user_deviceId],[user_name],[age],[sex],[qianMing],[city],[icon_url]) Values('1','867064010609137','小播','1992-1-15','女','爱一个人好难','江苏 南京','http://dubinwei.web-149.com/bobo/xiaobo.png');";
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sql1);
		db.execSQL(sql2);
		db.execSQL(sql3);
		db.execSQL(sql4);
		db.execSQL(receSql,new String[]{System.currentTimeMillis()+"",System.currentTimeMillis()+""});
		db.execSQL(friendSql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (newVersion) {
		case 3:
			db.execSQL(receSql);
			db.execSQL(friendSql);
		}
	}

}
