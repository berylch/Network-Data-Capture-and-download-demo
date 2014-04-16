package com.lxm.htmlparser;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyDialog extends Dialog {
	private TextView Myok;
	private Context context;
	private int Res_String_id = 0;
	private int layout_id = 0;
	private TextView Mycancel;
	// View.OnClickListener l = null;
	private boolean isLauncherSet = false;

	public MyDialog(Context context, int Res_String_id) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.Res_String_id = Res_String_id;
	}

	public MyDialog(Context context, int Res_String_id, int layout_id) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.Res_String_id = Res_String_id;
		this.layout_id = layout_id;
	}

	public MyDialog(Context context, int Res_String_id, int layout_id,
			View.OnClickListener l) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.Res_String_id = Res_String_id;
		this.layout_id = layout_id;

	}

	public void setlauncherset(boolean flag) {
		isLauncherSet = flag;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (layout_id != 0) {
			this.setContentView(layout_id);
		} else {
			this.setContentView(R.layout.dialog);
		}
		if (Res_String_id != 0) {
			TextView text = (TextView) this.findViewById(R.id.title);
			text.setText(Res_String_id);
		}
		Myok = (TextView) this.findViewById(R.id.button1);
		Myok.setText(R.string.queding);
		Myok.setOnClickListener((new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isLauncherSet) {
					MyUtil.OpenSetting(MyDialog.this.getContext());
				}
				MyDialog.this.dismiss();
			}
		}));
		if (layout_id != 0) {
			Mycancel = (TextView) this.findViewById(R.id.button2);
			Mycancel.setText(R.string.quxiao);
			Mycancel.setOnClickListener((new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					MyDialog.this.dismiss();
				}
			}));
		}
	}

}