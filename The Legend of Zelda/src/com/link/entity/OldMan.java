package com.link.entity;

import java.awt.Graphics;

import com.link.core.Game;

public class OldMan extends Entity {
	public int x;
	public int y;
	
	public boolean visible = false;
	
	public OldMan(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void tick() {
		
	}
	
	@Override
	public void render(Graphics g) {
		if (visible) g.drawImage(Game.testTileImg, x, y, null);
	}
	
	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
