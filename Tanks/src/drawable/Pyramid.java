package drawable;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Pyramid implements Drawable2 {
	private BufferedImage image = null;
	private Point location;
	private Dimension size;
	/**
	 * 
	 * @param size There are two sizes. Big and Small. 100px width verse 50px width. True = big, false = small
	 */
	public Pyramid(boolean size, Point position) {
			location = position;
		if (size) {
			this.size = new Dimension(100,100);
			try {
				image = ImageIO.read(getClass().getResourceAsStream("/img/pyramid_100x100.png"));
			} catch (IOException e) {
				System.out.println("The pyramid file requested does not exist! Please fix this before continuing!");
			}
		}else {
			this.size = new Dimension(50,50);
		}
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
		return location.y;
	}

	@Override
	public BufferedImage queryImage() {
		return image;
	}

}
