package drawable;

import java.awt.Color;
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
	private int shiftAmount = 0;

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
		//new Color(66,98,255,nightShiftAmount));
		if (day) {
			for (int i = 80; i > 0; i-- ) {
				if ((xLength - i) == x) {
					shiftAmount = 80 - i;
					return shiftAmount;
				}
			}
		} else if(!day) {
			for (int i = 0; i <= 80; i++ ) {
				if ((xLength - i) == x) {
					shiftAmount = i;
					return shiftAmount;
				}
			}
		}
		System.out.println("COULDN'T FIND XLENGTH MATCH");
		return shiftAmount;
	}

	/*
	public Color shiftNightColor() {
		if (day) {
			for (int i = 80; i > 0; i-- ) {
				if ((xLength - i) == x) {
					shiftAmount = 120 - i;
					//return shiftAmount;
				}
			}
		} else if(!day) {
			for (int i = 0; i <= 120; i++ ) {
				if ((xLength - i) == x) {
					shiftAmount = i;
					//return shiftAmount;
				}
			}
		}
		
		
		return new Color(66,98,255,shiftNightAmount());
		
	}
*/
	@Override
	public BufferedImage queryImage() {
		if (day) {
			return sun;
		}else {
			return moon;
		}
	}

}
