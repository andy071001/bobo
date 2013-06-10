package com.svo.love.model.thread;

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

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.svo.love.ExampleApplication;
import com.svo.love.R;
import com.svo.love.util.Constants;
import com.svo.love.util.PicUtil;

public class PicAsyncTask extends AsyncTask<Void, Void, File> {
	private ImageView imageView;
	private String url;
	public PicAsyncTask(ImageView imageView,String url) {
		this.imageView = imageView;
		this.url = url;
		File dir = new File(picPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
	String picPath = Constants.HEAD_ICON_PATH;
	@Override
	protected File doInBackground(Void... params) {
		File file = new File(picPath, url.hashCode()+"");
		if (file.exists()) {
			return file;
		}
		File tmpFile = new File(picPath, url.hashCode()+".tmp");
		if (tmpFile.exists()) {
			cancel(true);
		}
        try {  
        	File resultFile = PicUtil.savePic(tmpFile, url);
        	if (resultFile == null) {
				return null;
			}
        	boolean flag = resultFile.renameTo(file); 
        	if (flag) {
				return file;
			} else {
				return  tmpFile;
			}
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
		return file;
	}
	
	@Override
	protected void onPostExecute(File result) {
		super.onPostExecute(result);
		if (result != null) {
			imageView.setImageURI(Uri.parse(result.getAbsolutePath()));
		}else {
			imageView.setImageResource(R.drawable.umeng_xp_zhanwei);
		}
	}
}
