package com.link.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.link.load.SpriteSheet;

public class Icon {
	public int x;
	public int y;
	
	public int type;
	
	private BufferedImage image;
	
	public Icon(int x, int y, int type, BufferedImage image) {
		this.x = x;
		this.y = y;
		
		this.type = type;
		this.image = image;
	}
	
	public void render(Graphics g) {
		g.drawImage(SpriteSheet.grabImage(type, 0, 32, 32, 0, 0, image), x, y, null);
		g.drawImage(SpriteSheet.grabImage(3, 0, 32, 32, 0, 0, image), x + 32, y, null);
	}
}
