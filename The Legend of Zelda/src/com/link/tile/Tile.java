package com.link.tile;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.link.core.Game;
import com.link.interfaces.GameTiles;

public class Tile implements GameTiles{
	public double x;
	public double y;
	
	public int collisionValue;
	
	public int scrollDirection;
	
	public boolean renderOffScreen;
	
	public int layer;
	
	public BufferedImage image;
	
	public void render(Graphics g) {
		if ((x >= -64 && x < Game.WIDTH && y >= -64 && y < Game.HEIGHT) || renderOffScreen) g.drawImage(image, (int)x, (int)y, null);
	}
	
	public void tick() {
		
	}

	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
}
