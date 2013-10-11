package com.web2korea.nicemusicapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class AudioPlayerActivity extends Activity implements OnClickListener {

	final String AUDIO_PLAYER_TAG = "AudioPlayer";

	private IAudioPlayer __audioPlayer = null;
	private String __filePath = null;
	
	boolean __isConnected = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_player);

		ImageView albumView = (ImageView) findViewById(R.id.imageView1);
		Button startBtn = (Button) findViewById(R.id.playBtn);
		Button pauseBtn = (Button) findViewById(R.id.pauseBtn);

		Button rewBtn = (Button) findViewById(R.id.rewBtn);
		Button ffBtn = (Button) findViewById(R.id.ffBtn);
		
		Intent receivedIntent = this.getIntent();

		__filePath = receivedIntent.getStringExtra("filePath");
		String albumId = receivedIntent.getStringExtra("albumId");

		Log.i(AUDIO_PLAYER_TAG, "filePath: " + __filePath + " albumId: "
				+ albumId);

		int albumNum = Integer.parseInt(albumId);

		Uri sArtWorkUri = Uri.parse("content://media/external/audio/albumart");
		Uri sAlbumAArtUri = ContentUris.withAppendedId(sArtWorkUri, albumNum);

		albumView.setImageURI(sAlbumAArtUri);

		startBtn.setOnClickListener(this);
		pauseBtn.setOnClickListener(this);
		rewBtn.setOnClickListener(this);
		ffBtn.setOnClickListener(this);

		Intent intent = new Intent(this,
				com.web2korea.nicemusicapp.AudioService.class);

		startService(intent);
		boolean rs = bindService(intent, __serviceConnection, 0);
		if (!rs) {
			Log.i(AUDIO_PLAYER_TAG, "FAiled to bind the service.");
		}

		Log.i(AUDIO_PLAYER_TAG, "Called.. startService");

	}

	@Override
	public void onBackPressed() {

		if (__serviceConnection != null) {
			
			if (__isConnected)
			{
				unbindService(__serviceConnection);
				Log.i(AUDIO_PLAYER_TAG, "Called.. Unbind the service.");
			} else
				Log.e(AUDIO_PLAYER_TAG, "Called.. The connection is not connected.");
		}

		super.onBackPressed();
	}

	ServiceConnection __serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {

			__audioPlayer = IAudioPlayer.Stub.asInterface(arg1);
			Log.i(AUDIO_PLAYER_TAG, "Called.. onServiceConnected");
			
			try {
				__audioPlayer.playMusic(__filePath);
				__isConnected = true;
			} catch (RemoteException e) {
				Log.e(AUDIO_PLAYER_TAG, e.getMessage());
			}			
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {

			Log.i(AUDIO_PLAYER_TAG, "Called.. onServiceDisconnected");
			__isConnected = false;
		}
	};

	public void onClick(View view) {

		if (view.getId() == R.id.playBtn) {

			try {

				__audioPlayer.playMusic(__filePath);
				Log.i(AUDIO_PLAYER_TAG, "Called.. playMusic");

			} catch (Exception e) {
				Log.e(AUDIO_PLAYER_TAG, e.getMessage());
			}

		} else if (view.getId() == R.id.pauseBtn) {

			try {
				__audioPlayer.pauseMusic();
				Log.i(AUDIO_PLAYER_TAG, "Called.. pauseMusic");

			} catch (Exception e) {
				Log.e(AUDIO_PLAYER_TAG, e.getMessage());
			}
		} else if (view.getId() == R.id.rewBtn) {

			try {				
				__audioPlayer.backMusic();
				Log.i(AUDIO_PLAYER_TAG, "Called.. <<");

			} catch (Exception e) {
				Log.e(AUDIO_PLAYER_TAG, e.getMessage());
			}
		} else if (view.getId() == R.id.ffBtn) {

			try {
				__audioPlayer.fowardMusic();
				Log.i(AUDIO_PLAYER_TAG, "Called.. >>");

			} catch (Exception e) {
				Log.e(AUDIO_PLAYER_TAG, e.getMessage());
			}
		}
	}

}