package com.link.core;

import java.awt.Graphics;
import java.util.LinkedList;

import com.link.core.function.DroppedItemManager;
import com.link.core.function.ScreenScroller;
import com.link.core.function.SoundEffect;
import com.link.core.function.TileMap;
import com.link.entity.Entity;
import com.link.entity.ItemShop;
import com.link.entity.NPC;
import com.link.entity.Player;
import com.link.entity.Sword;
import com.link.load.SpriteSheet;
import com.link.tile.LevelTile;
import com.link.tile.ScrollTile;
import com.link.tile.Tile;
import com.link.ui.UserInterface;

public class Controller {
	public LinkedList<Tile> tiles = new LinkedList<Tile>();
	public LinkedList<Tile> scrollTiles = new LinkedList<Tile>();
	
	public LinkedList<Entity> npcs = new LinkedList<Entity>();

	public static final int MAP_WIDTH = 16;
	public static final int MAP_HEIGHT = 8;
	
	public static final int SCREEN_WIDTH = 16;
	public static final int SCREEN_HEIGHT = 11;
	
	private TileMap generatedTileMap;
	
	public int tickCount;
	
	public int[][][][] tileMap = new int[MAP_WIDTH][MAP_HEIGHT][SCREEN_WIDTH][SCREEN_HEIGHT];
	public int[][][][] collisionMap = new int[MAP_WIDTH][MAP_HEIGHT][SCREEN_WIDTH][SCREEN_HEIGHT];
	
	public int[][][][] caveTileMap = new int[MAP_WIDTH][MAP_HEIGHT][SCREEN_WIDTH][SCREEN_HEIGHT];
	public int[][][][] caveCollisionMap = new int[MAP_WIDTH][MAP_HEIGHT][SCREEN_WIDTH][SCREEN_HEIGHT];
	
	public int[][][][] dungeonTileMap = new int[MAP_WIDTH][MAP_HEIGHT][SCREEN_WIDTH][SCREEN_HEIGHT];
	public int[][][][] dungeonDoors = new int[MAP_WIDTH][MAP_HEIGHT][SCREEN_WIDTH][SCREEN_HEIGHT];
	public int[][][][] dungeonCollisionMap = new int[MAP_WIDTH][MAP_HEIGHT][SCREEN_WIDTH][SCREEN_HEIGHT];
	
	public int[][] dungeonBorderMap = new int[MAP_WIDTH][MAP_HEIGHT];
	
	public String[] messages;
	
	public int currentScreenX = 0;
	public int currentScreenY = 0;
	
	private int previousScreenX;
	private int previousScreenY;
	
	public int screenScrollDirection;
	
	public boolean scrollScreen;
	public boolean starting;
	
	public boolean animateCaveEntrance = false;
	public boolean animateCaveEntranceFinish = false;
	
	public boolean animateCaveEntranceDone = false;
	
	public int animateCaveEntranceTickCount;
	
	public boolean animateDungeonDoorExit = false;
	public int animateDungeonDoorExitTickCount;
	
	public UserInterface ui;
	
	public DroppedItemManager droppedItemManager;
	
	public int[] caveEntranceTileLocation = {0, 0};
	
	public Player player;
	public Sword sword;
	
	public int map;
	
	public ScreenScroller screenScroller;
	
	private SoundEffect music;
	private SoundEffect dungeon;
	private SoundEffect stairsSound;
	public SoundEffect swordSound;
	public SoundEffect pickUpHeart;
	public SoundEffect pickUpRupee;
	public SoundEffect pickUpItem;
	public SoundEffect grandItemPickUp;
	
	private ItemShop shop;
	
	private Tile caveEntryForegroundTile;
	
