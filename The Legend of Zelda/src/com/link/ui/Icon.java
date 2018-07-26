package com.link.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.link.load.SpriteSheet;

public class Icon {
	public int x;
	public int y;
	
	public int type;
	
	private BufferedImage image;
	private BufferedImage[] finalImages;
	
	public Icon(int x, int y, int type, BufferedImage image) {
		this.x = x;
		this.y = y;
		
		this.type = type;
		this.image = image;
		
		finalImages = new BufferedImage[3];
	}
	
	public void render(Graphics g) {
		g.drawImage(finalImages[0], x, y, null);
		g.drawImage(finalImages[1], x + 32, y, null);
	}
	
	public void tick() {
		finalImages[0] = SpriteSheet.grabImage(type, 0, 32, 32, 0, 0, image);
		finalImages[1] = SpriteSheet.grabImage(3, 0, 32, 32, 0, 0, image);
	}
}
