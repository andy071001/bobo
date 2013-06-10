package com.svo.love.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.text.TextUtils;

public class PicUtil {
	/**
	 * @param file
	 * @param tmpFile
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws FileNotFoundException
	 */
	public static File savePic(File file, String url) throws IOException,
			ClientProtocolException, FileNotFoundException {
		if (TextUtils.isEmpty(url)) {
			return null;
		}
		//httpGet连接对象  
		HttpGet httpRequest = new HttpGet(url);  
		//取得HttpClient 对象  
		HttpClient httpclient = new DefaultHttpClient();  
		//请求httpClient ，取得HttpRestponse  
		HttpResponse httpResponse = httpclient.execute(httpRequest);  
		if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  
		    //取得相关信息 取得HttpEntiy  
		    HttpEntity httpEntity = httpResponse.getEntity();  
		    //获得一个输入流  
		    InputStream is = httpEntity.getContent();  
		    BufferedInputStream bis = new BufferedInputStream(is);
		    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		    int length = 0;
		    byte[] buffer = new byte[4048];
		    while ((length = bis.read(buffer))>0) {
				bos.write(buffer, 0, length);
			}
		    is.close();  
		    bis.close();
		    bos.close();
		    return file;
		}else {
			return null;
		}
	}
}
