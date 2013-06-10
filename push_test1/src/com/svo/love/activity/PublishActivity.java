package com.svo.love.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TabWidget;

import com.svo.love.R;
import com.svo.love.activity.fragment.Tab3Fragment;
import com.svo.love.activity.fragment.Tab4Fragment;
import com.svo.love.activity.fragment.TaboneFragment;
import com.svo.love.activity.fragment.TabtwoFragment;

public class PublishActivity extends FragmentActivity {
	private FragmentTabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.publish);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator("找知己"),
				TaboneFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("contacts").setIndicator("爱情宣言"),
				TabtwoFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("custom").setIndicator("标签"),
				Tab3Fragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("throttle").setIndicator("自定义"),
				Tab4Fragment.class, null);
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float density = metrics.densityDpi/160f;
		TabWidget tabWidget = mTabHost.getTabWidget();
		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			tabWidget.getChildAt(i).getLayoutParams().height = (int) (45*density);
		}
	}
}
