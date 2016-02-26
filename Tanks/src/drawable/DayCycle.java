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
	private int wait = 0;
	private int count = 0;
	private int yLength;
	private int xLength;
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
	}

	@Override
	public Point getLocation() {
		if ( count == wait) {
			x++;
			count = 0;
			return new Point(x, Y());
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
		//System.out.println("x: " + x + "       Y: " + Y());
		return Y();
	}

	private int Y() {
		return (int) (800 - (Math.sqrt(Math.abs(-Math.pow(x, 2)+800*x+200000))));
	}

	public boolean shiftNight() {
		if (x >= xLength - 80) {
			return true;
		} else if (!day) {
			return true;
		} else {
			return false;
		}
	}

	public int shiftNightAmount() {
		for (int i = 1; i <= 80; i++ ) {
			if ((xLength - i) == x) {
				return i;
			}
		}

		System.out.println("COULDN'T FIND XLENGTH MATCH");
		return 0;
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
