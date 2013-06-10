package com.svo.love.share;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.sso.SinaSsoHandler;

public class SocialConfig {
	public static boolean SUPPORT_SINA = true;
	public static boolean SUPPORT_RENR = true;
	public static boolean SUPPORT_DOUBAN = true;
	public static boolean SUPPORT_QZONE = true;
	public static boolean SUPPORT_TENC = true;

	private static final Set<WeakReference<SocializeConfig>> wCigs = new HashSet<WeakReference<SocializeConfig>>();

	/**
	 * demo 中需要和侧边栏配置联动，所以使用代理方式获取Config 实例。
	 * 
	 * @return
	 */
	public SocializeConfig getSocialConfig(final Activity context) {
		SocializeConfig config = new SocializeConfig();
		WeakReference<SocializeConfig> ref = new WeakReference<SocializeConfig>(config);
		wCigs.add(ref);

		List<SHARE_MEDIA> supportMedias = new ArrayList<SHARE_MEDIA>();
		if (SUPPORT_SINA) {
			supportMedias.add(SHARE_MEDIA.SINA);
		}
		if (SUPPORT_TENC) {
			supportMedias.add(SHARE_MEDIA.TENCENT);
		}
		if (SUPPORT_RENR) {
			supportMedias.add(SHARE_MEDIA.RENREN);
		}
		if (SUPPORT_QZONE) {
			supportMedias.add(SHARE_MEDIA.QZONE);
		}
		config.setPlatforms(supportMedias.toArray(new SHARE_MEDIA[supportMedias.size()]));
		config.setSinaSsoHandler(new SinaSsoHandler());
		return config;
	}
}
