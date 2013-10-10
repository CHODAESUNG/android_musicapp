package com.web2korea.nicemusicapp;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class AudioService extends Service {
	MediaPlayer mMediaPlayer;
	String mMp3Path = "";

	@Override
	public void onCreate() {
		super.onCreate();

		//Create MediaPlayer
		mMediaPlayer = new MediaPlayer();
		
		Log.e("AudioService", "OnCreated");

		//When called OnCompletionListener().
		mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer arg0) {
				// stopSelf();
			}
		});
	}

	Runnable mRun = new Runnable() {

		public void run() {
			try {
				//Play media.
				mMediaPlayer.setDataSource(mMp3Path);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "service start..", Toast.LENGTH_SHORT).show();
	
		mMp3Path = intent.getStringExtra("filePath");
		Log.e("AudioService", "FilePath: " + mMp3Path);
		
		File mp3file = new File(mMp3Path);
		if (mp3file.exists()) {
			new Thread(mRun).start();
		}

		return START_STICKY;
	}


	@Override
	public void onDestroy() {
		Toast.makeText(this, "service destroy..", Toast.LENGTH_SHORT).show();
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
