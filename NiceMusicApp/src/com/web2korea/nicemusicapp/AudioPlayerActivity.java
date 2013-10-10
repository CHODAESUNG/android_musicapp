package com.web2korea.nicemusicapp;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AudioPlayerActivity extends Activity implements OnClickListener {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button startBtn = (Button) findViewById(R.id.playBtn);
		Button pauseBtn = (Button) findViewById(R.id.pauseBtn);
		Button restartBtn = (Button) findViewById(R.id.restartBtn);

		startBtn.setOnClickListener(this);
		pauseBtn.setOnClickListener(this);
		restartBtn.setOnClickListener(this);
	}

	public void onClick(View view) {

		if (view.getId() == R.id.playBtn) {

			try {
				Intent intent = new Intent(this,
						com.web2korea.nicemusicapp.AudioService.class);
				
				intent.putExtra("filePath", "/storage/emulated/0/Download/___001_029.mp3");		
				
				startService(intent);

				Toast.makeText(getApplicationContext(), "Play the music.",
						Toast.LENGTH_LONG).show();

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (view.getId() == R.id.pauseBtn) {
			try {
				Intent intent = new Intent(this,
						com.web2korea.nicemusicapp.AudioService.class);
				
				intent.putExtra("filePath", "/storage/emulated/0/Download/___001_029.mp3");				
				
				startService(intent);

				Toast.makeText(getApplicationContext(), "Pause the music.",
						Toast.LENGTH_LONG).show();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (view.getId() == R.id.restartBtn) {
			try {
				Intent intent = new Intent(this,
						com.web2korea.nicemusicapp.AudioService.class);
				startService(intent);

				Toast.makeText(getApplicationContext(), "Restart the music.",
						Toast.LENGTH_LONG).show();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void onDestroy() {
		super.onDestroy();
	}
}