	public Controller() {
		generatedTileMap = new TileMap();
		screenScroller = new ScreenScroller();
		
		generatedTileMap.generateTileData();
		
		this.tileMap = generatedTileMap.getTileData();		
		this.collisionMap = generatedTileMap.getCollisionData();
		
		this.caveTileMap = generatedTileMap.getCaveTileData();		
		this.caveCollisionMap = generatedTileMap.getCaveCollisionData();
		
		this.dungeonTileMap = generatedTileMap.getDungeonTileData();
		this.dungeonCollisionMap = generatedTileMap.getDungeonCollisionTileData();
		
		this.dungeonDoors = generatedTileMap.getDungeonForegroundData();
		
		messages = generatedTileMap.getMessgaes();
		
		
		for (int i = 0; i < Controller.SCREEN_HEIGHT; i++) {
			for (int j = 0; j < Controller.SCREEN_WIDTH; j++) {
				System.out.println(dungeonCollisionMap[0][0][j][i]);
			}
		}
		
		currentScreenX = 7;
		currentScreenY = 7;
		
		generateObjects();
		musicController();
	}
	
	private void generateObjects() {
		player = new Player();
		
		map = 0;
		
		generateTiles();
		
		
		ui = new UserInterface();
		droppedItemManager = new DroppedItemManager();
		sword = new Sword();
	}
	
	public void musicController() {
		music = new SoundEffect(Game.sound);		
		stairsSound = new SoundEffect(Game.stairSound);		
		swordSound = new SoundEffect(Game.swordSound);
		pickUpRupee = new SoundEffect(Game.pickUpRupee);
		pickUpHeart = new SoundEffect(Game.pickUpHeart);
		pickUpItem = new SoundEffect(Game.pickUpItem);
		dungeon = new SoundEffect(Game.dungeonMusic);
		grandItemPickUp = new SoundEffect(Game.grandPickUpSound);
				
		if (!music.isRunning()) music.startSound(true);
	}
	
	public void tick() {
		if (scrollScreen) {
			screenScroller.tick();
		}
		else {
			
			if (animateCaveEntrance || animateCaveEntranceFinish) {
				changeMap(map, true);
			}
			
			player.tick();
			sword.tick();
		}
		
		tickCount++;
		
		if (tickCount == 60) {
			tickCount = 0;
		}
		
		droppedItemManager.tick();
		ui.tick();
		if (shop != null) shop.tick();
	}
	
	public void render(Graphics g) {
		for (int i = 0; i < tiles.size(); i++) {
			try {
				if (tiles.get(i).layer == LevelTile.BACKGROUND) tiles.get(i).render(g);
			}
			catch (NullPointerException e) {
				System.err.println("Unable to completely render background tiles.");
			}
		}
		
		for (int i = 0; i < scrollTiles.size(); i++) {
			try {
				if (scrollTiles.get(i).layer == LevelTile.BACKGROUND) scrollTiles.get(i).render(g);
			}
			catch (NullPointerException e) {
				System.err.println("Unable to completely render background scroll tiles.");
			}
		}
		
		for (int i = 0; i < npcs.size(); i++) {
			try {
				npcs.get(i).render(g);
			}
			catch (NullPointerException e){
				System.err.println("Unabel to render all NPCs.");
			}
		}
			
		if (sword.isAttacking) {
			sword.render(g);
		}
		
		player.render(g);
		
		if (animateCaveEntrance || animateCaveEntranceFinish) {
			try {
				caveEntryForegroundTile.render(g);	
			}
			catch (NullPointerException e) {
				System.err.println("Unable to render cave entry tile");
			}
		}
		
		for (int i = 0; i < tiles.size(); i++) {
			try {
				if (tiles.get(i).layer == LevelTile.FOREGROUND) tiles.get(i).render(g);
			}
			catch (NullPointerException e) {
				System.err.println("Unable to completely render foreground tiles.");
			}
		}
		
		for (int i = 0; i < scrollTiles.size(); i++) {
			try {
				if (scrollTiles.get(i).layer == LevelTile.FOREGROUND) scrollTiles.get(i).render(g);
			}
			catch (NullPointerException e) {
				System.err.println("Unable to completely render foreground scroll tiles.");
			}
		}
			
		g.drawImage(Game.background, Controller.SCREEN_WIDTH * 64, 0, null);
		g.drawImage(Game.background, 0, Controller.SCREEN_HEIGHT * 64, null);
		
		g.drawString(String.valueOf(currentScreenX), 1200, 500);
		g.drawString(String.valueOf(currentScreenY), 1200, 550);
		
		try {
			ui.render(g);	
		}
		catch (NullPointerException e) {
			System.err.println("Unable to completely render UI.");
		}
		
		droppedItemManager.render(g);
		
		g.drawString("Press \"M\" to toggle sound.", 1030, 700);	
		if (SoundEffect.musicEnabled) g.drawString("PLAYING", 1030, 712);
		if (!SoundEffect.musicEnabled) g.drawString("MUTED", 1030, 712);
		
		if (shop != null) shop.render(g);
	}
	
