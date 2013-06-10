package com.svo.love.util;

public interface IConstants {
	//主配置文件名称
	String 	PREFERENCE_NAME = "love";
	int SEND_TO_MAN = 1; //男性
	int SEND_TO_WOMAN = 2; //女性
	int SEND_TO_ALL = 3; //男女都发
	int SEND_TAG = 0;
	String SEND_TYPE = "send_type"; //发送类型
	int SEND_WITH_IMEI = 11; //根据设备ID发给某个人
	int SEND_BASE_SEX = 12; //根据性别发送,可能发送给所有人
	
	//广播常量
	//广播大厅 对应消息接收者
	String MSGFRAGMENT_RECE = "com.svo.love.MsgFragment_MsgReceiver";
	String TAG_MSG = "com.svo.love.MsgFragment_tag";
	//好友消息接收者
	String FRIDEND_MSG = "com.svo.love.MsgFragment_friend_msg";
	String sina_shareContent = "想知道曾经爱你的人吗？想大声喊出心中的心声吗？使用播播(@svosvo)来呐喊吧，找知己，找爱人 . 下载地址： http://www.anzhi.com/soft_860581.html";
	String qq_shareContent = "想知道曾经爱你的人吗？想大声喊出心中的心声吗？使用播播(@dubinwei)来呐喊吧，找知己，找爱人 . 下载地址： http://www.anzhi.com/soft_860581.html";
	String ren_shareContent = "想知道曾经爱你的人吗？想大声喊出心中的心声吗？使用播播来呐喊吧，找知己，找爱人 . 下载地址： http://www.anzhi.com/soft_860581.html";
	String WEIXIN_ID = "wxbbd44a42502395de";
	
	String SINA_APPKEY = "2092259386";
	String SINA_SECRET = "f28a1f0c94c927b93dd16eb9c5907909";
	String SINA_CALLBACK_URL = "http://sns.whalecloud.com";
	
	String REN_APP_ID = "227424";
	String REN_API_KEY = "bd3c2aa857ed4776aa9acc7365e46f70";
	String REN_SECRET_KEY = "ba4147e1855e4819923b46a88b5dbbd1";
	// 分享到人人网之后显示客户端名称
	public static String REN_FROM = "播播";
	// 与上面对应的链接
	public static String REN_FROM_LINK = "http://www.mumayi.com/android-318390.html";
	public static String REN_FROM_MSG = "播播,喊出你的心声";
}
