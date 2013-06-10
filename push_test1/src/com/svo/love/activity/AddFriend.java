package com.svo.love.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.svo.love.R;
import com.svo.love.model.MFriend;
import com.svo.love.model.entity.ReceEntity;

public class AddFriend extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_friend);
	}
	public void click(View view) {
		EditText name_et = (EditText) findViewById(R.id.et1);
		EditText id_et = (EditText) findViewById(R.id.et2);
		String name = name_et.getText().toString().trim();
		String id = id_et.getText().toString().trim();
		if (TextUtils.isEmpty(name)||TextUtils.isEmpty(id)) {
			Toast.makeText(this, "内容不可为空", Toast.LENGTH_LONG).show();
		}
		int btn_id = view.getId();
		if (btn_id == R.id.add) {
			ReceEntity entity = new ReceEntity();
			entity.setName(name);
			entity.setImei(id);
			boolean flag = new MFriend(this).addFriend(entity );
			if (flag) {
				Toast.makeText(this, "添加好友成功", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "好友已存在", Toast.LENGTH_SHORT).show();
			}
		} else {
			finish();
		}
	}
}
