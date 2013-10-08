package com.web2korea.nicemusicapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button startBtn = (Button) findViewById(R.id.startBtn);
		startBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Toast.makeText(getApplicationContext(), "Pressed Start Button",
						Toast.LENGTH_LONG).show();

				Intent myIntent = new Intent(getApplicationContext(),
						MusicListActivity.class);
				startActivity(myIntent);

			}

		});

		Button start2Btn = (Button) findViewById(R.id.start2Btn);
		start2Btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://m.naver.com"));
				startActivity(myIntent);

			}

		});

		Button start3Btn = (Button) findViewById(R.id.start3Btn);
		start3Btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent myIntent1 = new Intent(Intent.ACTION_VIEW, Uri
						.parse("tel:010-1111-1111"));
				startActivity(myIntent1);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
