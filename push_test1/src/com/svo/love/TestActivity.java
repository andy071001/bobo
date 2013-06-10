package com.svo.love;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.umeng.socialize.view.ActionBarView;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.re);
      //创建ActionBar des参数是ActionBar的唯一标识，请确保不为空
        ActionBarView socializeActionBar = new ActionBarView(this,"android");

        LayoutParams layoutParams = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        socializeActionBar.setLayoutParams(layoutParams);
        //添加ActionBar
        relativeLayout.addView(socializeActionBar);
    }
}
