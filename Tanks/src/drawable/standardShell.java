package drawable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import physics.Projectile;
import physics.Wind;
import Main.Main;
import Main.sounds;
import terrain.Terrain;

/**
 * Class to create standardShell objects
 * 
 *
 * @author Joel Cherney
 *
 */
public class standardShell implements Runnable {

	private Thread t;
	private int[][] terrain;
	private int frameX;
	private int frameY;
	private double x = 0;
	private double y = 0;
	private int y0;
	private int x0;
	private double angle;
	private Terrain painter;
	private ArrayList<Drawable2> drawable;
	sounds sound = new sounds();
	Projectile projectile;
	double power;
	Wind wind;

	/**
	 * standardShell Constructor
	 * 
	 * @param painter Terrain object the shell will be drawn in
	 * @param landscape Array used to draw the terrain
	 * @param frameX Width of the window
	 * @param frameY Height of the window
	 * @param x0 Initial x position of the shell
	 * @param y0 Initial y position of the shell
	 * @param angle Angle that the shell is going at
	 * @param drawable Array list that objects are drawn from
	 */
	public standardShell(Terrain painter, int[][] landscape, int frameX, int frameY, int x0, int y0, double angle, ArrayList<Drawable2> drawable, int power, Wind wind) {
		terrain = landscape;
		this.frameX = frameX;
		this.frameY = frameY;
		this.x0 = x0;
		this.y0 = y0;
		this.angle = angle;
		this.painter = painter;
		this.drawable = drawable;
		t = new Thread(this, "standard shell thread");
		t.start(); // Start the thread
		this.power = power;
		this.wind = wind;
		
	}
	
	
	@Override
	public void run() {
		//set constants
		double time = 0;
		final int a = 1;
		
		//calculate x and y position of shell
//		while ( x < frameX && x >= 0 && y < frameY && y >= 0 && !(terrain[(int) x][(int) y] > 0)) {
//			x = (double) x0 + (double) v0 * Math.cos(angle) * (double) time;
//			y = (double) y0 + (double) v0 * Math.sin(angle) * (double) time + 0.5 * a * Math.pow(time, 2);
//			painter.repaint();
//
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//
//			time += .1;
//		}
		
		//THIS COMMENTED SECTION SHOULD NOW BE HANDLED IN
		//TERRAIN FROM THE DAMAGE METHOD
		/*
		//damage any tanks if necessary
		for(int k = 0; k < drawable.size(); k += 1){
			if(drawable.get(k) instanceof Tank){
				Tank t = (Tank) drawable.get(k);
				if(Math.abs((drawable.get(k).getX() + 19) - getX()) <= 19){
					t.setHealth(t.getHealth() - 3);
					if(t.getHealth() <= 0){
						drawable.remove(k);
						k -= 1;
					}
				}
				else if(Math.abs((drawable.get(k).getX() + 19) - getX()) <= 39){
					t.setHealth(t.getHealth() - 1);
					if(t.getHealth() <= 0){
						drawable.remove(k);
						k -= 1;
					}
				}
			}
		}
		*/
		//damage the terrain if relevant
		if ( (int) x >= 0 && (int) x <= frameX ) {
			sound.run("impact");
			painter.damage((int) x, (int)y, 25);
		}

		
		//remove the shell from the drawable array when it finishes
		for (int i = 0; i < drawable.size(); i++) {
			if (drawable.get(i) instanceof standardShell) {
				drawable.remove(i);
				painter.repaint();
			}
		}
		
		painter.setDrawable(drawable);
		painter.repaint();
	}//end of run method

	
	public Point getLocation() {
		return null;
	}

	
	public int getX() {
		projectile.setX(x);
		return (int) x;
		
	}

	public int getY() {
		projectile.setY(y);
		return (int) y;
	}

	
	public BufferedImage queryImage() {
		return null;
	}

}//end of standardShell class
