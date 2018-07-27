package com.link.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.link.core.Game;
import com.link.core.function.Collision;
import com.link.load.SpriteSheet;

public class DroppedItem {
	/*
	 * TYPES OF ITEM
	 * 
	 * 0 = singleRupee
	 * 1 = fiveRupees
	 * 2 = heart
	 * 3 = bomb
	 * 4 = key
	 * 5 = map
	 * 6 = compass
	 * 7 = stopWatch
	 * 8 = heartContainer
	 */
	public int type;
	
	public int x;
	public int y;
	
	private int position;
	
	private int tickCount;
	private int time = 0;
	
	public BufferedImage spriteSheet;
	private BufferedImage image;
	
	private boolean animated;
	private boolean despawn;
	
	public boolean pickedUp = false;
	
	private int animationState;
	
	public DroppedItem(int x, int y, BufferedImage image, int type, int position, boolean despawn) {
		this.x = x;
		this.y = y;
		
		this.despawn = despawn;
		
		this.position = position;
		
		spriteSheet = image;
		
		this.type = type;
		
		animation();
	}
	
	public void tick() {
		tickCount = Game.getController().tickCount;
		
		if (animated) {
			if (animationState == 0) {
				image = SpriteSheet.grabImage(type, 0, 64, 64, 0, 0, spriteSheet);
			}
			else {
				image = SpriteSheet.grabImage(type, 1, 64, 64, 0, 0, spriteSheet);
			}
			
			if (tickCount % 16 == 0 && animationState == 0) { 
				animationState = 1;
			}
			else if (tickCount % 16 == 0 && animationState == 1) {
				animationState = 0;
			}
		}
		else {
			image = SpriteSheet.grabImage(type, 0, 64, 64, 0, 0, spriteSheet);
		}
						
		collision();
		
		time++;
		
		if (pickedUp) {
			Game.getController().droppedItemManager.items[position] = null;
		}
		
		if (time >= 640 && despawn) {
			Game.getController().droppedItemManager.items[position] = null;
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(image, x, y, null);
	}
	
	private void animation() {
		if (type == 0 || type == 2) {
			animated = true;
		}
		else {
			animated = false;
		}
	}
	
	private void collision() {
		int[] playerHitbox = {(int) Game.getController().player.x + 8, (int) Game.getController().player.y + 8, Game.getController().player.collisionWidth, Game.getController().player.collisionHeight};
		int[] itemHitbox = {x, y, 32, 32};
		
		boolean colliding = false;
		
		if (Collision.isColliding(playerHitbox, itemHitbox)) {
			colliding = true;
		}
		
		if (colliding) {
			if (type == 0) Game.getController().player.rupees++;
			if (type == 1) Game.getController().player.rupees += 5;
			if (type == 2) Game.getController().player.health += 2;
			if (type == 3) Game.getController().player.bombs++;
			if (type == 4) Game.getController().player.keys++;
			//
			//
			//
			if (type == 8) {
				Game.getController().player.maxHealth += 2;
				Game.getController().player.health += 2;
			}
		}
		
		pickedUp = colliding;
	}
}
