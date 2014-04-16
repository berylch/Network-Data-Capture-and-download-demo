package com.lxm.htmlparser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import cn.waps.AppConnect;

import com.lxm.tools.ExitDoubleClick;
import com.lxm.tools.ExitDoubleClick.OnDoubleClick;

public class MyMainActivity extends Activity implements OnEditorActionListener {
	static String Search_key = "search";
	private EditText Search_text;
	private ExitDoubleClick ExitClick = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_main);
		Search_text = (EditText) findViewById(R.id.search_criteria);
		Search_text.setOnEditorActionListener(this);
		Button go_but = (Button) findViewById(R.id.go_button);
		ExitClick = ExitDoubleClick.getInstance(MyMainActivity.this,
				new OnDoubleClick() {

					@Override
					public void OnDoubleClickCb() {
						// TODO Auto-generated method stub
						Log.i("lxm", "exit");
						AppConnect.getInstance(MyMainActivity.this).close();
					}
				});
		go_but.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Search_text.getText().toString().length() <= 0) {
					Toast.makeText(MyMainActivity.this, "请输入搜索字串",
							Toast.LENGTH_LONG).show();
				} else {
					Intent go_Main = new Intent();
					go_Main.setClass(MyMainActivity.this, MainActivity.class);
					go_Main.putExtra(Search_key, Search_text.getText()
							.toString());
					startActivity(go_Main);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.my_main, menu);
		return true;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (actionId) {
		case EditorInfo.IME_ACTION_SEARCH:
			if (Search_text.getText().toString().length() <= 0) {
				Toast.makeText(MyMainActivity.this, "请输入搜索字串",
						Toast.LENGTH_LONG).show();
			} else {
				Intent go_Main = new Intent();
				go_Main.setClass(MyMainActivity.this, MainActivity.class);
				go_Main.putExtra(Search_key, Search_text.getText().toString());
				startActivity(go_Main);
			}
			break;
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		// ExitOkCancel();
		ExitOkCancel();
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
						// AppConnect.getInstance(MainActivity.this).close();
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		AppConnect.getInstance(this);
	}
}
