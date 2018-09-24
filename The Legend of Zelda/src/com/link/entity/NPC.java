package com.link.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.link.core.Game;
import com.link.load.SpriteSheet;

public class NPC extends Entity {
	public int x;
	public int y;
	
	public BufferedImage image;
	
	public boolean visible = false;
	
	public String type = "";
	
	public NPC(int x, int y, String type) {
		this.x = x;
		this.y = y;
		
		this.type = type;
		
		sheetImage = Game.npcSheet;
		
		switch (type) {
		case ("oldMan"):
			image = SpriteSheet.grabImage(0, 0, 64, 64, 0, 0, sheetImage);
			break;
		case ("oldWoman"):
			image = SpriteSheet.grabImage(1, 1, 64, 64, 0, 0, sheetImage);
			break;
		case ("shopkeeper"):
			image = SpriteSheet.grabImage(1, 0, 64, 64, 0, 0, sheetImage);
		}
	}
	
	@Override
	public void tick() {
		
	}
	
	@Override
	public void render(Graphics g) {
		if (visible) g.drawImage(image, x, y, null);
	}
	
	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
