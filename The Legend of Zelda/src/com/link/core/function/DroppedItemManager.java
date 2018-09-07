package com.link.core.function;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.link.core.Game;
import com.link.entity.DroppedItem;

public class DroppedItemManager {
	private BufferedImage itemSheet;
	
	//public LinkedList<DroppedItem> items = new LinkedList<DroppedItem>();
	public DroppedItem[] items = new DroppedItem[10];
	
	public DroppedItemManager() {
		itemSheet = Game.itemSheet;
		
		addItem(200, 250, 8, true);
	}
	
	public void tick() {
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) items[i].tick();
		}
	}
	
	public void render(Graphics g) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) items[i].render(g);
		}
	}
	
	public void addItem(int x, int y, int type, boolean despawn) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) { 
				items[i] = new DroppedItem(x, y, itemSheet, type, i, despawn);
				break;
			}		
		}
	}
	
	public void deleteItem(int position) {
		items[position] = null;
	}
	
	public void clear() {
		for (int i = 0; i < items.length; i++) {
			items[i] = null;
		}
	}
}
