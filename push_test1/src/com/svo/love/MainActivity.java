package com.svo.love;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.svo.love.activity.Login;
import com.svo.love.activity.SetActivity;
import com.svo.love.model.Mumeng;
import com.svo.love.share.MShare;
import com.svo.love.util.IConstants;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.UMSsoHandler;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends FragmentActivity{
//	private static final String TAG = "MainActivity";
	private ViewFlipper flipper;
	public static SoundPool soundPool; // 用来播放声音
	public static int id;
	private UMSocialService controller = UMServiceFactory.getUMSocialService("Android", RequestType.SOCIAL);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_frame);
        
        soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 100);
		id = soundPool.load(this, R.raw.folder, 1);
		
        flipper = (ViewFlipper) findViewById(R.id.main_frame_content);
        //判断有没有设置账户信息
        SharedPreferences preferences = getSharedPreferences(IConstants.PREFERENCE_NAME, MODE_PRIVATE);
		if (TextUtils.isEmpty(preferences.getString("sex", ""))) {
			startActivity(new Intent(this, Login.class));
			finish();
		}else {
			UmengUpdateAgent.update(this);
			UmengUpdateAgent.setUpdateOnlyWifi(false);
		}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(1, 1, 1, "找朋友").setIcon(R.drawable.menu_find_friend);
    	menu.add(1, 2, 2, "发送意见反馈").setIcon(R.drawable.menu_issue_report);
    	menu.add(1, 3, 3, "设置").setIcon(R.drawable.menu_setting);
    	menu.add(1, 4, 4, "版本更新").setIcon(R.drawable.menu_upgrade);
    	menu.add(1, 5, 5, "分享").setIcon(R.drawable.menu_share);
    	menu.add(1, 6, 6, "退出").setIcon(R.drawable.menu_quit);
    	return super.onCreateOptionsMenu(menu);
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case 1:
			break;
		case 2:
			FeedbackAgent feedbackAgent = new FeedbackAgent(this);
			feedbackAgent.startFeedbackActivity();
			break;
		case 3:
			startActivity(new Intent(this, SetActivity.class));
			break;
		case 4:
			new Mumeng().update(this);
			break;
		case 5:
			new MShare(this).openShare(controller, this);
			break;
		case 6:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
    //底部标签点击事件
    public void click(View view) {
		int viewId = view.getId();
		switch (viewId) {
		case R.id.layout1:
			flipper.setDisplayedChild(0);
			break;
		case R.id.layout2:
			flipper.setDisplayedChild(1);
			break;
		case R.id.layout3:
			flipper.setDisplayedChild(2);
			break;
		case R.id.layout4:
			flipper.setDisplayedChild(3);
			break;
		default:
			break;
		}
		changeSelect(flipper.getDisplayedChild());
	}
    private void changeSelect(int index) {
    	ImageView homeImg = (ImageView) findViewById(R.id.imageMain);
    	ImageView MsgImg = (ImageView) findViewById(R.id.imageMsg);
    	ImageView friImg = (ImageView) findViewById(R.id.imageFriend);
    	ImageView setImg = (ImageView) findViewById(R.id.image_me);
    	switch (index) {
		case 0:
			homeImg.setImageResource(R.drawable.tab_home_a);
			MsgImg.setImageResource(R.drawable.tab_search_a);
			friImg.setImageResource(R.drawable.tab_fav_n);
			setImg.setImageResource(R.drawable.tab_set_normal);
			break;
		case 1:
			MsgImg.setImageResource(R.drawable.tab_search_n);
			homeImg.setImageResource(R.drawable.tab_home_n);
			friImg.setImageResource(R.drawable.tab_fav_n);
			setImg.setImageResource(R.drawable.tab_set_normal);
			break;
		case 2:
			friImg.setImageResource(R.drawable.tab_fav_a);
			homeImg.setImageResource(R.drawable.tab_home_n);
			MsgImg.setImageResource(R.drawable.tab_search_a);
			setImg.setImageResource(R.drawable.tab_set_normal);
			break;
		case 3:
			setImg.setImageResource(R.drawable.tab_set_press);
			homeImg.setImageResource(R.drawable.tab_home_n);
			MsgImg.setImageResource(R.drawable.tab_search_a);
			friImg.setImageResource(R.drawable.tab_fav_n);
			break;
		default:
			break;
		}
	}
    @Override
    protected void onResume() {
    	super.onResume();
    	MobclickAgent.onResume(this);
    }
    @Override
    protected void onPause() {
    	super.onPause();
    	MobclickAgent.onPause(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 使用SSO必须添加，指定获取授权信息的回调页面，并传给SDK进行处理
         */
        UMSsoHandler sinaSsoHandler = controller.getConfig().getSinaSsoHandler();
        if ( sinaSsoHandler != null && requestCode == UMSsoHandler.DEFAULT_AUTH_ACTIVITY_CODE) {
            sinaSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
