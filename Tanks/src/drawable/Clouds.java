package drawable;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import Main.Ticker;
import terrain.Terrain;

/**
 * This class is used to create clouds in the game
 * 
 * @author Joel Cherney
 *
 */
public class Clouds implements Drawable2 {

	private double x = 0;
	private int y = 30;
	private Thread t;
	private Terrain terrain;
	private int yHeight;
	private int xLength;
	private int start;
	private BufferedImage cloud;
	private double rate;
	private int offset;

	/**
	 * This is the constructor for clouds
	 * 
	 * @param landscape The terrain object the cloud is being drawn on
	 * @param x1 Width of the frame
	 * @param y1 Height of the frame
	 * @param startingPosition Starting position of the cloud
	 */
	public Clouds(Terrain landscape, int x1, int y1, int startingPosition){
		terrain = landscape;
		xLength = x1;
		yHeight = y1;
		collectCloud();
		start = startingPosition;

		y = (int) (Math.random() * (yHeight / 3));
		x = Math.random() * landscape.getXTerrain();
		rate = Math.random() * 40 + 20;
		offset = (int) (Math.random() * 200);

		Ticker.addMethod(this::moveCloud);
	}//end of constructor

	/**
	 * Used to get the buffered cloud image
	 * @return cloud image
	 */
	public BufferedImage queryImage() {
		return cloud;
	}//end of queryImage method

	/**
	 * Gets the image from the file
	 */
	private void collectCloud() {
		try {                
			cloud = ImageIO.read(getClass().getResourceAsStream("/img/tempCloud.png"));
		} catch (IOException ex) {
			System.err.println("The cloud file requested does not exist! Please fix this before contueing!");
		}
	}//end of collectCloud method

	/**
	 * gets the x position of the cloud
	 */
	public int getX() {
		return (int) x;

	}//end of getX method

	/**
	 * gets the y position of the cloud
	 */
	public int getY() {
		return y;
	}//end of getY method

	private void moveCloud(long elapsedNanos) {
		if (x > xLength + offset) {
			x = -cloud.getWidth();
			y = (int) (Math.random() * (yHeight / 4)) + 50;
			offset = (int) (Math.random() * 200);
		}
		else x += rate * ((double) elapsedNanos / 1000000000.0);
	}

	@Override
	public Point getLocation() {
		return new Point((int) x, y);
	}

}//end of Clouds class