	public void changeMap(int map, boolean animate) {
		/*
		 * 0 = over world
		 * 1 = cave
		 * 2 = dungeon
		 * 3 = dungeon passage
		 */
		
		this.map = map;
		
		if (this.map == 0) {
			if (animate) {
				if (!animateCaveEntranceFinish && !animateCaveEntrance) {
					stairsSound.startSound(false);
					
					dungeon.stopSound();
					
					animateCaveEntrance = true;
					player.y = (caveEntranceTileLocation[1] + 1) * 64;
					player.x = caveEntranceTileLocation[0] * 64;
					player.caveAnimation();
					
					player.oldAnimationState = 0;
	
					clearScreen(map);				
					generateTiles();
				}
				else if (animateCaveEntrance && !animateCaveEntranceFinish) {
					player.caveAnimation();
				}
				else if (animateCaveEntranceFinish && !animateCaveEntrance) {
					player.y = caveEntranceTileLocation[1] * 64;
					player.x = caveEntranceTileLocation[0] * 64;
														
					animateCaveEntranceFinish = false;
					animateCaveEntrance = false;
					animateCaveEntranceDone = true;
					
					music.startSound(true);

					dungeon.stopSound();
				}
			}
			else {
				map = 0;
				
				music.startSound(true);
				
				player.y = (caveEntranceTileLocation[1] + 1) * 64;
				player.x = caveEntranceTileLocation[0] * 64;
				
				generateTiles();
				clearScreen(map);
				
				dungeon.stopSound();			
			}
		}
		
		else if (this.map == 1) {
			if (animate) {
				if (!animateCaveEntranceFinish && !animateCaveEntrance) {					
					animateCaveEntrance = true;
					player.caveAnimation();
					
					music.stopSound();
					stairsSound.startSound(false);
				}
				else if (animateCaveEntrance && !animateCaveEntranceFinish) {
					player.caveAnimation();
					
					ui.displayMessage(true);
				}
				else if (animateCaveEntranceFinish && !animateCaveEntrance) {
					generateTiles();
					clearScreen(map);
					
					for (Entity man : npcs) {
						man.setVisible(true);
					}
					
					player.x = (double)480;
					player.y = (double)576;
					
					animateCaveEntranceFinish = false;
					animateCaveEntrance = false;
				}
			}
			else {
				generateTiles();
				
				player.x = (double)480;
				player.y = (double)576;
			}
			
			if (animateCaveEntrance || animateCaveEntranceFinish) {
				caveEntryForegroundTile = new LevelTile(caveEntranceTileLocation[0] * 64, (caveEntranceTileLocation[1] + 1) * 64, SpriteSheet.grabImage(2, 0, 64, 64, 4, 4, Game.tileSet), 0, 1, false);			
			}
		}
		else if (this.map == 2) {
			if (animate) {
				if (!animateCaveEntranceFinish && !animateCaveEntrance) {					
					animateCaveEntrance = true;
					player.caveAnimation();
					
					music.stopSound();
					
					stairsSound.startSound(false);
				}
				else if (animateCaveEntrance && !animateCaveEntranceFinish) {
					player.caveAnimation();
				}
				else if (animateCaveEntranceFinish && !animateCaveEntrance) {
					int[] dungeonEntrancePosition = new int[3];
					
					dungeonEntrancePosition = dungeonEntrance();
					
					currentScreenX = dungeonEntrancePosition[3];
					currentScreenY = dungeonEntrancePosition[4];
					
					generateTiles();
					clearScreen(map);
					
					player.x = (double)dungeonEntrancePosition[1];
					player.y = (double)dungeonEntrancePosition[2];
					
					player.setDirection(dungeonEntrancePosition[0]);
					animateDungeonDoorExit = true;
					
					animateCaveEntranceFinish = false;
					animateCaveEntrance = false;
					
					dungeon.startSound(true);
				}
			}
			else {
				generateTiles();
				
				player.x = (double)480;
				player.y = (double)576;

			}
			
			if (animateCaveEntrance || animateCaveEntranceFinish) {
				caveEntryForegroundTile = new LevelTile(caveEntranceTileLocation[0] * 64, (caveEntranceTileLocation[1] + 1) * 64, SpriteSheet.grabImage(2, 0, 64, 64, 4, 4, Game.tileSet), 0, 1, false);			
			}
		}
	}
	
