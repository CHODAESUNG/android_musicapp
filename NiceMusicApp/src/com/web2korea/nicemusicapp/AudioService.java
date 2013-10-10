package com.web2korea.nicemusicapp;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class AudioService extends Service {
	MediaPlayer mMediaPlayer;
	String mMp3Path = "";
	
	final String AUDIO_SERVICE_TAG = "AudioService";

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
	
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		Toast.makeText(this, "service start..", Toast.LENGTH_SHORT).show();
//	
//		mMp3Path = intent.getStringExtra("filePath");
//		Log.e("AudioService", "FilePath: " + mMp3Path);
//		
//		File mp3file = new File(mMp3Path);
//		if (mp3file.exists()) {
//			new Thread(mRun).start();
//		}
//
//		return START_STICKY;
//	}


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
		return __binder;
	}
	
	IAudioPlayer.Stub __binder = new IAudioPlayer.Stub(){

		@Override
		public boolean playMusic(String filePath) throws RemoteException {
			
			Log.e(AUDIO_SERVICE_TAG, "Calling playMusic.. filePath: " + filePath);
			File file = new File(filePath);
			if (file.exists()) {
				new Thread(mRun).start();
			} else
			{
				Log.e(AUDIO_SERVICE_TAG, "Media file does not found!");
				return false;
			}
			
			return true;
		}

		@Override
		public boolean pauseMusic() throws RemoteException {
			
			if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
				mMediaPlayer.pause();
			} else
			{
				Log.e(AUDIO_SERVICE_TAG, "MediaPlayer is null. or Not Playing!");
				return false;
			}
			
			return true;
		}

		@Override
		public boolean restartMusic() throws RemoteException {

			if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
				mMediaPlayer.start();
			} else
			{
				Log.e(AUDIO_SERVICE_TAG, "MediaPlayer is null. or Not Playing!");
				return false;
			}
			
			return true;
			
		}
		
	};
	
	
	
}
