package com.svo.love.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间转换工具类
 * @author duweibin 
 * @version 创建时间：2012-10-29 上午10:30:26
 */
public class TimeConvert {

	/**
	 * 换算时间,换算为日期格式,如:2012-10-29,昨天,前天,1分钟前等等
	 * @param t 长整型时间
	 */
	public static String formatDate(long t) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String curTime = sdf.format(new Date()).toString();//当前毫秒数
		String time = sdf.format(t);//传过来的时间
		if (time.startsWith(curTime.substring(0,10))) {
			return "今天 "+time.substring(11);
		}else {
			return time.substring(5);
		}
	}
}
