package com.web2korea.nicemusicapp;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MusicListActivity extends Activity {
    
    private ListView __musicListView = null;
    private Cursor __cursor = null;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_list);
				
		__musicListView = (ListView) findViewById(R.id.musicListView);
		
        String[] cursorColumns = new String[] {
				MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.ARTIST
		};
		
		int id[] = new int[]{R.id.title, R.id.artist};
                
		__cursor = getCursor();        
		extracted(__cursor);
        @SuppressWarnings("deprecation")
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.music_list_entry, __cursor, cursorColumns, id);
        __musicListView.setAdapter(adapter);
        __musicListView.setOnItemClickListener(__onItemClickListener);
    }
    
    private AdapterView.OnItemClickListener __onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long l_position) {
        	        	       	
        	Log.e("MusicApp", "####" + position);
        	
        	if (__cursor.moveToPosition(position))
        	{
        		int fileColumn = __cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        		int mimeColumn = __cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE);   
        		int albumColumn = __cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
        		
        		String filePath = __cursor.getString(fileColumn);
        		String mineType = __cursor.getString(mimeColumn);     	
        		String albumId = __cursor.getString(albumColumn);
        		
            	Log.e("MusicApp", "####" + filePath + "AlbumID: " + albumId);
        		
        		Intent intent = new Intent(android.content.Intent.ACTION_VIEW);     	
        		File mediaFile = new File(filePath);
        		intent.setDataAndType(Uri.fromFile(mediaFile), mineType);
        		startActivity(intent);
        	}  	  	        	
        }
    };
    
	@SuppressWarnings("deprecation")
	private void extracted(Cursor cursor) {
		startManagingCursor(cursor);
	}
        
    public Cursor getCursor()
    {
    	Cursor cursor = null;
    	
		String[] cursorColumns = new String[] {
				MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.MIME_TYPE,
				MediaStore.Audio.Media.ALBUM_ID
		};
				
	    cursor = (Cursor) getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				//MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
	    		cursorColumns, null, null, null);
    	
    	return cursor;
    }    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}