package com.lohanry.andfilemanage.tools;

import java.io.File;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Handler.Callback;
import android.os.Message;

public class AsyncTaskFindFile extends AsyncTask<FindFileC, Void, ArrayList<File>> implements Callback{
	ArrayList<File> mFileList ;
	ArrayList<File> mFindFileList ; 
	ArrayList<File> temp_mFindFileList;

	@Override
	protected ArrayList<File> doInBackground(FindFileC... params) {
		// TODO Auto-generated method stub
		String needToFindName = params[0].needToFindName;
		File[] temp_F = params[0].mList.listFiles();
		mFileList = new ArrayList<File>(temp_F.length);// 只是用来存没有查询过的文件夹
		mFindFileList = new ArrayList<File>();
		boolean isRoot = true;
		while (mFileList.size()>0){
			if (isRoot){
				for (File i : temp_F){
					if (i.isDirectory()){
						mFileList.add(i);
					}
					if (i.isFile()){
						if(Tools.findFile(i,needToFindName)!=null){
							mFindFileList.add(i);
						}
					}
				}
				isRoot = false;
				continue;
			}
			temp_mFindFileList = getFileList(mFileList.get(0));
			if(temp_mFindFileList==null){mFileList.remove(0);continue;}
			for (File i : mFindFileList){
				if(Tools.findFile(i,needToFindName)!=null){
					mFindFileList.add(i);
				}
			}
		}
		
		return mFindFileList;
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * 返回文件列表
	 * @param mFile
	 * @return 文件列表
	 */
	private ArrayList<File> getFileList(File mFile) {
		//for ()
		return null;
		
	}
}
