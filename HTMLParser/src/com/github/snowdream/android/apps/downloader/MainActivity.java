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

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.snowdream.android.app.DownloadListener;
import com.github.snowdream.android.app.DownloadManager;
import com.github.snowdream.android.app.DownloadStatus;
import com.github.snowdream.android.app.DownloadTask;
import com.github.snowdream.android.util.Log;
import com.lxm.htmlparser.MyToast;
import com.lxm.htmlparser.Myapplication;

/**
 * @author snowdream <yanghui1986527@gmail.com>
 * @version v1.0
 * @date Sep 29, 2013
 */
// @EActivity(R.layout.activity_main)
public class MainActivity extends ListActivity {

	private DownloadManager downloadManager = null;
	private List<DownloadTask> list = null;
	private DownloadTaskAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		downloadManager = DownloadManager.getInstance(this);
		list = new ArrayList<DownloadTask>();
		adapter = new DownloadTaskAdapter(this, list,
				((Myapplication) getApplication()).getDownloadListener());
		;
		((Myapplication) getApplication()).putDownloadListener(listener);
		setListAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		//adapter.destory();
		super.onDestroy();
		((Myapplication) getApplication()).popDownloadListener(listener);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((DownloadTaskAdapter) getListAdapter()).notifyDataSetChanged();
		// ((DownloadTaskAdapter) getListAdapter()).notifyDataSetInvalidated();
	}

	static private int[] statustbl = { DownloadStatus.STATUS_NONE,
			DownloadStatus.STATUS_RUNNING, DownloadStatus.STATUS_FINISHED,
			DownloadStatus.STATUS_DELETED, };

	@Override
	public void onBackPressed() {

		super.onBackPressed();

	}

	private DownloadListener listener = new DownloadListener<Integer, DownloadTask>() {
		/**
		 * The download task has been added to the sqlite.
		 * <p/>
		 * operation of UI allowed.
		 * 
		 * @param downloadTask
		 *            the download task which has been added to the sqlite.
		 */
		@Override
		public void onAdd(DownloadTask downloadTask) {
			super.onAdd(downloadTask);
			Log.i("onAdd()");
			// list.add(downloadTask);
			Log.i("" + downloadTask);
			adapter.notifyDataSetChanged();
		}

		/**
		 * The download task has been delete from the sqlite
		 * <p/>
		 * operation of UI allowed.
		 * 
		 * @param downloadTask
		 *            the download task which has been deleted to the sqlite.
		 */
		@Override
		public void onDelete(DownloadTask downloadTask) {
			super.onDelete(downloadTask);
			Log.i("onDelete()");
			((DownloadTaskAdapter) getListAdapter()).notifyDataSetChanged();
			// ((DownloadTaskAdapter)
			// getListAdapter()).notifyDataSetInvalidated();
		}

		/**
		 * The download task is stop
		 * <p/>
		 * operation of UI allowed.
		 * 
		 * @param downloadTask
		 *            the download task which has been stopped.
		 */
		@Override
		public void onStop(DownloadTask downloadTask) {
			super.onStop(downloadTask);
			Log.i("onStop()");
			((DownloadTaskAdapter) getListAdapter()).notifyDataSetChanged();
			// ((DownloadTaskAdapter)
			// getListAdapter()).notifyDataSetInvalidated();
		}

		/**
		 * Runs on the UI thread before doInBackground(Params...).
		 */
		@Override
		public void onStart(DownloadTask task) {
			super.onStart(task);
			Log.i("onStart()----");
			((DownloadTaskAdapter) getListAdapter()).notifyDataSetChanged();
			// ((DownloadTaskAdapter)
			// getListAdapter()).notifyDataSetInvalidated();
		}

		@Override
		public void onStart() {
			super.onStart();
			Log.i("onStart()");
			((DownloadTaskAdapter) getListAdapter()).notifyDataSetChanged();
			// ((DownloadTaskAdapter)
			// getListAdapter()).notifyDataSetInvalidated();
		}

		/**
		 * Runs on the UI thread after publishProgress(Progress...) is invoked.
		 * The specified values are the values passed to
		 * publishProgress(Progress...).
		 * 
		 * @param values
		 *            The values indicating progress.
		 */
		@Override
		public void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			((DownloadTaskAdapter) getListAdapter()).notifyDataSetChanged();
			// ((DownloadTaskAdapter)
			// getListAdapter()).notifyDataSetInvalidated();
			Log.i("onProgressUpdate");
		}

		/**
		 * Runs on the UI thread after doInBackground(Params...). The specified
		 * result is the value returned by doInBackground(Params...). This
		 * method won't be invoked if the task was cancelled.
		 * 
		 * @param downloadTask
		 *            The result of the operation computed by
		 *            doInBackground(Params...).
		 */
		@Override
		public void onSuccess(DownloadTask downloadTask) {
			super.onSuccess(downloadTask);
			Log.i("onSuccess()");
			((DownloadTaskAdapter) getListAdapter()).notifyDataSetChanged();
			// ((DownloadTaskAdapter)
			// getListAdapter()).notifyDataSetInvalidated();
		}

		/**
		 * Applications should preferably override onCancelled(Object). This
		 * method is invoked by the default implementation of
		 * onCancelled(Object). Runs on the UI thread after cancel(boolean) is
		 * invoked and doInBackground(Object[]) has finished.
		 */
		@Override
		public void onCancelled() {
			super.onCancelled();
			Log.i("onCancelled()");
			((DownloadTaskAdapter) getListAdapter()).notifyDataSetChanged();
			// ((DownloadTaskAdapter)
			// getListAdapter()).notifyDataSetInvalidated();
		}

		@Override
		public void onError(Throwable thr) {
			super.onError(thr);
			// Log.i("onError()");
			// MyToast.show(MainActivity.this, "下载出错，请确认网络链接情况 以及插入SD卡",
			// Toast.LENGTH_LONG);
			((DownloadTaskAdapter) getListAdapter()).notifyDataSetChanged();
			// ((DownloadTaskAdapter)
			// getListAdapter()).notifyDataSetInvalidated();
		}

		/**
		 * Runs on the UI thread after doInBackground(Params...) when the task
		 * is finished or cancelled.
		 */
		@Override
		public void onFinish() {
			super.onFinish();
			Log.i("onFinish()");
			// // searching.setText(R.string.yiwancheng);
			((DownloadTaskAdapter) getListAdapter()).notifyDataSetChanged();
			// ((DownloadTaskAdapter)
			// getListAdapter()).notifyDataSetInvalidated();

		}

		/**
		 * Runs on the UI thread after doInBackground(Params...) when the task
		 * is finished or cancelled.
		 */
		@Override
		public void onFinish(DownloadTask task) {
			super.onFinish(task);
			Log.i("onFinish()----");
			// // searching.setText(R.string.yiwancheng);
			// MyToast.show(MainActivity.this, "<<" + task.getName() + ">>"
			// + "文档已下载完成\n路径为:" + task.getPath(), Toast.LENGTH_LONG);
			// ((DownloadTaskAdapter) getListAdapter()).notifyDataSetChanged();
			// ((DownloadTaskAdapter)
			// getListAdapter()).notifyDataSetInvalidated();

		}
	};
}
