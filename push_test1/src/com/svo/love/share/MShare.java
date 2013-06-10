package com.svo.love.share;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

import com.svo.love.MainActivity;
import com.svo.love.model.MPush;
import com.svo.love.util.IConstants;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeUser;
import com.umeng.socialize.bean.UMShareMsg;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchUserListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

public class MShare {
	
	protected static final String TAG = "MShare";
	private Activity activity;
	private UMSocialService controller;
	public UMSocialService getController() {
		return controller;
	}
	public MShare(Activity activity){
		this.activity = activity;
		controller = UMServiceFactory.getUMSocialService("Android", RequestType.SOCIAL);
		controller.setConfig(new SocialConfig().getSocialConfig(activity));
	}
	public void openShare(UMSocialService controller,Activity activity) {
		controller.setShareContent("想知道谁曾经暗恋过你吗?敢大声喊出你心中的爱吗?快去使用播播吧,痛快去喊,痛快去爱.再不疯狂就老了.");
		controller.openShare(activity, false);
	}
	public void share(String shareContent, SHARE_MEDIA media) {
		//构建分享内容
		UMShareMsg shareMsg = new UMShareMsg();
		//设置分享文字
		shareMsg.text = shareContent;
		controller.postShare(activity, media, shareMsg, null);
	}
	
	public void login(SHARE_MEDIA arg1, SocializeClientListener arg2) {
		controller.login(activity, arg1, arg2);
	}
	public void getUserInfo(FetchUserListener listener) {
		controller.getUserInfo(activity, listener);
	}
	private Sina sina;
	public Sina getSina() {
		return sina;
	}
	private ProgressDialog dialog;
	public void showDialog(String msg) {
		dialog = ProgressDialog.show(activity, null, msg, false, true);
	}

	public void dismissDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}
	public void sinaLogin(final Activity activity) {
		sina = new Sina(activity);
		showDialog("登录中...");
		sina.bind(new WeiboAuthListener() {
			
			@Override
			public void onWeiboException(WeiboException arg0) {
				arg0.printStackTrace();
				dismissDialog();
			}
			
			@Override
			public void onError(WeiboDialogError arg0) {
				arg0.printStackTrace();
				dismissDialog();
			}
			
			@Override
			public void onComplete(Bundle values) {
				Log.i(TAG, "onComplete");
				String token = values.getString("access_token");
	            String expires_in = values.getString("expires_in");
	            String uid = values.getString("uid");
	            //将从新浪微博获得的访问密钥存入配置文件中
	            Editor editor = activity.getSharedPreferences("sina", Context.MODE_PRIVATE).edit();
	            editor.putString("sina_AccessToken", token);
	            editor.putString("sina_expires_in", expires_in);
	            editor.putString("uid", uid);
	            editor.putLong("sina_bind_time", System.currentTimeMillis());
	            editor.commit();
	            sina.GuanZhuMe();
	            sina.reqUserInfo(Long.parseLong(uid), new RequestListener() {
					
					@Override
					public void onIOException(IOException arg0) {
						arg0.printStackTrace();
						dismissDialog();
					}
					
					@Override
					public void onError(WeiboException arg0) {
						arg0.printStackTrace();
						dismissDialog();
					}
					
					@Override
					public void onComplete(String arg0) {
						Log.i(TAG, "msg:"+arg0);
						saveUserInfo(arg0);
						new MPush(activity).setSexTag(activity.getSharedPreferences(IConstants.PREFERENCE_NAME,Context.MODE_PRIVATE).getString("sex", "woman"),new MPush(activity).getDeviceId());
						new MPush(activity).sayHello();
						dismissDialog();
						activity.startActivity(new Intent(activity, MainActivity.class));
						activity.finish();
					}
				});
			}
			
			@Override
			public void onCancel() {
			}
		});
	}
	public void saveUserInfo(SocializeUser user) {
		SharedPreferences preferences = activity.getSharedPreferences(IConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("userName", user.loginAccount.getUserName());
		editor.putString("age", user.loginAccount.getBirthday());
		editor.putString("uid", user.loginAccount.getUsid());
		String sex = user.loginAccount.getGender().name().equalsIgnoreCase("male")?"man":"woman";
		editor.putString("sex", sex);
		editor.putString("extra", user.loginAccount.getExtendArgs());
		editor.putString("icon_url", user.loginAccount.getAccount_icon_url());
		editor.putString("platform", user.loginAccount.getPlatform());
		editor.commit();
	}
	//新浪保存用户信息
	public void saveUserInfo(String json) {
		try {
			JSONObject object = new JSONObject(json);
			SharedPreferences preferences = activity.getSharedPreferences(IConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
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
}
