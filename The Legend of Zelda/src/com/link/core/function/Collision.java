package com.link.core.function;

import java.awt.Rectangle;

public class Collision {
	public static boolean isColliding(int[] ob1Hitbox, int[] ob2Hitbox) {
		Rectangle ob1Rect = toRectangle(ob1Hitbox);
		Rectangle ob2Rect = toRectangle(ob2Hitbox);
		
		if (ob1Rect.intersects(ob2Rect)) {
			return true;
	    }
	    return false;
	}
	
	private static Rectangle toRectangle(int[] hitbox) {
		int x = hitbox[0];
		int y = hitbox[1];
		int width = hitbox[2];
		int height = hitbox[3];
		
		return new Rectangle(x, y, width, height);
	}
}
