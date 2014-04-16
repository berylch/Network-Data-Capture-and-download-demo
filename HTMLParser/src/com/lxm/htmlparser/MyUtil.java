package com.lxm.htmlparser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.github.snowdream.android.app.DownloadTask;

public class MyUtil {
	public static final int cNetError = 0;
	public static final int UploadUrl = 1;
	public static final int QuitLoad = 2;
	public static final int cIntruptError = 3;
	public static final int UploadUrlEnd = 4;
	public static final int Uploading = 5;
	public static final int goingUpload = 6;
	public static final int NoMore = 7;
	public static final int Cancel = 8;
	public static final int Search = 9;
	public static final int Title = 10;
	public static final int GetDownURL = 11;
	public static final int DownLoadErr = 12;
	public static final int DownLoadOver = 13;
	public static final int clean = 14;
	public static final int UploadUrlforup = 15;

	public static boolean isNetworkAvailable(Context con) {
		ConnectivityManager cm = (ConnectivityManager) con
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null)
			return false;
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if (netinfo == null) {
			return false;
		}
		if (netinfo.isConnected()) {
			return true;
		}
		return false;
	}

	public static void fenxiang(Context context, DownloadTask Task) {

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain"); // "image/*"
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享小说，推荐分享到微信朋友圈");
		intent.putExtra(
				Intent.EXTRA_TEXT,
				"小说名:" + Task.getName() + "\n" + "大小："
						+ numberFormat2(Task.getSize()) + "\n" + "下载地址："
						+ Task.getUrl() + "\n本推荐來自"
						+ context.getResources().getText(R.string.app_name)
						+ ".apk" + "\n下载地址：");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(intent, "选择分享类型"));
	}

	public static void OpenSetting(Context con) {
		if (android.os.Build.VERSION.SDK_INT > 10) {
			// 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
			con.startActivity(new Intent(
					android.provider.Settings.ACTION_SETTINGS));
		} else {
			con.startActivity(new Intent(
					android.provider.Settings.ACTION_WIRELESS_SETTINGS));
		}
	}

	public static MyProgressDialog showprocessDialog(Context con) {
		MyProgressDialog pd = new MyProgressDialog(con);
		pd.show();
		pd.setContentView(R.layout.myprogress);
		// pd.show(con, "处理", "正在处理中…");
		return pd;
	}

	public static String numberFormat(double num1, double num2) {

		// int num1 = 2;

		// int num2 = 3;

		NumberFormat numberFormat = NumberFormat.getInstance();

		numberFormat.setMaximumFractionDigits(2);
		if (num1 == 0 || num2 == 0) {
			return "0.00%";
		}
		return numberFormat.format((double) num1 / (double) num2 * 100) + "%";

		// System.out.print(result);
	}

	public static String numberFormat2(double num1) {

		NumberFormat numberFormat = NumberFormat.getInstance();

		numberFormat.setMaximumFractionDigits(2);
		if (num1 < 1024) {
			return numberFormat.format((double) num1) + "byte";
		} else if (num1 < 1024 * 1024) {
			return numberFormat.format((double) num1 / 1024) + "K";
		} else {
			return numberFormat.format((double) num1 / (1024 * 1024)) + "M";
		}

		// System.out.print(result);
	}

	/**
	 * 正则替换字符串里面的汉字部分。
	 * 
	 * @author 赵学庆 www.java2000.net
	 */

	private static String zhPattern = "[\u4e00-\u9fa5]+";

	/**
	 * 替换字符串卷
	 * 
	 * @param str
	 *            被替换的字符串
	 * @param charset
	 *            字符集
	 * @return 替换好的
	 * @throws UnsupportedEncodingException
	 *             不支持的字符集
	 */
	public static String encode(String str, String charset)
			throws UnsupportedEncodingException {
		Pattern p = Pattern.compile(zhPattern);
		Matcher m = p.matcher(str);
		StringBuffer b = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(b, URLEncoder.encode(m.group(0), charset));
		}
		m.appendTail(b);
		return b.toString();
	}

}
