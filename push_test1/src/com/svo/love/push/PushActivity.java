package com.svo.love.push;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import cn.jpush.api.DeviceEnum;
import cn.jpush.api.ErrorCodeEnum;
import cn.jpush.api.JPushClient;
import cn.jpush.api.MessageResult;

import com.svo.love.R;

public class PushActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.push);
	}
	public void click(View view) {
		// 指定某种设备发送，并且指定离线消息保存时间
				JPushClient jpush = new JPushClient("6b154e84b2203f6d0c6968b5", "d363e0c02d0cae3975ec7eae", 864000, DeviceEnum.Android);
				//jpush.setEnableSSL(true);
				int sendNo = 1;
				//我的设备ID
				String imei = "867064010609137";
				String msgTitle = "title2";
				String msgContent = "content";
				MessageResult msgResult = jpush.sendNotificationWithImei(sendNo,imei,  msgTitle, msgContent);
				if (null != msgResult) {
				    if (msgResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
				        System.out.println("发送成功， sendNo=" + msgResult.getSendno());
				    } else {
				        System.out.println("发送失败， 错误代码=" + msgResult.getErrcode() + ", 错误消息=" + msgResult.getErrmsg());
				    }
				} else {
				    System.out.println("无法获取数据");
				}
	}
}
