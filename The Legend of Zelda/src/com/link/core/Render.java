package com.link.core;


public class Render implements Runnable {
	Game g;
	
	public Render(Game g) {
		this.g = g;
	}
	
	@Override
	public void run() {
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
			
			g.renderUpdates++;
		}
	}
}
