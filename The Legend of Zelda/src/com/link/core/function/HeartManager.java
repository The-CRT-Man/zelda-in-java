package com.link.core.function;

import java.util.LinkedList;

import com.link.core.Game;
import com.link.entity.Heart;
import com.link.load.SpriteSheet;


public class HeartManager {
	public LinkedList<Heart> tick(boolean startup) {
		LinkedList<Heart> hearts = new LinkedList<Heart>();
		
		if (!startup) {
			for (int i = 0; i < 16; i++) {
				if (i < 8) {
					hearts.add(new Heart(1030 + (i * 32), 8 + 32, SpriteSheet.grabImage(Game.getController().player.heartValues[i], 0, 32, 32, 0, 0, Game.heartSheet)));
				}
				else {
					hearts.add(new Heart(1030 + (i * 32) - 256, 8 + 0, SpriteSheet.grabImage(Game.getController().player.heartValues[i], 0, 32, 32, 0, 0, Game.heartSheet)));
				}
			}
		}
		else {
			for (int i = 0; i < 16; i++) {
				if (i < 8) {
					hearts.add(new Heart(1030 + (i * 32), 8 + 32, SpriteSheet.grabImage(0, 0, 32, 32, 0, 0, Game.heartSheet)));
				}
				else {
					hearts.add(new Heart(1030 + (i * 32) - 256, 8 + 0, SpriteSheet.grabImage(0, 0, 32, 32, 0, 0, Game.heartSheet)));
				}
			}
		}
		return hearts;
	}
}
