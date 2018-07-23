package com.link.load;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioLoader {
	private AudioInputStream stream;
	
	public AudioInputStream loadAudio(String path) throws IOException, UnsupportedAudioFileException {
		stream = AudioSystem.getAudioInputStream(getClass().getResource(path));
		return stream;
	}
}
