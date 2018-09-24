package com.link.core;


public class Render implements Runnable {
	Game g;
	
	public Render(Game g) {
		this.g = g;
	}
	
	public void run() {
		System.out.println("Hello from another thread");
		
		try {
			Thread.sleep(1000);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		while (g.running) {
			if (!g.isTicking) {
				g.render();			
			}
		}
	}
}
