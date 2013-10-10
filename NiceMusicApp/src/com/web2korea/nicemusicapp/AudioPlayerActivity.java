package com.web2korea.nicemusicapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class AudioPlayerActivity extends Activity implements OnClickListener {

	final String AUDIO_APP_TAG = "AudioApp";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ImageView albumView = (ImageView) findViewById(R.id.imageView1);
		Button startBtn = (Button) findViewById(R.id.playBtn);
		Button pauseBtn = (Button) findViewById(R.id.pauseBtn);
		Button restartBtn = (Button) findViewById(R.id.restartBtn);
		
		Uri sArtWorkUri = Uri.parse("content://media/external/audio/albumart");
		Uri sAlbumAArtUri = ContentUris.withAppendedId(sArtWorkUri, 37);
		
		albumView.setImageURI(sAlbumAArtUri);
		
		
		startBtn.setOnClickListener(this);
		pauseBtn.setOnClickListener(this);
		restartBtn.setOnClickListener(this);
	}
	
	ServiceConnection __serviceConnection = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			
			IAudioPlayer audioPlayer = IAudioPlayer.Stub.asInterface(arg1);
			try {
				audioPlayer.playMusic("/storage/emulated/0/Download/___001_029.mp3");
			} catch (RemoteException e) {
				
			}
			
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	

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