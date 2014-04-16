package com.lxm.htmlparser;

import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ReadUrlThread extends Thread {
	private String Url;
	public static Handler MyHandle = null;
	boolean bExitFlag = false;
	public Handler CallerHandler;
	public static boolean bIsRuning = false;
	Document Doc = null;
	private int num = 0;
	private static int getnum = 10;
	private String getlist;
	private boolean bCanLoading;
	private String bakUrl;
	private boolean cancel = false;
	private boolean bIsfirstRun = true;
	private String SeachStr = null;

	static public class SearchRet {
		boolean bCanLoading;
		boolean bIserror;
		boolean bIsend;
		ArrayList al;
		int searchNum;

		public SearchRet() {
			// TODO Auto-generated constructor stub
			bCanLoading = true;
			bIserror = false;
			bIsend = false;
			al = new ArrayList();
			searchNum = 0;
		}

		public void init() {
			// TODO Auto-generated constructor stub
			bCanLoading = true;
			bIserror = false;
			bIsend = false;
			al = new ArrayList();
			searchNum = 0;
		}
	}

	Search sear = new Search();
	private SearchRet Ret = new SearchRet();

	// 进行构造
	public ReadUrlThread(String Url, Handler CallerHandler) {
		this.Url = Url;
		this.CallerHandler = CallerHandler;
	}

	public ReadUrlThread(Handler CallerHandler) {
		// this.Url = Url;
		this.CallerHandler = CallerHandler;
	}

	public Handler GetMyHandle() {
		return MyHandle;
	}

	// 运行
	public void run() {
		Looper.prepare();

		bIsRuning = true;
		MyHandle = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				// 开始加载
				case MyUtil.UploadUrl:
					getlist = Url;
					// Gooning();
					bIsfirstRun = false;
					break;
				// 结束加载
				case MyUtil.QuitLoad:
					MyHandle.getLooper().quit();
					break;
				// 继续加载
				case MyUtil.goingUpload:
					if (!bIsfirstRun) {
						getlist = bakUrl;
						Gooning();
						bIsfirstRun = false;
					} else {
						getlist = Url;
						Gooning();
						bIsfirstRun = false;
					}
					break;
				// 撤销
				case MyUtil.Cancel:
					cancel = true;
					break;
				// 搜索
				case MyUtil.Search:
					Search();
					break;
				case MyUtil.GetDownURL:
					ArrayList al = (ArrayList) msg.obj;
					String geturl = (String) al.get(0);
					String getfilename = (String) al.get(1);
					getdownloadUrlEx(geturl, getfilename);
				}

			};

		};
		Looper.loop();
		bIsRuning = false;
	}

	private void Gooning() {

		int i = 0;
		do {
			Ret = sear.Searching();
			while (Ret.bIsend != true && i < getnum && cancel == false
					&& Ret.bCanLoading && (Ret.bIserror != true)) {

				SendMessageToCaller(MyUtil.Uploading, Ret.al);
				i++;
				if (i < getnum) {
					Ret = sear.Searching();
				}

			}
		} while (false);
		if (!Ret.bCanLoading && Ret.bIsend == true) {
			SendMessageToCaller(MyUtil.NoMore, "");
		} else {
			SendMessageToCaller(MyUtil.UploadUrlEnd, "");
		}
		cancel = false;

	}

	private void Search() {
		sear.init(SeachStr);// 初始化数据
		Ret.init(); // 初始化返回数据

		int i = 0;
		do {

			Ret = sear.initSql();
			if (Ret.bIsend == true || !Ret.bCanLoading
					|| (Ret.bIserror == true)) {
				if (Ret.bIsend == true) {
					SendMessageToCaller(MyUtil.Title, Ret.al);
				}
				break;
			}
			SendMessageToCaller(MyUtil.Title, Ret.al);
			Ret = sear.Searching();
			while (Ret.bIsend != true && i < getnum && cancel == false
					&& Ret.bCanLoading && (Ret.bIserror != true)) {

				SendMessageToCaller(MyUtil.Uploading, Ret.al);
				i++;
				if (i < getnum) {
					Ret = sear.Searching();
				}
			}
		} while (false);
		if (!Ret.bCanLoading && Ret.bIsend == true) {
			SendMessageToCaller(MyUtil.NoMore, "");
		} else {
			SendMessageToCaller(MyUtil.UploadUrlEnd, "");
		}
		cancel = false;
	}

	@SuppressWarnings("finally")
	private String UploadUrl_ex(String UpdaUrl) {
		try {
			num = 0;
			Doc = null;
			bakUrl = UpdaUrl;
			Connection conn = Jsoup.connect(UpdaUrl);
			Doc = Jsoup.connect(UpdaUrl).get();
		} catch (Exception e) {
			e.printStackTrace();
			SendMessageToCaller(MyUtil.cNetError, e.getMessage());
			bCanLoading = true;
			return null;
		} finally {
			if (Doc == null) {
				// 网络出错
				bCanLoading = true;
				SendMessageToCaller(MyUtil.cNetError, "error");
				return null;
			}

			Element Elem2 = Doc.getElementsByClass("article_next_prev").first();
			Elem2 = Elem2.select("li.prev_article").first();
			if (Elem2 == null) {
				bCanLoading = false;
				SendMessageToCaller(MyUtil.UploadUrlEnd, "error");
				return null;
			}
			Elem2 = Elem2.select("a[href]").first();
			ArrayList al = new ArrayList();
			al.add(Elem2.text());
			al.add(Elem2.attr("abs:href"));
			SendMessageToCaller(MyUtil.Uploading, al);
			return Elem2.attr("abs:href");
		}
	}

	private void SendMessageToCaller(int error, Object errString) {
		Message MyMessage = MyHandle.obtainMessage(error, errString);
		CallerHandler.sendMessage(MyMessage);
	}

	public void SetInitUrl(String SeachStr) {
		// this.Url=URl;
		cancel = false;
		bCanLoading = true;
		getlist = null;
		bIsfirstRun = true;
		this.SeachStr = SeachStr;
	}

	public void getdownloadUrlEx(String url, String filename) {
		String geturl = getdownloadUrl(url);
		ArrayList al = new ArrayList();
		if (geturl == null) {
			Log.i("lxm222", "geturl null");
		} else {

			Log.i("lxm222", "geturl=" + geturl);
		}
		if (geturl == null) {
			al.add("");
			al.add(filename);
			SendMessageToCaller(MyUtil.GetDownURL, al);
		} else {
			al.add(geturl);
			al.add(filename);
			SendMessageToCaller(MyUtil.GetDownURL, al);
		}
	}

	public String getdownloadUrl(String url) {
		
		Document Doc = null;
		try {

			Doc = null;
			Connection conn = Jsoup.connect(url);
			Doc = conn.get();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (Doc == null) {
				return null;
			}
			Element Elem2 = Doc.select("a.dla").first();
			if(Elem2==null)
			{
				return null;
			}
			return Elem2.attr("href");
		}

	}
}
