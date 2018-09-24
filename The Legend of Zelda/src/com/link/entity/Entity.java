package com.link.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Entity {
	public BufferedImage sheetImage;
	
	public abstract void tick();
	public abstract void render(Graphics g);
	public abstract void setVisible(boolean visible);
}
