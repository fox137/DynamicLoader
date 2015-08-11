package com.example.dynamicplugin1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements OnClickListener{
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout ll = new LinearLayout(mContext);
		TextView tv = new TextView(mContext);
		tv.setText("I am plugin 1");
		ll.addView(tv);
		
		Button btn = new Button(mContext);
		btn.setText("jump");
		btn.setTag(1);
		btn.setOnClickListener(this);
		ll.addView(btn);
		setContentView(ll);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch ((Integer)v.getTag()) {
		case 1:
			startActivity(new Intent(mContext, InnerActivity.class));
			break;

		default:
			break;
		}
	}
}
