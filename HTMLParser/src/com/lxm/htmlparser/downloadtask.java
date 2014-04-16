package com.lxm.htmlparser;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

class downloadtask implements Runnable {
	private DownloadService servcie;
	private String target;
	private Context context;
	private String filename;
	Handler CallerHandler;
	public downloadtask(String target, Context context,String filename,Handler CallerHandler)  {
		this.target = target;
		this.context = context;
		this.filename=filename;
		this.CallerHandler=CallerHandler;
	}

	@Override
	public void run() {
		try {
			//Log.i("lxm", "download");
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File destination = Environment.getExternalStorageDirectory();
				servcie = new DownloadService(target, destination, 1, context,filename,CallerHandler);
			}

			servcie.download(new DownloadListener() {

				@Override
				public void onDownload(int downloaded_size) {
					Message message = new Message();
					message.what = 1;
					message.getData().putInt("size", downloaded_size);
					// handler.sendMessage(message);
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
