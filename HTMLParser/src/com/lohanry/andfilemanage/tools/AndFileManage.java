package com.lohanry.andfilemanage.tools;
/*
 * Copyright (C) 2012 Lohanry Le
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
/**
 * 整个程序的开始
 * 数据包装传送
 * 数据->Bundle->Intent->
 * @author Administrator
 * @version 1.0
 * Created on 2012-7-20
 */
public class AndFileManage {
	private final Intent intent;
	private final Bundle b;
	private Activity mActivity;
	/**
	 * 不带参数的初始化
	 * 根目录开始
	 * @param mActivity
	 */
	public AndFileManage(Activity mActivity) {
		// TODO Auto-generated constructor stub
		b = new Bundle();
		intent=new Intent(mActivity, com.lohanry.andfilemanage.AndFileManageActivity.class);
		this.mActivity=mActivity;
	}
	/**
	 * 带路径参数的初始化
	 * @param mActivity
	 * @param PATH 开始目录
	 */
	public AndFileManage(Activity mActivity , String PATH) {
		// TODO Auto-generated constructor stub
		b = new Bundle();
		b.putString("PATH", PATH);
		intent=new Intent(mActivity, com.lohanry.andfilemanage.AndFileManageActivity.class);
		this.mActivity=mActivity;
	}
	/**
	 * 带参数的初始化
	 * @param mActivity
	 * @param PATH 开始目录
	 * @param Extension 后缀列表
	 * @param isRemove true:只包括后缀列表内的文件,false:排除后缀列表内的文件
	 */
	public AndFileManage(Activity mActivity , String PATH , String[] Extension , Boolean isRemove) {
		b = new Bundle();
		b.putStringArray("Extension", Extension);
		b.putBoolean("isRemove", isRemove);
		b.putString("PATH", PATH);
		intent=new Intent(mActivity, com.lohanry.andfilemanage.AndFileManageActivity.class);
		this.mActivity=mActivity;
	}
	public void startOpenActivity() {
		intent.putExtra("Lohanry_andFileMnagae_BundleExtra", b);
		this.mActivity.startActivityForResult(intent,ConstantParameterTable.AFMA);
	}
}
