package com.lxm.htmlparser;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.widget.Toast;

import com.github.snowdream.android.app.DownloadListener;
import com.github.snowdream.android.app.DownloadTask;
import com.github.snowdream.android.apps.downloader.DownloadTaskAdapter;
import com.github.snowdream.android.apps.downloader.MainActivity;
import com.github.snowdream.android.util.Log;

public class Myapplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if (ListenerArr == null) {
			ListenerArr = new ArrayList<DownloadListener<Integer, DownloadTask>>();
		}
	}

	public void putDownloadListener(
			DownloadListener<Integer, DownloadTask> Listener) {
		if (Listener == null) {
			Log.i("Listener null");
			return;
		}
		if (ListenerArr == null) {
			ListenerArr = new ArrayList<DownloadListener<Integer, DownloadTask>>();
		}
		for (DownloadListener<Integer, DownloadTask> every : ListenerArr) {
			if (every == Listener) {
				return;// 存在时，直接返回
			}
		}
		ListenerArr.add(Listener);

		;
	}

	public void popDownloadListener(
			DownloadListener<Integer, DownloadTask> Listener) {
		if (Listener == null) {
			Log.i("Listener null");
			return;
		}
		if (ListenerArr == null) {
			return;
		}
		for (DownloadListener<Integer, DownloadTask> every : ListenerArr) {
			if (every == Listener) {
				ListenerArr.remove(every);
				break;
			}
		}

	}

	public DownloadListener<Integer, DownloadTask> getDownloadListener() {
		return listener;
	}

	private List<DownloadListener<Integer, DownloadTask>> ListenerArr = null;;
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
			for (DownloadListener<Integer, DownloadTask> every : ListenerArr) {
				every.onAdd(downloadTask);
			}

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
			for (DownloadListener<Integer, DownloadTask> every : ListenerArr) {
				every.onDelete(downloadTask);
			}
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
			for (DownloadListener<Integer, DownloadTask> every : ListenerArr) {
				every.onStop(downloadTask);
			}
		}

		/**
		 * Runs on the UI thread before doInBackground(Params...).
		 */
		@Override
		public void onStart(DownloadTask task) {
			super.onStart(task);
			for (DownloadListener<Integer, DownloadTask> every : ListenerArr) {
				every.onStart(task);
			}
		}

		@Override
		public void onStart() {
			super.onStart();
			for (DownloadListener<Integer, DownloadTask> every : ListenerArr) {
				every.onStart();
			}
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
			for (DownloadListener<Integer, DownloadTask> every : ListenerArr) {
				every.onProgressUpdate(values);
			}
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
			for (DownloadListener<Integer, DownloadTask> every : ListenerArr) {
				every.onSuccess(downloadTask);
			}
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
			for (DownloadListener<Integer, DownloadTask> every : ListenerArr) {
				every.onCancelled();
			}
		}

		@Override
		public void onError(Throwable thr) {
			super.onError(thr);
			for (DownloadListener<Integer, DownloadTask> every : ListenerArr) {
				every.onError(thr);
			}
		}

		/**
		 * Runs on the UI thread after doInBackground(Params...) when the task
		 * is finished or cancelled.
		 */
		@Override
		public void onFinish() {
			super.onFinish();
			for (DownloadListener<Integer, DownloadTask> every : ListenerArr) {
				every.onFinish();
			}

		}

		/**
		 * Runs on the UI thread after doInBackground(Params...) when the task
		 * is finished or cancelled.
		 */
		@Override
		public void onFinish(DownloadTask task) {
			super.onFinish(task);

			for (DownloadListener<Integer, DownloadTask> every : ListenerArr) {
				every.onFinish(task);
			}

		}
	};

}
