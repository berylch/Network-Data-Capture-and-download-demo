package com.lxm.htmlparser;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

public class MyProgressDialog extends ProgressDialog {

	public MyProgressDialog(Context cont) {
		// TODO Auto-generated constructor stub
		super(cont);
		//setContentView(R.layout.myprogress);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// this.dismiss();
		if (ReadUrlThread.bIsRuning) {
			ReadUrlThread.MyHandle.sendEmptyMessage(MyUtil.Cancel);
		}
		super.onBackPressed();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}
}
