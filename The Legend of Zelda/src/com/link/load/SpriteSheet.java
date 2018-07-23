package com.link.load;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	public static BufferedImage grabImage(int column, int row, int width, int height, int horizontalSpacing, int verticalSpacing, BufferedImage image) {
		BufferedImage img = image.getSubimage(column * (width + horizontalSpacing) + horizontalSpacing, row * (height + verticalSpacing) + verticalSpacing, width, height);
		return img;
	}
}
