package drawable;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import Main.Main;

public class Snowman implements Drawable2 {
	private BufferedImage image = null;
	private Point location;
	private Dimension size;
	private int heightOffset = 0;
	/**
	 * 
	 */
	public Snowman(Point position) {
			location = position;
				try {
					image = ImageIO.read(getClass().getResourceAsStream("/img/snowman.png"));
				} catch (Exception e) {
					Main.error(e);
				}
				this.size = new Dimension(image.getWidth(),image.getHeight());
	}

	@Override
	public Point getLocation() {
		return location;
	}

	@Override
	public int getX() {
		return location.x;
	}

	@Override
	public int getY() {
		return location.y + heightOffset;
	}

	@Override
	public BufferedImage queryImage() {
		return image;
	}

}
