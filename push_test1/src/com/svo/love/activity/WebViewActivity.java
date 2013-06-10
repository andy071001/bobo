package com.svo.love.activity;

import com.svo.love.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

public class WebViewActivity extends Activity {
	private WebView webView;
	private TextView titleTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		Intent intent = getIntent();
		String title = intent.getStringExtra("title");
		String url = intent.getStringExtra("url");
		titleTv = (TextView) findViewById(R.id.title);
		titleTv.setText(title);
		webView = (WebView) findViewById(R.id.webView1);
		webView.loadUrl(url);
	}
}
