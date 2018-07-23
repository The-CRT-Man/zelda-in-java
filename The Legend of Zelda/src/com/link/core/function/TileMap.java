package com.link.core.function;

import java.util.Random;

import com.link.core.Controller;
import com.link.core.Game;

public class TileMap {
	public boolean tileRandomisationComplete;
	
	private int[][][][] tileData = new int[Controller.MAP_WIDTH][Controller.MAP_HEIGHT][Controller.SCREEN_WIDTH][Controller.SCREEN_HEIGHT];
	private int[][][][] collisionData = new int[Controller.MAP_WIDTH][Controller.MAP_HEIGHT][Controller.SCREEN_WIDTH][Controller.SCREEN_HEIGHT];
	
	private int[][][][] caveTileData = new int[Controller.MAP_WIDTH][Controller.MAP_HEIGHT][Controller.SCREEN_WIDTH][Controller.SCREEN_HEIGHT];
	private int[][][][] caveCollisionData = new int[Controller.MAP_WIDTH][Controller.MAP_HEIGHT][Controller.SCREEN_WIDTH][Controller.SCREEN_HEIGHT];
	
	private int[][][][] dungeonTileData = new int[Controller.MAP_WIDTH][Controller.MAP_HEIGHT][Controller.SCREEN_WIDTH][Controller.SCREEN_HEIGHT];
	private int[][][][] dungeonForegroundData = new int[Controller.MAP_WIDTH][Controller.MAP_HEIGHT][Controller.SCREEN_WIDTH][Controller.SCREEN_HEIGHT];
	private int[][][][] dungeonCollisionTileData = new int[Controller.MAP_WIDTH][Controller.MAP_HEIGHT][Controller.SCREEN_WIDTH][Controller.SCREEN_HEIGHT];

	public int[][][][] getTileData() {
		return tileData;
	}
	
	public int[][][][] getCaveTileData() {
		return caveTileData;
	}
	
	public int[][][][] getCollisionData() {
		return collisionData;
	}
	
	public int[][][][] getCaveCollisionData() {
		return caveCollisionData;
	}

	public void setTileData(int[][][][] tileData) {
		this.tileData = tileData;
	}
	
	public void generateTileData() {	
		GenerateTileMap gtm = new GenerateTileMap();
	
		int[][] tileList = gtm.getTileListFromTextFile(Game.map);		
		this.tileData = gtm.getTileMap(tileList);
		
		int[] collision = gtm.getCollisionDataFromTextFile(Game.collisionData, 151);		
		this.collisionData = gtm.getCollisionMap(collision, this.tileData);
		
		int[][] caveTileList = gtm.getTileListFromTextFile(Game.caveMap);		
		this.caveTileData = gtm.getTileMap(caveTileList);
		
		int[] caveCollisionList = gtm.getCollisionDataFromTextFile(Game.collisionData, 151);		
		this.caveCollisionData = gtm.getCollisionMap(caveCollisionList, this.caveTileData);
		
		int[][] dungeonTileList = gtm.getTileListFromTextFile(Game.dungeonMap);
		this.dungeonTileData = gtm.getTileMap(dungeonTileList);
		
		int[] dungeonCollisionList = gtm.getCollisionDataFromTextFile(Game.dungeonCollisionData, 279);
		this.dungeonCollisionTileData = gtm.getCollisionMap(dungeonCollisionList, this.dungeonTileData);
		
		int[][] dungeonForegroundList = gtm.getTileListFromTextFile(Game.dungeonForegroundMap);
		this.setDungeonForegroundData(gtm.getTileMap(dungeonForegroundList));
	}
	
	public void randomiseCollisionData() {
		Random rand = new Random();
		
		for (int i = 0; i < Controller.MAP_HEIGHT; i++) {
			for (int j = 0; j < Controller.MAP_WIDTH; j++) {
				for (int k = 0; k < Controller.SCREEN_HEIGHT; k++) {
					for (int l = 0; l < Controller.SCREEN_WIDTH; l++) {
						collisionData[j][i][l][k] = rand.nextInt(1);
					}
				}
			}
		}
	}

	public int[][][][] getDungeonTileData() {
		return dungeonTileData;
	}

	public void setDungeonTileData(int[][][][] dungeonTileData) {
		this.dungeonTileData = dungeonTileData;
	}

	public int[][][][] getDungeonCollisionTileData() {
		return dungeonCollisionTileData;
	}

	public void setDungeonCollisionTileData(int[][][][] dungeonCollisionTileData) {
		this.dungeonCollisionTileData = dungeonCollisionTileData;
	}

	public int[][][][] getDungeonForegroundData() {
		return dungeonForegroundData;
	}

	public void setDungeonForegroundData(int[][][][] dungeonforegroundData) {
		this.dungeonForegroundData = dungeonforegroundData;
	}
}