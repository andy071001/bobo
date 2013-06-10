package com.svo.love.activity.fragment;

import com.svo.love.R;
import com.svo.love.model.MPush;
import com.svo.love.util.IConstants;
import com.svo.love.util.NetStateUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Tab4Fragment extends Fragment implements OnClickListener {
	private View root;
	private String title = "";
	private int sendType = IConstants.SEND_TO_ALL;
	private EditText self_type;//自定义广播类型名字
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.tab_four, null);
		root.findViewById(R.id.send_btn).setOnClickListener(this);
		self_type= (EditText) root.findViewById(R.id.self_type);
		RadioGroup leiXing = (RadioGroup) root.findViewById(R.id.radioGroup3);
		leiXing.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.radio31) {
					sendType = IConstants.SEND_TO_MAN;
				} else if (checkedId == R.id.radio32) {
					sendType = IConstants.SEND_TO_WOMAN;
				} else if (checkedId == R.id.radio33) {
					sendType = IConstants.SEND_TO_ALL;
				}
			}
		});
		return root;
	}

	@Override
	public void onClick(View v) {
		sendClick(v);
	}

	// 发送按钮处理方法
	private void sendClick(View v) {
		if (!NetStateUtil.isNetworkAvailable(getActivity())) {
			Toast.makeText(getActivity(), "网络不可用,发送失败", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		title = self_type.getText().toString().trim();
		final String subHead = ((EditText) root.findViewById(R.id.title_et))
				.getText().toString();
		final String content = ((EditText) root.findViewById(R.id.content_et))
				.getText().toString();
		if (TextUtils.isEmpty(title)) {
			Toast.makeText(getActivity(), "请输入播播类型", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(content)) {
			Toast.makeText(getActivity(), "请输入内容", Toast.LENGTH_SHORT).show();
			return;
		} else {
			new AsyncTask<Void, Void, Boolean>() {

				protected void onPreExecute() {
					Toast.makeText(getActivity(), "发送中,请稍候...",
							Toast.LENGTH_SHORT).show();
				}

				@Override
				protected Boolean doInBackground(Void... params) {
					MPush mPush = new MPush(getActivity());
					boolean flag = new MPush(getActivity()).send(title,
							subHead, content, sendType);
					if (flag) {
						mPush.saveSendInfo(title, subHead, content);
					}
					return flag;
				}

				protected void onPostExecute(Boolean result) {
					String hint = "";
					if (result) {
						hint = "发送成功";
					} else {
						hint = "发送失败";
					}
					Toast.makeText(getActivity(), hint, Toast.LENGTH_SHORT)
							.show();
				}
			}.execute();
		}
	}
}
