package com.link.core.function;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import com.link.load.AudioLoader;

public class SoundEffect {
//	private boolean playingMusic = false;
	
	private Clip clip;
	
	public SoundEffect(String path) {
		try {
		    AudioInputStream stream;
		    AudioLoader loader = new AudioLoader();
		    AudioFormat format;
		    DataLine.Info info;
		    
		    stream = loader.loadAudio(path);
		    format = stream.getFormat();
		    info = new DataLine.Info(Clip.class, format);
		    clip = (Clip) AudioSystem.getLine(info);
		    clip.open(stream);
		}
		catch (Exception e) {
		    e.printStackTrace();
		}	
	}
	
	public void stopSound() {
		clip.stop();
	}
	
	public void startSound(boolean shouldLoop) {
		if (shouldLoop) {
	    	clip.loop(Clip.LOOP_CONTINUOUSLY);
	    }
	    
		clip.setFramePosition(0);
	    clip.start();
	}
	
	public boolean isRunning() {
		return clip.isActive();
	}
}
