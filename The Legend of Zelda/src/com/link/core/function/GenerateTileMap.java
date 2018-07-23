package com.link.core.function;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.link.core.Controller;
import com.link.load.TextFileLoader;

public class GenerateTileMap {
	public GenerateTileMap() {
		
	}
	
	public int[][][][] getTileMap(int[][] tileList) {
		int[][][][] tileMap = new int[Controller.MAP_WIDTH][Controller.MAP_HEIGHT][Controller.SCREEN_WIDTH][Controller.SCREEN_HEIGHT];
		
		for (int i = 0; i < Controller.MAP_HEIGHT; i++) {
			for (int j = 0; j < Controller.MAP_WIDTH; j++) {
				
				
				for (int k = 0; k < Controller.SCREEN_HEIGHT; k++) {
					for (int l = 0; l < Controller.SCREEN_WIDTH; l++) {
						tileMap[j][i][l][k] = tileList[l + (Controller.SCREEN_WIDTH * j)][k + (Controller.SCREEN_HEIGHT * i)];
					}
				}
			}
		}
		
		return tileMap;
	}
	
	public int[][] getTileListFromTextFile(String fileName) {
		int[][] tiles = new int[Controller.MAP_WIDTH * Controller.SCREEN_WIDTH][Controller.MAP_HEIGHT * Controller.SCREEN_HEIGHT];
		
		TextFileLoader fileLoader = new TextFileLoader();
		
		try {
			BufferedReader bufferedReader = fileLoader.loadFile(fileName);
			
			for (int i = 0; i < Controller.MAP_HEIGHT * Controller.SCREEN_HEIGHT; i++) {
				for (int j = 0; j < Controller.MAP_WIDTH * Controller.SCREEN_WIDTH; j++) {
					String line = bufferedReader.readLine();
					tiles[j][i] = Integer.valueOf(line);
				}
			}
			
			bufferedReader.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return tiles;
	}
	
	public int[] getCollisionDataFromTextFile(String fileName, int length) {
		int[] collision = new int[length];
		
		TextFileLoader fileLoader = new TextFileLoader();
		
		try {
			BufferedReader bufferedReader = fileLoader.loadFile(fileName);
			
			for (int i = 0; i < length; i++) {
				String line = bufferedReader.readLine();
				collision[i] = Integer.valueOf(line);
			}
			
			bufferedReader.close();
		}
		catch (FileNotFoundException e) {
			
		}
		catch (IOException e) {
			
		}
		
		return collision;
	}
	
	public int[][][][] getCollisionMap(int[] collision, int[][][][] tileMap) {
		int[][][][] collisionMap = new int[Controller.MAP_WIDTH][Controller.MAP_HEIGHT][Controller.SCREEN_WIDTH][Controller.SCREEN_HEIGHT];
		
		for (int i = 0; i < Controller.MAP_HEIGHT; i++) {
			for (int j = 0; j < Controller.MAP_WIDTH; j++) {
				for (int k = 0; k < Controller.SCREEN_HEIGHT; k++) {
					for (int l = 0; l < Controller.SCREEN_WIDTH; l++) {
						int collisionValue = tileMap[j][i][l][k];
						collisionMap[j][i][l][k] = collision[collisionValue];
					}
				}	
			}
		}
		
		return collisionMap;
	}
}
