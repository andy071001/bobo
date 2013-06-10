package com.svo.love.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.svo.love.R;
import com.svo.love.model.MMsg;
import com.svo.love.model.MPush;
import com.svo.love.model.entity.ReceEntity;

/**
 * 广播查看详细页面
 * @author duweibn
 */
public class ReplyActivity extends Activity {
	private ArrayList<ReceEntity> entities;
	private int position;
	private TextView name_tv;// 顶部名字textview
	private ReceEntity curEntity;// 当前实体
	private ImageView top_back;
	private ViewPager viewPager;
	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			curEntity = entities.get(arg0);
			name_tv.setText(curEntity.getName());
			//标记为已读
			new MMsg(ReplyActivity.this).update2read(curEntity.get_id(),"rece");
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply);
		// /顶部回退按钮
		top_back = (ImageView) findViewById(R.id.top_back);
		top_back.setOnClickListener(backListener);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		name_tv = (TextView) findViewById(R.id.name_tv);
		// 接收传送过来的内容
		entities = (ArrayList<ReceEntity>) getIntent().getSerializableExtra(
				"entities");
		position = getIntent().getIntExtra("position", 0);

		curEntity = entities.get(position);
		name_tv.setText(curEntity.getName());
		viewPager.setOnPageChangeListener(pageChangeListener);
		viewPager.setAdapter(new DetailAdapter());
		viewPager.setCurrentItem(position);
	}

	private OnClickListener backListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	/**
	 * 点击回复消息
	 */
	public void click(View view) {
		int viewId = view.getId();
		if (viewId == R.id.friend_detail) {
			Intent intent = new Intent(this, FriendDetailActivity.class);
			intent.putExtra("entity", entities.get(viewPager.getCurrentItem()));
			intent.putExtra("from", "ReplyActivity");
			startActivity(intent);
		} else {
			ReceEntity entity = entities.get(position);
			MPush push = new MPush(this);
			
			// 判断是否为空
			EditText replyEt = (EditText) findViewById(R.id.editText1);
			String replyTxt = replyEt.getText().toString();
			if (TextUtils.isEmpty(replyTxt)) {
				Toast.makeText(this, "请输入回复内容", Toast.LENGTH_SHORT).show();
				return;
			}
			boolean flag = push.sendWithImei(replyTxt, entity.getImei(),"reply");
			if (flag) {
				Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
				replyEt.setText("");
			} else {
				Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
			}
		}
	}

	class DetailAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return entities.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = LayoutInflater.from(ReplyActivity.this).inflate(R.layout.msg_detail_item, null);
			TextView textView1 = (TextView) view.findViewById(R.id.purpose_content);
			TextView textView2 = (TextView) view.findViewById(R.id.content);
			TextView title_tv = (TextView) view.findViewById(R.id.title);
			textView1.setText("播播类型:"+entities.get(position).getTitle());
			textView2.setText("内容:　"+entities.get(position).getMsg());
			title_tv.setText("标题:　"+entities.get(position).getSubHead());
			((ViewPager) container).addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
}