	public void scrollScreen(int direction, boolean complete) {
		boolean failed = false;
		screenScrollDirection = direction;
		
		int yDirection = 0;
		int xDirection = 0;
		
		clearScreen(map);
			
		if (scrollScreen || !complete) {
			previousScreenX = currentScreenX;
			previousScreenY = currentScreenY;
			
			if (direction == 0) {		
				currentScreenX++;
			//	player.x = 0;
			}
			else if (direction == 1) {
				currentScreenX--;
			//	player.x = 960;
			}
			else if (direction == 2) {
				currentScreenY++;
			//	player.y = 0;
			}
			else if (direction == 3) {
				currentScreenY--;
			//	player.y = 640;
			}
		}
		
		scrollTiles.clear();
		
		if (map == 1) {
			currentScreenY--;
			changeMap(0, true);
			return;
		}
		
		boolean leaveDungeonProperties = false;
		
		if (map == 2) leaveDungeonProperties = dungeonExit(currentScreenX, currentScreenY, previousScreenX, previousScreenY);
		
		if (leaveDungeonProperties && (scrollScreen || !complete) && map == 2) {
			changeMap(0, true);
			return;
		}
		
		switch (direction) {
		case 0:
			xDirection = 1;
			yDirection = 0;
			break;
		case 1:
			xDirection = -1;
			yDirection = 0;
			break;
		case 2:
			xDirection = 0;
			yDirection = 1;
			break;
		case 3:
			xDirection = 0;
			yDirection = -1;
			break;
		}
		
		for (int i = 0; i < SCREEN_WIDTH; i++) {
			for (int j = 0; j < SCREEN_HEIGHT; j++) {
				int v = 22;
				int u = 0;
				
				if (map == 0) {
					try {
						v = tileMap[previousScreenX + xDirection][previousScreenY + yDirection][i][j];
					}
					catch (Exception e) {					
						failed = true;
					}
				}
				else if (map == 1) {
					try {
						v = caveTileMap[previousScreenX + xDirection][previousScreenY + yDirection][i][j];
					}
					catch (Exception e) {					
						failed = true;
					}
				}
				else if (map == 2) {
					try {
						v = dungeonTileMap[previousScreenX + xDirection][previousScreenY + yDirection][i][j];
						u = dungeonDoors[previousScreenX + xDirection][previousScreenY + yDirection][i][j];
					}
					catch (Exception e) {					
						failed = true;
						
						e.printStackTrace();
					}
				}

				if (map == 0 || map == 1) scrollTiles.add(new ScrollTile((SCREEN_WIDTH * xDirection + i) * 64, (SCREEN_HEIGHT * yDirection + j) * 64, SpriteSheet.grabImage(v % 18, (int)Math.ceil(v / 18), 64, 64, 4, 4, Game.tileSet), 0, false));
				if (map == 2) scrollTiles.add(new ScrollTile((SCREEN_WIDTH * xDirection + i) * 64, (SCREEN_HEIGHT * yDirection + j) * 64, SpriteSheet.grabImage(v % 32, (int)Math.ceil(v / 32), 64, 64, 12, 12, Game.dungeonTileSet), 0, false));				
				
				if (u != 36 && map == 2) {
					if (u == 0 || u == 1) scrollTiles.add(new ScrollTile((SCREEN_WIDTH * xDirection + i) * 64, (SCREEN_HEIGHT * yDirection + j) * 64, SpriteSheet.grabImage(u % 72, (int)Math.ceil(u / 72), 128, 128, 0, 0, Game.dungeonDoors), 1, false));
					if (u == 2 || u == 3) scrollTiles.add(new ScrollTile((SCREEN_WIDTH * xDirection + i) * 64, (SCREEN_HEIGHT * yDirection + j) * 64 + 32, SpriteSheet.grabImage(u % 72, (int)Math.ceil(u / 72), 128, 128, 0, 0, Game.dungeonDoors), 1, false));
				}
			}
		}
		
		if (map == 2) scrollTiles.add(new ScrollTile((SCREEN_WIDTH * xDirection) * 64, (SCREEN_HEIGHT * yDirection ) * 64, SpriteSheet.grabImage(0, 0, 1024, 704, 0, 0, Game.dungeonBorderSet), 0, true));

		starting = false;
		
		if (scrollScreen || !complete) {
			scrollScreen = true;
			return;
		}
				
		tiles.clear();
		scrollTiles.clear();
			
		if (direction == 0) {		
			//currentScreenX++;
			player.x = 0;
		}
		else if (direction == 1) {
			//currentScreenX--;
			player.x = 960;
		}
		else if (direction == 2) {
			//currentScreenY++;
			player.y = 0;
		}
		else if (direction == 3) {
			//currentScreenY--;
			player.y = 640;
		}
		
		if (map == 2) {
			animateDungeonDoorExit = true;
		}
		
		failed = generateTiles();
		
		
		if (failed) {
			if (currentScreenX < 0) {
				currentScreenX++;
				player.x = 0;
			}
			
			if (currentScreenX > MAP_WIDTH - 1) {
				currentScreenX--;
				player.x = 960;
			}
			
			if (currentScreenY < 0) {
				currentScreenY++;
				player.y = 0;
			}
			
			if (currentScreenY > MAP_HEIGHT - 1) {
				currentScreenY--;
				player.y = 640;
			}
			
			generateTiles();
		}
	}
	
