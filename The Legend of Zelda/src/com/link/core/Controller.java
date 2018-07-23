package com.link.core;

import java.awt.Graphics;
import java.util.LinkedList;

import com.link.core.function.HeartManager;
import com.link.core.function.ScreenScroller;
import com.link.core.function.SoundEffect;
import com.link.core.function.TileMap;
import com.link.entity.Heart;
import com.link.entity.Player;
import com.link.entity.Sword;
import com.link.load.SpriteSheet;
import com.link.tile.LevelTile;
import com.link.tile.ScrollTile;
import com.link.tile.Tile;

public class Controller {
	public LinkedList<Tile> tiles = new LinkedList<Tile>();
	public LinkedList<Tile> scrollTiles = new LinkedList<Tile>();
	
	public LinkedList<Heart> hearts = new LinkedList<Heart>();

	public static final int MAP_WIDTH = 16;
	public static final int MAP_HEIGHT = 8;
	
	public static final int SCREEN_WIDTH = 16;
	public static final int SCREEN_HEIGHT = 11;
	
	private TileMap generatedTileMap;
	
	public int tickCount;
	
	public HeartManager heartManager;
	
	public int[][][][] tileMap = new int[MAP_WIDTH][MAP_HEIGHT][SCREEN_WIDTH][SCREEN_HEIGHT];
	public int[][][][] collisionMap = new int[MAP_WIDTH][MAP_HEIGHT][SCREEN_WIDTH][SCREEN_HEIGHT];
	
	public int[][][][] caveTileMap = new int[MAP_WIDTH][MAP_HEIGHT][SCREEN_WIDTH][SCREEN_HEIGHT];
	public int[][][][] caveCollisionMap = new int[MAP_WIDTH][MAP_HEIGHT][SCREEN_WIDTH][SCREEN_HEIGHT];
	
	public int[][][][] dungeonTileMap = new int[MAP_WIDTH][MAP_HEIGHT][SCREEN_WIDTH][SCREEN_HEIGHT];
	public int[][][][] dungeonDoors = new int[MAP_WIDTH][MAP_HEIGHT][SCREEN_WIDTH][SCREEN_HEIGHT];
	public int[][][][] dungeonCollisionMap = new int[MAP_WIDTH][MAP_HEIGHT][SCREEN_WIDTH][SCREEN_HEIGHT];
	
	public int[][] dungeonBorderMap = new int[MAP_WIDTH][MAP_HEIGHT];
	
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
	
	public int[] caveEntranceTileLocation = {0, 0};
	
	public Player player;
	public Sword sword;
	
	public int map;
	
	public ScreenScroller screenScroller;
	
	private SoundEffect music;
	private SoundEffect dungeon;
	private SoundEffect stairsSound;
	public SoundEffect swordSound;
	
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
		
		
		for (int i = 0; i < Controller.SCREEN_HEIGHT; i++) {
			for (int j = 0; j < Controller.SCREEN_WIDTH; j++) {
				System.out.println(dungeonCollisionMap[0][0][j][i]);
			}
		}
		
