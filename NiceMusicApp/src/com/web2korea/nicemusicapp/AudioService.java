package com.web2korea.nicemusicapp;

import java.io.File;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class AudioService extends Service {

	final static int PLAY_STATE = 1;
	final static int PAUSE_STATE = 2;
	
	private MediaPlayer __mediaPlayer = null;
	private String __filePath = null;
	
	final String AUDIO_SERVICE_TAG = "AudioService";
	final int NOTIFICATION_REMOVE = 0xffff0001;
	
	private int __state = 0;  

	
	@Override
	public void onCreate() {
		super.onCreate();

		//Create MediaPlayer
		__mediaPlayer = new MediaPlayer();
		
		Log.i("AudioService", "Created the __mediaPlayer.");		

		//When called OnCompletionListener().
		__mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer arg0) {
				__state = 0;				
			}
		});	
		
		__mediaPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener() {
		    @Override
		    public void onSeekComplete(MediaPlayer mediaPlayer) {
		        Log.d("VID_PLAYER","Seek Complete. Current Position: " + mediaPlayer.getCurrentPosition());
		        mediaPlayer.start();
		    }
		});		
				
		IntentFilter receiverFilter = new IntentFilter();
		receiverFilter.addAction(Intent.ACTION_POWER_CONNECTED);
		registerReceiver(__musicReceiver, receiverFilter);
				
		NotificationManager noficicationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	    NotificationCompat.Builder builder =  
	            new NotificationCompat.Builder(this)  
	            .setSmallIcon(R.drawable.smile)  
	            .setContentTitle("Playing the music.")  
	            .setContentText("NiceMusicApp");  		
	    
		Intent intent = new Intent(this, com.web2korea.nicemusicapp.MusicListActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		builder.setContentIntent(pendingIntent); 
	    Notification notification = builder.build();
		
		notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
		//notification.flags = Notification.FLAG_ONGOING_EVENT;
		notification.flags = Notification.FLAG_AUTO_CANCEL;

		noficicationManager.notify(NOTIFICATION_REMOVE, notification);
	}
	
	@Override
	public int onStartCommand(Intent intent,int flags,int startId) {
	    super.onStartCommand(intent, flags, startId);

		Log.i("AudioService", "Called.. onStartCommand.");
	    
	    return START_NOT_STICKY;
	}
	
	
	BroadcastReceiver __musicReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			
			if (Intent.ACTION_POWER_CONNECTED == intent.getAction())
			{		

				Toast.makeText(getBaseContext(), "Intent.ACTION_POWER_CONNECTED is received.", Toast.LENGTH_SHORT).show();
				
				NotificationManager noficicationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				noficicationManager.cancel(NOTIFICATION_REMOVE);
				
				Intent recevIntent = new Intent(context, com.web2korea.nicemusicapp.AudioService.class);
				context.stopService(recevIntent);		
			}				
		}			
	};	
	
	Runnable __runable = new Runnable() {

		public void run() {
			try {
				//Play media.
				__mediaPlayer.setDataSource(__filePath);
				__mediaPlayer.prepare();
				__mediaPlayer.start();
				
				Log.i(AUDIO_SERVICE_TAG, "The thread is finished.");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};	
	
	@Override
	public void onDestroy() {
		
		NotificationManager noficicationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		noficicationManager.cancel(NOTIFICATION_REMOVE);
		
		unregisterReceiver(__musicReceiver);
		
		if (__mediaPlayer != null && __mediaPlayer.isPlaying()) {
			__mediaPlayer.stop();
			__mediaPlayer.release();
			__mediaPlayer = null;
		}
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return __binder;
	}
	
	
	
	boolean startMusicPlayer()
	{		
		Log.i(AUDIO_SERVICE_TAG, "Stop the music.");
		if (__state != 0)
		{
			__mediaPlayer.stop();
			__mediaPlayer.reset();			
		}
		
		File file = new File(__filePath);
		if (file.exists()) {
			new Thread(__runable).start();
		} else
		{
			Log.e(AUDIO_SERVICE_TAG, "Media file does not found!");
			return false;
		}
		
		return true;
	}
		
	
	IAudioPlayer.Stub __binder = new IAudioPlayer.Stub(){

		@Override
		public boolean playMusic(String filePath) throws RemoteException {					
						
			if(__mediaPlayer == null) {
				
				Log.e(AUDIO_SERVICE_TAG, "MediaPlayer must be not null.");
				
				return false;				
				
			} else if (__state == PAUSE_STATE && __filePath.equals(filePath)) {
				
				__mediaPlayer.start();		
				Log.i(AUDIO_SERVICE_TAG, "Resume the music.");
				Toast.makeText(getApplicationContext(), "Resume the music.",
						Toast.LENGTH_LONG).show();	
				
			} else if (__state == PLAY_STATE && __filePath.equals(filePath)) //Already Playing. Ignore.
			{
				Toast.makeText(getApplicationContext(), "The music is already playing.",
						Toast.LENGTH_LONG).show();				
				
			} else	//Play the new music.
			{		
				Log.i(AUDIO_SERVICE_TAG, "Calling playMusic.. filePath: " + filePath);				
				__filePath = filePath;				
				startMusicPlayer();					
				Log.i(AUDIO_SERVICE_TAG, "Start the music.");	
				Toast.makeText(getApplicationContext(), "Play the music.",
						Toast.LENGTH_LONG).show();		
			}
			
			__state = PLAY_STATE;
			
			return true;
		}

		@Override
		public boolean pauseMusic() throws RemoteException {
			
			if (__mediaPlayer != null && __mediaPlayer.isPlaying()) {
				__mediaPlayer.pause();
				__state = PAUSE_STATE;
				Toast.makeText(getApplicationContext(), "Pause the music.",
						Toast.LENGTH_LONG).show();
			} else
			{
				Log.e(AUDIO_SERVICE_TAG, "MediaPlayer is null. or Not Playing!");
				return false;
			}
			
			return true;
		}

		@Override
		public void backMusic() throws RemoteException {
			
			if (__mediaPlayer != null) {
				
				int currentPos = __mediaPlayer.getCurrentPosition();
				int seekPos = 0;
				
				if (currentPos > 10000)
					seekPos = 10000;
				else
					seekPos = currentPos;
				
				__mediaPlayer.seekTo(currentPos - seekPos);

				Toast.makeText(getApplicationContext(), "Seek to << " + seekPos/1000 + " (s)",
						Toast.LENGTH_LONG).show();
			} else
				Log.e(AUDIO_SERVICE_TAG, "MediaPlayer is null.");	
			
		}	

		@Override
		public void fowardMusic() throws RemoteException {
			
			if (__mediaPlayer != null) {
			
				int currentPos = __mediaPlayer.getCurrentPosition();
				int duration = __mediaPlayer.getDuration();
				int seekPos = 0;
				
				if (currentPos + 10000 < duration)
					seekPos = 10000;
				else
					seekPos = duration - currentPos;
				
				__mediaPlayer.seekTo(currentPos + seekPos);
				
				Toast.makeText(getApplicationContext(), "Seek to >> " + seekPos/1000 + " (s)",
						Toast.LENGTH_LONG).show();						
			} else
				Log.e(AUDIO_SERVICE_TAG, "MediaPlayer is null.");
		}		
	};
	
}
