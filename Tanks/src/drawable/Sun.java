package drawable;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.*;

import javax.imageio.ImageIO;

public class Sun implements Drawable2 {
	private BufferedImage sun;
	private int x = 0;
	private int y = 0;
	private int radius;
	private int wait = 2;
	private int count = 0;
	private int yLength;
	private int xLength;
	private int xEquation = (int) Math.sqrt(Math.pow(y, 2) - radius);
	private int yEquation;

	public Sun(int xLength, int yLength) {
		try {                
			sun = ImageIO.read(getClass().getResourceAsStream("/img/sun.png"));
		} catch (IOException ex) {
			System.out.println("The sun file requested does not exist! Please fix this before contueing!");
		}
		this.yLength = yLength;
		this.xLength = xLength;
		
		radius = xLength/2;
		yEquation  = (int) Math.sqrt(Math.pow((x - xLength/2), 2) - radius);
	}

	@Override
	public Point getLocation() {
		if ( count == wait) {
			x++;
			count = 0;
			return new Point(x, yEquation);
		} else {
			count++;
			return new Point(x,y);
		}
	}

	@Override
	public int getX() {
		if ( count == wait) {
			x++;
			count = 0;
//			System.out.println(x + "       y:" + y);
			return x;
		} else {
			count++;
			return x;
		}
	}

	@Override
	public int getY() {
		//return 250;
		System.out.println((int) Math.sqrt(Math.pow((x - xLength/2), 2) - radius));
		return (int) (-1 * Math.sqrt(-(x-xLength)*x) + 400);//Math.sqrt(Math.pow((x - xLength/2), 2) + Math.pow(radius,2));
	}

	@Override
	public BufferedImage queryImage() {
		return sun;
	}

}
