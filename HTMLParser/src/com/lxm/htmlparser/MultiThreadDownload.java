﻿package com.lxm.htmlparser;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public final class MultiThreadDownload extends Thread {
	public int id;
	private RandomAccessFile savedFile;
	private String path;
	/* 当前已下载量 */
	public int currentDownloadSize = 0;
	/* 下载状态 */
	public boolean finished;
	/* 用于监视下载状态 */
	private final DownloadService downloadService;
	/* 线程下载任务的起始点 */
	public int start;
	/* 线程下载任务的结束点 */
	private int end;
	Handler CallerHandler;
	private String filename;
	public MultiThreadDownload(int id, File savedFile, int block, String path, Integer downlength, DownloadService downloadService,Handler CallerHandler,String filename) throws Exception {
		this.id = id;
		this.path = path;
		if (downlength != null) this.currentDownloadSize = downlength;
		this.savedFile = new RandomAccessFile(savedFile, "rwd");
		this.downloadService = downloadService;
		start = id * block + currentDownloadSize;
		end = (id + 1) * block;
		this.CallerHandler=CallerHandler;
		this.filename=filename;
		Log.i("lxm", ""+start);
	}
	
	public String getdownloadName()
	{
		return filename;
	}
	private void SendMessageToCaller(int error, Object errString) {
		Message MyMessage = CallerHandler.obtainMessage(error, errString);
		CallerHandler.sendMessage(MyMessage);
	}
	@Override
	public void run() {
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
			conn.setConnectTimeout(10000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("RANGE", "bytes=" + start + "-" + end); // 设置获取数据的范围

			InputStream in = conn.getInputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			savedFile.seek(start);
			while (!downloadService.isPause && (len = in.read(buffer)) != -1) {
				savedFile.write(buffer, 0, len);
				currentDownloadSize += len;
			}
			savedFile.close();
			in.close();
			conn.disconnect();
			if (!downloadService.isPause) Log.i(DownloadService.TAG, "Thread " + (this.id + 1) + "finished"+(end-start));
			finished = true;
		} catch (Exception e) {
			e.printStackTrace();
			SendMessageToCaller(MyUtil.DownLoadErr,filename);
		}
	}
}
