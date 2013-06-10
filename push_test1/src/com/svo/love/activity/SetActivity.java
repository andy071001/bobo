package com.svo.love.activity;

import java.util.LinkedHashSet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.TextUtils;

import com.svo.love.ExampleApplication;
import com.svo.love.R;
import com.svo.love.model.MPush;
import com.svo.love.model.Mumeng;
import com.svo.love.util.IConstants;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

public class SetActivity extends PreferenceActivity implements OnPreferenceClickListener,OnPreferenceChangeListener{
	private Preference share_account;
	private Preference check_update;
	private Preference advise;
	private Preference about;
	private Preference author;
	private CheckBoxPreference sound_hint;
	private SharedPreferences settingSP;
	private EditTextPreference set_tag;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);
		settingSP = PreferenceManager.getDefaultSharedPreferences(this);
		share_account = findPreference("share_account");
		check_update = findPreference("check_update");
		advise = findPreference("advise");
		about = findPreference("about");
		author = findPreference("author");
		set_tag = (EditTextPreference) findPreference("set_tag");
		sound_hint = (CheckBoxPreference) findPreference("sound_hint");
		sound_hint.setChecked(settingSP.getBoolean("sound_hint", true));
		share_account.setOnPreferenceClickListener(this);
		check_update.setOnPreferenceClickListener(this);
		advise.setOnPreferenceClickListener(this);
		about.setOnPreferenceClickListener(this);
		author.setOnPreferenceClickListener(this);
		sound_hint.setOnPreferenceClickListener(this);
		set_tag.setOnPreferenceChangeListener(this);
		set_tag.setOnPreferenceClickListener(this);
		set_tag.setDialogMessage(Html.fromHtml("<font color='red' size='19px'>多个标签以逗号分隔</font>"));
	}
	@Override
	public boolean onPreferenceClick(Preference preference) {
		String key = preference.getKey();
		if ("share_account".equals(key)) {
			UMSocialService controller = UMServiceFactory.getUMSocialService("Android", RequestType.SOCIAL);
			controller.openUserCenter(this);
		}else if ("check_update".equals(key)) {
			new Mumeng().update(this);
		}else if ("advise".equals(key)) {
			FeedbackAgent feedbackAgent = new FeedbackAgent(this);
			feedbackAgent.startFeedbackActivity();
		}else if ("about".equals(key)) {
			Intent intent = new Intent(this, WebViewActivity.class);
			intent.putExtra("title", "关于播播");
			intent.putExtra("url", "file:///android_asset/about.html");
			startActivity(intent);
		}else if ("author".equals(key)) {
			Intent intent = new Intent(this, WebViewActivity.class);
			intent.putExtra("title", "关于作者");
			intent.putExtra("url", "file:///android_asset/about_author.html");
			startActivity(intent);
		}else if ("sound_hint".equals(key)) {
			ExampleApplication.sound_hint = sound_hint.isChecked();
			settingSP.edit().putBoolean("sound_hint", sound_hint.isChecked()).commit();
		}
		return true;
	}
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		String key = preference.getKey();
		if ("set_tag".equals(key)) {
			String value = String.valueOf(newValue);
			if (!TextUtils.isEmpty(value)) {
				String[] ss = value.replace("，", ",").split(",");
				String sex = getSharedPreferences(IConstants.PREFERENCE_NAME, Context.MODE_PRIVATE).getString("sex", "woman");
				LinkedHashSet<String> set = new LinkedHashSet<String>();
				set.add(sex);
				for (String s : ss) {
					if (!TextUtils.isEmpty(s)) {
						set.add(s.trim());
					}
				}
				new MPush(this).setTag(set, null);
			}
		}
		return true;
	}
}
