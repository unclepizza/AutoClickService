package com.example.autoclickdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView tvStart;
	TextView tvStop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvStart = (TextView) findViewById(R.id.tv_start);
		tvStop = (TextView) findViewById(R.id.tv_stop);
		tvStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, AutoService.class);
				startService(i);
			}
		});

		tvStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, AutoService.class);
				stopService(i);
				tvStart.setText("开启服务");
			}
		});
	}

	/**
	 * 打印点击的点的坐标
	 * 
	 * @param event
	 * @return
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int x = (int) event.getX();
		int y = (int) event.getY();
		tvStart.setText("X at " + x + ";Y at " + y);
		return true;
	}
}
