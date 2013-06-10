package com.svo.love.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.svo.love.model.dao.DBHelper;
import com.svo.love.model.entity.ReceEntity;
import com.svo.love.model.entity.SendEntity;
import com.svo.love.util.Constants;
import com.svo.love.util.IConstants;

public class MMsg {
	public static final String broadcast_tab = "rece";
	public static final String friend_tab = "friend_msg";
//	private Context context;
	private DBHelper dbHelper;
	public MMsg(Context context){
//		this.context = context;
		dbHelper = DBHelper.getInstance(context);
	}
	/**
	 * 查询接收到的朋友的消息。分组查询
	 * @return
	 */
	public ArrayList<ReceEntity> getMsg(String tabName) {
		String sql = "select *,max(rece_time) from "+tabName+" group by user_deviceId order by rece_time desc";
		return queryMsg(sql);
	}
	/**
	 * 查询所有接收到的广播信息
	 * @return
	 */
	public ArrayList<ReceEntity> getMsg() {
		String sql = "select * from "+broadcast_tab+" order by rece_time desc limit 500";
		return queryMsg(sql);
	}
	/**
	 * @param sql
	 * @return
	 */
	private ArrayList<ReceEntity> queryMsg(String sql) {
		ArrayList<ReceEntity> entities = new ArrayList<ReceEntity>();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		try {
			ReceEntity entity;
			while (cursor.moveToNext()) {
				entity = new ReceEntity();
				entity.set_id(cursor.getString(cursor.getColumnIndex("_id")));
				entity.setTime(cursor.getString(cursor.getColumnIndex("send_time")));
				entity.setReceTime(cursor.getString(cursor.getColumnIndex("rece_time")));
				entity.setSubHead(cursor.getString(cursor.getColumnIndex("subhead")));
				entity.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				entity.setMsg(cursor.getString(cursor.getColumnIndex("content")));
				entity.setImei(cursor.getString(cursor.getColumnIndex("user_deviceId")));
				entity.setName(cursor.getString(cursor.getColumnIndex("user_name")));
				entity.setAge(cursor.getString(cursor.getColumnIndex("age")));
				entity.setSex(cursor.getString(cursor.getColumnIndex("sex")));
				entity.setSend_type(cursor.getInt(cursor.getColumnIndex("send_type")));
				entity.setQianMing(cursor.getString(cursor.getColumnIndex("qianMing")));
				entity.setCity(cursor.getString(cursor.getColumnIndex("city")));
				entity.setIcon_url(cursor.getString(cursor.getColumnIndex("icon_url")));
				boolean flag = TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("isRead")));
				entity.setRead(flag);//空代表阅读过了。
				entities.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			cursor.close();
			db.close();
		}
		return entities;
	}
	/**
	 * 查询男性的消息
	 * @param broadcastTab 表名
	 * @return
	 */
	public ArrayList<ReceEntity> getMsg_man(String broadcastTab) {
		String sql = "select * from "+broadcastTab+" where sex='男'";
		return queryMsg(sql);
	}
	/**
	 * 查询女性的消息
	 * @param broadcastTab 表名
	 * @return
	 */
	public ArrayList<ReceEntity> getMsg_woman(String broadcastTab) {
		String sql = "select * from "+broadcastTab+" where sex='女'";
		return queryMsg(sql);
	}
	/**
	 * 得到自己发送过的信息
	 * @return
	 */
	public LinkedList<SendEntity> getSendMsg() {
		String sql = "select * from send where send_type isnull order by send_time desc";
		LinkedList<SendEntity> entities = new LinkedList<SendEntity>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		try {
			SendEntity entity;
			while (cursor.moveToNext()) {
				entity = new SendEntity();
				entity.set_id(cursor.getString(cursor.getColumnIndex("_id")));
				entity.setSend_num(cursor.getString(cursor.getColumnIndex("send_num")));
				entity.setSend_time(cursor.getString(cursor.getColumnIndex("send_time")));
				entity.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				entity.setSubHead(cursor.getString(cursor.getColumnIndex("subhead")));
				entity.setContent(cursor.getString(cursor.getColumnIndex("content")));
				entity.setSend_type(cursor.getInt(cursor.getColumnIndex("send_type")));
				entities.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			cursor.close();
			db.close();
		}
		return entities;
	}
	/**
	 * 保存发送给某个人的消息
	 * @param content
	 * @param entity
	 */
	public void addMsg2send(String content, String deviceId) {
		String sql = "insert into send(send_time,content,send_type,deviceId) values(?,?,?,?)";
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.execSQL(sql, new String[]{System.currentTimeMillis()+"",content,IConstants.SEND_WITH_IMEI+"",deviceId});
	}
	/**
	 * 查询与某人的聊天信息
	 * @param deviceId
	 * @return
	 */
	public List<ReceEntity> getChatMsg(String deviceId) {
		List<ReceEntity> sendEntities = getSendEntity(deviceId);
		List<ReceEntity> receEntities = getReceEntity(deviceId);
		sendEntities.addAll(receEntities);
		Collections.sort(sendEntities);//对消息进行排序 
		return sendEntities;
	}
	
	private List<ReceEntity> getReceEntity(String deviceId) {
		String sql = "select rece_time,content,isRead,icon_url from friend_msg where user_deviceId=?";
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery(sql, new String[]{deviceId});
		List<ReceEntity> entities = new LinkedList<ReceEntity>();
		ReceEntity entity;
		while (cursor.moveToNext()) {
			String time = cursor.getString(cursor.getColumnIndex("rece_time"));
			String content = cursor.getString(cursor.getColumnIndex("content"));
			String icon_url = cursor.getString(cursor.getColumnIndex("icon_url"));
			boolean flag = TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("isRead")));
			entity = new ReceEntity();
			entity.setReceTime(time);
			entity.setMsg(content);
			entity.setRead(flag);
			entity.setIcon_url(icon_url);
			entities.add(entity);
		}
		cursor.close();
		return entities;
	}
	/**
	 * 为了查询这个人的信息
	 */
	public ReceEntity getDetail(String deviceId) {
		String sql = "select user_deviceId,user_name,age,sex,qianMing,city,icon_url from friend_msg where user_deviceId=?";
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery(sql, new String[]{deviceId});
		ReceEntity entity = null;
		if(cursor.moveToNext()) {
			entity = new ReceEntity();
			entity.setImei(cursor.getString(cursor.getColumnIndex("user_deviceId")));
			entity.setName(cursor.getString(cursor.getColumnIndex("user_name")));
			entity.setAge(cursor.getString(cursor.getColumnIndex("age")));
			entity.setSex(cursor.getString(cursor.getColumnIndex("sex")));
			entity.setQianMing(cursor.getString(cursor.getColumnIndex("qianMing")));
			entity.setCity(cursor.getString(cursor.getColumnIndex("city")));
			entity.setIcon_url(cursor.getString(cursor.getColumnIndex("icon_url")));
		}
		cursor.close();
		return entity;
	}
	/**
	 * 从发送表中查询出给某人发送的消息
	 * @param deviceId 某人的设备ID
	 * @return
	 */
	private List<ReceEntity> getSendEntity(String deviceId) {
		String sql = "select send_time,content from send where send_type=? and deviceId=?";
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery(sql, new String[]{IConstants.SEND_WITH_IMEI+"",deviceId});
		List<ReceEntity> entities = new LinkedList<ReceEntity>();
		ReceEntity entity;
		while (cursor.moveToNext()) {
			String time = cursor.getString(cursor.getColumnIndex("send_time"));
			String content = cursor.getString(cursor.getColumnIndex("content"));
			entity = new ReceEntity();
			entity.setReceTime(time);
			entity.setMsg(content);
			entity.setTime("send");
			entities.add(entity);
		}
		cursor.close();
		return entities;
	}
	/**
	 * 将广播信息未读标记为已读(一条)
	 */
	public void update2read(final String _id,final String tableName) {
		new Thread(){
			public void run() {
				String sql = "update "+tableName+" set isRead = '' where _id = ?";
				SQLiteDatabase database = dbHelper.getWritableDatabase();
				database.execSQL(sql, new String[]{_id});
			}
		}.start();
	}
	/**
	 * 将好友信息标记为已读（所有）
	 */
	public void update2read(final String deviceId) {
		new Thread(){
			public void run() {
				String sql = "update friend_msg set isRead = '' where user_deviceId = ?";
				SQLiteDatabase database = dbHelper.getWritableDatabase();
				database.execSQL(sql, new String[]{deviceId});
			}
		}.start();
	}
	/**
	 * 删除某条信息（一样的也会删除）
	 * @param friendName 好友名字
	 * @param string 待删除的信息
	 * 
	 */
	public boolean delMsg(String deviceId, String msg) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		return database.delete("friend_msg", "user_deviceId=? and content=?", new String[]{deviceId,msg})>0;
	}
}
