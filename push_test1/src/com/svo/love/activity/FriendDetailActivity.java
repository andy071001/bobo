package com.svo.love.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.svo.love.R;
import com.svo.love.model.MFriend;
import com.svo.love.model.MMsg;
import com.svo.love.model.entity.ReceEntity;

public class FriendDetailActivity extends Activity implements OnClickListener{
	private ReceEntity entity;
	private Button sendMsg;
	private Button addButton;
	private String from;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fri_info_detail);
		TextView userName = (TextView) findViewById(R.id.userName_tv);
		TextView userId = (TextView) findViewById(R.id.userId_tv);
		TextView sex = (TextView) findViewById(R.id.sex);
		TextView age = (TextView) findViewById(R.id.age);
		TextView address = (TextView) findViewById(R.id.address);
		TextView qianMing = (TextView) findViewById(R.id.qianMing);
		ImageView backImg = (ImageView) findViewById(R.id.back_img);
		sendMsg = (Button) findViewById(R.id.send_msg);
		addButton = (Button) findViewById(R.id.top_right);
		sendMsg.setOnClickListener(this);
		addButton.setOnClickListener(this);
		backImg.setOnClickListener(this);
		entity = (ReceEntity) getIntent().getSerializableExtra("entity");
		from = getIntent().getStringExtra("from");
		if ("ReplyActivity".equals(from)) {
			sendMsg.setVisibility(View.GONE);
		}else if ("FriendFragment".equals(from)) {
			addButton.setVisibility(View.GONE);
		}else if ("Chat".equals(from)) {
			sendMsg.setVisibility(View.GONE);
			String deviceId = getIntent().getStringExtra("deviceId");
			entity = new MMsg(this).getDetail(deviceId);
		}
		userName.setText(entity.getName());
		userId.setText("播播号：\n"+entity.getImei());
		sex.setText(entity.getSex());
		age.setText(entity.getAge());
		address.setText(entity.getCity());
		qianMing.setText(entity.getQianMing());
		
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.back_img://返回按钮
			finish();
			break;
		case R.id.top_right://添加好友
			addFriend();
			break;
		case R.id.send_msg://发送信息
			openChat(entity);
			break;
		default:
			break;
		}
	}
	/**
	 * 打开聊天界面
	 * @param entity2
	 */
	private void openChat(ReceEntity entity2) {
		Intent openInten = new Intent(this, Chat.class);
		openInten.putExtra("deviceId", entity2.getImei());
		openInten.putExtra("friendName", entity2.getName());
		openInten.putExtra("icon_url", entity2.getIcon_url());
		startActivity(openInten);
	}

	/**
	 * 将该人添加为好友
	 */
	private void addFriend() {
		boolean flag = new MFriend(this).addFriend(entity);
		if (flag) {
			Toast.makeText(this, "添加好友成功", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "好友已存在", Toast.LENGTH_SHORT).show();
		}
	}
}
