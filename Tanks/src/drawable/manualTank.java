package drawable;

import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import terrain.*;

/**
 * Used to create manualTank objects
 * 
 * @author Joel Cherney
 */
public class manualTank extends drawable{ 
	private BufferedImage tank;
	
	/**
	 * Constructs a manualTank object for gameplay
	 * 
	 * @param landscape Terrain object to draw the tank on
	 * @param x 
	 * @param max
	 */
	public manualTank(Terrain landscape, int max) {
		collectTank();
		maxX = max;
		randomStartX();

	}//end of manualTank constructor

	public void upAngle() {
		
	}
	
	/**
	 * Used to find the angle of rotation of the tank for keeping it level on the terrain
	 * 
	 * @param x X position of the tank
	 * @param points Terrain array of points for determining slope
	 * 
	 * @return the angle in radians
	 */
	public double angle(int x, boolean[][] points) {
		int y1 = 0;
		int y2 = 0;
		for(int i = 0; i < points[x].length; i += 1){
			if(points[x][i]){
				y1 = i;
				break;
			}
		}
		
		for(int i = 0; i < points[x].length; i += 1){
			if(points[x - 15][i]){
				y2 = i;
				break;
			}
		}
		double angle = Math.atan((y2 - y1) / -15.0);
		tankAngle = angle;
		return angle;
	}//end of angle method

	/**
	 * @return The angle of the barrel
	 */
	public double barrelAngle() {
		return barrelAngle;
	}//end of barrelAngle method
	
	/**
	 * Starts the tank at a random position
	 */
	private void randomStartX() {
		int tempRandom;
		while(true) {
			tempRandom = (int) (Math.random() * 1000);;
			if (tempRandom > 40 && tempRandom < maxX - 40) {
				break;
			}
		}
		startX = tempRandom;
	}

	/**
	 * Used to get the actual tank image
	 */
	public BufferedImage queryImage() {
		return tank;
	}//end of queryImage method

	/**
	 * gets the tank image from a file
	 */
	private void collectTank() {
		try {                
			tank = ImageIO.read(getClass().getResourceAsStream("/img/temporaryTank.png"));
		} catch (IOException ex) {
			System.out.println("The tank file requested does not exist! Please fix this before contueing!");
		}
	}//end of collectTank method
	

	@Override
	public int getX() {
		return startX;
	}


	@Override
	public int getY() {
		return 0;
	}

}//end of manualTank class
