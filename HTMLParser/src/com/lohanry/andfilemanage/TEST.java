package com.lohanry.andfilemanage;

import java.util.ArrayList;

import com.lohanry.andfilemanage.tools.AndFileManage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class TEST extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AndFileManage A = new AndFileManage(this, "/system", new String[]{".txt",".jpg",".apk"}, false);
		A.startOpenActivity();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		ArrayList<String> A = data.getBundleExtra("PATHBUNDLE").getStringArrayList("PATHARRAY");
		super.onActivityResult(requestCode, resultCode, data);
	}

}
