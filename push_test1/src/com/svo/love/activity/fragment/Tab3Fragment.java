package com.svo.love.activity.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.svo.love.R;
import com.svo.love.model.MPush;
import com.svo.love.util.NetStateUtil;

public class Tab3Fragment extends Fragment implements OnClickListener {
	private View root;
	private MultiAutoCompleteTextView tagEt;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.tab_three, null);
		tagEt = (MultiAutoCompleteTextView) root.findViewById(R.id.multiAutoCompleteTextView1);
		initTagEt();
		root.findViewById(R.id.send_btn).setOnClickListener(this);
		return root;
	}

	private void initTagEt() {
		String tagStr = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("set_tag", "");
		if (!TextUtils.isEmpty(tagStr)) {
			tagEt.setThreshold(2);
			tagEt.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer()); 
			String[] items = tagStr.replaceAll("，", ",").split(",");
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.multi_tags,R.id.subject,items);
			tagEt.setAdapter(adapter);
		}
	}

	@Override
	public void onClick(View v) {
		sendClick(v);
	}
	private void sendClick(View v) {
		if (!NetStateUtil.isNetworkAvailable(getActivity())) {
			Toast.makeText(getActivity(), "网络不可用,发送失败", Toast.LENGTH_SHORT).show();
			return;
		}
		final String tag = tagEt.getText().toString().trim();
		if (TextUtils.isEmpty(tag)) {
			Toast.makeText(getActivity(), "标签名不可为空", Toast.LENGTH_SHORT).show();
			return;
		}
		final String subHead = ((EditText)root.findViewById(R.id.title_et)).getText().toString();
		final String content = ((EditText)root.findViewById(R.id.content_et)).getText().toString();
		if (TextUtils.isEmpty(content)) {
			Toast.makeText(getActivity(), "请输入发送内容", Toast.LENGTH_SHORT).show();
			return;
		} else {
			new AsyncTask<Void, Void, String>(){
				
				protected void onPreExecute() {
					Toast.makeText(getActivity(), "发送中,请稍候...", Toast.LENGTH_SHORT).show();
				}
				@Override
				protected String doInBackground(Void... params) {
					MPush mPush = new MPush(getActivity());
					String flag = new MPush(getActivity()).sendByTag("标签广播", subHead,content,tag);
					if ("发送成功".equals(flag)) {
						mPush.saveSendInfo("标签广播",subHead,content);
					}
					return flag;
				}
				
				protected void onPostExecute(String result) {
					Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
				}
			}.execute();
		}
	}
}
