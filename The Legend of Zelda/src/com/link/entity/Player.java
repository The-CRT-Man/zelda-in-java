package com.link.entity;

import java.awt.Graphics;
import java.util.Random;

import com.link.core.Game;
import com.link.core.function.Collision;
import com.link.core.function.KeyInput;
import com.link.interfaces.GameObjects;
import com.link.load.SpriteSheet;

public class Player implements GameObjects {
	public double x = 12 * 64;
	public double y = 7 * 64;
	
	private int xVel;
	private int yVel;
	
	private double oldMapX;
	private double oldMapY;
	
	private int tileX;
	private int tileY;
	
	private boolean slow;
	
	private int collisionWidth = 48;
	private int collisionHeight = 48;
	
	private int animationSpeed = 6;
	
	private int animationState;
	private int oldAnimationState;
	private int animationFrame;
	private boolean animatedState;
	
	private int powerUpState;
	private int shieldState = 0;
	
	public int maxHealth = 32;
	public int health = 5;
	public int[] heartValues = new int[32];
	
	private int spriteX = 0;
	private int spriteY = 0;
	
	private int attackCount = 0;
	private int attackCoolDown = 0;
	private boolean attackKeyReleased = true;
	
	private int direction;
	
	//private int level;
	
	private boolean attacking = false;
	
	private int tickCount = 0;
	
	Random random = new Random();
	
	public Player() {
		if (shieldState == 0) {
			oldAnimationState = 0;
		}
		else {
			oldAnimationState = 4;
		}
	}
	
