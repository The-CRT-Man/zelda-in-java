package com.link.entity;

import java.awt.Graphics;

import com.link.core.Game;
import com.link.load.SpriteSheet;

public class Sword {
	public boolean isAttacking;
	
	private double x;
	private double y;
	
	private int spriteX;
	
	public Sword() {
		
	}
	
	public void tick() {
		isAttacking = Game.getController().player.isAttacking();
		
		if (Game.getController().player.getDirection() == 0 && Game.getController().player.isAttacking()) {
			this.x = (int)Game.getController().player.x + 48;
			this.y = (int)Game.getController().player.y;
			spriteX = 0;
		}
		else if (Game.getController().player.getDirection() == 1 && Game.getController().player.isAttacking()) {
			this.x = (int)Game.getController().player.x - 48;
			this.y = (int)Game.getController().player.y;
			spriteX = 1;
		}
		else if (Game.getController().player.getDirection() == 2 && Game.getController().player.isAttacking()) {
			this.x = (int)Game.getController().player.x + 8;
			this.y = (int)Game.getController().player.y + 44;
			spriteX = 3;
		}
		else if (Game.getController().player.getDirection() == 3 && Game.getController().player.isAttacking()) {
			this.x = (int)Game.getController().player.x - 8;
			this.y = (int)Game.getController().player.y - 32;
			spriteX = 2;
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(SpriteSheet.grabImage(spriteX, 0, 64, 64, 8, 8, Game.swordSheet), (int)x, (int)y, null);
	}
}
