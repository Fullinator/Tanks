package drawable;

import java.awt.image.BufferedImage;

/**
 * 
 * @author Joel Cherney
 * 
 * Abstract class that everything that is drawn must extend
 *
 */
public abstract class drawable {
	
	public int startX = -1;
	public int maxX = -1;
	public double barrelAngle = 0.0;
	public double tankAngle = 0.0;
	private int playerNumber;
	private int gas = 500;
	private int moved = 0;
	private int health = 10;
	public int v0 = 15;
	
	/**
	 * figure out which player the drawable is for
	 */
	public int playerNumber() {
		return playerNumber;		
	}//end of playerNumber method
	
	/**
	 * Set which player the drawable is for
	 * 
	 * @param i The Player number
	 */
	public void setPlayerNumber(int i) {
		playerNumber = i;
		return;
	}//end of setPlayer Number
	
	/**
	 * Used to see how much gas the tank has left
	 */
	public int getGas(){
		return gas;
	}//end of getGas method
	
	/**
	 * sets the amount of gas the tank has
	 * 
	 * @param g New amount of gas
	 */
	public void setGas(int g){
		gas = g;
	}//end of setGas method
	
	/**
	 * gets the amount the tank moved
	 */
	public int getMove(){
		return moved;
	}//end of getMove method
	
	/**
	 * sets how much the tank moved
	 * 
	 * @param m
	 */
	public void setMove(int m){
		moved = m;
	}//end of setMoved method
	
	/**
	 * gets the players current health
	 */
	public int getHealth(){
		return health;
	}//end of getHealth method
	
	/**
	 * sets the players health
	 * 
	 * @param h new health value
	 */
	public void setHealth(int h){
		health = h;
	}//end of setHealth method

	public abstract int getX();
	
	public abstract int getY();
	
	public abstract BufferedImage queryImage();

	
}//End of drawable class
