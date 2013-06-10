package com.svo.love.util;

import android.content.Context;

public class Constants implements IConstants{
	Context context;
	public Constants(Context context) {
		this.context = context;
		APP_ROOT = context.getCacheDir().getAbsolutePath();
		HEAD_ICON_PATH = APP_ROOT + "/headPic/";
	}
	public static String APP_ROOT;
	public static String HEAD_ICON_PATH;
}
