package com.lxm.htmlparser;

import java.util.ArrayList;

import org.jsoup.nodes.Document;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class Mydownloadgetdata extends Thread {
	private String Url;
	public static Handler MyHandle = null;
	boolean bExitFlag = false;
	public Handler CallerHandler;
	public static boolean bIsRuning = false;
	Document Doc = null;
	DBHelper dbHelper;// = new DBHelper(context);
	Context c;
	//DBDownLoadEnd dbOk;

	public Mydownloadgetdata(Handler CallerHandler, Context c) {
		// this.Url = Url;
		this.CallerHandler = CallerHandler;
		dbHelper = new DBHelper(c);
		//dbOk=new DBDownLoadEnd(c);
	}

	public Handler GetMyHandle() {
		return MyHandle;
	}

	// 运行
	public void run() {
		Looper.prepare();

		MyHandle = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				// 开始加载
				case MyUtil.UploadUrl:
					bIsRuning = true;
					UpdataAdapter(MyUtil.Uploading);
					UpdataAdapterOk(MyUtil.Uploading);
					bIsRuning = false;
					// Log.i("lxm", "2222");
					break;
				case MyUtil.UploadUrlforup:
					bIsRuning = true;
					UpdataAdapter(MyUtil.UploadUrlforup);
					UpdataAdapterOk(MyUtil.UploadUrlforup);
					bIsRuning = false;
					// Log.i("lxm", "2222");
					break;
				default:
					break;
				}
			};

		};
		Looper.loop();

	}

	private void SendMessageToCaller(int error, Object errString) {
		Message MyMessage = CallerHandler.obtainMessage(error, errString);
		CallerHandler.sendMessage(MyMessage);
	}

	public void UpdataAdapter(int Type) {
		// 获取数据库Phones的Cursor
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cur = db.query("fileDownloading", new String[] { "downLength",
				"downSize", "downname" }, null, null, null, null, null);
		// dbHelper.
		int miCount = cur.getCount();
		// Log.i("down", ""+miCount);
		ArrayList Arr = new ArrayList();
		//Arr.add("下载列表：" + "\n" + String.valueOf(miCount));
		// Arr.add("333");
		//SendMessageToCaller(MyUtil.Title, Arr);
		if (cur.moveToFirst())
			;
		while (cur != null && cur.getCount() > 0) {
			ArrayList Arr2 = new ArrayList();

			// Log.i("down", "" + cur.getString(0));
			// Log.i("down", "" + cur.getString(1));
			// Log.i("down", "" + cur.getString(2));
			double num1 = 0;
			double num2 = 1;
			// Arr.clear();
			Arr2.add(cur.getString(2));
			try {
				num1 = Integer.parseInt(cur.getString(0));
				num2 = Integer.parseInt(cur.getString(1));
			} catch (Exception e) {
				// TODO: handle exception
			}
			// Log.i("down", MyUtil.numberFormat(num1,num2));
			Arr2.add(MyUtil.numberFormat(num1, num2));
			SendMessageToCaller(Type, Arr2);
			if (!cur.moveToNext())
				break;
			// }
			//db.close();
			//SendMessageToCaller(MyUtil.UploadUrlEnd, "");
		}
		
		{
			db.close();
			SendMessageToCaller(MyUtil.UploadUrlEnd, "");
		}

	}
	public void UpdataAdapterOk(int Type) {
	
	}
}
