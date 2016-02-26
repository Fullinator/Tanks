package drawable;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.*;

import javax.imageio.ImageIO;

public class DayCycle implements Drawable2 {
	private BufferedImage sun;
	private BufferedImage moon;
	private int x = -150;
	private int y = 0;
	private int radius;
	private int wait = 3;
	private int count = 0;
	private int yLength;
	private int xLength;
	private int xEquation = (int) Math.sqrt(Math.pow(y, 2) - radius);
	private int yEquation;
	private boolean day = true;

	public DayCycle(int xLength, int yLength) {
		try {                
			sun = ImageIO.read(getClass().getResourceAsStream("/img/sun.png"));
		} catch (IOException ex) {
			System.out.println("The sun file requested does not exist! Please fix this before contueing!");
		}
		try {                
			moon = ImageIO.read(getClass().getResourceAsStream("/img/moon.png"));
		} catch (IOException ex) {
			System.out.println("The moon file requested does not exist! Please fix this before contueing!");
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
		if (x == (xLength + 5)) {
			x = -150;
			day = !day;
		}
		if ( count == wait) {
			x++;
			count = 0;
			return x;
		} else {
			count++;
			return x;
		}
	}

	@Override
	public int getY() { 
		//System.out.println("x: " + x + "       Y: " + (int) (850 - (Math.sqrt(Math.abs(-Math.pow(x, 2)+800*x+200000)))));
		return (int) (800 - (Math.sqrt(Math.abs(-Math.pow(x, 2)+800*x+200000))));
	}

	@Override
	public BufferedImage queryImage() {
		if (day) {
			return sun;
		}else {
			return moon;
		}
	}

}
