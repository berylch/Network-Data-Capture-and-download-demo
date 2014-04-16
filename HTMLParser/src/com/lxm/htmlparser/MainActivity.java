package com.lxm.htmlparser;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import cn.waps.AppConnect;
import cn.waps.extend.QuitPopAd;
import cn.waps.extend.SlideWall;

import com.github.snowdream.android.app.DownloadManager;
import com.github.snowdream.android.app.DownloadTask;

import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

public class MainActivity extends ListActivity implements
		OnEditorActionListener {
	private TextView tv;
	private Handler MyHandler;
	Button Updatabut;
	private boolean bIsSave;
	private PopupWindow popupWindow = null;
	public static ReadUrlThread MyThread;
	// PackageManager p = getPackageManager();
	private MyProgressDialog pd = null;
	private PullToRefreshListView listView;
	private Myapplication Myapp;
	private int list_pos_click = 0; // listitem click pos save
	private SlideWall SW;
	private PaginationAdapter adapter;
	// private View loadMoreView;
	// private Button loadMoreButton;
	private Handler handler = new Handler();
	static private DownloadManager downloadManager = null;

	// 主 下载管理器
	private ScrollView MyScrollView;
	private TextView ShowNum;
	private EditText SearchText;
	private ImageView searchButton;
	private TextView zhichiyixia;
	private TextView fankui;
	private View slidingDrawerView;

	/**
	 * market://search?q=pnames:<package>
	 */
	private void startSearchPNAMESIntent(Context context) {
		Uri uri = Uri.parse("market://search?q=pnames:" + getPackageName());
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		PackageManager Pm = context.getPackageManager();
		List<ResolveInfo> Resolve = Pm.queryIntentActivities(it, 0);
		if (Resolve.size() == 0) {
			MyToast.show(context, "没有手机市场，囧！！", Toast.LENGTH_LONG);
		} else {
			startActivity(it);
		}
	}

	private void ExitOkCancel() {
		new AlertDialog.Builder(this)
				.setTitle("确认退出吗？")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 点击“确认”后的操作
						// 直接 退出
						if (ReadUrlThread.bIsRuning) {
							MainActivity.MyThread.GetMyHandle().getLooper()
									.quit();
						}
						AppConnect.getInstance(MainActivity.this).close();
						Process.killProcess(Process.myPid());

					}
				})
				.setNegativeButton("返回", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 点击“返回”后的操作,这里不设置没有任何操作
					}
				}).show();

	}

	public static String GetSystemVersion() {

		return android.os.Build.VERSION.RELEASE;
	}

	private void disableConnectionReuseIfNecessary() {
		// Work around pre-Froyo bugs in HTTP connection reuse.
		// if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO)
		// {
		System.setProperty("http.keepAlive", "false");
		Log.i("disableConnectionReuseIfNecessary", "eeee");
		// }
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (actionId) {
		case EditorInfo.IME_ACTION_SEARCH:
			if (SearchText.getText().toString().length() != 0) {
				adapter.clean();
				adapter.notifyDataSetChanged();
				if (MyThread != null) {
					Log.i("lxm", "MyThread");
				}
				MyThread.SetInitUrl(SearchText.getText().toString());

				Seachering(MyUtil.Search);
			} else {
				MyToast.show(MainActivity.this, "请输入关键字", Toast.LENGTH_LONG);
			}
			break;
		}
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		downloadManager = DownloadManager.getInstance(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		disableConnectionReuseIfNecessary();
		MyScrollView = (ScrollView) findViewById(R.id.tongzhi);
		ShowNum = (TextView) findViewById(R.id.show_num);
		SearchText = (EditText) findViewById(R.id.SearchText);
		SearchText.setOnEditorActionListener(this);
		searchButton = (ImageView) findViewById(R.id.searchButton);
		zhichiyixia = (TextView) findViewById(R.id.zhichiyixia);
		fankui = (TextView) findViewById(R.id.fankuiwenti);
		// 互动广告调用方式
		// LinearLayout layout = (LinearLayout) this
		// .findViewById(R.id.AdLinearLayout);
		// AppConnect.getInstance(this).showBannerAd(this, layout);

		fankui.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AppConnect.getInstance(MainActivity.this).showFeedback(
						MainActivity.this);
			}
		});

		((TextView) findViewById(R.id.xiazailiebiao))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent();
						i.setClassName("com.lxm.htmlparser",
								"com.github.snowdream.android.apps.downloader.MainActivity");
						startActivity(i);
					}
				});
		((TextView) findViewById(R.id.tuijianliebiao))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						AppConnect.getInstance(MainActivity.this)
								.showAppOffers(MainActivity.this);
					}
				});
		zhichiyixia.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startSearchPNAMESIntent(MainActivity.this);
			}
		});
		// 初始化统计器，并通过代码设置APP_ID, APP_PID
		AppConnect.getInstance("ac4969450c8a2b8a99f251bf9f8b4f82", "default",
				this);
		AppConnect.getInstance(this).initAdInfo();
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (SearchText.getText().toString().length() != 0) {
					adapter.clean();
					adapter.notifyDataSetChanged();
					if (MyThread != null) {
						Log.i("lxm", "MyThread");
					}
					MyThread.SetInitUrl(SearchText.getText().toString());

					Seachering(MyUtil.Search);
					if (getCurrentFocus() != null) {
						((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
								.hideSoftInputFromWindow(getCurrentFocus()
										.getWindowToken(),
										InputMethodManager.HIDE_NOT_ALWAYS);
					}
				} else {
					MyToast.show(MainActivity.this, "请输入关键字", Toast.LENGTH_LONG);
				}
			}
		});
		MyHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				Log.i("what", "" + msg.what);
				switch (msg.what) {

				case MyUtil.UploadUrlEnd:
					String Doc = (String) msg.obj;
					bIsSave = true;
					((PullToRefreshListView) getListView()).onRefreshComplete();
					pd.dismiss();
					if (!MyUtil.isNetworkAvailable(MainActivity.this)) {
						MyToast.show(MainActivity.this, R.string.jiancewangluo,
								Toast.LENGTH_LONG);
					}
					break;
				case MyUtil.cNetError:
					MyToast.showToast(MainActivity.this, R.string.net_return,
							R.drawable.ic_launcher);
					bIsSave = true;
					((PullToRefreshListView) getListView()).onRefreshComplete();
					pd.dismiss();
					// MyToast.show(MainActivity.this, R.string.jiancewangluo,
					// Toast.LENGTH_LONG);
					break;
				case MyUtil.Uploading:
					ArrayList al2 = (ArrayList) msg.obj;
					if (al2 != null) {
						String get = (String) al2.get(0);
						String get2 = (String) al2.get(1);
						String get3 = (String) al2.get(2);
						String get4 = (String) al2.get(3);
						String get5 = (String) al2.get(4);
						if (get != null) {
							loadMoreData(get, get2, get3, get4, get5, true);
						}
					}
					adapter.notifyDataSetChanged();
					break;
				case MyUtil.NoMore:
					((PullToRefreshListView) getListView()).onRefreshComplete();
					MyToast.show(MainActivity.this, "所有的已加载完毕!",
							Toast.LENGTH_LONG);
					pd.dismiss();
					break;
				case MyUtil.Title:
					ArrayList al = (ArrayList) msg.obj;
					if (al != null) {
						String get1 = (String) al.get(0);
						if (Integer.parseInt((String) al.get(1)) == 0) {
							MyScrollView.setVisibility(View.VISIBLE);
							ShowNum.setText("0");
						} else {
							MyScrollView.setVisibility(View.GONE);

							ShowNum.setText((String) al.get(1));
						}

					}

					adapter.notifyDataSetChanged();
					break;
				case MyUtil.GetDownURL:
					pd.dismiss();
					ArrayList a23 = (ArrayList) msg.obj;
					String geturl = (String) a23.get(0);
					String getfilename = (String) a23.get(1);
					if (geturl.equals("")) {
						Toast.makeText(
								MainActivity.this.getApplicationContext(),
								"获取下载地址失败，请重试！", Toast.LENGTH_SHORT).show();
						return;
					}
					Log.i("download", geturl);
					try {
						if (!Environment.getExternalStorageState().equals(
								android.os.Environment.MEDIA_MOUNTED)) {
							Toast.makeText(
									MainActivity.this.getApplicationContext(),
									"请插入T卡", Toast.LENGTH_SHORT).show();
							return;

						}
						Toast.makeText(
								MainActivity.this.getApplicationContext(),
								"下载开始！", Toast.LENGTH_SHORT).show();
						DownloadManager downloadManager = DownloadManager
								.getInstance(MainActivity.this);
						DownloadTask task = new DownloadTask(MainActivity.this);
						task.setUrl(MyUtil.encode(geturl, "UTF-8"));
						Log.i("download", geturl);
						String[] split = geturl.split("\\.");
						Log.i("download", "" + split.length);
						if (split.length == 1) {
							task.setName(getfilename);
						} else {
							task.setName(getfilename + "."
									+ split[split.length - 1]);
						}
						downloadManager.add(task,
								((Myapplication) getApplication())
										.getDownloadListener());
						// downloadManager.start(task,
						// ((Myapplication) getApplication())
						// .getDownloadListener());
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(
								MainActivity.this.getApplicationContext(),
								"下载线程创建失败，请重试！", Toast.LENGTH_SHORT).show();
					}
					break;
				// 下载中出错捕获
				case MyUtil.DownLoadErr:
					String errfilename = (String) msg.obj;
					Toast.makeText(MainActivity.this.getApplicationContext(),
							"下载 " + errfilename + " 出错 ,请重试！",
							Toast.LENGTH_SHORT).show();
					((PullToRefreshListView) getListView()).onRefreshComplete();
					break;
				case MyUtil.DownLoadOver:
					String overfilename = (String) msg.obj;
					Toast.makeText(MainActivity.this.getApplicationContext(),
							"下载 " + overfilename + " 完成！", Toast.LENGTH_SHORT)
							.show();
					((PullToRefreshListView) getListView()).onRefreshComplete();
					break;
				default:
					bIsSave = false;
					break;
				}
			};
		};
		MyThread = new ReadUrlThread(MyHandler);
		MyThread.start();
		MyThread.setName("url parser Thread");
		// AppConnect.getInstance(this);
		// 抽屉式应用墙
		// 1,将drawable-hdpi文件夹中的图片全部拷贝到新工程的drawable-hdpi文件夹中
		// 2,将layout文件夹中的detail.xml和slidewall.xml两个文件，拷贝到新工程的layout文件夹中
		// 获取抽屉样式的自定义广告
		SW = SlideWall.getInstance();
		slidingDrawerView = SlideWall.getInstance().getView(this);
		if (slidingDrawerView != null) {
			this.addContentView(slidingDrawerView, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
		if (MyUtil.isNetworkAvailable(MainActivity.this)) {

			try {
				Thread.sleep(100);
			} catch (Exception Ex) {
				Ex.printStackTrace();
			}

		} else {

			MyDialog Dialog = new MyDialog(MainActivity.this,
					R.string.net_error, R.layout.custom_dialog);
			Dialog.setlauncherset(true);
			Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			Dialog.show();
		}
		listView = (PullToRefreshListView) getListView();
		initializeAdapter();
		getListView().setAdapter(adapter);
		listView.RemoveFooterView();
		((PullToRefreshListView) getListView())
				.setOnRefreshListener(new OnRefreshListener() {
					@Override
					public void onRefresh() {
						updataurlgoing(MyUtil.goingUpload);
						// ((PullToRefreshListView) getListView())
						// .onRefreshComplete();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		// ExitOkCancel();
		if (SW.IsOpenSlidingDrawer()) {
			SW.closeSlidingDrawer();
		} else {

			QuitPopAd.getInstance().show(this);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// case R.id.action_about:
		// MyDialog Dialog = new MyDialog(MainActivity.this, R.string.Author);
		// Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Dialog.show();
		// break;
		case R.id.action_exit:
			// ExitOkCancel();
			break;
		case R.id.download_list:
			Intent i = new Intent();
			i.setClassName("com.lxm.htmlparser",
					"com.github.snowdream.android.apps.downloader.MainActivity");
			startActivity(i);
			break;
		// case R.id.action_tuijian:
		// // AppConnect.getInstance(this).showGameOffers(this);
		// break;
		case R.id.action_settings:

			Intent intent = new Intent(this, MySetting.class);
			this.startActivity(intent);
			break;
		// case R.id.action_updata:
		// (MyUtil.goingUpload);
		// break;
		// case R.id.action_Search:
		// final View DialogView = factory.inflate(R.layout.dialog, null);
		// final EditText cc = new EditText(this);
		// // cc.setId(100);
		// AlertDialog a = new AlertDialog.Builder(this)
		// .setTitle("请输入")
		// .setIcon(android.R.drawable.ic_dialog_info)
		// .setView(cc)
		// .setPositiveButton("确定",
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		//
		//
		// }
		// }).setNegativeButton("取消", null).show();
		// break;
		}

		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.description1) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.downloader, menu);
		} else {

		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		// info.position
		switch (item.getItemId()) {
		case R.id.downloader:
			break;
		// case R.id.menu_item_delete_all:
		// break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);

	}

	/**
	 * 初始化ListView的适配器
	 */
	private void initializeAdapter() {
		List<News> news = new ArrayList<News>();
		adapter = new PaginationAdapter(news);
	}

	/**
	 * 加载更多数据
	 */
	private void loadMoreData(String Title, String Content, String downloadurl,
			String zuozhe, String zuopin, boolean isshowdownload) {
		int count = adapter.getCount();
		News item = new News();
		item.setTitle(Title);
		item.setContent(Content);
		item.isshowdownload(isshowdownload);
		item.setdownloadurl(downloadurl);
		item.setzuozhe(zuozhe);
		item.setzuopin_leibie(zuopin);
		adapter.addNewsItem(item);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		switch (event.getAction()) {
		case KeyEvent.ACTION_DOWN: {
			if (keyCode == KeyEvent.KEYCODE_MENU) {

				initPopupWindow();
			}
		}
			break;
		}
		return super.onKeyDown(keyCode, event);

	};

	class PaginationAdapter extends BaseAdapter {

		List<News> newsItems;

		public PaginationAdapter(List<News> newsitems) {
			this.newsItems = newsitems;
		}

		@Override
		public int getCount() {
			return newsItems.size();
		}

		@Override
		public Object getItem(int position) {
			return newsItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View view, ViewGroup parent) {
			boolean bIsdownload = newsItems.get(position).showdownload();
			boolean click = newsItems.get(position).getclick();

			if (bIsdownload) {

				view = getLayoutInflater().inflate(R.layout.list_item1, null);

			} else {
				view = getLayoutInflater().inflate(R.layout.list_item2, null);

			}

			TextView tvTitle = (TextView) view.findViewById(R.id.newstitle);
			tvTitle.setText(newsItems.get(position).getTitle());
			if (bIsdownload) {
				((TextView) view.findViewById(R.id.zuozhe)).setText("作者："
						+ newsItems.get(position).getzuozhe());
				((TextView) view.findViewById(R.id.zuopinleibie)).setText("类别："
						+ newsItems.get(position).getzuopin_leibie());
				final TextView tvMore = (TextView) view
						.findViewById(R.id.newsMore);
				tvMore.setText(newsItems.get(position).getContent());
				if (click) {
					tvMore.setSingleLine(false);
				} else {
					tvMore.setSingleLine(true);
				}
				((View) view.findViewById(R.id.body))
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								if (list_pos_click == position) {
									News Arr = adapter.getNewsItem(position);
									if (Arr.getclick()) {
										adapter.SetClick(position, false);
									} else {
										adapter.SetClick(position, true);
									}
									adapter.notifyDataSetChanged();
								} else {

									adapter.SetClick(list_pos_click, false);
									adapter.SetClick(position, true);
									list_pos_click = position;
									adapter.notifyDataSetChanged();
								}

							}
						});
				TextView app_status = (TextView) view
						.findViewById(R.id.app_status);
				app_status.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						adapter.Setisdown(position, true);
						adapter.notifyDataSetChanged();
						News Arr = adapter.getNewsItem(position);
						String downgeturl = Arr.getdownloadurl();
						try {
							Log.i("download", downgeturl);

							String downurl = null;
							ArrayList ttt = new ArrayList();
							ttt.add(downgeturl);
							ttt.add(Arr.getTitle());
							getdownurl(MyUtil.GetDownURL, ttt);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}

			return view;
		}

		/**
		 * 添加数据列表项
		 * 
		 * @param newsitem
		 */
		public void addNewsItem(News newsitem) {
			newsItems.add(newsitem);
		}

		public News getNewsItem(int pos) {
			return newsItems.get(pos);
		}

		public void clean() {
			newsItems.clear();
		}

		public void SetClick(int pos, boolean click) {
			newsItems.get(pos).Setclick(click);
		}

		public boolean GetClick(int pos) {
			return newsItems.get(pos).getclick();
		}

		public void Setisdown(int pos, boolean click) {
			newsItems.get(pos).setdownloading(click);
		}

		public boolean Getisdown(int pos) {
			return newsItems.get(pos).Getdownloading();
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);

		return true;
	}

	private void updataurlgoing(int Type) {
		if (!ReadUrlThread.bIsRuning)
			return;

		MyThread.GetMyHandle().sendEmptyMessage(Type);
		// pd = MyUtil.showprocessDialog(MainActivity.this);
	}

	private void Seachering(int Type) {
		if (!ReadUrlThread.bIsRuning)
			return;
		// loadMoreButton.setText(R.string.on_loading);
		MyThread.GetMyHandle().sendEmptyMessage(Type);
		pd = MyUtil.showprocessDialog(MainActivity.this);
	}

	private void getdownurl(int Type, ArrayList url) {
		if (!ReadUrlThread.bIsRuning)
			return;
		Message MyMessage = MyThread.GetMyHandle().obtainMessage(Type, url);
		// MyThread.GetMyHandle().sendEmptyMessage(Type);
		MyThread.GetMyHandle().sendMessage(MyMessage);
		pd = MyUtil.showprocessDialog(MainActivity.this);
	}

	static public DownloadManager getMainDownloadManager() {
		return downloadManager;
	}

	private final class ItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();// 关闭
				// getParent().getWindow().c
			}

			switch (position) {
			case 7:
				// initjinduPopupWindow(turntest.this);
				break;
			case 6: // 分享
				// MyUtil.fenxiang(turntest.this);
				break;
			case 4: // 娱乐
				// AppConnect.getInstance(turntest.this).showGameOffers(
				// turntest.this);
				break;
			case 2: // 亮度调节
				startSearchPNAMESIntent(MainActivity.this);
				break;
			case 5: // 关于
				// MyDialog();
				break;
			case 1: //
				Intent i = new Intent();
				i.setClassName("com.lxm.htmlparser",
						"com.github.snowdream.android.apps.downloader.MainActivity");
				startActivity(i);
				break;
			case 3:
				AppConnect.getInstance(MainActivity.this).showFeedback(
						MainActivity.this);
				break;
			case 0:
				AppConnect.getInstance(MainActivity.this).showGameOffers(
						MainActivity.this);
				break;

			}
		}
	}

	private String[] mTitles = { "推荐列表", "下载列表", "支持作者", "反馈问题" };

	/**
	 * 弹出PopupWindow
	 */
	public void initPopupWindow() {
		// 加载PopupWindow的布局文件
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.popup, null);
		View parentView = layoutInflater.inflate(R.layout.first, null);
		TextView black = (TextView) view.findViewById(R.id.black_txt);
		black.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		});

		// black.get
		GridView gridView = (GridView) view.findViewById(R.id.popup_grid);
		GridViewAdapter adapter = new GridViewAdapter(this, mTitles,
				R.layout.grid);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new ItemClickListener());
		gridView.setOnItemSelectedListener(new OnItemSelectedListener() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * android.widget.AdapterView.OnItemSelectedListener#onItemSelected
			 * (android.widget.AdapterView, android.view.View, int, long)
			 */
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * android.widget.AdapterView.OnItemSelectedListener#onNothingSelected
			 * (android.widget.AdapterView)
			 */
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		// 声明并实例化PopupWindow
		popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		// 为PopupWindow设置弹出的位置
		// popupWindow.showAsDropDown(parentView);
		// popupWindow.setAnimationStyle(R.style.AnimationPreview);

		// popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true);
		popupWindow.setTouchable(true);
		// popupWindow.
		// popupWindow.update();
		// popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);// 点击窗口外消失,需要设置背景、焦点、touchable、update
		// popupWindow.set
		popupWindow.setWindowLayoutMode(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		popupWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0);
		// popupWindow.setFocusable(true);
		// 这句是为了防止弹出菜单获取焦点之后，点击activity的其他组件没有响应
		// popupWindow.setAnimationStyle(R.style.popup_show_out);
		popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
		popupWindow.getContentView().findViewById(R.id.main)
				.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						// TODO Auto-generated method stub
						Log.i("lxm", "key press");
						switch (event.getAction()) {
						case KeyEvent.ACTION_DOWN:
							if (keyCode == KeyEvent.KEYCODE_MENU
									|| keyCode == KeyEvent.KEYCODE_BACK) {
								if (popupWindow != null) {
									popupWindow.dismiss();
								}
							}
							break;
						}
						return false;
					}
				});
	}
}
