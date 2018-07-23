package com.link.tile;

import java.awt.image.BufferedImage;

public class LevelTile extends Tile {

	public static final int BACKGROUND = 0;
	public static final int FOREGROUND = 1;
	
	public LevelTile (double x, double y, BufferedImage image, int collisionValue, int layer) {
		this.x = x;
		this.y = y;
		
		this.collisionValue = collisionValue;
		
		this.layer = layer;
		
		this.image = image;
	}
}

/*
 * Collision Value
 * 0 = Non-Solid
 * 1 = Solid
 * 2 = Water
 * 3 = No-Ladder Water
 * 4 = Ladder
 * 5 = ???
 * 
 * 
 * Layer
 * 
 * 0 = Background
 * 1 = Foreground
 */