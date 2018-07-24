package com.link.core.function;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.link.core.Controller;
import com.link.core.Game;

public class KeyInput extends KeyAdapter {	
	public static boolean keyUp = false;
	public static boolean keyDown = false;
	public static boolean keyLeft = false;
	public static boolean keyRight = false;
	
	public static boolean keyAttack = false;
	
	public KeyInput(Controller c) {
		
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
			keyUp = true;		
		}
		else if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
			keyDown = true;
		}		
		else if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
			keyRight = true;
		}
		else if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
			keyLeft = true;
		}
		
		if (key == KeyEvent.VK_PERIOD || key == KeyEvent.VK_SPACE) {
			keyAttack = true;
		}
		
		if (key == KeyEvent.VK_M) {
			if (SoundEffect.musicEnabled) { 
				SoundEffect.musicEnabled = false;
				Game.getController().stopSounds();
			}
			else {
				SoundEffect.musicEnabled = true;
				Game.getController().updateSounds();
			}
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
			keyUp = false;
		}
		else if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
			keyDown = false;
		}
		else if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
			keyRight = false;
		}
		else if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
			keyLeft = false;
		}
		
		if (key == KeyEvent.VK_PERIOD || key == KeyEvent.VK_SPACE) {
			keyAttack = false;
		}
	}
}
//