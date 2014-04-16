/*
 * Copyright (C) 2013 Snowdream Mobile <yanghui1986527@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.snowdream.android.apps.downloader;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.snowdream.android.app.DownloadListener;
import com.github.snowdream.android.app.DownloadManager;
import com.github.snowdream.android.app.DownloadStatus;
import com.github.snowdream.android.app.DownloadTask;
import com.github.snowdream.android.app.dao.ISql;
import com.github.snowdream.android.util.Log;
import com.lxm.htmlparser.MyUtil;
import com.lxm.htmlparser.R;
import com.lxm.tools.OpenFiles;

public class DownloadTaskAdapter extends BaseAdapter {
	private Context mContext;
	private List<DownloadTask> mItems;
	private int status = DownloadStatus.STATUS_NONE;
	private int list_pos_click = 0;
	// 数据库 实例
	private ISql iSql;
	// 查询多个匹配
	private HashMap<String, Object> EntityMap;
	private DownloadManager DM = null;
	private DownloadListener listener;

	public DownloadTaskAdapter(Context context, List<DownloadTask> items,
			DownloadListener listener) {

		mContext = context;

		DM = DownloadManager.getInstance(context);
		mItems = DM.getList();
		this.listener = listener;

	}

	@Override
	public int getCount() {
		int count = 0;
		if (mItems != null) {
			count = mItems.size();
		}
		return count;
	}

	// 清除数据
	public void cleanCount() {

		if (mItems != null) {
			mItems.clear();

		}
	}

	//
	// public void setLoadByStatus(int statusby) {
	//
	// if (mItems != null) {
	// mItems.clear();
	// }
	// status = statusby;
	// quretData();
	// }

	@Override
	public Object getItem(int position) {
		Object obj = null;
		if (position >= 0 && position < getCount()) {
			obj = mItems.get(position);
		}
		return obj;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		Object item = getItem(position);
		boolean click = ((DownloadTask) DownloadTaskAdapter.this
				.getItem(position)).getClick();
		if (item == null || !(item instanceof DownloadTask)) {
			Log.e("Error");
			return null;
		}

		final DownloadTask task = (DownloadTask) item;

		if (v == null) {
			v = LayoutInflater.from(mContext).inflate(
					R.layout.share_download_list_item, parent, false);
		}

		TextView title = (TextView) v.findViewById(R.id.app_name);
		// 显示下载的内容 ： 神雕侠侣.rar
		TextView size = (TextView) v.findViewById(R.id.progress_text);
		// 显示状态，比如下载速率 130kb/s 或者出错时显示出错类别。完成时直接消失
		ProgressBar bar = (ProgressBar) v.findViewById(R.id.download_progress);
		// 下载完成时直接消失
		TextView size_total = (TextView) v
				.findViewById(R.id.complete_download_size);
		// show : 10M/13.5M 下载完成时 显示 13.5M
		long filesize = 0l;
		File file = new File(task.getPath());
		if (file.exists()) {
			filesize = file.length();
		}
		final long filesize_for = filesize;

		v.findViewById(R.id.pkg_item_layout_top).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						if (list_pos_click == position) {
							DownloadTask Task = (DownloadTask) DownloadTaskAdapter.this
									.getItem(position);
							if (Task.getClick()) {
								Task.setClick(false);
							} else {
								Task.setClick(true);
							}
							DownloadTaskAdapter.this.notifyDataSetChanged();
						} else {

							if (list_pos_click <getCount()) {
								((DownloadTask) DownloadTaskAdapter.this
										.getItem(list_pos_click))
										.setClick(false);
							}

							((DownloadTask) DownloadTaskAdapter.this
									.getItem(position)).setClick(true);
							list_pos_click = position;
							DownloadTaskAdapter.this.notifyDataSetChanged();
						}
					}
				});
		title.setText(task.getName());
		android.util.Log.i("lxmlxmlxm", "" + task.getSpead());
		size.setText(MyUtil.numberFormat2(task.getSpead() * 1000) + "/s");
		size_total.setText(MyUtil.numberFormat2(filesize) + "/"
				+ MyUtil.numberFormat2(task.getSize()));
		int progress = 0;
		if (task.getSize() > 0) {
			progress = (int) (filesize * 100 / task.getSize());
		}

		bar.setProgress(progress);
		final TextView app_status = (TextView) v
				.findViewById(R.id.btn_app_status);
		app_status.setText(R.string.zanting);
		app_status.setCompoundDrawablesWithIntrinsicBounds(0,
				R.drawable.btn_icon_pause, 0, 0);

		if (task.getSize() > 0 && filesize == task.getSize()) {
			task.setStatus(DownloadStatus.STATUS_FINISHED);
		}
		View popup_bottom = v.findViewById(R.id.layout_popup_bottom);
		if (click) {
			popup_bottom.setVisibility(View.VISIBLE);
			TextView btn_share_apk = (TextView) v
					.findViewById(R.id.btn_share_apk);
			// 分享
			btn_share_apk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MyUtil.fenxiang(mContext, task);
				}
			});
			TextView btn_redownload_apk = (TextView) v
					.findViewById(R.id.btn_redownload_apk);
			// 重新下载
			btn_redownload_apk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (task.getStatus() == DownloadStatus.STATUS_FINISHED) {
						// popup_bottom.setVisibility(View.GONE);
						DM.deleteforever(task, listener);
						task.setClick(false);
						DM.add(task, listener);
						mItems = DM.getList();
						notifyDataSetChanged();
					}
				}
			});

			TextView btn_delete_apk = (TextView) v
					.findViewById(R.id.btn_delete_apk);
			// 重新下载
			btn_delete_apk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// if (task.getStatus() == DownloadStatus.STATUS_FINISHED) {
					DM.deleteforever(task, listener);
					// DM.add(task, listener);
					mItems = DM.getList();
					notifyDataSetChanged();
					// }
				}
			});
		} else {
			popup_bottom.setVisibility(View.GONE);
		}
		bar.setVisibility(View.VISIBLE);
		switch (task.getStatus()) {
		case DownloadStatus.STATUS_PENDING:
			app_status.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.btn_icon_waiting, 0, 0);
			app_status.setText(R.string.dengdaizhong);
			size.setVisibility(View.INVISIBLE);
			break;
		case DownloadStatus.STATUS_FAILED:
			if (MyUtil.isNetworkAvailable(mContext)) {
				app_status.setText(R.string.dengdaizhong);
				app_status.setCompoundDrawablesWithIntrinsicBounds(0,
						R.drawable.btn_icon_waiting, 0, 0);
				task.setStatus(DownloadStatus.STATUS_PENDING);
				DM.start(task, listener);
			} else {
				app_status.setCompoundDrawablesWithIntrinsicBounds(0,
						R.drawable.btn_icon_download, 0, 0);
				app_status.setText(R.string.jixu);
				size.setVisibility(View.INVISIBLE);

			}
			break;
		case DownloadStatus.STATUS_STOPPED:
			app_status.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.btn_icon_download, 0, 0);
			app_status.setText(R.string.jixu);
			size.setVisibility(View.INVISIBLE);
			break;
		case DownloadStatus.STATUS_RUNNING:
			app_status.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.btn_icon_pause, 0, 0);
			app_status.setText(R.string.zanting);
			size.setVisibility(View.VISIBLE);
			break;
		case DownloadStatus.STATUS_FINISHED:
			// downloadManager.start(task, listener);
			if (task.getSize() > 0 && filesize == task.getSize()) {
				app_status.setCompoundDrawablesWithIntrinsicBounds(0,
						R.drawable.btn_icon_open, 0, 0);
				app_status.setText(R.string.open);
				bar.setVisibility(View.INVISIBLE);
				size.setVisibility(View.INVISIBLE);

				size_total.setText(MyUtil.numberFormat2(filesize));
			} else {
				app_status.setCompoundDrawablesWithIntrinsicBounds(0,
						R.drawable.btn_icon_download, 0, 0);
				app_status.setText(R.string.jixu);
				bar.setVisibility(View.VISIBLE);
				size.setVisibility(View.INVISIBLE);
				size_total.setText(MyUtil.numberFormat2(filesize) + "/"
						+ MyUtil.numberFormat2(task.getSize()));
			}
			break;
		default:
			break;
		}
		app_status.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (task.getStatus()) {

				case DownloadStatus.STATUS_FAILED:
				case DownloadStatus.STATUS_STOPPED:
					app_status.setText(R.string.dengdaizhong);
					app_status.setCompoundDrawablesWithIntrinsicBounds(0,
							R.drawable.btn_icon_waiting, 0, 0);
					task.setStatus(DownloadStatus.STATUS_PENDING);
					DM.start(task, listener);
					// DlBut.setText(R.string.zanting);
					android.util.Log.i("lxmlxm", "333");
					break;
				case DownloadStatus.STATUS_PENDING:
				case DownloadStatus.STATUS_RUNNING:
					app_status.setText(R.string.zantingzhong);
					DM.stop(task, listener);

					android.util.Log.i("lxmlxm", "444");
					break;
				case DownloadStatus.STATUS_FINISHED:
					if (task.getSize() > 0 && filesize_for == task.getSize()) {

						OpenFile(new File(task.getPath()));
					} else {
						app_status.setText(R.string.dengdaizhong);
						app_status.setCompoundDrawablesWithIntrinsicBounds(0,
								R.drawable.btn_icon_waiting, 0, 0);
						DM.start(task, listener);
					}
					// DlBut.setText(R.string.zanting);
					android.util.Log.i("lxmlxm", "555");
					break;
				default:
					app_status.setText(R.string.zanting);
					DM.start(task, listener);
					android.util.Log.i("lxmlxm", "666");
					// DlBut.setText(R.string.zanting);
					break;
				}
			}
		});
		return v;
	}

//	public void destory() {
//		if (mContext == null) {
//			return;
//		}
//
//		DownloadManager downloadManager = DownloadManager.getInstance(mContext);
//		if (mItems != null) {
//			for (DownloadTask task : mItems) {
//				downloadManager.stop(task, null);
//			}
//		}
//	}

	// private void init(Context context) {
	// iSql = new ISqlImpl(context);
	// EntityMap = new HashMap<String, Object>();
	// EntityMap.clear();
	// }

	// private void quretData() {
	// try {
	// EntityMap.put("status", status);
	// mItems = iSql.queryDownloadTask(EntityMap);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	//
	// }

	private void OpenFile(File currentPath) {
		PackageManager Pm = mContext.getPackageManager();
		if (currentPath != null && currentPath.isFile()) {
			String fileName = currentPath.toString();
			List<ResolveInfo> Resolve = Pm.queryIntentActivities(
					OpenFiles.getIntent(fileName, currentPath), 0);
			if (Resolve.size() == 0) {
				showMessage("没有对应的打开工具！！！");
			} else {
				mContext.startActivity(OpenFiles.getIntent(fileName,
						currentPath));
			}
		} else {
			showMessage("打开文件出错了！！！");
		}

	}

	private void showMessage(String Txt) {
		Toast.makeText(mContext, Txt, Toast.LENGTH_LONG).show();
	}
}
