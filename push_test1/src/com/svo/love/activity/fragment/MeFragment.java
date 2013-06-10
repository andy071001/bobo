package com.svo.love.activity.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.svo.love.R;
import com.svo.love.activity.HistoryActvity;
import com.svo.love.activity.SetAccountActivity;
import com.svo.love.activity.SetActivity;
import com.svo.love.model.MPush;

public class MeFragment extends Fragment implements OnClickListener{
	private Button person_btn;//个人中心
	private Button my_broad_btn;//我的广播
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.me_fragment, null);
		person_btn = (Button) view.findViewById(R.id.person_btn);
		my_broad_btn = (Button) view.findViewById(R.id.my_broad_btn);
		view.findViewById(R.id.device_btn).setOnClickListener(this);
		view.findViewById(R.id.setting).setOnClickListener(this);
		view.findViewById(R.id.my_tag).setOnClickListener(this);
		person_btn.setOnClickListener(this);
		my_broad_btn.setOnClickListener(this);
		return view;
	}
	//按钮点击事件 
	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		if (viewId == R.id.person_btn) {
			startActivity(new Intent(getActivity(), SetAccountActivity.class));
		} else if (viewId == R.id.my_broad_btn) {
			startActivity(new Intent(getActivity(), HistoryActvity.class));
		}else if (viewId == R.id.device_btn) {
			showDeviceIdDialog();
		}else if (viewId == R.id.setting) {
			startActivity(new Intent(getActivity(), SetActivity.class));
		}else if (viewId == R.id.my_tag) {
			SharedPreferences settingSP = PreferenceManager.getDefaultSharedPreferences(getActivity());
			String tags = settingSP.getString("set_tag", "");
			showTagsDialog(tags);
		}
	}
	private void showTagsDialog(String tags) {
		if (TextUtils.isEmpty(tags)) {
			Toast.makeText(getActivity(), "您还没有设置标签", Toast.LENGTH_SHORT).show();
			return;
		}
		String[] items = tags.replaceAll("，",",").split(",");
		new AlertDialog.Builder(getActivity()).setTitle("我的标签").setPositiveButton("确定", null).setItems(items, null).show();
	}
	/**
	 * 显示用户的设备ID
	 */
	private void showDeviceIdDialog() {
		final EditText deviceId_et = new EditText(getActivity());
		deviceId_et.setText(new MPush(getActivity()).getDeviceId());
		new AlertDialog.Builder(getActivity()).setTitle("我的设备号").setView(deviceId_et).setPositiveButton("复制", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ClipboardManager clip = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
				clip.setText(deviceId_et.getText().toString()); // 复制
				Toast.makeText(getActivity(), "已复制到剪切板", Toast.LENGTH_SHORT).show();
			}
		}).setNegativeButton("短信发送", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				String appName = getString(R.string.app_name);
				intent.putExtra("sms_body", "我的设备号是:"+deviceId_et.getText().toString()+".使用"+appName+"可以通过设备号添加我为好友"); 
				intent.setType("vnd.android-dir/mms-sms"); 
				startActivity(intent);
			}
		}).show();
	}
}
