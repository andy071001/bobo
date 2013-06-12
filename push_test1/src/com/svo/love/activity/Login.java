package com.svo.love.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.svo.love.MainActivity;
import com.svo.love.R;
import com.svo.love.model.MPush;
import com.svo.love.model.MUser;
import com.svo.love.share.MShare;
import com.svo.love.util.IConstants;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.SocializeUser;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchUserListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.weibo.sdk.android.sso.SsoHandler;

public class Login extends Activity implements OnClickListener {
	private Button sinaBtn;
	private Button qqBtn;
	private Button renrenBtn;
	private ImageView tryUseIv;
	private MShare mShare;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		sinaBtn = (Button) findViewById(R.id.sina);
		qqBtn = (Button) findViewById(R.id.qq);
		renrenBtn = (Button) findViewById(R.id.renren);
		tryUseIv = (ImageView) findViewById(R.id.try_use);
		sinaBtn.setOnClickListener(this);
		qqBtn.setOnClickListener(this);
		renrenBtn.setOnClickListener(this);
		tryUseIv.setOnClickListener(this);
		mShare = new MShare(this);
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.sina:
			mShare.sinaLogin(this);
			break;
		case R.id.qq:
			mShare.login(SHARE_MEDIA.TENCENT, new MyListener(0));
			break;
		case R.id.renren:
			mShare.login(SHARE_MEDIA.RENREN, new MyListener(1));
			break;
		case R.id.try_use:
			finish();
			break;
		}
	}
	private ProgressDialog progressDialog;
	private class MyListener implements SocializeClientListener{
		int which = 0;//腾讯
		public MyListener(int which) {
			this.which = which;
		}
		@Override
		public void onComplete(int arg0, SocializeEntity arg1) {
			if (which == 0) {
				mShare.share(IConstants.qq_shareContent,SHARE_MEDIA.TENCENT);
			}else {
				mShare.share(IConstants.ren_shareContent,SHARE_MEDIA.RENREN);
			}
			mShare.getUserInfo(userInfolistener);
		}

		@Override
		public void onStart() {
			progressDialog = ProgressDialog.show(Login.this, null, "登录中...", false, false);
		}
		
	}
	private FetchUserListener userInfolistener = new FetchUserListener() {
		
		@Override
		public void onStart() {
			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.setMessage("初始化信息...");
			}
		}
		
		@Override
		public void onComplete(int arg0, SocializeUser user) {
			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (user != null && user.loginAccount != null) {
				new MUser(Login.this).saveUserInfo(user);
				new MPush(Login.this).setSexTag(getSharedPreferences(IConstants.PREFERENCE_NAME,MODE_PRIVATE).getString("sex", "woman"),new MPush(Login.this).getDeviceId());
				new MPush(Login.this).sayHello();
				startActivity(new Intent(Login.this, MainActivity.class));
				finish();
			} else {
				Toast.makeText(Login.this, "登录失败", Toast.LENGTH_LONG).show();
			}
		}
	};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    /**
	     * 使用SSO必须添加，指定获取授权信息的回调页面，并传给SDK进行处理
	     */
//	    UMSsoHandler sinaSsoHandler = mShare.getController().getConfig().getSinaSsoHandler();
	    SsoHandler sinaSsoHandler = mShare.getSina().getSsoHandler();
	    if ( sinaSsoHandler != null) {
	        sinaSsoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }
	}
	
}
