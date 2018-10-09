package com.link.tile;

import java.awt.image.BufferedImage;

public class ScrollTile extends Tile {
	public ScrollTile(double x, double y, BufferedImage image, int layer, boolean renderOffScreen) {
		this.x = x;
		this.y = y;
		
		this.image = image;
		
		this.layer = layer;
		
		this.renderOffScreen = renderOffScreen;
	}
}
