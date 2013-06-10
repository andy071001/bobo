package com.svo.love.model;

import android.content.Context;
import android.widget.Toast;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

public class Mumeng {

	public void update(final Context context) {
		UmengUpdateAgent.update(context);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
				switch (updateStatus) {
				case 0: // has update
					UmengUpdateAgent.showUpdateDialog(context, updateInfo);
					break;
				case 1: // has no update
					Toast.makeText(context, "已是最新", Toast.LENGTH_SHORT).show();
					break;
				case 3: // time out
					Toast.makeText(context, "请求超时", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
	}
}