	public void tick() {
		tickCount = Game.getController().tickCount;		
		
		tileX = (int)Math.floor((x / 4) / 16);
		tileY = (int)Math.floor((y / 4) / 16); 
		
		if (!Game.getController().animateCaveEntrance && !Game.getController().animateDungeonDoorExit) {
			keyMovement();
		}
		else if (Game.getController().animateCaveEntrance) {
			xVel = 0;
			yVel = 0;
		}
		else if (Game.getController().animateDungeonDoorExit) {
			dungeonDoorAnimation();
		}
		
		heartControl();

		attack();
		animation();
		
		float speed;
		
		if (slow) {
			speed = 2;
		}
		else {
			speed = 1;
		}
		
		if ((xVel != 0) && (yVel != 0)) {
			yVel = 0;
		}
		
		if (tickCount % speed == 0) {
			x += xVel;
			y += yVel;
		}
		
		if (tickCount % animationSpeed == 0 && (xVel != 0 || yVel != 0) && animatedState) {
			if (animationFrame == 0) {
				spriteX = animationState * 2;
				animationFrame = 1;
			}
			else {
				spriteX = (animationState * 2) + 1;
				animationFrame = 0;
			}
		}
		else if (tickCount % animationSpeed == 0 && animatedState) {
			spriteX = animationState * 2;
			animationFrame = 1;
		}
		else if (tickCount % animationSpeed == 0 && !animatedState) {
			spriteX = (animationState * 2) - (animationState - 7) - 1;
		}
		
		try {
			if (x < 0)  {
				//x = 960;
				Game.getController().scrollScreen(1, false);
				Game.getController().starting = true;
			}
			if (x > 960) {
				//x = 0;
				Game.getController().scrollScreen(0, false);
				Game.getController().starting = true;
			}
			
			if (y < 0) {
				//y = 640;
				Game.getController().scrollScreen(3, false);
				Game.getController().starting = true;
			}
			if (y > 640) {
				//y = 0;
				Game.getController().scrollScreen(2, false);
				Game.getController().starting = true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		collisionDetection();
	}
	
	public void render(Graphics g) {
		if (!Game.getController().scrollScreen) g.drawImage(SpriteSheet.grabImage(spriteX, spriteY, 64, 64, 8, 8, Game.linkImg), (int)Math.floor(this.x / 4) * 4, (int)Math.floor(this.y / 4) * 4, null);
		if (Game.getController().scrollScreen) g.drawImage(SpriteSheet.grabImage(spriteX, spriteY, 64, 64, 8, 8, Game.linkImg), (int)this.x, (int)this.y, null);
		
/*		String position = "(" + (int)(x / 4) + ", " + (int)(y / 4) + ")";
		String tilePosition = "(" + tileX + ", " + tileY + ")";
		
		g.drawString(position, (int)x, (int)y);
		g.drawString(tilePosition, (int)x, (int)y - 12); 
*/	}
	
	public void setVelocity(int xVelocity, int yVelocity) {
		xVel = xVelocity;
		yVel = yVelocity;
	}
	
	private void keyMovement() {	
		if (!attacking) {
			if (KeyInput.keyRight == true) {
				xVel = 4;
			}
			else if (KeyInput.keyLeft == true) {
				xVel = -4;
			}
			else {
				xVel = 0;
			}
			
			if (KeyInput.keyUp == true) {
				yVel = -4;
			}
			else if (KeyInput.keyDown == true) {
				yVel = 4;
			}
			else {
				yVel = 0;
			}
		}
		else if (attacking) {
			xVel = 0;
			yVel = 0;
		}
		
		if (KeyInput.keyAttack == true && !attacking && attackCoolDown == 0 && attackKeyReleased == true) {
			attacking = true;
			attackKeyReleased = false;
			Game.getController().swordSound.startSound(false);
		}
		else if (KeyInput.keyAttack == false) {
			attackKeyReleased = true;
		}
	}
	
	private void animation() {
		if (xVel > 0) direction = 0;
		if (xVel < 0) direction = 1;		
		if (yVel > 0) direction = 2;
		if (yVel < 0) direction = 3;
		
		if (!attacking) {
			if (shieldState == 0) {
				animationState = oldAnimationState;	
				
				if (powerUpState == 0 && yVel > 0) {
					animationState = 0;
					oldAnimationState = animationState;
				}
				
				if (powerUpState == 0 && yVel < 0) {
					animationState = 1;
					oldAnimationState = animationState;
				}
				
				if (powerUpState == 0 && xVel < 0) {
					animationState = 2;
					oldAnimationState = animationState;
				}
				
				if (powerUpState == 0 && xVel > 0) {
					animationState = 3;
					oldAnimationState = animationState;
				}
				
				if (powerUpState == 0 && xVel == 0 && yVel == 0) {
					animationState = oldAnimationState;
				}		
			}
			else if (shieldState == 1) {
				animationState = oldAnimationState;
				
				if (powerUpState == 0 && yVel < 0) {
					animationState = 1;
					oldAnimationState = animationState;
				}
				
				if (powerUpState == 0 && yVel > 0) {
					animationState = 4;
					oldAnimationState = animationState;
				}
				
				if (powerUpState == 0 && xVel > 0) {
					animationState = 6;
					oldAnimationState = animationState;
				}
				
				if (powerUpState == 0 && xVel < 0) {
					animationState = 5;
					oldAnimationState = animationState;
				}
				
				if (powerUpState == 0 && xVel == 0 && yVel == 0) {
					animationState = oldAnimationState;
				}
			}
			
			animatedState = true;	
		}
		else {
			if (powerUpState == 0 && direction == 3) {
				animationState = 9;
			}
			
			if (powerUpState == 0 && direction == 2) {
				animationState = 8;
			}
			
			if (powerUpState == 0 && direction == 0) {
				animationState = 10;
			}
			
			if (powerUpState == 0 && direction == 1) {
				animationState = 11;
			}
			animatedState = false;
		}
		
	}
	
	private void attack() {		
		if (tickCount % 2 == 0 && attacking) {
			attackCount++;
		}
		
		if (attackCount == 8) {
			attacking = false;
			attackCount = 0;
			attackCoolDown = 1;
		}
		
		if (attackCoolDown >= 1) attackCoolDown++;
		if (attackCoolDown == 15) attackCoolDown = 0;
	}
	
	private void collisionDetection() {
		/*
		 * Collision Value
		 * 0 = Non-Solid
		 * 1 = Solid
		 * 2 = Water
		 * 3 = No-Ladder Water
		 * 4 = Ladder
		 * 5 = Entrance with Animation 
		 * 6 = Vertical Slab Collision on Left
		 * 7 = Vertical Slab Collision on Right
		 */
		
		boolean colliding = false;
		int collisionValue = 0;
		boolean[] finalCollisionValue = new boolean[8];
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int checkX = tileX + j;
				int checkY = tileY + i;
					
				if (Game.getController().map == 0) {
					try {
						collisionValue = Game.getController().collisionMap[Game.getController().currentScreenX][Game.getController().currentScreenY][checkX][checkY];
					}
					catch (Exception e) {
						break;
					}
				}
				
				else if (Game.getController().map == 1) {
					try {
						collisionValue = Game.getController().caveCollisionMap[Game.getController().currentScreenX][Game.getController().currentScreenY][checkX][checkY];
					}
					catch (Exception e) {
						break;
					}
				}
				
				else if (Game.getController().map == 2) {
					try {
						collisionValue = Game.getController().dungeonCollisionMap[Game.getController().currentScreenX][Game.getController().currentScreenY][checkX][checkY];
					}
					catch (Exception e) {
						break;
					}
				}
				
				int[] playerHitbox = {(int) x + 8, (int) y + 8, collisionWidth, collisionHeight};
				
				int[] tileHitbox = {(checkX * 64), (checkY * 64), 64, 64};
				int[] leftVerticalHitbox = {(checkX * 64), (checkY * 64), 32, 64};
				int[] rightVerticalHitbox = {(checkX * 64) + 32, (checkY * 64), 32, 64};
				
				if (Collision.isColliding(playerHitbox, tileHitbox) && (collisionValue != 0) && collisionValue != 6 && collisionValue != 7) {
					colliding = true;
					finalCollisionValue[collisionValue] = true;
					
					if (collisionValue == 5) {
						Game.getController().caveEntranceTileLocation[0] = checkX;
						Game.getController().caveEntranceTileLocation[1] = checkY;
					}
				}
				
				if (Collision.isColliding(playerHitbox, leftVerticalHitbox) && collisionValue != 0 && (collisionValue == 6)) {
					colliding = true;
					finalCollisionValue[collisionValue] = true;
				}
				
				if (Collision.isColliding(playerHitbox, rightVerticalHitbox) && collisionValue != 0 && (collisionValue == 7)) {
					colliding = true;
					finalCollisionValue[collisionValue] = true;
				}
			}
		}
		
		if (colliding) {
			if (finalCollisionValue[1]== true || finalCollisionValue[2] || finalCollisionValue[6] || finalCollisionValue[7]) {
				if (xVel > 0) {
					this.x -= 4;
					//xVel = -4;
				}
				else if (xVel < 0) {
					this.x += 4;
					//xVel = 4;
				}
				else if (yVel > 0) {
					this.y -=4;
					//yVel = -4;
				}
				else if (yVel < 0) {
					this.y +=4;
					//yVel = 4;
				}
			}		
		}
		
		if (finalCollisionValue[4] == true) {
			slow = true;
		}
		else {
			slow = false;
		}
		
		if (finalCollisionValue[5] == true && !Game.getController().animateCaveEntrance && !Game.getController().animateCaveEntranceFinish && !Game.getController().animateCaveEntranceDone) {
			if (this.y == Game.getController().caveEntranceTileLocation[1] * 64) {
				if (Game.getController().map == 0) {
					oldMapX = this.x;
					oldMapY = this.y;
					Game.getController().changeMap(2, true);
				}
				else if (Game.getController().map == 1) {
					Game.getController().changeMap(0, true);
				}
			}
		}
		
		if (this.y - (Game.getController().caveEntranceTileLocation[1] * 64) > 64) {
			Game.getController().animateCaveEntranceDone = false;
		}
	}
	
