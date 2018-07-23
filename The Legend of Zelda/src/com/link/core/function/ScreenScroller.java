package com.link.core.function;

import com.link.core.Controller;
import com.link.core.Game;

public class ScreenScroller {
	private int count = 0;
	
	public void tick() {
		if (Game.getController().scrollScreen == true) {
			for (int i = 0; i < Game.getController().tiles.size(); i++) {
				if (Game.getController().screenScrollDirection == 0) {
					Game.getController().tiles.get(i).x -= 10;
				}
				else if (Game.getController().screenScrollDirection == 1) {
					Game.getController().tiles.get(i).x += 10;
				}
				else if (Game.getController().screenScrollDirection == 2) {
					Game.getController().tiles.get(i).y -= 10;
				}
				else if (Game.getController().screenScrollDirection == 3) {
					Game.getController().tiles.get(i).y += 10;
				}
			}
			
			for (int i = 0; i < Game.getController().scrollTiles.size(); i++) {
				if (Game.getController().screenScrollDirection == 0) {
					Game.getController().scrollTiles.get(i).x -= 10;
				}
				else if (Game.getController().screenScrollDirection == 1) {
					Game.getController().scrollTiles.get(i).x += 10;
				}
				else if (Game.getController().screenScrollDirection == 2) {
					Game.getController().scrollTiles.get(i).y -= 10;
				}
				else if (Game.getController().screenScrollDirection == 3) {
					Game.getController().scrollTiles.get(i).y += 10;
				}
			}
			
			Game.getController();
			if (Game.getController().screenScrollDirection == 0 && count < ((Controller.SCREEN_WIDTH - 1) * 64) / 10) Game.getController().player.x -= 10;
			else if (Game.getController().screenScrollDirection == 1 && count < ((Controller.SCREEN_WIDTH - 1) * 64) / 10) Game.getController().player.x += 10;
			else if (Game.getController().screenScrollDirection == 2 && count < ((Controller.SCREEN_HEIGHT - 1) * 64) / 10) Game.getController().player.y -= 10;
			else if (Game.getController().screenScrollDirection == 3 && count < ((Controller.SCREEN_HEIGHT - 1) * 64) / 10) Game.getController().player.y += 10;
			
			count++;
			
			if ((Game.getController().screenScrollDirection == 0 || Game.getController().screenScrollDirection == 1) && count > (Controller.SCREEN_WIDTH * 64) / 10) {
				count = 0;
				Game.getController().scrollScreen = false;
				Game.getController().scrollScreen(Game.getController().screenScrollDirection, true);
			}
			else if ((Game.getController().screenScrollDirection == 2 || Game.getController().screenScrollDirection == 3) && count > (Controller.SCREEN_HEIGHT * 64) / 10) {
				count = 0;
				Game.getController().scrollScreen = false;
				Game.getController().scrollScreen(Game.getController().screenScrollDirection, true);
			}
		}
	}
}
