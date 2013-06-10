package com.svo.love.model.entity;

import java.io.Serializable;

public class Friend implements Serializable{
	private static final long serialVersionUID = 2890433505091546534L;
	private String _id;
	private String user_deviceId;
	private String user_name;
	private String age;
	private String sex;
	private String city;
	private String icon_url;
	public String getIcon_url() {
		return icon_url;
	}
	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getQianMing() {
		return qianMing;
	}
	public void setQianMing(String qianMing) {
		this.qianMing = qianMing;
	}
	private String qianMing;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getUser_deviceId() {
		return user_deviceId;
	}
	public void setUser_deviceId(String user_deviceId) {
		this.user_deviceId = user_deviceId;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	@Override
	public String toString() {
		return "user_deviceId:"+user_deviceId+";user_name:"+user_name+";age:"+age+";sex:"+sex+";city:"+city+";qianMing:"+qianMing;
	}
}
