package com.svo.love;


import com.svo.love.util.Constants;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

/**
 * For developer startup JPush SDK
 * 
 * 一般建议在自定义 Application 类里初始化。也可以在主 Activity 里。
 */
public class ExampleApplication extends Application {
    private static final String TAG = "ExampleApplication";
    public static boolean sound_hint = true;
    public static Context context;
    @Override
    public void onCreate() {
    	 Log.d(TAG, "onCreate");
         super.onCreate();
         context = getApplicationContext();
         new Constants(context);
         JPushInterface.setDebugMode(true); 	//设置开启日志,发布时请关闭日志
         JPushInterface.init(this);     		// 初始化 JPush
         SharedPreferences settingSP = PreferenceManager.getDefaultSharedPreferences(this);
         sound_hint = settingSP.getBoolean("sound_hint", true);
    }
}
