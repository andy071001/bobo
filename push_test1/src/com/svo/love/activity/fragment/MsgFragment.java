package com.svo.love.activity.fragment;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.svo.love.MainActivity;
import com.svo.love.R;
import com.svo.love.activity.Chat;
import com.svo.love.model.MMsg;
import com.svo.love.model.entity.ReceEntity;
import com.svo.love.util.IConstants;
import com.svo.love.util.TimeConvert;

public class MsgFragment extends Fragment implements OnItemClickListener {
	
//	private static final String TAG = "MsgFragment";
	private MsgReceiver msgReceiver;
	private ListView msg_list;
	private ArrayList<ReceEntity> entities;
	private TextView no_msg_tv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.msg_fragment, null);
		msg_list = (ListView) view.findViewById(R.id.msgListview);
		no_msg_tv = (TextView) view.findViewById(R.id.no_info_tv);
		msg_list.setOnItemClickListener(this);
		msgReceiver = new MsgReceiver();
		return view;
	}
	
	private void refreshView() {
		entities = new MMsg(getActivity()).getMsg(MMsg.friend_tab);
		if (entities == null || entities.size() == 0) {
			no_msg_tv.setVisibility(View.VISIBLE);
		} else {
			no_msg_tv.setVisibility(View.INVISIBLE);
			msg_list.setAdapter(new MsgAdapter(getActivity(),entities));
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		refreshView();
		IntentFilter filter = new IntentFilter(IConstants.FRIDEND_MSG);
		getActivity().registerReceiver(msgReceiver, filter);
		IntentFilter filter2 = new IntentFilter(IConstants.TAG_MSG);
		getActivity().registerReceiver(msgReceiver, filter2);
	}
	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(msgReceiver);
	}
	/**
	 * 接收消息广播
	 * @author duweibin
	 */
	private class MsgReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			refreshView();
		}
	}
	
	private static class MsgAdapter extends BaseAdapter{
		private ArrayList<ReceEntity> entities;
		private Context context;
		public MsgAdapter(FragmentActivity fragmentActivity, ArrayList<ReceEntity> entities) {
			this.entities = entities;
			context = fragmentActivity;
		}

		@Override
		public int getCount() {
			return entities.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.msg_item, null);
				holder = new ViewHolder();
				holder.icon = (TextView) convertView.findViewById(R.id.msg_type);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.msg = (TextView) convertView.findViewById(R.id.msg_tv);
                holder.time = (TextView) convertView.findViewById(R.id.rece_time);
                convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			ReceEntity entity = entities.get(position);
			holder.icon.setText(entity.getTitle());
			holder.name.setText(entity.getName());
			holder.msg.setText(entity.getMsg());
			holder.time.setText(TimeConvert.formatDate((Long.parseLong(entity.getReceTime()))));
			if (!entity.isRead()) {
				holder.name.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.news, 0);
			}else {
				holder.name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			}
			return convertView;
		}
		static class ViewHolder {
			TextView icon;
            TextView name;
            TextView msg;
            TextView time;
        }
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ReceEntity receEntity = entities.get(arg2);
		//标记为已读
		new MMsg(getActivity()).update2read(receEntity.get_id(),"friend_msg");
		Intent intent = new Intent(getActivity(),Chat.class);
		intent.putExtra("deviceId", receEntity.getImei());
		intent.putExtra("friendName", receEntity.getName());
		startActivity(intent);
	}
}
