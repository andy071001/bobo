package com.svo.love.activity;

import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.svo.love.R;
import com.svo.love.model.MMsg;
import com.svo.love.model.MRece;
import com.svo.love.model.entity.SendEntity;
import com.svo.love.util.TimeConvert;

public class HistoryActvity extends Activity implements OnItemLongClickListener{
	
	private ListView listView;
	private LinkedList<SendEntity> sendEntities;
	private SendAdapter adapter;
	private LinearLayout no_pro;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		listView = (ListView) findViewById(R.id.listView1);
		listView.setOnItemLongClickListener(this);
		no_pro = (LinearLayout) findViewById(R.id.no_par);
	}
	protected void onResume() {
		super.onResume();
		sendEntities = new MMsg(this).getSendMsg();
		if (sendEntities.size() == 0) {
			no_pro.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		} else {
			listView.setVisibility(View.VISIBLE);
			no_pro.setVisibility(View.GONE);
			adapter = new SendAdapter(sendEntities);
			listView.setAdapter(adapter);
		}
	}
	public void go_broad(View view) {
		startActivity(new Intent(this, PublishActivity.class));
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2,long arg3) {
		new AlertDialog.Builder(this).setTitle("确认").setMessage("删除这条信息？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new MRece(HistoryActvity.this).delOneItem(sendEntities.get(arg2).get_id());
				sendEntities.remove(arg2);
				adapter.notifyDataSetChanged();
			}
		}).setNegativeButton("取消", null).show();
		return false;
	}
	private class SendAdapter extends BaseAdapter{
		private LinkedList<SendEntity> sendEntities;
		public SendAdapter(LinkedList<SendEntity> sendEntities) {
			this.sendEntities = sendEntities;
		}

		@Override
		public int getCount() {
			return sendEntities.size();
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
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(HistoryActvity.this).inflate(R.layout.msg_item, null);
				holder = new ViewHolder();
				holder.icon = (TextView) convertView.findViewById(R.id.msg_type);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.msg = (TextView) convertView.findViewById(R.id.msg_tv);
                holder.time = (TextView) convertView.findViewById(R.id.rece_time);
                convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			SendEntity entity = sendEntities.get(position);
			holder.name.setText(entity.getSubHead());
			holder.msg.setText(entity.getContent());
			holder.time.setText(TimeConvert.formatDate(Long.parseLong(entity.getSend_time())));
			holder.icon.setText(entity.getTitle());
			return convertView;
		}
	}
	static class ViewHolder {
		TextView icon;
		TextView name;
		TextView msg;
		TextView time;
	}
}
