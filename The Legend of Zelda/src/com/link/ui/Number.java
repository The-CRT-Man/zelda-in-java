package com.link.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.link.load.SpriteSheet;

public class Number {
	public int number;
	
	public int x;
	public int y;
	
	public BufferedImage image;
	private BufferedImage[] finalImages;
	
	private int length;
	
	private int[] digits;
	
	public Number(int x, int y, BufferedImage image) {
		digits = new int[3];
		
		number = 0;
		
		this.x = x;
		this.y = y;
		
		this.image = image;
		
		finalImages = new BufferedImage[3];
	}
	
	public void render(Graphics g) {
		g.drawImage(finalImages[0], x, y, null);
		if (length >= 2) g.drawImage(finalImages[1], x + 32, y, null);
		if (length >= 3) g.drawImage(finalImages[2], x + 64, y, null);
	}

	public void tick() {
		String numberWord;
		char[] numberChars;
		
		numberWord = Integer.toString(number);
		
		numberChars = numberWord.toCharArray();
		
		length = numberChars.length;
		
		for (int i = 0; i < numberChars.length; i++) {
			String character = Character.toString(numberChars[i]);
			
			digits[i] = Integer.valueOf(character);
		}
		
		finalImages[0] = SpriteSheet.grabImage(digits[0], 0, 32, 32, 0, 0, image);
		finalImages[1] = SpriteSheet.grabImage(digits[1], 0, 32, 32, 0, 0, image);
		finalImages[2] = SpriteSheet.grabImage(digits[2], 0, 32, 32, 0, 0, image);
	}

}
