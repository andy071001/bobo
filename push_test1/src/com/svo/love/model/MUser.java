package com.svo.love.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.svo.love.util.IConstants;
import com.umeng.socialize.bean.SocializeUser;

public class MUser {
//	private Activity activity;
	private SharedPreferences preferences;
	public MUser(Context context){
//		this.activity = activity;
		preferences = context.getSharedPreferences(IConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
	}
	/**
	 * 新浪保存用户信息
	 * @param json
	 */
	public void saveUserInfo(String json) {
		try {
			JSONObject object = new JSONObject(json);
			Editor editor = preferences.edit();
			editor.putString("userName", object.getString("screen_name"));
			editor.putString("uid", object.getString("idstr"));
			String sex = object.getString("gender").equalsIgnoreCase("m")?"man":"woman";
			editor.putString("sex", sex);
			editor.putString("qianMing", object.getString("description"));
			editor.putString("city", object.getString("location"));
			editor.putString("icon_url", object.getString("profile_image_url"));
			editor.putString("platform", "sina");
			editor.commit();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 腾讯与人人保存用户信息
	 * @param user
	 */
	public void saveUserInfo(SocializeUser user) {
		Editor editor = preferences.edit();
		editor.putString("userName", user.loginAccount.getUserName());
		editor.putString("age", user.loginAccount.getBirthday());
		editor.putString("uid", user.loginAccount.getUsid());
		String sex = user.loginAccount.getGender().name().equalsIgnoreCase("male")?"man":"woman";
		editor.putString("sex", sex);
		editor.putString("extra", user.loginAccount.getExtendArgs());
		editor.putString("icon_url", user.loginAccount.getAccount_icon_url());
		editor.putString("platform", user.loginAccount.getPlatform());
		if ("renren".equals(user.loginAccount.getPlatform())) {
			editor.putString("realName", user.loginAccount.getUserName());
		}
		editor.commit();
	}
	/**
	 * 返回用户的真实姓名
	 * @return
	 */
	public String getUserRealName() {
		return preferences.getString("realName", "");
	}
}
