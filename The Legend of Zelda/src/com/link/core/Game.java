package com.link.core;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JFrame;

import com.link.core.function.KeyInput;
import com.link.load.BufferedImageLoader;
import com.link.load.ImageLoader;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private static JFrame frame;
	private Thread thread;
	private boolean running = false;
	
	public static final String TITLE = "The Legend of Zelda";
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	
	public static BufferedImage tileMap = null;
	
	public static BufferedImage background = null;
	public static BufferedImage playerImg = null;
	public static BufferedImage testTileImg = null;
	
	public static BufferedImage linkImg = null;
	public static BufferedImage swordSheet = null;
	
	public static BufferedImage heartSheet = null;
	public static BufferedImage iconSheet = null;
	
	public static BufferedImage numbers = null;
	public static BufferedImage alphabet = null;
	
	public static BufferedImage tileSet = null;	
	public static BufferedImage dungeonTileSet = null;
	
	public static BufferedImage dungeonBorderSet = null;
	public static BufferedImage dungeonDoors = null;
	
	public static BufferedImage itemSheet = null;
	
	public static BufferedImage icon = null;
	
	public static String sound;
	public static String dungeonMusic;
	
	public static String stairSound;
	public static String swordSound;
	public static String pickUpRupee;
	public static String pickUpHeart;
	public static String pickUpItem;
	
	public static String map;
	public static String collisionData;
	
	public static String caveMap;
	
	public static String dungeonMap;
	public static String dungeonForegroundMap;
	public static String dungeonCollisionData;
	
	public static String messages;

	private static Controller controller;
	
	public static void main(String[] args) {
		Game game = new Game();
		
		// Configuring the canvas
		Dimension windowSize = new Dimension(WIDTH, HEIGHT);
		ImageLoader loader = new ImageLoader();
		
		game.setPreferredSize(windowSize);
		game.setMaximumSize(windowSize);
		game.setMinimumSize(windowSize);
		
		game.getClass();
			
		Image windowImg = null;
		Image taskImg = null;
		
		try {
			windowImg = loader.loadImage("/icon.png");
			taskImg = loader.loadImage("/taskbar_icon.png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		LinkedList<Image> images = new LinkedList<Image>();
		
		images.add(windowImg);
		images.add(taskImg);
		
		frame = new JFrame(TITLE);
		frame.add(game);
		
		frame.pack();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setIconImages(images);
		frame.setVisible(true);
		
		game.start();
	}

	@Override
	public void run() {
		init();
		
		long lastTime = System.nanoTime();
		double amountOfTicks = 50;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;
					
			if (System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				System.out.println("FRAMES: " + frames + " TICKS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
	    stop();
	}
	
	private void tick() {
		controller.tick();
	}
	
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		controller.render(g);
		
		g.drawString("The Legend of Zelda, remade in Java. Art, music and game design are copyrights of Nintendo.", 7, 720);
		
		g.dispose();
		bs.show();
	}
	
	// Starting the thread
	private synchronized void start() {
		if (this.running) {
			return;
		}
		
		this.running = true;
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	// Starting the thread
	private synchronized void stop() {
	    if (!this.running) {
	      return;
	    }
	    this.running = false;
	    try
	    {
	      this.thread.join();
	    }
	    catch (InterruptedException e)
	    {
	      e.printStackTrace();
	    }
	    System.exit(1);
	}
	
	public void init() {
		requestFocus();
		
		BufferedImageLoader loader = new BufferedImageLoader();
		
		try {
			background = loader.loadImage("/black.png");
			
			tileSet = loader.loadImage("/tiles/tile_set.png");
			dungeonTileSet = loader.loadImage("/tiles/dungeon_tile_set.png");
			dungeonBorderSet = loader.loadImage("/new_dungeon_outline.png");
			dungeonDoors = loader.loadImage("/doors.png");
			
			linkImg = loader.loadImage("/link_sheet.png");
			swordSheet = loader.loadImage("/sword_sheet.png");
			
			heartSheet = loader.loadImage("/hearts.png");
			iconSheet = loader.loadImage("/interface/icons.png");
			
			numbers = loader.loadImage("/interface/numbers.png");
			alphabet = loader.loadImage("/interface/alphabet.png");
			
			itemSheet = loader.loadImage("/items.png");
			
			sound = "/overworld.wav";			
			dungeonMusic = "/dungeon.wav";
			
			stairSound = "/sound/LOZ_Stairs.wav";
			
			swordSound = "/sound/LOZ_Sword_Slash.wav";
			
			pickUpRupee = "/sound/LOZ_Get_Rupee.wav";
			pickUpHeart = "/sound/LOZ_Get_Heart.wav";
			pickUpItem = "/sound/LOZ_Get_Item.wav";

			map = "/data/map.txt";
			collisionData = "/data/collision.txt";
			
			dungeonMap = "/data/dungeon.txt";
			dungeonForegroundMap = "/data/dungeon_foreground_map.txt";
			dungeonCollisionData = "/data/collision_dungeon.txt";
			
			caveMap = "/data/cave_map.txt";
			
			messages = "/data/text.txt";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		controller = new Controller();
		
		addKeyListener(new KeyInput(controller));		
	}
	
	public static Controller getController() {
		return controller;
	}
}
