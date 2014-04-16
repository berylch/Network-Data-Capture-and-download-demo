package com.lohanry.andfilemanage.tools;

import com.lxm.htmlparser.R;
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


/**
 * 一些参数
 * 有些需要自己到Java中修改,没有完全抽出来
 * @author Lohanry Le
 * @version 1.1
 * Created on 2012-7-20
 *
 */
public class ConstantParameterTable {
	//在返回数据时候,可以比对的请求Code;
	public static final int AFMA=0x001f;
	public static final int SUCCESS=0x0000;
	public static final int FAIL=0x0001;
	public static final int NOHIGHFOLDER=0x0002;
	//主activity的XML布局文件
	public static final int MAINVIEW=R.layout.lohanry_andfilemanage_main_activity;
	public static final int MAINVIEWDETAILSITEMS=R.layout.lohanry_andfilemanage_details_items;
	public static final int MAINVIEWDETAILSITEMSICON=R.id.lohanry_andfilemanage_details_items_icon;
	public static final int MAINVIEWDETAILSITEMSFILENAME=R.id.lohanry_andfilemanage_details_items_filename;
	public static final int MAINVIEWDETAILSITEMSFILESIZE=R.id.lohanry_andfilemanage_details_items_filesize;
	public static final int MAINVIEWDETAILSITEMSFILEPERMISSONS=R.id.lohanry_andfilemanage_details_items_permissions;
	public static final int MAINVIEWDETAILSITEMSTIMESTAMP=R.id.lohanry_andfilemanage_details_items_timestamp;
	public static final int MAINVIEWDETAILSITEMSISCHOOSE= R.id.lohanry_andfilemanage_details_items_ischoose;
	//默认文件后缀缩略图显示
	public static final int DEFAULTICON = R.drawable.ic_launcher;
	//后缀文件ICON表,必须与后缀名表对应
	public static final int[] EXTENSIONICON = new int[]{R.drawable.folder};
	//后缀名文件表,文件夹folder必须存在,比如".txt"全部采用小写,或者可以到Tools中修改
	public static final String[]  EXTENSION = new String[]{"folder"};
	//文件夹ICON
	//public static final int FILEFOLDERICON = R.drawable.folder;
	public static final String RETURNTHERE = "选择当前目录";
	public static final String RETURNHIGHFOLDER = "返回上级文件夹";
	public static final int FLING_MIN_DISTANCEFX = 50;
	public static final int FLING_MIN_VELOCITX = 45;
	public static final int FLING_MAX_DISTANCEFY= 50;
	//选择项图标
	public static final int NOTCHOOSE = R.drawable.btn_check_buttonless_off;
	public static final int CHOOSE = R.drawable.btn_check_buttonless_on;
	//menu图标
	public static final int OPENCHOOSEACTIVTIYICON = R.drawable.ic_menu_add;
	public static final int MULTIPLECHOOSE = R.string.multiplechoose;
	public static final int ALLCHOOSE = R.string.allchoose;
	public static final int TOGGLE = R.string.toggle;
	public static final int SURE = R.string.sure;
	public static final int CANCEL =R.string.cancel;
	
}
