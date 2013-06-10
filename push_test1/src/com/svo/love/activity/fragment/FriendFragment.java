package com.svo.love.activity.fragment;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.LinkedList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.svo.love.R;
import com.svo.love.activity.AddFriend;
import com.svo.love.activity.FriendDetailActivity;
import com.svo.love.model.MFriend;
import com.svo.love.model.entity.Friend;
import com.svo.love.model.entity.ReceEntity;
import com.svo.love.model.thread.PicAsyncTask;
import com.svo.love.util.Constants;
import com.svo.love.util.PicUtil;

public class FriendFragment extends Fragment implements OnItemLongClickListener,OnItemClickListener,View.OnClickListener{
	private View rootView;
	private ListView listView;
	private LinkedList<Friend> friends;
	private MFriend mFriend;
	private FriendAdapter adapter;
	private Button addFriend_btn;
	private TextView no_friend_tv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.friend_fragment, null);
		mFriend = new MFriend(getActivity());
		listView = (ListView) rootView.findViewById(R.id.friend_listview);
		addFriend_btn = (Button) rootView.findViewById(R.id.top_right);
		no_friend_tv = (TextView) rootView.findViewById(R.id.no_info_tv);
		addFriend_btn.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		return rootView;
	}
	@Override
	public void onResume() {
		super.onResume();
		friends = mFriend.getFriends();
		if (friends == null || friends.size() == 0) {
			no_friend_tv.setVisibility(View.VISIBLE);
		}else {
			no_friend_tv.setVisibility(View.INVISIBLE);
			adapter = new FriendAdapter();
			listView.setAdapter(adapter);
		}
	}
	private class FriendAdapter extends BaseAdapter{
		public FriendAdapter() {
			//下载自己的头像
			new Thread(){
				public void run() {
					File dir = new File(Constants.HEAD_ICON_PATH);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					String my_icon_url = getActivity().getSharedPreferences(Constants.PREFERENCE_NAME, 0).getString("icon_url", "");
					File file = new File(dir,my_icon_url.hashCode()+"");
					if (file != null && file.exists() || TextUtils.isEmpty(my_icon_url)) {
						return;
					}
					try {
						Log.i("json", "url:"+my_icon_url);
						PicUtil.savePic(file, my_icon_url);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
		@Override
		public int getCount() {
			return friends.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			convertView = inflater.inflate(R.layout.friend_item, null);
			ImageView imageView = (ImageView) convertView.findViewById(R.id.touxiang);
			TextView textView = (TextView) convertView.findViewById(R.id.name);
			String icon_url = friends.get(position).getIcon_url();
			if (!TextUtils.isEmpty(icon_url)) {
				new PicAsyncTask(imageView, icon_url).execute();
			}
			textView.setText(friends.get(position).getUser_name());
			return convertView;
		}
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2,
			long arg3) {
		new AlertDialog.Builder(getActivity()).setTitle("确认").setMessage("删除该好友？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				boolean flag = mFriend.delFriend(friends.get(arg2).get_id());
				String result = "删除好友成功";
				if (!flag) {
					result = "删除好友失败";
				}
				Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
				friends.remove(arg2);
				adapter.notifyDataSetChanged();
			}
		}).setNegativeButton("取消", null).show();
		return false;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(getActivity(), FriendDetailActivity.class);
		intent.putExtra("from", "FriendFragment");
		Friend friend = friends.get(arg2);
		ReceEntity receEntity = new ReceEntity();
		receEntity.setAge(friend.getAge());
		receEntity.setName(friend.getUser_name());
		receEntity.setQianMing(friend.getQianMing());
		receEntity.setCity(friend.getCity());
		receEntity.setImei(friend.getUser_deviceId());
		receEntity.setSex(friend.getSex());
		receEntity.setIcon_url(friend.getIcon_url());
		intent.putExtra("entity", receEntity);
		startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {
		startActivity(new Intent(getActivity(), AddFriend.class));
	}
}
