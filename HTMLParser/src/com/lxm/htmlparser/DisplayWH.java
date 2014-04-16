package com.lxm.htmlparser;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;

public class DisplayWH {
	
	public static String TAG="DisplayWH";
	static int DisplayWH_W()
	{
		return 1;
	}
	static ShowWH DisplayWH(Activity context)
	{
	    // 获取屏幕密度（方法1）
	    int screenWidth ; //= context.getWindowManager().getDefaultDisplay().getWidth();		// 屏幕宽（像素，如：480px）
	    int screenHeight ;//= context.getWindowManager().getDefaultDisplay().getHeight();		// 屏幕高（像素，如：800p）
	    ShowWH Wh=new ShowWH();
	    // 获取屏幕密度（方法2）
	    DisplayMetrics dm; //= new DisplayMetrics();
	   // dm = context.getResources().getDisplayMetrics();
	    
	    float density  ;//= dm.density;		// 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
	    int densityDPI;// = dm.densityDpi;		// 屏幕密度（每寸像素：120/160/240/320）
	    float xdpi ;//= dm.xdpi;			
	    float ydpi;// = dm.ydpi;
	    
//	    Log.e(TAG + "  DisplayMetrics", "xdpi=" + xdpi + "; ydpi=" + ydpi);
//	    Log.e(TAG + "  DisplayMetrics", "density=" + density + "; densityDPI=" + densityDPI);
//	    
//	    screenWidth  = dm.widthPixels;		// 屏幕宽（像素，如：480px）
//	    screenHeight = dm.heightPixels;		// 屏幕高（像素，如：800px）
//	    
//	    Log.e(TAG + "  DisplayMetrics(111)", "screenWidth=" + screenWidth + "; screenHeight=" + screenHeight);
//	    
	    
	    
	    // 获取屏幕密度（方法3）
	    dm = new DisplayMetrics();
	    context.getWindowManager().getDefaultDisplay().getMetrics(dm);
	    
	    density  = dm.density;		// 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
	    densityDPI = dm.densityDpi;		// 屏幕密度（每寸像素：120/160/240/320）
	    xdpi = dm.xdpi;			
	    ydpi = dm.ydpi;
	    
	  	    
	    Wh.screenWidth = dm.widthPixels;		// 屏幕宽（dip，如：320dip）
	    Wh.screenHeight = dm.heightPixels;		// 屏幕宽（dip，如：533dip）

	    //Log.e(TAG + "  DisplayMetrics(222)", "screenWidthDip=" + screenWidthDip + "; screenHeightDip=" + screenHeightDip);
	    
	   // Wh.screenWidth  //= (int)(dm.widthPixels * density + 0.5f);		// 屏幕宽（px，如：480px）
	   // Wh.screenHeight //= (int)(dm.heightPixels * density + 0.5f);		// 屏幕高（px，如：800px）
	    return Wh;
	  //  Log.e(TAG + "  DisplayMetrics(222)", "screenWidth=" + screenWidth + "; screenHeight=" + screenHeight);
	}
}
