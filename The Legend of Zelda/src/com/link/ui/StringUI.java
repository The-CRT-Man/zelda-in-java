package com.link.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import com.link.core.Game;
import com.link.load.SpriteSheet;

public class StringUI {
	public String text;
	
	public int x;
	public int y;
	
	public BufferedImage image;
	
	public boolean display;
	
	public int tickCount;
	public int secondCount;
	
	LinkedList<BufferedImage> finalImages;
	LinkedList<Integer> characterX;
	LinkedList<Integer> characterY;
	
	public StringUI(String text, int x, int y) {
		this.text = text;
		
		this.x = x;
		this.y = y;
		
		this.image = Game.alphabet;
	}
	
	public void tick(){
		char[] textChars;
		LinkedList<Integer> valueOfChars = new LinkedList<>();
		
		tickCount++;
		
		if (tickCount > 5) {
			secondCount++;
			tickCount = 0;
		}
		
		finalImages = new LinkedList<>();
		characterX = new LinkedList<>();
		characterY = new LinkedList<>();
		
		textChars = text.toCharArray();
		
		int lineCount = 0;
		int columnCount = -1;
		
		int loopNumber = 0;
		
		if (textChars.length < secondCount) loopNumber = textChars.length;
		if (secondCount <= textChars.length) loopNumber = secondCount;
		
		for (int i = 0; i < loopNumber; i++) {	
			String space = " ";
			String apostrophe = "'";
			String dot = ".";
			String exclamation = "!";
			String hash = "#";
			
			if (textChars[i] == space.charAt(0)) {
				columnCount++;
				valueOfChars.add(43);
			}
			else if (textChars[i] == apostrophe.charAt(0)) {
				columnCount++;
				valueOfChars.add(37);
			}
			else if (textChars[i] == dot.charAt(0)) {
				columnCount++;
				valueOfChars.add(39);
			}
			else if (textChars[i] == exclamation.charAt(0)) {
				columnCount++;
				valueOfChars.add(42);
			}
			else if (textChars[i] == hash.charAt(0)) {
				lineCount++;
				columnCount = 0;
				valueOfChars.add(null);
			}
			else if (Character.isAlphabetic(textChars[i])) {
				int temp = (int) textChars[i];
				int temp_int = 96;
					
				if (temp <= 122 && temp >= 97) {
					valueOfChars.add(temp - temp_int + 9);
				}
				
				columnCount++;
			}
			else {
				valueOfChars.add(Integer.valueOf(String.valueOf(textChars[i])));			
				columnCount++;
			}
			
			characterX.add(x + columnCount * 32);
			characterY.add(y + lineCount * 32);
			
			if (valueOfChars.get(i) != null) finalImages.add(SpriteSheet.grabImage(valueOfChars.get(i), 0, 32, 32, 0, 0, image));
		}
	}
	
	public void setMessage(String text) {
		this.text = text;
	}
	
	public void render(Graphics g) {
		try {
			for (int i = 0; i < finalImages.size(); i++) {
				g.drawImage(finalImages.get(i), characterX.get(i), characterY.get(i), null);
			}
		}
		catch (Exception e) {
			
		}
	}
}
