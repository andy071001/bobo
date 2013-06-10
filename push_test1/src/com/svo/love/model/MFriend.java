package com.svo.love.model;

import java.util.LinkedList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.svo.love.model.dao.DBHelper;
import com.svo.love.model.entity.Friend;
import com.svo.love.model.entity.ReceEntity;

public class MFriend {
	private DBHelper dbHelper;
	public MFriend(Context context){
		dbHelper = DBHelper.getInstance(context);
	}
	/**
	 * 添加一个好友
	 * @param entity
	 * @return
	 */
	public boolean addFriend(ReceEntity entity) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("user_deviceId", entity.getImei());
		values.put("user_name", entity.getName());
		values.put("age", entity.getAge());
		values.put("sex", entity.getSex());
		values.put("city", entity.getCity());
		values.put("icon_url", entity.getIcon_url());
		values.put("qianMing", entity.getQianMing());
		return db.insert("friend", "title", values)>0;
	}
	/**
	 * 查询所有好友
	 * @return LinkedList<Friend>
	 */
	public LinkedList<Friend> getFriends() {
		LinkedList<Friend> friends = new LinkedList<Friend>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from friend", null);
		try {
			if (cursor != null) {
				while (cursor.moveToNext()) {
					Friend friend = new Friend();
					friend.set_id(cursor.getString(cursor.getColumnIndex("_id")));
					friend.setUser_deviceId(cursor.getString(cursor.getColumnIndex("user_deviceId")));
					friend.setUser_name(cursor.getString(cursor.getColumnIndex("user_name")));
					friend.setAge(cursor.getString(cursor.getColumnIndex("age")));
					friend.setSex(cursor.getString(cursor.getColumnIndex("sex")));
					friend.setCity(cursor.getString(cursor.getColumnIndex("city")));
					friend.setQianMing(cursor.getString(cursor.getColumnIndex("qianMing")));
					friend.setIcon_url(cursor.getString(cursor.getColumnIndex("icon_url")));
					friends.add(friend);
				}
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return friends;
	}
	/**
	 * 删除某个好友
	 * @param get_id
	 * @return
	 */
	public boolean delFriend(String get_id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		return db.delete("friend", "_id = ?", new String[]{get_id})>0;
	}
}
