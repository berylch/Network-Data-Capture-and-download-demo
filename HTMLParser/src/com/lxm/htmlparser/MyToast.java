package com.lxm.htmlparser;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyToast {
	public static final int LENGTH_SHORT = android.widget.Toast.LENGTH_SHORT;
	public static final int LENGTH_LONG = android.widget.Toast.LENGTH_LONG;

	private static android.widget.Toast toast;
	private static Handler handler = new Handler();

	// 字串 +显示一个提示图
	public static void showToast(Context context, String text, int id) {
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.BOTTOM, 0, -20);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setDuration(Toast.LENGTH_SHORT);
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		ImageView iv = new ImageView(context);
		TextView tv = new TextView(context);
		tv.setText(text);
		iv.setImageResource(id);
		linearLayout.addView(iv);
		linearLayout.addView(tv);
		toast.setView(linearLayout);
		toast.show();
	}

	public static void showToast(Activity context, int text, int id) {
		ShowWH MyWh = DisplayWH.DisplayWH(context);
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.BOTTOM, 0, -30);
		toast.setDuration(Toast.LENGTH_SHORT);
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		ImageView iv = new ImageView(context);
		TextView tv = new TextView(context);
		tv.setText(text);
		iv.setImageResource(id);
		linearLayout.addView(iv);
		linearLayout.addView(tv);
		// linearLayout.setBackgroundColor(context.getResources().getColor
		// (android.R.color.background_dark));
		toast.setView(linearLayout);
		toast.show();
	}

	private static Runnable run = new Runnable() {
		public void run() {
			toast.cancel();
		}
	};

	private static void toast(Context ctx, CharSequence msg, int duration) {
		handler.removeCallbacks(run);
		// handler的duration不能直接对应Toast的常量时长，在此针对Toast的常量相应定义时长
		switch (duration) {
		case LENGTH_SHORT:// Toast.LENGTH_SHORT值为0，对应的持续时间大概为1s
			duration = 1000;
			break;
		case LENGTH_LONG:// Toast.LENGTH_LONG值为1，对应的持续时间大概为3s
			duration = 3000;
			break;
		default:
			break;
		}
		if (null != toast) {
			toast.setText(msg);
		} else {
			toast = android.widget.Toast.makeText(ctx, msg, duration);
		}
		handler.postDelayed(run, duration);
		toast.show();
	}

	/**
	 * 弹出Toast
	 * 
	 * @param ctx
	 *            弹出Toast的上下文
	 * @param msg
	 *            弹出Toast的内容
	 * @param duration
	 *            弹出Toast的持续时间
	 */
	public static void show(Context ctx, CharSequence msg, int duration)
			throws NullPointerException {
		if (null == ctx) {
			throw new NullPointerException("The ctx is null!");
		}
		if (0 > duration) {
			duration = LENGTH_SHORT;
		}
		toast(ctx, msg, duration);
	}

	/**
	 * 弹出Toast
	 * 
	 * @param ctx
	 *            弹出Toast的上下文
	 * @param msg
	 *            弹出Toast的内容的资源ID
	 * @param duration
	 *            弹出Toast的持续时间
	 */
	public static void show(Context ctx, int resId, int duration)
			throws NullPointerException {
		if (null == ctx) {
			throw new NullPointerException("The ctx is null!");
		}
		if (0 > duration) {
			duration = LENGTH_SHORT;
		}
		toast(ctx, ctx.getResources().getString(resId), duration);
	}

}
