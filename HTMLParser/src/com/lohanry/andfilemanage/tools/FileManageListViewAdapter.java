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
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * ListView的适配器
 * 优化问题看getView方法;
 * @author Lohanry Le
 * @version 1.1
 * Created on 2012-7-20
 *
 */
public class FileManageListViewAdapter extends BaseAdapter{
	private Context mContext;
	private int mResource;
	private ArrayList<FileInfo> fileList;
	private int[] mItemIDs;
	private LayoutInflater mInflater;
	private FileInfo tempFileInfo=null;
	private boolean isInChooseActivity = false;
	private ViewHolder holder;
	private View convertView = null;
	private ViewGroup parent = null;
	public FileManageListViewAdapter(Context context , int resource , ArrayList<FileInfo> filelist, int[] ItemIDs) {
		// TODO Auto-generated constructor stub
		mContext=context;
		mResource=resource;
		fileList=filelist;
		mItemIDs=ItemIDs;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fileList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	/**
	 * 第一次新建View,并且查询权限,时间在18ms左右
	 * 查询到权限情况下，重复用View的话 时间在1ms左右
	 * 具体可以自己跟踪，被注释掉了，
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//long startTime=System.currentTimeMillis();   //获取开始时间
		this.convertView = null;
		this.convertView = convertView;
		this.parent = parent;
		this.holder = null;
		if (this.convertView == null) {
			creatActionX();
        } else {
        	this.holder = (ViewHolder) this.convertView.getTag();
        }
		initActionX(position);
		//long endTime=System.currentTimeMillis();
		//Log.d("getFileList", (endTime-startTime)+"ms");
		this.parent = null;
		return this.convertView;
	}
	private void initActionX(int position) {
		// TODO Auto-generated method stub
		//下面代码可以优化
		tempFileInfo = fileList.get(position);
		this.holder.icon.setImageResource(tempFileInfo.icon);
		this.holder.filename.setText(tempFileInfo.filename);
		switch (tempFileInfo.mark) {
		case 0x01:
			this.holder.timestamp.setText("");
			this.holder.permissions.setText("");
			this.holder.filesize.setText("");
			this.holder.isChoose.setVisibility(View.GONE);//Item头部不显示
			break;
		case 0x00:
			this.holder.timestamp.setText(tempFileInfo.timestamp);
			this.holder.permissions.setText("");
			this.holder.filesize.setText("");
			this.holder.isChoose.setVisibility(View.GONE);//Item头部不显示
			break;
		case 0x06:
			this.holder.timestamp.setText(tempFileInfo.timestamp);
			if (tempFileInfo.m!=null){
				if (tempFileInfo.permissions==null){tempFileInfo.permissions=Tools.getFilePermissions(tempFileInfo.m);}
				this.holder.permissions.setText(tempFileInfo.permissions);
			}
			if (tempFileInfo.isFile){this.holder.filesize.setText(tempFileInfo.filesize);}else{this.holder.filesize.setText("");}
			if (getIsInChooseActivity()){
				this.holder.isChoose.setVisibility(View.VISIBLE);
				this.holder.isChoose.setImageResource((tempFileInfo.isChoose?ConstantParameterTable.CHOOSE:ConstantParameterTable.NOTCHOOSE));
			}else{
				this.holder.isChoose.setVisibility(View.GONE);
			}
			break;
		}
		tempFileInfo = null;
	}
	private void creatActionX() {
		// TODO Auto-generated method stub
		this.convertView = mInflater.inflate(mResource, parent, false);
		this.holder = new ViewHolder();
		//mTo[0]用法是错误的,但是偷懒了....资源文件必须与mTo对应:)
		this.holder.icon = (ImageView) convertView.findViewById(mItemIDs[0]);
		this.holder.filename = (TextView) convertView.findViewById(mItemIDs[1]);
		this.holder.timestamp = (TextView) convertView.findViewById(mItemIDs[2]);
		this.holder.permissions = (TextView) convertView.findViewById(mItemIDs[3]);
		this.holder.filesize = (TextView) convertView.findViewById(mItemIDs[4]);
		this.holder.isChoose = (ImageView) convertView.findViewById(mItemIDs[5]);
		this.convertView.setTag(holder);
	}
	public void setIsInChooseActivity(boolean b ){
		isInChooseActivity = b;
	}
	public boolean getIsInChooseActivity(){
		return isInChooseActivity;
	}
	static class ViewHolder{
		public ImageView icon;
		public TextView filename;
		public TextView timestamp;
		public TextView permissions;
		public TextView filesize;
		public ImageView isChoose;
	}

}
