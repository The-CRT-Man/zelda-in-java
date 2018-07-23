package com.link.load;


import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BufferedImageLoader {
	  private BufferedImage image;
	  
	  public BufferedImage loadImage(String path)
	    throws IOException
	  {
	    this.image = ImageIO.read(getClass().getResource(path));
	    return this.image;
	  }
}
