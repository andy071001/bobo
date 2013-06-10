package com.svo.love.model;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.google.gson.Gson;
import com.svo.love.ExampleApplication;
import com.svo.love.MainActivity;
import com.svo.love.model.dao.DBHelper;
import com.svo.love.model.entity.ReceEntity;
import com.svo.love.util.IConstants;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class MRece implements IConstants{
	private static final String TAG = "MRece";
	private Context context;
	private DBHelper dbHelper;
	public MRece(Context context){
		this.context = context;
		dbHelper = DBHelper.getInstance(context);
	}
	
	/**
	 * 将接收到的广播消息保存到数据库中
	 * @param bundle
	 */
	public boolean saveMsg(Bundle bundle) {
		ReceEntity entity = getEntity(bundle);
		//如果是自己发送的消息则不保存到数据库
//		if (entity.getImei() == new MPush(context).getDeviceId()) {
//			return false;
//		}
		saveMsg(entity);
		return true;
	}

	/**
	 * @param bundle 广播 的消息内容放在bundle里面
	 * @return
	 */
	private ReceEntity getEntity(Bundle bundle) {
		String title = bundle.getString("cn.jpush.android.TITLE");
		String msg = bundle.getString("cn.jpush.android.MESSAGE");
		String extra = bundle.getString("cn.jpush.android.EXTRA");
		ReceEntity entity = new ReceEntity();
		try {
			JSONObject jsonObject = new JSONObject(extra);
			entity.setSend_type(jsonObject.getInt("send_type"));
			entity.setTime(jsonObject.getString("time"));
			entity.setSex(jsonObject.getString("sex"));
			entity.setImei(jsonObject.getString("imei"));
			entity.setAge(jsonObject.getString("age"));
			entity.setName(jsonObject.getString("name"));
			entity.setQianMing(jsonObject.getString("qianMing"));
			entity.setCity(jsonObject.getString("city"));
			entity.setSubHead(jsonObject.getString("subhead"));
			entity.setIcon_url(jsonObject.getString("icon_url"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		entity.setTitle(title);
		entity.setMsg(msg);
		entity.setReceTime(System.currentTimeMillis()+"");
		return entity;
	}
	//保存广播信息
	private void saveMsg(ReceEntity entity){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "insert into rece(send_time,rece_time,title,subhead,content,user_deviceId,user_name,age,sex,qianMing,city,icon_url,send_type,isRead) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		db.execSQL(sql, new Object[]{entity.getTime(),entity.getReceTime(),entity.getTitle(),entity.getSubHead(),entity.getMsg(),entity.getImei(),entity.getName(),entity.getAge(),entity.getSex(),entity.getQianMing(),entity.getCity(),entity.getIcon_url(),entity.getSend_type(),"notRead"});
		dbHelper.close();
	}
	
	//保存好友信息
	private boolean saveFriendMsg(Bundle bundle){
		//将接收到的信息存放到实体里
		ReceEntity entity = getEntity(bundle);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "insert into friend_msg(send_time,rece_time,title,subhead,content,user_deviceId,user_name,age,sex,qianMing,city,icon_url,send_type,isRead) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		db.execSQL(sql, new Object[]{entity.getTime(),entity.getReceTime(),entity.getTitle(),entity.getSubHead(),entity.getMsg(),entity.getImei(),entity.getName(),entity.getAge(),entity.getSex(),entity.getQianMing(),entity.getCity(),entity.getIcon_url(),entity.getSend_type(),"notRead"});
		dbHelper.close();
		return true;
	}
	/**
	 * 接收过来的消息
	 * @param bundle
	 */
	public void handleMsg(Bundle bundle) {
		String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
		String title = bundle.getString(JPushInterface.EXTRA_TITLE);//比如新人报到等
		Gson gson = new Gson();
		ReceEntity entity = gson.fromJson(extra, ReceEntity.class);
		if (entity == null || "新人报到".equals(title)) {
			return ;
		}
		switch (entity.getSend_type()) {
		case SEND_WITH_IMEI: //发送给某个设备的.(消息)
			if (saveFriendMsg(bundle)) {
        		//给msgFragment内部的广播
        		context.sendBroadcast(new Intent(FRIDEND_MSG));
        		soundHint();
			}
			break;
		case SEND_BASE_SEX: //根据性别发送的(广播)
			boolean flag = saveMsg(bundle);
        	if (flag) {
        		//给PublishFragment内部的广播
        		context.sendBroadcast(new Intent(MSGFRAGMENT_RECE));
        		soundHint();
			}
			break;
		case SEND_TAG: //标签发送的(广播)
        	if (saveFriendMsg(bundle)) {
        		//给PublishFragment内部的广播
        		context.sendBroadcast(new Intent(TAG_MSG));
        		soundHint();
			}
			break;
		case 13: 
			break;
		case 14: 
			break;
		default:
			break;
		}
	}

	/**
	 * 声音提示
	 */
	private void soundHint() {
		if (ExampleApplication.sound_hint) {
			MainActivity.soundPool.play(MainActivity.id, 1, 1, 0, 0, 1);
		}
	}
	
	/**
	 * 删除某条广播 
	 * @param get_id
	 */
	public void delOneItem(String _id) {
		String sql = "delete from send where _id = ?";
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.execSQL(sql, new String[] { _id });
	}
}
