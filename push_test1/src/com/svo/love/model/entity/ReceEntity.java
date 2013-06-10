package com.svo.love.model.entity;

import java.io.Serializable;

public class ReceEntity implements Serializable,Comparable<ReceEntity>{
	private static final long serialVersionUID = -2548423326370752573L;
	private String _id;
	private String age;
	private String city = "";
	//发送者设备ID
	private String imei;
	private boolean isRead;
	private String msg;
	//发送者的名字
	private String name;
	private String qianMing = "";
	//接收时间
	private String receTime;
	
	private int send_type; //发送消息类型
	private String sex;
	private String subHead;//副标题
	//发送时间
	private String time;
	private String title;
	private String icon_url;
	public String getIcon_url() {
		return icon_url;
	}
	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}
	@Override
	public int compareTo(ReceEntity another) {
		long time1 = Long.parseLong(getReceTime());
		long time2 = Long.parseLong(another.getReceTime());
		return (int) (time1 - time2);
	}
	public String get_id() {
		return _id;
	}
	public String getAge() {
		return age;
	}
	public String getCity() {
		return city;
	}
	public String getImei() {
		return imei;
	}
	public String getMsg() {
		return msg;
	}
	public String getName() {
		return name;
	}
	public String getQianMing() {
		return qianMing;
	}
	public String getReceTime() {
		return receTime;
	}
	public int getSend_type() {
		return send_type;
	}
	public String getSex() {
		if ("woman".equals(sex)) {
			return "女";
		}else if ("man".equals(sex)) {
			return "男";
		}else if ("renyao".equals(sex)) {
			return "人妖";
		}
		return sex;
	}
	public String getSubHead() {
		return subHead;
	}
	public String getTime() {
		return time;
	}
	public String getTitle() {
		return title;
	}
	public boolean isRead() {
		return isRead;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setQianMing(String qianMing) {
		this.qianMing = qianMing;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	public void setReceTime(String receTime) {
		this.receTime = receTime;
	}
	public void setSend_type(int send_type) {
		this.send_type = send_type;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public void setSubHead(String subHead) {
		this.subHead = subHead;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "title:"+title+";msg:"+msg+";sendName:"+name+";age:"+age+";sex:"+sex+";imei:"+imei+";sendType:"+send_type+";qianMing:"+qianMing+";city:"+city;
	}
}
