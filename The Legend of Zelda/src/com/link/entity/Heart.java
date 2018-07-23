package com.link.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.link.interfaces.GameObjects;

public class Heart implements GameObjects {
	public int state;
	
	private int x;
	private int y;
	
	BufferedImage image;
	
	public Heart(int x, int y, BufferedImage image) {
		this.x = x;
		this.y = y;
		
		this.image = image;
	}

	public void render(Graphics g) {
		g.drawImage(image, (int)this.x, (int)this.y, null);
	}

	public void tick() {
		
	}
}
