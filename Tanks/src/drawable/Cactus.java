package drawable;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import Main.Main;

public class Cactus implements Drawable2 {
	private BufferedImage image = null;
	private Point location;
	private Dimension size;
	private int heightOffset = 325;
	/**
	 * 
	 * @param size There are two sizes. Big and Small. 100px width verse 50px width. True = big, false = small
	 */
	public Cactus(Point position) {
			location = position;
			int max = 4;
			int min = 1;
			Random random = new Random();
			int type = random.nextInt(max - min + 1) + min;
			
			switch (type) {
			
			case 1:
				try {
					image = ImageIO.read(getClass().getResourceAsStream("/img/cactus_001.png"));
					this.size = new Dimension(image.getWidth(),image.getHeight());
				} catch (IOException e) {
					Main.error(e);
				}
				break;
				
			case 2:
				try {
					image = ImageIO.read(getClass().getResourceAsStream("/img/cactus_002.png"));
					this.size = new Dimension(image.getWidth(),image.getHeight());
				} catch (IOException e) {
					Main.error(e);
				}
				break;
				
			case 3:
				try {
					image = ImageIO.read(getClass().getResourceAsStream("/img/cactus_003.png"));
					this.size = new Dimension(image.getWidth(),image.getHeight());
				} catch (IOException e) {
					Main.error(e);
				}
				break;
				
			case 4:
				try {
					image = ImageIO.read(getClass().getResourceAsStream("/img/cactus_004.png"));
					this.size = new Dimension(image.getWidth(),image.getHeight());
				} catch (IOException e) {
					Main.error(e);
				}
				break;
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
		return location.y + heightOffset;
	}

	@Override
	public BufferedImage queryImage() {
		return image;
	}

}
