package drawable;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import terrain.Terrain;

/**
 * This class is used to create clouds in the game
 * 
 * @author Joel Cherney
 *
 */
public class Clouds extends drawable implements Runnable{

	private int x = 1;
	private int y = 30;
	private Thread t;
	private Terrain terrain;
	private int yHeight;
	private int xLength;
	private int start;
	private BufferedImage cloud;

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
		t = new Thread(this, "Cloud Thread");
		t.start(); // Start the thread
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
			System.out.println("The cloud file requested does not exist! Please fix this before contueing!");
		}
	}//end of collectCloud method

	/**
	 * gets the x position of the cloud
	 */
	public int getX() {
		return x;

	}//end of getX method

	/**
	 * gets the y position of the cloud
	 */
	public int getY() {
		return y;
	}//end of getY method

	/**
	 * makes the cloud move across the screen
	 */
	@Override
	public void run() {
		while(true) {
			x = start;
			if (start < 0) {// This is the cloud that runs from the left
				x = -384;
				while(true) {// generates a random Y position
					y = (int)(Math.random()*160);
					if ( y < (yHeight - (yHeight / 4)) && y > 50){
						break;
					}
				}
				while(true){
					if (x == xLength){
						break;
					}
					x++;
					terrain.repaint();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else {// This is the cloud that runs from the right
				while(true) {// generates a random Y position
					y = (int)(Math.random()*160);
					if ( y < (yHeight - (yHeight / 4)) && y > 50){
						break;
					}
				}

				while(true){
					if (x == -384){
						break;
					}
					x = x -1;
					//System.out.println(x);
					terrain.repaint();
					try {
						Thread.sleep(125);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}//end of run method

}//end of Clouds class