	public void caveAnimation() {
		Game.getController().animateCaveEntranceTickCount++;
		
		if (Game.getController().map == 2) y++;
		if (Game.getController().map == 1) y++;
		if (Game.getController().map == 0) y--;
		
		if (Game.getController().animateCaveEntranceTickCount > 56) {
			Game.getController().animateCaveEntranceFinish = true;
			Game.getController().animateCaveEntrance = false;
			Game.getController().animateCaveEntranceTickCount = 0;
		}
	}
	
	public void dungeonDoorAnimation() {
		Game.getController().animateDungeonDoorExitTickCount++;
		
		if (direction == 0) xVel = 4;
		if (direction == 1) xVel = -4;
		if (direction == 2) yVel = 4;
		if (direction == 3) yVel = -4;
		
		if (Game.getController().animateDungeonDoorExitTickCount > 20) {
			xVel = 0;
			yVel = 0;
			
			Game.getController().animateDungeonDoorExit = false;
			Game.getController().animateDungeonDoorExitTickCount = 0;
		}
	}
	
	private void heartControl() {
		for (int i = 0; i < maxHealth / 2; i++) {
			int heartValue;
			
			if (i < Math.floor(health / 2)) {
				heartValue = 0;
			}
			else if (i == Math.floor(health / 2)) {
				heartValue = 1;
			}
			else {
				heartValue = 2;
			}
			
			heartValues[i] = heartValue;
		}
	}
	
	public int getVelocityX() {
		return xVel;
	}
	
	public int getVelocityY() {
		return yVel;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public boolean isAttacking() {
		return attacking;
	}
	
	public double[] getOldMapPos() {
		double[] pos = {oldMapX, oldMapY};
		return pos;
	}
}
