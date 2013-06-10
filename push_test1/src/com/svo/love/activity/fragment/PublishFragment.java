package com.svo.love.activity.fragment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.svo.love.R;
import com.svo.love.activity.PublishActivity;
import com.svo.love.activity.ReplyActivity;
import com.svo.love.model.MMsg;
import com.svo.love.model.MRece;
import com.svo.love.model.entity.ReceEntity;
import com.svo.love.util.IConstants;
import com.svo.love.util.TimeConvert;

/**
 * 广播大厅
 * @author duweibn
 */
public class PublishFragment extends Fragment implements OnItemClickListener,OnItemLongClickListener{
	
	private static final String TAG = "PublishFragment";
	private MsgReceiver msgReceiver;
	private ListView msg_list;
	private ArrayList<ReceEntity> entities;
	private Spinner filter_spinner;
	private MsgAdapter msgAdapter;
	private TextView no_info_tv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		View view = inflater.inflate(R.layout.publish_fragment, null);
		filter_spinner = (Spinner) view.findViewById(R.id.filter_spinner);
		filter_spinner.setOnItemSelectedListener(spinner_listener);
		no_info_tv = (TextView) view.findViewById(R.id.no_info_tv);
		Button sendBtn = (Button) view.findViewById(R.id.send_btn);
		//我要广播点击事件
		sendBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				preferences.edit().putBoolean("isFirst", false).commit();
				startActivity(new Intent(getActivity(), PublishActivity.class));
			}
		});
		msg_list = (ListView) view.findViewById(R.id.msgListview);
		msg_list.setOnItemClickListener(this);
		msg_list.setOnItemLongClickListener(this);
		msgReceiver = new MsgReceiver();
		
		return view;
	}
	
	private void refreshView() {
		entities = new MMsg(getActivity()).getMsg();
		if (entities.size() == 0) {
			no_info_tv.setVisibility(View.VISIBLE);
		}else {
			no_info_tv.setVisibility(View.GONE);
		}
		msgAdapter = new MsgAdapter();
		msg_list.setAdapter(msgAdapter);
	}
	@Override
	public void onResume() {
		super.onResume();
		refreshView();
		Log.i(TAG, "onResume");
		IntentFilter filter = new IntentFilter(IConstants.MSGFRAGMENT_RECE);
		getActivity().registerReceiver(msgReceiver, filter);
	}
	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(msgReceiver);
	}
	private OnItemSelectedListener spinner_listener = new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			switch (arg2) {
			case 0: //所有
				entities = new MMsg(getActivity()).getMsg();
				break;
			case 1:
				entities = new MMsg(getActivity()).getMsg_man(MMsg.broadcast_tab);
				break;
			case 2:
				entities = new MMsg(getActivity()).getMsg_woman(MMsg.broadcast_tab);
				break;
			}
			msgAdapter.notifyDataSetChanged();
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	};
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
	
	private class MsgAdapter extends BaseAdapter{
		public int getCount() {
			return entities.size();
		}
		public Object getItem(int arg0) {
			return arg0;
		}
		public long getItemId(int position) {
			return position;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.msg_item, null);
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
			holder.msg.setText(entity.getSubHead());
			holder.time.setText(TimeConvert.formatDate(Long.parseLong(entity.getTime())));
			if (!entity.isRead()) {
				holder.name.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.news, 0);
			}else {
				holder.name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			}
			return convertView;
		}
	}
	static class ViewHolder {
		TextView icon;
		TextView name;
		TextView msg;
		TextView time;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		//标记为已读
		new MMsg(getActivity()).update2read(entities.get(arg2).get_id(),"rece");
		Intent intent = new Intent(getActivity(),ReplyActivity.class);
		intent.putExtra("entities", entities);
		intent.putExtra("position", arg2);
		startActivity(intent);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2,long arg3) {
		new AlertDialog.Builder(getActivity()).setTitle("确认").setMessage("删除这条信息？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new MRece(getActivity()).delOneItem(entities.get(arg2).get_id());
				entities.remove(arg2);
				msgAdapter.notifyDataSetChanged();
			}
		}).setNegativeButton("取消", null).show();
		return false;
	}
}
