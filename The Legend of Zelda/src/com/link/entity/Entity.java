package com.link.entity;

import java.awt.Graphics;

public abstract class Entity {
	public abstract void tick();
	public abstract void render(Graphics g);
	public abstract void setVisible(boolean visible);
}
