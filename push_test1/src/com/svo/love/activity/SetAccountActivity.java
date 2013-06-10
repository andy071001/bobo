package com.svo.love.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.svo.love.R;
import com.svo.love.model.MPush;
import com.svo.love.util.IConstants;

/**
 * 设置账户信息
 * @author duweibn
 */
public class SetAccountActivity extends Activity {
	
//	private static final String TAG = "SetAccountActivity";

	private EditText name_et;
	EditText qianMing_et;//个性签名文本框
	EditText city_et;
	private EditText age_et;//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_account);
		//find
		name_et = (EditText) findViewById(R.id.editText1);
		age_et = (EditText) findViewById(R.id.editText2);
		qianMing_et = (EditText)findViewById(R.id.editText4);//个性签名文本框
		city_et = (EditText)findViewById(R.id.editText5);
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup2);
		RadioButton manRb = (RadioButton) findViewById(R.id.radio0);
		RadioButton womanRb = (RadioButton) findViewById(R.id.radio1);
		//设置默认值
		final SharedPreferences preferences = getSharedPreferences(IConstants.PREFERENCE_NAME, MODE_PRIVATE);
		String userName = preferences.getString("userName", "");
		name_et.setText(userName);
		String userAge = preferences.getString("age", "");
		if (userAge.equalsIgnoreCase("null") || TextUtils.isEmpty(userAge)) {
			userAge = "";
		}
		age_et.setText(userAge);
		String sex = preferences.getString("sex", "");
		if (sex.equals("man")) {
			manRb.setChecked(true);
		}else if (sex.equals("woman")) {
			womanRb.setChecked(true);
		}
		//选择性别事件
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				Editor editor = preferences.edit();
				String sex = "woman";
				if (checkedId == R.id.radio0) {
					sex = "man";
				} else if (checkedId == R.id.radio1) {
					sex = "woman";
				} 
				editor.putString("sex", sex).commit();
			}
		});
		String qianMing = preferences.getString("qianMing", "");
		if (!"".equals(qianMing)) {
			qianMing_et.setText(qianMing);
		}
		String city = preferences.getString("city", "");
		if (!"".equals(city)) {
			city_et.setText(city);
		}
	}
	
	//完成按钮点击事件 
	public void click(View view) {
		String name = name_et.getText().toString();
		String age = age_et.getText().toString();
		SharedPreferences preferences = getSharedPreferences(IConstants.PREFERENCE_NAME, MODE_PRIVATE);
		if (!TextUtils.isEmpty(name)) {
			preferences.edit().putString("userName", name).commit();
		}else {
			Toast.makeText(this, "请输入姓名", Toast.LENGTH_LONG).show();
			return;
		}
        preferences.edit().putString("age", age).commit();
        if (TextUtils.isEmpty(preferences.getString("sex", ""))) {
        	preferences.edit().putString("sex", "woman").commit();
		}
		String qianMing = qianMing_et.getText().toString();
		String city = city_et.getText().toString();
        preferences.edit().putString("qianMing", qianMing).putString("city", city).commit();
        new MPush(SetAccountActivity.this).setSexTag(preferences.getString("sex", "woman"),new MPush(this).getDeviceId());
	}
}
