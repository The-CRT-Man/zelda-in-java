package com.link.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import com.link.core.Game;
import com.link.core.function.HeartManager;

public class UserInterface {
	public HeartManager heartManager;
	public LinkedList<Heart> hearts = new LinkedList<Heart>();
	
	public Icon rupeeIcon;
	public Icon bombIcon;
	public Icon keyIcon;
	
	public Number rupeeNum;
	public Number keyNum;
	public Number bombNum;
	
	public BufferedImage iconSheet;
	public BufferedImage numberSheet;
	
	private StringUI message;
	
	public int uiX = 1028;
	public int uiY = 80;
	
	public UserInterface() {
		heartManager = new HeartManager();
		hearts = heartManager.tick(true);
		
		message = new StringUI("", 3 * 64, 5 * 32);
		
		iconSheet = Game.iconSheet;
		numberSheet = Game.numbers;
		
		rupeeIcon = new Icon(uiX, uiY, 0, iconSheet);
		keyIcon = new Icon(uiX, uiY + 64, 1, iconSheet);
		bombIcon = new Icon(uiX, uiY + 64 + 32, 2, iconSheet);
		
		rupeeNum = new Number(uiX + 64, uiY, numberSheet);
		keyNum = new Number(uiX + 64, uiY + 64, numberSheet);
		bombNum = new Number(uiX + 64, uiY + 128 -32, numberSheet);
		
		rupeeNum.number = 999;
		keyNum.number = 999;
		bombNum.number = 999;
	}
	
	public void tick() {		
		hearts = heartManager.tick(false);
		
		rupeeNum.tick();
		keyNum.tick();
		bombNum.tick();
		
		rupeeIcon.tick();
		keyIcon.tick();
		bombIcon.tick();
		
		message.tick();
		message.setMessage(Game.getController().caveText);
	}
	
	public void render(Graphics g) throws NullPointerException {	
		for (int i = 0; i < Game.getController().player.maxHealth / 2; i++) {
			hearts.get(i).render(g);
		}
		
		rupeeIcon.render(g);
		keyIcon.render(g);
		bombIcon.render(g);
		
		rupeeNum.render(g);
		bombNum.render(g);
		keyNum.render(g);
		
		if (message.display) message.render(g);
	}
	
	public void displayMessage(boolean display) {
		message.display = display;
		
		message.tickCount = 0;
		message.secondCount = 0;
	}
}
