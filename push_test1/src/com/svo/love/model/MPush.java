package com.svo.love.model;

import java.util.HashMap;
import java.util.LinkedHashSet;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.api.DeviceEnum;
import cn.jpush.api.ErrorCodeEnum;
import cn.jpush.api.JPushClient;
import cn.jpush.api.MessageResult;

import com.svo.love.model.dao.DBHelper;
import com.svo.love.util.IConstants;

/**
 * 用来发送消息的类
 * @author duweibn
 */
public class MPush implements IConstants{
	private static final String TAG = "MPush";
	private Context context;
	public static int sendNo;
	private SharedPreferences preferences;
	public MPush(Context context){
		this.context = context;
		preferences = context.getSharedPreferences(IConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		sendNo = preferences.getInt("sendNo", 1);
	}
	/**
	 * @param sex
	 */
	public void setSexTag(String sex,String alias) {
		LinkedHashSet<String> set = new LinkedHashSet<String>();
		set.add(sex);
		setTag(set, alias);
	}
	/**
	 * 设置标签
	 * @param set
	 */
	public void setTag(final LinkedHashSet<String> set,final String alias) {
		new Thread() {
			public void run() {
				JPushInterface.setAliasAndTags(context, alias, set);
			}
		}.start();
	}
	/**
	 * 发送自定义消息给所有人
	 * @param msgTitle 消息的标题
	 * @param msgContent 消息的内容
	 * @return 
	 */
	public boolean sendMsg(String msgTitle,String msgContent) {
		return sendMsg(msgTitle, msgContent, 2);
	}
	
	/**
	 * 发送通知给所有人
	 * @param msgTitle 消息的标题
	 * @param msgContent 消息的内容
	 * @return 
	 */
	public boolean sendNotificaion(String msgTitle,String msgContent) {
		return sendMsg(msgTitle, msgContent, 1);
	}
	/**
	 * 给所有人发送消息或者通知给所有人
	 * @param msgContent 消息的内容
	 * @param msgTitle 消息的标题
	 * @param msgType 1表示通知，2表示自定义消息
	 * @return boolean
	 */
	public boolean sendMsg(String msgTitle,String msgContent,int msgType) {
		JPushClient jpush = getJPushClient();
		//jpush.setEnableSSL(true);
		HashMap<String, Object> map = getBasicInfo();
		map.put(SEND_TYPE, SEND_BASE_SEX);
		MessageResult msgResult = jpush.sendCustomMessageWithAppKey(msgType, msgTitle, msgContent, msgType+"", map);
		return isSendOk(msgResult);
	}
	/**
	 * 根据MessageResult判断发送成功与否
	 * @param msgResult
	 * @return
	 */
	private boolean isSendOk(MessageResult msgResult) {
		if (null != msgResult) {
		    if (msgResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
		    	Log.i(TAG, "发送成功， sendNo=" + msgResult.getSendno());
		    	sendNo++;
		    	context.getSharedPreferences(IConstants.PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putInt("sendNo", sendNo);
		        return true;
		    } else {
		    	Log.i(TAG, "发送失败， 错误代码=" + msgResult.getErrcode() + ", 错误消息=" + msgResult.getErrmsg());
		        return false;
		    }
		} else {
			Log.i(TAG, "发送失败， 无法获取数据");
		    return false;
		}
	}
	
	private JPushClient getJPushClient(){
		return new JPushClient("6b154e84b2203f6d0c6968b5", "d363e0c02d0cae3975ec7eae", 864000, DeviceEnum.Android);
	}
	/**
	 * 获得设备ID
	 * @return
	 */
	public String getDeviceId() {
		/*TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = telephonyManager.getDeviceId();
		if (deviceId.matches("0+")||TextUtils.isEmpty(deviceId)||"null".equals(deviceId)) {
			return null;
		}
		return deviceId;*/
		return JPushInterface.getUdid(context);
	}

	/**
	 * UUID
	 * @return
	 */
//	public String getUUID() {
//		UUID uuid = UUID.randomUUID();
//		return uuid.toString().replace("-", "");
//	}
	/**
	 * 得到用户的基本信息(用户名，年龄，性别)
	 * @return
	 */
	private HashMap<String, Object> getBasicInfo() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		SharedPreferences preferences = context.getSharedPreferences(IConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		map.put("name", preferences.getString("userName", "null"));
		map.put("age", preferences.getString("age", "20"));
		map.put("sex", preferences.getString("sex", "woman"));
		map.put("qianMing", preferences.getString("qianMing", ""));
		map.put("city", preferences.getString("city", ""));
		map.put("time", System.currentTimeMillis()+""); //发送时间
		map.put("imei", getDeviceId()); //设备ID
		map.put("icon_url", preferences.getString("icon_url", "")); //设备ID
		return map;
	}
	/**
	 * 保存发送成功信息
	 * @param title
	 * @param content
	 */
	public void saveSendInfo(String title, String subhead,String content) {
		DBHelper dbHelper = DBHelper.getInstance(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "insert into send(send_num,send_time,title,subhead,content) values(?,?,?,?,?)";
		db.execSQL(sql, new Object[]{sendNo,System.currentTimeMillis(),title,subhead,content});
	}
	/**
	 * 给异性或者同性发送消息
	 * @param title 
	 * @param content
	 * @param subhead 副标题
	 * @param sendType 发送类型
	 * @return 是否发送成功
	 */
	public boolean send(String title, String subhead, String content,  int sendType) {
		String send_tag = "";
		JPushClient pushClient = getJPushClient();
		HashMap<String, Object> map = getBasicInfo();
		map.put(SEND_TYPE, SEND_BASE_SEX);
		map.put("subhead", subhead);
		switch (sendType) {
		case 1:
			send_tag = "man";
			break;
		case 2:
			send_tag = "woman";
			break;
		case 3:
			return sendMsgForTags(title, subhead,content, "man","woman");
		default:
			break;
		}
		MessageResult msgResult = pushClient.sendCustomMessageWithTag(sendNo, send_tag, title, content, "2", map);
		return isSendOk(msgResult);
	}
	public void sayHello() {
		new Thread(){
			public void run() {
				send("新人报到", "大家好,我刚加入播播,欢迎打招呼", "在这个浮躁的社会,我们的内心都渴望着真诚的聆听和倾诉，远离那些喧嚣，剥下虚假的面纱，在播播的世界里您的一句话可能就会滋润我的心田", 3);
			}
		}.start();
	}
	/**
	 * 给多个标签发送
	 * @param title
	 * @param content
	 * @param string 
	 * @param string
	 * @param string2
	 * @return 这里表示给男女发送
	 */
	private boolean sendMsgForTags(String title, String subhead, String content, String tag1, String tag2) {
		boolean flag1 = send(title,subhead, content, 1);
		boolean flag2 = send(title,subhead, content, 2);
		return  flag1||flag2;
	}
	/**
	 * 给某个设备发送消息
	 * @param content 发送的消息内容
	 * @param entity 
	 * @return 
	 */
	public boolean sendWithImei(final String content, final String deviceId,String from) {
		String msgTille = "好友消息";
		if ("reply".equals(from)) {
			msgTille = "广播回复";
		}
		JPushClient pushClient = getJPushClient();
		HashMap<String, Object> map = getBasicInfo();
		map.put(SEND_TYPE, SEND_WITH_IMEI); //发送的消息类型
		MessageResult msgResult = pushClient.sendCustomMessageWithImei(++sendNo, deviceId, msgTille, content, "2", map);
		boolean flag = isSendOk(msgResult);
		if (!flag) {
			//对方没有真实的IMEI
			if (1007 == msgResult.getErrcode()&&msgResult.getErrmsg().contains("IMEI error")) {
				msgResult = pushClient.sendCustomMessageWithAlias(++sendNo, deviceId, msgTille, content, "2", map);
				flag = isSendOk(msgResult);
			}
		}
		if (flag) {
			new Thread(){
				public void run() {
					MMsg msg = new MMsg(context);
					msg.addMsg2send(content,deviceId);
				}
			}.start();
		}
		return flag;
	}
	/**
	 * 标签用户 发送
	 * @param title 发送目的，比如找知己
	 * @param subHead 标题
	 * @param content 正文
	 * @param tag2  标签名字
	 * @return
	 */
	public String sendByTag(String title, String subHead, String content,
			String tag2) {
		JPushClient pushClient = getJPushClient();
		HashMap<String, Object> map = getBasicInfo();
		map.put(SEND_TYPE, SEND_TAG);
		map.put("subhead", subHead);
		MessageResult msgResult = pushClient.sendCustomMessageWithTag(sendNo, tag2, title, content, "2", map);
		if (null != msgResult) {
		    if (msgResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
		    	Log.i(TAG, "发送成功， sendNo=" + msgResult.getSendno());
		    	sendNo++;
		    	context.getSharedPreferences(IConstants.PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putInt("sendNo", sendNo);
		        return "发送成功";
		    } else {
		    	Log.i(TAG, "发送失败， 错误代码=" + msgResult.getErrcode() + ", 错误消息=" + msgResult.getErrmsg());
		    	switch (msgResult.getErrcode()) {
				case 1011:
					return "还没有用户注册‘"+tag2+"'标签";
				}
		    	return "发送失败";
		    }
		} else {
		    return "发送失败";
		}
	}
}
