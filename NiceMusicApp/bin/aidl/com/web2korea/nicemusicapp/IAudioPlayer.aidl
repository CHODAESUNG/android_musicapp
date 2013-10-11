package com.web2korea.nicemusicapp;

interface IAudioPlayer {
	
	boolean playMusic(String filePath);
	boolean pauseMusic();
    void backMusic();
	void fowardMusic();
}
