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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.text.DecimalFormat;

import android.util.Log;
/**
 * 一些工具方法,都是static
 * @author Lohanry Le
 * @version 1.0
 * Created on 2012-7-20
 */
public class Tools {
	public Tools() {
	}
	/**
	 * 友好获取文件大小
	 * 最大显示1024TB
	 * @param filesize_bytes
	 * @return 根据情况返回B,KB,MB,GB,TB
	 */
	public static String getFileSize(long filesize_bytes){
		DecimalFormat mD = new DecimalFormat("####.##");
		StringBuffer sb = new StringBuffer();
		if (filesize_bytes<(1024)){return sb.append(mD.format(filesize_bytes)).append("B").toString();}
		if (filesize_bytes<(1024*1024)){return sb.append(mD.format(filesize_bytes/(1024.0))).append("KB").toString();}
		if (filesize_bytes<(1024*1024*1024)){return sb.append(mD.format(filesize_bytes/(1024*1024.0))).append("MB").toString();}
		if (filesize_bytes<(1024*1024*1024*1024)){return sb.append(mD.format(filesize_bytes/(1024*1024*1024.0))).append("GB").toString();}
		if (filesize_bytes<(1024*1024*1024*1024*1024)){return sb.append(mD.format(filesize_bytes/(1024*1024*1024*1024.0))).append("TB").toString();}
		return ""+filesize_bytes;
	}
	public static String getFilePermissions(File file) {
		String path = file.getAbsolutePath();
		Process process=null;
		DataOutputStream toWrite=null;
		DataInputStream toRead=null;
		try{
			Log.i("System.out","");
			if (file.isFile()){process = Runtime.getRuntime().exec("ls -l "+path+"\n");}
			else {process = Runtime.getRuntime().exec("ls -ld "+path+"\n");}
			toWrite = new DataOutputStream(process.getOutputStream());
			toRead = new DataInputStream(process.getInputStream());
			toWrite.flush();
			if (process.waitFor()!=0){return "---------";}
			String temp = toRead.readLine();
			String root = temp.substring(1,temp.indexOf(" "));
			Log.i("System.out","");
			return root;
		}catch(Exception e){
			return "---------";
		}finally{
			try{
				toRead.close();
				toWrite.close();
			}catch(Exception e){}
			process.destroy();
		}
	}
	public static String getFileExtensions(File file) {
		String name = file.getName();
		if (name.lastIndexOf(".")==-1){
			if (file.isDirectory()){return "folder";}
			else{return ".";}
		}
		//if (name.startsWith(".")){return "folder";}
		return name.substring(name.lastIndexOf("."),(name.length()));
	}
	/**
	 * 查找后缀名表返回在后缀ICON表中的位置
	 * @param extension
	 * @return int或者null;
	 */
	public static int getExtensionIconPosition(String extension) {
		//这里只是简单的用遍历方法查找,
		//你可以用自己其他算法
		String _extension = extension.toLowerCase();
		for (int i=0 ; i<ConstantParameterTable.EXTENSION.length ; i++){
			if (_extension.equals(ConstantParameterTable.EXTENSION[i])){return i;}
		}
		//没有找到返回,后缀ICON表元素下标+1
		return (ConstantParameterTable.EXTENSION.length+1);
	}
	public static int getIcon(int position) {
		if (position>ConstantParameterTable.EXTENSIONICON.length){return ConstantParameterTable.DEFAULTICON;}
		return ConstantParameterTable.EXTENSIONICON[position];
	}
	/**
	 *  文件比对
	 * @param mFile
	 * @return 返回null不是，File表示找到的文件
	 */
	public static File findFile(File mFile , String name) {
		if (mFile.getName().endsWith(name)){return mFile;}
		return null;
	}

}
