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
/**
 * 
 * @author Lohanry Le
 *
 */
public class FileInfo {
	public String filename;
	public String timestamp;
	public String filesize;
	public String absolutePath;
	public int icon;
	public boolean isFile;
	public String permissions;
	public java.io.File m = null;//指代自己
	public boolean isEnd;//判断是否可以返回;
	public int mark = 0x06;//判断头部item,0x00为返回上级,0x01返回当前目录
	public boolean isDel;//List判断
	public boolean isChoose;
}
