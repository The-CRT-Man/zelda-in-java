package com.link.core.function;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import com.link.core.Game;
import com.link.ui.Heart;
import com.link.ui.Icon;

public class UserInterface {
	public HeartManager heartManager;
	public LinkedList<Heart> hearts = new LinkedList<Heart>();
	
	public Icon rupees;
	public Icon bombs;
	public Icon keys;
	
	public BufferedImage iconSheet;
	
	public int uiX = 1028;
	public int uiY = 80;
	
	public UserInterface() {
		heartManager = new HeartManager();
		hearts = heartManager.tick(true);
		
		iconSheet = Game.iconSheet;
		
		rupees = new Icon(uiX, uiY, 0, iconSheet);
		keys = new Icon(uiX, uiY + 64, 1, iconSheet);
		bombs = new Icon(uiX, uiY + 64 + 32, 2, iconSheet);
	}
	
	public void tick() {		
		hearts.clear();		
		hearts = heartManager.tick(false);
	}
	
	public void render(Graphics g) {	
		for (int i = 0; i < Game.getController().player.maxHealth / 2; i++) {
			hearts.get(i).render(g);
		}
		
		rupees.render(g);
		keys.render(g);
		bombs.render(g);
	}
}
