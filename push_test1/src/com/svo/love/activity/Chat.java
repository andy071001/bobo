package com.svo.love.activity;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.svo.love.R;
import com.svo.love.model.MMsg;
import com.svo.love.model.MPush;
import com.svo.love.model.entity.ReceEntity;
import com.svo.love.util.Constants;
import com.svo.love.util.IConstants;
import com.svo.love.util.NetStateUtil;
import com.svo.love.util.TimeConvert;
/**
 * send表示发送出去的信息 实体setTime(),send
 * @author Administrator
 *
 */
public class Chat extends Activity {
	private ListView listView;
	private List<ReceEntity> entities;
	private ChatAdapter adapter;
	private String deviceId;
	private String friendName;
	private MsgReceiver msgReceiver;
	private EditText replyEt;
	private String icon_url;//好友图片URL地址
	private TextView friend_name_tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);
		listView = (ListView) findViewById(R.id.chat_list);
		replyEt = (EditText) findViewById(R.id.editText1);
		friend_name_tv = (TextView) findViewById(R.id.name_tv);
		deviceId = getIntent().getStringExtra("deviceId");
		friendName = getIntent().getStringExtra("friendName");
		friend_name_tv.setText(friendName);
		icon_url = getIntent().getStringExtra("icon_url");
		msgReceiver = new MsgReceiver();
		listView.setDivider(null);
		refreshView();
		registerForContextMenu(listView);
	}
	/**
	 * 发送信息按钮
	 * @param view
	 */
	public void click(View view) {
		MPush push = new MPush(this);
		String replyTxt = replyEt.getText().toString();
		if (!NetStateUtil.isNetworkAvailable(this)) {
			Toast.makeText(this, "网络不可用,请开启网络", Toast.LENGTH_SHORT).show();
		}
		if (TextUtils.isEmpty(replyTxt)) {
			Toast.makeText(this, "请输入回复内容", Toast.LENGTH_SHORT).show();
			return;
		}
		boolean flag = push.sendWithImei(replyTxt, deviceId,"chat");
		if (flag) {
			Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
			replyEt.setText("");
			//界面显示发送的信息
			ReceEntity receEntity = new ReceEntity();
			receEntity.setReceTime(System.currentTimeMillis()+"");
			receEntity.setMsg(replyTxt);
			receEntity.setTime("send");
			entities.add(receEntity);
			adapter.notifyDataSetChanged();
			listView.setSelection(entities.size()-1);
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); 
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
		} else {
			Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
		}
	}
	//查看对方详细资料按钮点击事件
	public void fri_detail(View view) {
		Intent intent = new Intent(this, FriendDetailActivity.class);
		intent.putExtra("deviceId", deviceId);
		intent.putExtra("from", "Chat");
		startActivity(intent);
	}
	/**
	 * 接收消息广播
	 * @author duweibin
	 */
	private class MsgReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			refreshView();
			new MMsg(context).update2read(deviceId);
		}
	}
	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(msgReceiver);
	}
	@Override
	public void onResume() {
		super.onResume();
		refreshView();
		IntentFilter filter = new IntentFilter(IConstants.FRIDEND_MSG);
		registerReceiver(msgReceiver, filter);
	}
	//刷新界面
	private void refreshView() {
		entities = new MMsg(this).getChatMsg(deviceId);
		adapter = new ChatAdapter();
		listView.setAdapter(adapter);
		listView.setSelection(adapter.getCount()-1);
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(friendName);
		menu.add(0, 0, 0, "删除消息");
		menu.add(0, 1, 1, "复制消息");
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		ReceEntity entity = entities.get(info.position);
		switch (item.getItemId()) {
		case 0:
			boolean flag = new MMsg(Chat.this).delMsg(deviceId,entity.getMsg());
			String hint = "删除成功";
			if (flag) {
				entities.remove(info.position);
				adapter.notifyDataSetChanged();
			} else {
				hint = "删除失败";
			}
			Toast.makeText(this, hint, Toast.LENGTH_SHORT).show();
			break;
		case 1:
			ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
			clip.setText(entity.getMsg()); // 复制
			Toast.makeText(this, "已复制到剪切板", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	//返回按钮点击事件
	public void back(View view) {
		finish();
	}
	private class ChatAdapter extends BaseAdapter{
		private String my_icon_url;
		public ChatAdapter() {
			my_icon_url = getSharedPreferences(Constants.PREFERENCE_NAME, 0).getString("icon_url", "");
		}
		public int getCount() {
			return entities.size();
		}
		public Object getItem(int arg0) {
			return arg0;
		}
		public long getItemId(int position) {
			return position;
		}
		@Override
		public int getViewTypeCount() {
			return 2;
		}
		@Override
		public int getItemViewType(int position) {
			ReceEntity receEntity = entities.get(position);
			if ("send".equals(receEntity.getTime())) {
				return 1;//自己发送的,右边
			}else {
				return 0;//好友发送的,左边
			}
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			ViewHolder1 holder1 = null;
			int type = getItemViewType(position);
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(Chat.this);
				switch (type) {
				case 0://靠左
					convertView = inflater.inflate(R.layout.chat_item, null);
					holder = new ViewHolder();
					holder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
					holder.textView2 = (TextView) convertView.findViewById(R.id.textView2);
					holder.imageView = (ImageView) convertView.findViewById(R.id.head_icon);
					convertView.setTag(holder);
					break;
				case 1://靠右
					convertView = inflater.inflate(R.layout.chat_item2, null);
					holder1 = new ViewHolder1();
					holder1.textView1 = (TextView) convertView.findViewById(R.id.textView1);
					holder1.textView2 = (TextView) convertView.findViewById(R.id.textView2);
					holder1.imageView = (ImageView) convertView.findViewById(R.id.head_icon);
					convertView.setTag(holder1);
					break;
				}
			} else {
				switch (type) {
				case 0:
					holder = (ViewHolder) convertView.getTag();
					break;
				case 1:
					holder1 = (ViewHolder1) convertView.getTag();
					break;
				}
			}
			ReceEntity receEntity = entities.get(position);
			switch (type) {
			case 0:
				if (isTimeVis(entities,position)) {
					holder.textView1.setVisibility(View.VISIBLE);
					holder.textView1.setText(TimeConvert.formatDate(Long.parseLong(receEntity.getReceTime())));
				}else {
					holder.textView1.setVisibility(View.GONE);
				}
				holder.textView2.setText(receEntity.getMsg());
				holder.textView2.setBackgroundResource(R.drawable.chat_bg);
				try {
					Uri uri = Uri.parse(Constants.HEAD_ICON_PATH+icon_url.hashCode());
					if (uri != null) {
						holder.imageView.setImageURI(uri);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 1:
				if (isTimeVis(entities,position)) {
					holder1.textView1.setVisibility(View.VISIBLE);
					holder1.textView1.setText(TimeConvert.formatDate(Long.parseLong(receEntity.getReceTime())));
				}else {
					holder1.textView1.setVisibility(View.GONE);
				}
				holder1.textView1.setText(TimeConvert.formatDate(Long.parseLong(receEntity.getReceTime())));
				holder1.textView2.setText(receEntity.getMsg());
				holder1.textView2.setBackgroundResource(R.drawable.chatto_bg);
				if (!TextUtils.isEmpty(my_icon_url)) {
					holder1.imageView.setImageURI(Uri.parse(Constants.HEAD_ICON_PATH+my_icon_url.hashCode()));
				}
				break;
			}
			return convertView;
		}
		/**
		 * 是否显示时间
		 * @param entities
		 * @param position
		 * @return
		 */
		private boolean isTimeVis(List<ReceEntity> entities, int position) {
			if (position == 0) {
				return true;
			}
			long time1 = Long.parseLong(entities.get(position).getReceTime());
			long time2 = Long.parseLong(entities.get(position-1).getReceTime());
			if (time1 - time2 > 10*60*1000) {
				return true;
			}
			return false;
		}
	}
	static class ViewHolder{
		ImageView imageView;
		TextView textView1;
		TextView textView2;
	}
	static class ViewHolder1{
		ImageView imageView;
		TextView textView1;
		TextView textView2;
	}
}