	private boolean generateTiles() {
		boolean failed = false;
		
		tiles.clear();
		
		for (int i = 0; i < SCREEN_WIDTH; i++) {
			for (int j = 0; j < SCREEN_HEIGHT; j++) {
				int v = 0;
				int w = 0;
				
				int u = 0;
				
				try {
					if (map == 0) {
						v = tileMap[currentScreenX][currentScreenY][i][j];
						w = collisionMap[currentScreenX][currentScreenY][i][j];
					}
					else if (map == 1) {
						v = caveTileMap[currentScreenX][currentScreenY][i][j];
						w = caveCollisionMap[currentScreenX][currentScreenY][i][j];
					}
					else if (map == 2) {
						v = dungeonTileMap[currentScreenX][currentScreenY][i][j];
						w = dungeonCollisionMap[currentScreenX][currentScreenY][i][j];
						
						u = dungeonDoors[currentScreenX][currentScreenY][i][j];
					}
					
					failed = false;
				}
				catch (Exception e) {					
					failed = true;
					
					return failed;
				}

				if (map == 0 || map == 1) tiles.add(new LevelTile(i * 64, j * 64, SpriteSheet.grabImage(v % 18, (int)Math.ceil(v / 18), 64, 64, 4, 4, Game.tileSet), w, 0, false));
				if (map == 2) tiles.add(new LevelTile(i * 64, j * 64, SpriteSheet.grabImage(v % 32, (int)Math.ceil(v / 32), 64, 64, 12, 12, Game.dungeonTileSet), w, 0, false));
				
				if (u != 36 && map == 2) {
					if (u == 0 || u == 1) tiles.add(new LevelTile(i * 64, j * 64, SpriteSheet.grabImage(u % 72, (int)Math.ceil(u / 72), 128, 128, 0, 0, Game.dungeonDoors), 0, 1, false));
					if (u == 2 || u == 3) tiles.add(new LevelTile(i * 64, j * 64 + 32, SpriteSheet.grabImage(u % 72, (int)Math.ceil(u / 72), 128, 128, 0, 0, Game.dungeonDoors), 0, 1, false));
				}
			}
		}
		
		if (map == 2) tiles.add(new LevelTile(0, 0, SpriteSheet.grabImage(dungeonBorderMap[currentScreenX][currentScreenY], 0, 1024, 704, 0, 0, Game.dungeonBorderSet), 0, 0, true));
		
		return failed;
	}
	
