package com.lohanry.andfilemanage;
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
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.lohanry.andfilemanage.tools.ConstantParameterTable;
import com.lohanry.andfilemanage.tools.FileInfo;
import com.lohanry.andfilemanage.tools.FileManageListViewAdapter;
import com.lohanry.andfilemanage.tools.Tools;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListView;
import android.widget.Toast;
/**
 * Activity类用来显示
 * @author Lohanry le
 * @version 1.1
 * Created on 2012-7-20
 *
 */
public class AndFileManageActivity extends ListActivity implements OnTouchListener,OnGestureListener {
	private static final String TAG = "com.lohanry.com.AndFileManage" ;
	private String path = "/";
	private FileManageListViewAdapter mFileManageLVAdapter;
	private ArrayList<FileInfo> fileList;
	private boolean isOutOfList;
	private boolean isUseOutOfList;
	private String[] OutOfList;
	private FileInfo getFile_mFileInfo;
	private GestureDetector mGestureDetector = null;
	private static final int OPENCHOOSEACTIVTIY = Menu.FIRST;
	private static final int ALLCHOOSE = Menu.FIRST +1;
	private static final int CANCEL = Menu.FIRST+2;
	private static final int TOGGLE = Menu.FIRST+3;
	private static final int SURE = Menu.FIRST+4;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(ConstantParameterTable.MAINVIEW);
        Bundle b=getIntent().getBundleExtra("Lohanry_andFileMnagae_BundleExtra");
        getListView().setOnTouchListener(this);
		init(b);
        createActiveX();
        initActiveX();
        listenActiveX();
    }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.destroy();
	}
	private void destroy() {
		// TODO Auto-generated method stub
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
	@Override
	protected void onRestoreInstanceState(Bundle state) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(state);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		changeTitleText();
		super.onResume();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) && (!mFileManageLVAdapter.getIsInChooseActivity())){
			File mFile = new File(path);
			mFile = mFile.getParentFile();
			if (mFile==null){finishAndturn(ConstantParameterTable.FAIL,null);return true;}//是否要return未知
			reFresh(mFile);
			return true;
		}
		else if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) && (mFileManageLVAdapter.getIsInChooseActivity())){
			mFileManageLVAdapter.setIsInChooseActivity(false);
			cleanAllChoose();
			mFileManageLVAdapter.notifyDataSetChanged();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		FileInfo tempFileList = fileList.get(position);
		ArrayList<String> temp_array = new ArrayList<String>();
		if (mFileManageLVAdapter.getIsInChooseActivity()){
			tempFileList.isChoose = !tempFileList.isChoose;
			super.onListItemClick(l, v, position, id);
			mFileManageLVAdapter.notifyDataSetChanged();
			return;
		}
		if (tempFileList.isEnd){
			switch (tempFileList.mark) {
			case 0:
				File mFile = new File(path);
				mFile = mFile.getParentFile();
				if (mFile==null){finishAndturn(ConstantParameterTable.FAIL,null);}
				reFresh(mFile);
				break;
			case 1:
				temp_array.add(path);
				finishAndturn(ConstantParameterTable.SUCCESS, temp_array);
				break;
			default:
				temp_array.add(tempFileList.absolutePath);
				finishAndturn(ConstantParameterTable.SUCCESS, temp_array);
				break;
			}
		}else{
			reFresh(tempFileList.m);
		}
		super.onListItemClick(l, v, position, id);
	}
	private void finishAndturn(int ResultCode ,ArrayList<String> patharray){
		Bundle b = new Bundle();
		b.putStringArrayList("PATHARRAY", patharray);
		Intent intent = new Intent();
		intent.putExtra("PATHBUNDLE", b);
		setResult(ResultCode, intent);
		AndFileManageActivity.this.finish();
	}
	private void init(Bundle b) {
		// TODO Auto-generated method stub
		if (b==null){
	        Log.e(TAG,"Can found Bundle");
		}else{
			this.path = (b.getString("PATH")!=null)?b.getString("PATH"):"/";
			this.isUseOutOfList = (b.getStringArray("Extension")!=null)?true:false;
			//下面的代码获取后缀表必须要有,否则报错,注意!!!
			if (this.isUseOutOfList){
				this.OutOfList = b.getStringArray("Extension");
				this.isOutOfList = b.getBoolean("isRemove");
			}
		}
		mGestureDetector = new GestureDetector(this);
		mFileManageLVAdapter = new FileManageListViewAdapter(this,
				ConstantParameterTable.MAINVIEWDETAILSITEMS,
				fileList=getFileList(new File(path)),
				new int[]{ConstantParameterTable.MAINVIEWDETAILSITEMSICON,
			              ConstantParameterTable.MAINVIEWDETAILSITEMSFILENAME,
			              ConstantParameterTable.MAINVIEWDETAILSITEMSTIMESTAMP,
			              ConstantParameterTable.MAINVIEWDETAILSITEMSFILEPERMISSONS,
			              ConstantParameterTable.MAINVIEWDETAILSITEMSFILESIZE,
			              ConstantParameterTable.MAINVIEWDETAILSITEMSISCHOOSE});
	}
	private void createActiveX() {
		// TODO Auto-generated method stub
	}
	private void initActiveX() {
		// TODO Auto-generated method stub
		setListAdapter(mFileManageLVAdapter);
	}
	private void listenActiveX() { 
		// TODO Auto-generated method stub
	}
	private void changeTitleText() {
		setTitle(path);
	}
	private void reFresh(File file) {
		ArrayList<FileInfo> _fileList = getFileList(file);
		if (_fileList==null){
			doFileFail("No permission! Can't open it!",0);
			return;
		}
		fileList.clear();
		for(FileInfo i : _fileList){
			fileList.add(i);
			//依然遍历,自己修改
		}
		changeTitleText();
		mFileManageLVAdapter.notifyDataSetChanged();
	}
	private void doFileFail(String msg , int i) {
		switch (i) {
		case 0:
			Toast.makeText(this, msg,Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
	}
	private ArrayList<FileInfo> getFileList(File file){
		long startTime=System.currentTimeMillis();   //获取开始时间
		try{
			if (!file.exists()){
				finishAndturn(ConstantParameterTable.FAIL, null);
			}
		}catch(Exception e){
			return getFileList(file.getParentFile());
		}
		if (!file.isDirectory()){finishAndturn(ConstantParameterTable.FAIL, null);}
		File[] mFileList=file.listFiles();
		if (mFileList==null){finishAndturn(ConstantParameterTable.FAIL, null);}
		this.path = file.getAbsolutePath();
		ArrayList<FileInfo> temp = new ArrayList<FileInfo>((mFileList.length+2));
		if (file.getParentFile()!=null){addHighFolderItem(temp);}
		addNowFolderItem(temp);
		FileInfo tempinfo;
		for (File i : mFileList){
			tempinfo = getFile(i);
			if (tempinfo!=null){
				temp.add(tempinfo);
			}
		}
		long endTime=System.currentTimeMillis();
		Log.d(TAG+"getFileList", (endTime-startTime)+"ms");
		return temp;
	}
	private void addHighFolderItem(ArrayList<FileInfo> temp) {
		// TODO Auto-generated method stub
		FileInfo mFileInfo = new FileInfo();
		mFileInfo.filename = "...";
		mFileInfo.timestamp = ConstantParameterTable.RETURNHIGHFOLDER;
		mFileInfo.icon = ConstantParameterTable.EXTENSIONICON[0];
		mFileInfo.isEnd = true;
		mFileInfo.mark = 0x00;
		temp.add(mFileInfo);//返回上级目录
	}
	private void addNowFolderItem(ArrayList<FileInfo> temp) {
		// TODO Auto-generated method stub
		FileInfo mFileInfo = new FileInfo();
		mFileInfo.filename = ConstantParameterTable.RETURNTHERE;
		mFileInfo.icon = ConstantParameterTable.EXTENSIONICON[0];
		mFileInfo.isEnd = true;
		mFileInfo.mark = 0x01;
		temp.add(mFileInfo);//返回当前目录
	}
	private FileInfo getFile(File file) {
		if (!file.exists()){return null;}
		String exName = Tools.getFileExtensions(file);
		if (isUseOutOfList){
			boolean isF = true;
			if (exName.equals("folder")){
				isF = false;
			}
			if (isOutOfList && isF){
				//去除不在列表内的文件
				boolean a = true;
				for (String i : OutOfList){if (exName.equals(i)){a=false;break;}}
				if (a){return null;}
			}
			else if ((!isOutOfList) && isF){
				//去除在列表内的文件
				boolean a =false;
				for (String i : OutOfList){if (exName.equals(i)){a=true; break;}}
				if (a){return null;}
			}
		}//判断是否在排除或者包括
		getFile_mFileInfo = new FileInfo();//这里优化注意
		getFile_mFileInfo.m = file;
		getFile_mFileInfo.filename = file.getName();
		//根据API有安全权限,
		getFile_mFileInfo.filesize = Tools.getFileSize(file.length());
		getFile_mFileInfo.timestamp = ""+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(file.lastModified());
		getFile_mFileInfo.absolutePath = file.getAbsolutePath();
		getFile_mFileInfo.icon = Tools.getIcon(Tools.getExtensionIconPosition(exName));
		getFile_mFileInfo.isEnd = file.isFile();
		getFile_mFileInfo.isFile = file.isFile();
		return getFile_mFileInfo;
	}
	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		Log.i(TAG,""+e2.getX()+"dasda"+e1.getX());
		boolean isright = isRight(e1.getX(), e2.getX());
		if (isright && (!mFileManageLVAdapter.getIsInChooseActivity())){
			if ((Math.abs(e2.getX()-e1.getX())>ConstantParameterTable.FLING_MIN_DISTANCEFX)
					&& (Math.abs(velocityX)>ConstantParameterTable.FLING_MIN_VELOCITX)){
					if (Math.abs(e2.getY()-e1.getY())<ConstantParameterTable.FLING_MAX_DISTANCEFY){
						mFileManageLVAdapter.setIsInChooseActivity(true);
						mFileManageLVAdapter.notifyDataSetChanged();
					}
				}
		}
		if ((!isright) && mFileManageLVAdapter.getIsInChooseActivity()){
			if ((Math.abs(e2.getX()-e1.getX())>ConstantParameterTable.FLING_MIN_DISTANCEFX)
					&& (Math.abs(velocityX)>ConstantParameterTable.FLING_MIN_VELOCITX)){
					if (Math.abs(e2.getY()-e1.getY())<ConstantParameterTable.FLING_MAX_DISTANCEFY){
						mFileManageLVAdapter.setIsInChooseActivity(false);
						cleanAllChoose();
						mFileManageLVAdapter.notifyDataSetChanged();
					}
				}
		}
		return false;
	}
	private void cleanAllChoose() {
		// TODO Auto-generated method stub
		for (FileInfo i : fileList){
			i.isChoose = false;
		}
	}
	private boolean isRight(float e1, float e2) {
		// TODO Auto-generated method stub
		if ((e1 - e2)>0){
			return true;
		}
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuItem MENU_first = menu.add(Menu.FIRST , OPENCHOOSEACTIVTIY ,Menu.NONE , ConstantParameterTable.MULTIPLECHOOSE);
		MENU_first.setIcon(ConstantParameterTable.OPENCHOOSEACTIVTIYICON);
		MenuItem MENU_second = menu.add(Menu.FIRST+1 , ALLCHOOSE ,Menu.NONE , ConstantParameterTable.ALLCHOOSE);
		//MENU_second.setIcon(icon);
		MenuItem MENU_third = menu.add(Menu.FIRST+1 , TOGGLE ,Menu.NONE , ConstantParameterTable.TOGGLE);
		//MENU_third.setIcon(icon);
		MenuItem MENU_fourth = menu.add(Menu.FIRST+1 , SURE ,Menu.NONE , ConstantParameterTable.SURE);
		//MENU_fourth.setIcon(icon);
		MenuItem MENU_fifth = menu.add(Menu.FIRST+1 , CANCEL ,Menu.NONE , ConstantParameterTable.CANCEL);
		//MENU_fifith.setIcon(icon);
		
		
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		boolean b = mFileManageLVAdapter.getIsInChooseActivity();
		if (b){
			menu.setGroupVisible(Menu.FIRST,false);
			menu.setGroupVisible(Menu.FIRST+1, true);
		}
		if (!b){
			//没有打开多选；打开
			menu.setGroupVisible(Menu.FIRST,true);
			menu.setGroupVisible(Menu.FIRST+1, false);
		}
		return super.onPrepareOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case OPENCHOOSEACTIVTIY:
			mFileManageLVAdapter.setIsInChooseActivity(true);
			mFileManageLVAdapter.notifyDataSetChanged();
			break;
		case ALLCHOOSE:
			for (FileInfo i : fileList){
				i.isChoose = true;
			}
			mFileManageLVAdapter.notifyDataSetChanged();
			break;
		case TOGGLE:
			for (FileInfo i : fileList){
				i.isChoose = !i.isChoose;
			}
			mFileManageLVAdapter.notifyDataSetChanged();
			break;
		case SURE:
			ArrayList<String> temp_array = new ArrayList<String>(32);// 根据情况设置...
			for (FileInfo i : fileList){
				if (i.isChoose){
					temp_array.add(i.absolutePath);
				}
			}
			finishAndturn(ConstantParameterTable.SUCCESS,temp_array);
			break;
		case CANCEL:
			mFileManageLVAdapter.setIsInChooseActivity(false);
			cleanAllChoose();
			mFileManageLVAdapter.notifyDataSetChanged();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}