		generateObjects();
		musicController();
	}
	
	private void generateObjects() {
		player = new Player();
		heartManager = new HeartManager();
		
		for (int i = 0; i < SCREEN_WIDTH; i++) {
			for (int j = 0; j < SCREEN_HEIGHT; j++) {
				int v = tileMap[currentScreenX][currentScreenY][i][j];
				int w = collisionMap[currentScreenX][currentScreenY][i][j];
				
				tiles.add(new LevelTile(i * 64, j * 64, SpriteSheet.grabImage(v % 18, (int)Math.ceil(v / 18), 64, 64, 4, 4, Game.tileSet), w, 0));
			}
		}
		
		hearts = heartManager.tick(true);
		
		sword = new Sword();
	}
	
	public void musicController() {
		music = new SoundEffect(Game.sound);		
		stairsSound = new SoundEffect(Game.stairSound);		
		swordSound = new SoundEffect(Game.swordSound);		
		dungeon = new SoundEffect(Game.dungeonMusic);
				
		if (!music.isRunning()) music.startSound(true);
	}
	
	public void tick() {
		if (scrollScreen) {
			screenScroller.tick();
		}
		else {
			player.tick();
			sword.tick();
			
			hearts.clear();
			
			hearts = heartManager.tick(false);
			
			if (animateCaveEntrance || animateCaveEntranceFinish) {
				changeMap(map, true);
			}
		}
		
		tickCount++;
		
		if (tickCount == 60) {
			tickCount = 0;
		}
	}
	
	public void render(Graphics g) {
		for (int i = 0; i < tiles.size(); i++) {
			if (tiles.get(i).layer == LevelTile.BACKGROUND) tiles.get(i).render(g);
		}
		
		for (int i = 0; i < scrollTiles.size(); i++) {
			scrollTiles.get(i).render(g);
		}
			
		if (sword.isAttacking) {
			sword.render(g);
		}
		
		player.render(g);
		
		if (animateCaveEntrance || animateCaveEntranceFinish) {
			Tile foreground = new LevelTile(caveEntranceTileLocation[0] * 64, (caveEntranceTileLocation[1] + 1) * 64, SpriteSheet.grabImage(2, 0, 64, 64, 4, 4, Game.tileSet), 0, 1);			
			foreground.render(g);
		}
		
		for (int i = 0; i < tiles.size(); i++) {
			if (tiles.get(i).layer == LevelTile.FOREGROUND) tiles.get(i).render(g);
		}
		
		for (int i = 0; i < scrollTiles.size(); i++) {
			if (scrollTiles.get(i).layer == LevelTile.FOREGROUND) scrollTiles.get(i).render(g);
		}
		
		g.drawImage(Game.background, Controller.SCREEN_WIDTH * 64, 0, null);
		g.drawImage(Game.background, 0, Controller.SCREEN_HEIGHT * 64, null);
		
		for (int i = 0; i < player.maxHealth / 2; i++) {
			hearts.get(i).render(g);
		}
	}
	
	public void changeMap(int map, boolean animate) {
		/*
		 * 0 = over world
		 * 1 = cave
		 * 2 = dungeon
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
					
					tiles.clear();
					
					map = 0;
					
					for (int i = 0; i < SCREEN_WIDTH; i++) {
						for (int j = 0; j < SCREEN_HEIGHT; j++) {
							int v = tileMap[currentScreenX][currentScreenY][i][j];
							int w = collisionMap[currentScreenX][currentScreenY][i][j];
							
							tiles.add(new LevelTile(i * 64, j * 64, SpriteSheet.grabImage(v % 18, (int)Math.ceil(v / 18), 64, 64, 4, 4, Game.tileSet), w, 0));
						}
					}
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
				
				for (int i = 0; i < SCREEN_WIDTH; i++) {
					for (int j = 0; j < SCREEN_HEIGHT; j++) {
						int v = tileMap[currentScreenX][currentScreenY][i][j];
						int w = collisionMap[currentScreenX][currentScreenY][i][j];
						
						tiles.add(new LevelTile(i * 64, j * 64, SpriteSheet.grabImage(v % 18, (int)Math.ceil(v / 18), 64, 64, 4, 4, Game.tileSet), w, 0));
					}
				}
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
				}
				else if (animateCaveEntranceFinish && !animateCaveEntrance) {
					tiles.clear();
					
					for (int i = 0; i < SCREEN_WIDTH; i++) {
						for (int j = 0; j < SCREEN_HEIGHT; j++) {
							int v = caveTileMap[currentScreenX][currentScreenY][i][j];
							int w = caveCollisionMap[currentScreenX][currentScreenY][i][j];
							
							tiles.add(new LevelTile(i * 64, j * 64, SpriteSheet.grabImage(v % 18, (int)Math.ceil(v / 18), 64, 64, 4, 4, Game.tileSet), w, 0));
						}
					}
					
					
					player.x = (double)480;
					player.y = (double)576;
					
					animateCaveEntranceFinish = false;
					animateCaveEntrance = false;
				}
			}
			else {
				tiles.clear();
				
				for (int i = 0; i < SCREEN_WIDTH; i++) {
					for (int j = 0; j < SCREEN_HEIGHT; j++) {
						int v = caveTileMap[currentScreenX][currentScreenY][i][j];
						int w = caveCollisionMap[currentScreenX][currentScreenY][i][j];
						
						tiles.add(new LevelTile(i * 64, j * 64, SpriteSheet.grabImage(v % 18, (int)Math.ceil(v / 18), 64, 64, 4, 4, Game.tileSet), w, 0));
					}
				}
				
				
				player.x = (double)480;
				player.y = (double)576;
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
					tiles.clear();
					
					for (int i = 0; i < SCREEN_WIDTH; i++) {
						for (int j = 0; j < SCREEN_HEIGHT; j++) {
							int v = dungeonTileMap[currentScreenX][currentScreenY][i][j];
							int w = dungeonCollisionMap[currentScreenX][currentScreenY][i][j];
							
							int u = dungeonDoors[currentScreenX][currentScreenY][i][j];
							
							tiles.add(new LevelTile(i * 64, j * 64, SpriteSheet.grabImage(v % 31, (int)Math.ceil(v / 32), 64, 64, 12, 12, Game.dungeonTileSet), w, 0));
							if (u != 36) {
								if (u == 0 || u == 1) tiles.add(new LevelTile(i * 64, j * 64, SpriteSheet.grabImage(u % 72, (int)Math.ceil(u / 72), 128, 128, 0, 0, Game.dungeonDoors), 0, 1));
								if (u == 2 || u == 3) tiles.add(new LevelTile(i * 64, j * 64 + 32, SpriteSheet.grabImage(u % 72, (int)Math.ceil(u / 72), 128, 128, 0, 0, Game.dungeonDoors), 0, 1));
							}
						}
					}
					
					tiles.add(new LevelTile(0, 0, SpriteSheet.grabImage(dungeonBorderMap[currentScreenX][currentScreenY], 0, 1024, 704, 0, 0, Game.dungeonBorderSet), 0, 0));
									
					player.x = (double)480;
					player.y = (double)256;
					
					animateCaveEntranceFinish = false;
					animateCaveEntrance = false;
					
					dungeon.startSound(true);
				}
			}
			else {
				tiles.clear();
				
				for (int i = 0; i < SCREEN_WIDTH; i++) {
					for (int j = 0; j < SCREEN_HEIGHT; j++) {
						int v = dungeonTileMap[currentScreenX][currentScreenY][i][j];
						int w = dungeonCollisionMap[currentScreenX][currentScreenY][i][j];
						
						tiles.add(new LevelTile(i * 64, j * 64, SpriteSheet.grabImage(v % 32, (int)Math.ceil(v / 32), 64, 64, 12, 12, Game.dungeonTileSet), w, 0));
					}
				}
				
				tiles.add(new LevelTile(0, 0, SpriteSheet.grabImage(dungeonBorderMap[currentScreenX][currentScreenY], 0, 1024, 704, 0, 0, Game.dungeonBorderSet), 0, 0));
				
				player.x = (double)480;
				player.y = (double)576;

			}
		}
	}
	
	public void scrollScreen(int direction, boolean complete) {
		boolean failed = false;
		screenScrollDirection = direction;
		
		int yDirection = 0;
		int xDirection = 0;
			
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
			changeMap(0, true);
			return;
		}
		
		boolean leaveDungeonProperties = dungeonExit(currentScreenX, currentScreenY, previousScreenX, previousScreenY);
		
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

				if (map == 0 || map == 1) scrollTiles.add(new ScrollTile((SCREEN_WIDTH * xDirection + i) * 64, (SCREEN_HEIGHT * yDirection + j) * 64, SpriteSheet.grabImage(v % 18, (int)Math.ceil(v / 18), 64, 64, 4, 4, Game.tileSet), 0));
				if (map == 2) scrollTiles.add(new ScrollTile((SCREEN_WIDTH * xDirection + i) * 64, (SCREEN_HEIGHT * yDirection + j) * 64, SpriteSheet.grabImage(v % 32, (int)Math.ceil(v / 32), 64, 64, 12, 12, Game.dungeonTileSet), 0));				
				
				if (u != 36 && map == 2) {
					if (u == 0 || u == 1) scrollTiles.add(new ScrollTile((SCREEN_WIDTH * xDirection + i) * 64, (SCREEN_HEIGHT * yDirection + j) * 64, SpriteSheet.grabImage(u % 72, (int)Math.ceil(u / 72), 128, 128, 0, 0, Game.dungeonDoors), 1));
					if (u == 2 || u == 3) scrollTiles.add(new ScrollTile((SCREEN_WIDTH * xDirection + i) * 64, (SCREEN_HEIGHT * yDirection + j) * 64 + 32, SpriteSheet.grabImage(u % 72, (int)Math.ceil(u / 72), 128, 128, 0, 0, Game.dungeonDoors), 1));
				}
			}
		}
		
		if (map == 2) scrollTiles.add(new ScrollTile((SCREEN_WIDTH * xDirection) * 64, (SCREEN_HEIGHT * yDirection ) * 64, SpriteSheet.grabImage(0, 0, 1024, 704, 0, 0, Game.dungeonBorderSet), 0));

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
				}
				catch (Exception e) {					
					failed = true;
				}

				if (map == 0 || map == 1) tiles.add(new LevelTile(i * 64, j * 64, SpriteSheet.grabImage(v % 18, (int)Math.ceil(v / 18), 64, 64, 4, 4, Game.tileSet), w, 0));
				if (map == 2) tiles.add(new LevelTile(i * 64, j * 64, SpriteSheet.grabImage(v % 32, (int)Math.ceil(v / 32), 64, 64, 12, 12, Game.dungeonTileSet), w, 0));
				
				if (u != 36 && map == 2) {
					if (u == 0 || u == 1) tiles.add(new LevelTile(i * 64, j * 64, SpriteSheet.grabImage(u % 72, (int)Math.ceil(u / 72), 128, 128, 0, 0, Game.dungeonDoors), 0, 1));
					if (u == 2 || u == 3) tiles.add(new LevelTile(i * 64, j * 64 + 32, SpriteSheet.grabImage(u % 72, (int)Math.ceil(u / 72), 128, 128, 0, 0, Game.dungeonDoors), 0, 1));
				}
			}
		}
		
		if (map == 2) tiles.add(new LevelTile(0, 0, SpriteSheet.grabImage(dungeonBorderMap[currentScreenX][currentScreenY], 0, 1024, 704, 0, 0, Game.dungeonBorderSet), 0, 0));
		
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
			
			tiles.clear();
			
			for (int i = 0; i < SCREEN_WIDTH; i++) {
				for (int j = 0; j < SCREEN_HEIGHT; j++) {
					int v = 0;
					int w = 0;
					
					v = tileMap[currentScreenX][currentScreenY][i][j];
					w = tileMap[currentScreenX][currentScreenY][i][j];
					
					tiles.add(new LevelTile(i * 64, j * 64, SpriteSheet.grabImage(v % 18, (int)Math.ceil(v / 18), 64, 64, 4, 4, Game.tileSet), w, 0));
				}
			}
		}
	}
	
	private boolean dungeonExit(int newX, int newY, int oldX, int oldY) {
		boolean exitProperties = false;
		
		if (oldX == 0 && oldY == 1 && newX == 1 && newY == 1) {
			exitProperties = true;
			
			currentScreenX = 0;
			currentScreenY = 0;
		}
		
		return exitProperties;
	}
}
