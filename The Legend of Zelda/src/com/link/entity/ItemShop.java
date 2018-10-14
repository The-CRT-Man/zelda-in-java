package com.link.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import com.link.core.Game;
import com.link.ui.Number;

public class ItemShop {
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
	private BufferedImage itemSheet = Game.itemSheet;
	private BufferedImage numSheet = Game.numbers;
	
	private static final int DEFAULT_SHOP_X = 400;
	private static final int DEFAULT_SHOP_Y = 400;
	
	private List<DroppedItem> shopItems;
	private List<Number> displayedPrices;
	private List<Integer> prices;
	
	private DroppedItem heldItem;
	
	private int numberOfItems;
	
	private int space = 32 * 4;
	
	private int state = 0;
	
	/*
	 * State 0 is Shop Mode
	 * State 1 is Hold Mode
	 * State 2 is Disabled
	 */
	
	public ItemShop(int[] itemIDs, int[] prices) {
		itemSheet = Game.itemSheet;
		numSheet = Game.numbers;
		
		numberOfItems = itemIDs.length;
		
		shopItems = new LinkedList<DroppedItem>();
		this.prices = new LinkedList<Integer>();
		
		generateShop(itemIDs, prices);
		generateDisplayedPrices();
	}
	
	private void generateShop(int[] itemIDs, int[] prices) {
		for (int i = 0; i < itemIDs.length; i++) {
			shopItems.add(new DroppedItem(DEFAULT_SHOP_X + (i * space), DEFAULT_SHOP_Y, itemSheet, itemIDs[i], false, DroppedItem.PICKUP_TYPE_DISABLED));
			this.prices.add(prices[i]);
		}
	}
	
	private void generateDisplayedPrices() {
		displayedPrices = new LinkedList<Number>();
		for (int i = 0; i < prices.size(); i++) {
			Number n = new Number(DEFAULT_SHOP_X + (i * space), DEFAULT_SHOP_Y + 64, numSheet);
			n.number = prices.get(i);
			displayedPrices.add(n);
		}
	}
	
	public void tick() {
		if (state == 0) {
			for (Number n : displayedPrices) {
				n.tick();
			}
			
			for (int i = 0; i < prices.size(); i++) {
				if (Game.getController().player.rupees < prices.get(i)) {
					shopItems.get(i).setPickupType(DroppedItem.PICKUP_TYPE_DISABLED);
				}
				else {
					shopItems.get(i).setPickupType(DroppedItem.PICKUP_TYPE_GRAND);
				}
			}
			
			for (DroppedItem d : shopItems) {
				d.tick();
				
				if (d.shouldBeDestroyed) {
					purchaseItem(d.type);
				}
			}
		}
		else if (state == 1) {
			heldItem.tick();
			
			if (!Game.getController().player.isHoldingItem()) {
				state = 2;
			}
		}
		else if (state == 2) {
			
		}
	}
	
	public void render(Graphics g) {
		if (state == 0) {
			for (DroppedItem d : shopItems) {
				d.render(g);
			}
			
			for (Number n : displayedPrices) {
				if (n.number != 0) n.render(g);
			}
		}
		else if (state == 1) {
			heldItem.render(g);
		}
		else if (state == 2) {
			
		}
	}
	
	private void purchaseItem(int itemID) {
		state = 1;
		Player p = Game.getController().player;
		
		int position = 0;
		for (int i = 0; i < this.numberOfItems; i++) {
			if (shopItems.get(i).type == itemID) {
				position = i;
				displayedPrices.clear();
				
				heldItem = shopItems.get(i);
				heldItem.pickedUp = false;
				heldItem.x = (int) p.x;
				heldItem.y = (int) p.y - 71;
				heldItem.setPickupType(DroppedItem.PICKUP_TYPE_DISABLED);
			}
		}
		
		p.rupees -= prices.get(position);
		
		shopItems.clear();
	}
}
