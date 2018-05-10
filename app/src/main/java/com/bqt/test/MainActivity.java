package com.bqt.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_activity);
		findViewById(R.id.tv).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SecondActivity.class)));
	}
}