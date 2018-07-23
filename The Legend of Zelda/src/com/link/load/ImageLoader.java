package com.link.load;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
	private Image image;
	  
	  public Image loadImage(String path)
	    throws IOException
	  {
	    this.image = ImageIO.read(getClass().getResource(path));
	    return this.image;
	  }
}