	private boolean dungeonExit(int newX, int newY, int oldX, int oldY) {
		boolean exitProperties = false;
		
		if (oldX == 0 && oldY == 1 && newX == 1 && newY == 1) {
			exitProperties = true;
			
			currentScreenX = 6;
			currentScreenY = 6;
		}
		
		return exitProperties;
	}
	
	public String caveText = "";
	
	// Run by Player in collision detection!!
	public int enterSubMap() {
		int map = 1;
		
		if (currentScreenX == 6 && currentScreenY == 6) {
			map = 2;
		}
		
		if (map == 1) {
			int screen = ((currentScreenY - 1) * MAP_WIDTH) + currentScreenX;
			
			switch (screen) {
			case 103:
				caveText = messages[0];
				npcs.add(new NPC(15 * 32, 4 * 64, "oldMan"));
				shop = new ItemShop(new int[] {9}, new int[] {0});
				break;
			case 84:
				caveText = messages[1];
				npcs.add(new NPC(15 * 32, 4 * 64, "oldWoman"));
				break;
			}
		}
		
		return map;
	}
	
	private int[] dungeonEntrance() {
		// Array Structure [direction, x, y]
		// REMEMBER: If the door is on the right, then the player will be walking left, etc!!!
		
		String door = "bottom";
		int[] properties = new int[5];
		
		if (currentScreenX == 6 && currentScreenY == 6) {
			properties[3] = 0;
			properties[4] = 1;
			
			door = "right";
		}
		else {
			changeMap(0, false);
		}
		
		if (door == "right") {
			properties[0] = 1;
			properties[1] = 15 * 64;
			properties[2] = 5 * 64;
		}
		else if (door == "left") {
			properties[0] = 0;
			properties[1] = 0 * 64;
			properties[2] = 5 * 64;
		}
		else if (door == "bottom") {
			properties[0] = 3;
			properties[1] = (7 * 64) + 32;
			properties[2] = 10 * 64;
		}
		else if (door == "top") {
			properties[0] = 2;
			properties[1] = (7 * 64) + 32;
			properties[2] = 0;
		}
		
		return properties;
	}
	
	private void clearScreen(int map) {
		if (map != 1) Game.getController().caveText = "";
		droppedItemManager.clear();
		if (map != 1) npcs.clear();
	}
	
	public void stopSounds() {
		music.stopSound();
		dungeon.stopSound();
	}
	
	public void updateSounds() {
		if (map == 0) {
			dungeon.stopSound();
			music.startSound(true);
		}
		else if (map == 2) {
			music.stopSound();
			dungeon.startSound(true);
		}
	}
}
