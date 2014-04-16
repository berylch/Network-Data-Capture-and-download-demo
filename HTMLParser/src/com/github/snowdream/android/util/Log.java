package com.github.snowdream.android.util;


public class Log
{
	public static void w(String Loginfo)
	{
		android.util.Log.w("lxm",Loginfo);
		
	}
	
	public static void i(String Loginfo)
	{
		android.util.Log.i("lxm",Loginfo);
		
	}
	
	public static void e(String Loginfo,Exception e)
	{
		android.util.Log.e("lxm",Loginfo);
		
	}
	public static void e(String Loginfo)
	{
		android.util.Log.e("lxm",Loginfo);
		
	}
	public static void d(String Loginfo)
	{
		android.util.Log.d("lxm",Loginfo);
		
	}
}