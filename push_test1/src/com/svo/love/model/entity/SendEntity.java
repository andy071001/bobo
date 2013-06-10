package com.svo.love.model.entity;

import java.io.Serializable;

public class SendEntity implements Serializable{
	private static final long serialVersionUID = -2548423326370752573L;
	private String _id;
	private String title;
	private String subHead;
	public String getSubHead() {
		return subHead;
	}
	public void setSubHead(String subHead) {
		this.subHead = subHead;
	}
	private String content;
	//发送数
	private String send_num;
	//发送时间
	private String send_time;
	private int send_type; //发送消息类型
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSend_num() {
		return send_num;
	}
	public void setSend_num(String send_num) {
		this.send_num = send_num;
	}
	public String getSend_time() {
		return send_time;
	}
	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}
	public int getSend_type() {
		return send_type;
	}
	public void setSend_type(int send_type) {
		this.send_type = send_type;
	}
	
}
