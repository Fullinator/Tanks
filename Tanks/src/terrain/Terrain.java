package terrain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

import Jama.Matrix;
import drawable.AITank;
import drawable.Clouds;
import drawable.drawable;
import drawable.manualTank;
import drawable.Drawable2;
import drawable.Tank;


@SuppressWarnings("serial")
public abstract class Terrain extends JPanel implements KeyListener{
	protected int[][] terrain; // This will hold all of the points that will be painted
	protected int xPanel = 0;// This will be set to the JPanels width
	protected int yPanel = 0;// This will be set to the JPanels height
	public int maxPlayers;
	protected int currentPlayer = 1;
	protected double y;
	protected double a;
	protected double b;
	protected double c;
	protected double d;
	protected int maxHuman = -1;
	public JLabel angle;
	public JLabel power;
	public JLabel playerName;
    //protected ArrayList<drawable> drawable;
    Color primary;
    Color secondary;
    protected ArrayList<Drawable2> drawable;
	protected ArrayList<Tank> players;
	
	/**
	 *
	 * @param x width of JPanel
	 * @param y height of JPanel
	 */
	protected Terrain(int x, int y, int maxH) {
		setXTerrain(x);
		setYTerrain(y);
		maxHuman = maxH;
		generate();
	}
	
	public drawable currentTank() {
		for ( int i = 0; i < drawable.size(); i++) {
			if (drawable.get(i).playerNumber() == currentPlayer) {
				return drawable.get(i);
			}
		}
		assert false; //Execution should never reach this point
		return null;
	}
	

	/**
	 * used to get the array of points used to draw the terrain
	 * 
	 * @return boolean array of points
	 */
	public int[][] getTerrain(){
		return terrain;
	}

	public void setDrawable(ArrayList<Drawable2> drawable) {
		this.drawable = drawable;
	}

	public int findY(int x){
		if(x > 0 && x < xPanel){
			for(int i = 0; i < terrain[0].length; i += 1){
				if(terrain[x][i] > 0){
					return i;
				}
			}
		}
		return (int)(a + b * x + c * Math.pow(x, 2) + d * Math.pow(x, 3));	
	}
	

	/**
	 *
	 * @param x the JPanels width
	 */
	public void setXTerrain(int x) {
		xPanel = x;
	}

	/**
	 *
	 * @param y the JPanels height
	 */
	public void setYTerrain(int y) {
		yPanel = y;
	}

	/**
	 *
	 * @return the JPanels width
	 */
	public int getXTerrain() {
		return xPanel;
	}

	/**
	 *
	 * @return the JPanels height
	 */
	public int getYTerrain() {
		return yPanel;
	}

	

	/**
	 * Generate random points and uses cubic regression to find the equation of the line
	 */
	protected void generate() {
		
		terrain = new int[getXTerrain()][getYTerrain()];// holds the elements of the terrain to be drawn

		double[] xPoints = new double[7]; // array that holds randomly created x coordinates 
		double[] yPoints = new double[7]; // array that holds randomly created y coordinates
		xPoints[5] = getXTerrain();
		xPoints[6] = getXTerrain();

		int xGenCount = 0;

		while (xGenCount < xPoints.length) { // X-coordinate generation
			double temp = Math.round(Math.random() * 1000); 
			if (xGenCount == 0) {
				xPoints[xGenCount] = 0;
				xGenCount++;
			}
			if (xGenCount == 1) {
				xPoints[xGenCount] = getXTerrain();
				xGenCount++;
			}
			if (xGenCount == 2) {
				xPoints[xGenCount] = (getXTerrain()-200);
				xGenCount++;
			}
			else if (temp <= getXTerrain() && temp >= 0) {
				xPoints[xGenCount] = temp;
				xGenCount++;
			}
		}// end of x-coordinate generation

		int yGenCount = 0;

		while (yGenCount < yPoints.length) {// Y-coordinate generation
			double temp = Math.round(Math.random() * 1000);
			if (temp < getYTerrain() && temp > 300) {
				yPoints[yGenCount] = temp;
				yGenCount++;
			}
		}// end of Y-coordinate generation



		double[][] xArray = new double[7][4]; 

		for (int i = 0; i < 7 ; i++) {  // creates a 2D array with the x-coordinates in the cubic regression formula
			// [ 1, x1, x1^2, x1^3 ]
			// [ 1, x2, x2^2, x2^3 ]
			// [ 1, .............. ]
			int j = 0; 
			xArray[i][j] = 1.0; 
			j++; 
			xArray[i][j] = xPoints[i]; 
			j++; 
			xArray[i][j] = Math.pow(xPoints[i], 2); 
			j++; 
			xArray[i][j] = Math.pow(xPoints[i], 3);
		}


		double [][] yArray = new double[7][1]; 

		for (int i = 0; i < yPoints.length; i++) {// creates a 2D array with the y-coordinates in the cubic regression formula
			yArray[i][0] = yPoints[i]; 
		} 


		Matrix xMatrix = new Matrix(xArray);  // convert the x cubic regression styled 2D array to a matrix
		Matrix yMatrix = new Matrix(yArray);  // convert the y cubic regression styled 2D array to a matrix

		// Cubic regression: B = (xT*x)^-1*xT*y

		Matrix first = xMatrix.transpose().times(xMatrix); // xT*x
		first = first.inverse();  // (xT*x)^-1
		Matrix second = xMatrix.transpose().times(yMatrix); // xT*y
		Matrix B = first.times(second);  // (xT*x)^-1*xT*y

		double[][] bArray = B.getArray();  // convert B matrix back into a 2D array


		double x = 0.0; 
		a = bArray[0][0]; 
		b = bArray[1][0]; 
		c = bArray[2][0]; 
		d = bArray[3][0]; 

		System.out.println("A: " + a + " B: " + b +" C: " + c + " D: " + d);
		
		y = a+b*x+c*Math.pow(x, 2)+d*Math.pow(x, 3);

		while ((int)x <= getXTerrain()) {// Puts points in the array that will form the cubic line
			y = a+b*x+c*Math.pow(x, 2)+d*Math.pow(x, 3);
			if (((int)Math.round(y)+1 > getYTerrain()) || ((int)Math.round(y) < 1)) {
				generate();
				break;
			}
			if ( ((int)x < getXTerrain()) && ((int)x >= 0) && ((int)Math.round(y) >= 0)  && ((int)Math.round(y) < getYTerrain())) {

				terrain[(int) x][(int) Math.round(y)] = 1; 
			} 
			x++; 
		} 

		fill();// calls a method that fills in the points underneath the cubic
		createTanks(maxHuman);
	}

	
	protected void createTanks(int numberOfTanks) {
		drawable =  new ArrayList<Drawable2>();
		Clouds cloudOne = new Clouds(this, getXTerrain(), getYTerrain(), getXTerrain() - (getXTerrain() + 1));
		Clouds cloudTwo = new Clouds(this, getXTerrain(), getYTerrain(), getXTerrain() - 1);;
		if (maxHuman == 1) {
			manualTank tankOne = new manualTank(this, getXTerrain());
			tankOne.setPlayerNumber(1);
			drawable.add(tankOne);
			//CHANGE THIS FOR AI LATER
			maxPlayers = 1;
		}
		if (maxHuman == 2) {
			manualTank tankOne = new manualTank(this, getXTerrain());
			manualTank tankTwo = new manualTank(this, getXTerrain());
			tankOne.setPlayerNumber(1);
			tankTwo.setPlayerNumber(2);
			drawable.add(tankOne);
			drawable.add(tankTwo);
			maxPlayers = 2;
		}
		if (maxHuman == 3) {
			manualTank tankOne = new manualTank(this, getXTerrain());
			manualTank tankTwo = new manualTank(this, getXTerrain());
			manualTank tankThree = new manualTank(this, getXTerrain());
			tankOne.setPlayerNumber(1);
			tankTwo.setPlayerNumber(2);
			tankThree.setPlayerNumber(3);
			drawable.add(tankOne);
			drawable.add(tankTwo);
			drawable.add(tankThree);
			maxPlayers = 3;
		}


		setFocusTraversalKeysEnabled(false);
		addKeyListener(this);

		drawable.add(cloudOne);
		drawable.add(cloudTwo);
	}
	/**
	 * fills the space underneath the cubic
	 */
	protected void fill() {
		for(int i = 0; i < terrain.length; i += 1){
			y = a+b*i+c*Math.pow(i, 2)+d*Math.pow(i, 3);
			if(y < 1 || y > getYTerrain()){
				generate();
			}
		}

		int x = 0;
		Random random = new Random();
		while (x < getXTerrain() - 1) {
			for (int i = 0; i < getYTerrain() - 1;i++) {
				if (terrain[x][i] == 1) {
					for (int j = i; j < getYTerrain() - 1; j++) {
						if (random.nextInt(2 - 1 + 1) + 1 == 1){
							terrain[x][j] = 1;
						} else { 
							terrain[x][j] = 2;
						}
					}
					break;
				}
			}
			x++;
		}
	}


	/**
	 * Removes a circle from the boolean terrain array
	 *
	 * @param x X coordinate of the center of the hole
	 * @param y Y coordinate of the center of the hole
	 * @param mag magnitude of the hole 
	 *
	 */
	public void damage(int x, int y, int mag){
		
		//create the hole
		for(int i = y + mag; i > y - mag; i -= 1){
			int low = (int)(-Math.sqrt(Math.pow(mag, 2) - Math.pow(i - y, 2)) + x);// Finds the lower x coordinate for the given y coordinate
			int high = (int)(Math.sqrt(Math.pow(mag, 2) - Math.pow(i - y, 2)) + x);// Finds the upper x coordiante for the given y corrdinate
			for(int j = low; j < high; j += 1){// loops from the lower x to the upper x
				if(j >= 0 && j < xPanel && i >= 0 && i < yPanel){
					terrain[j][i] = 0;//sets points equal to false
				}
			}
		}

		//implement gravity
		for(int k = 0; k < yPanel; k += 1){
			for(int i = x - mag; i < x + mag; i += 1){
				for(int j = y + mag; j > 0; j -= 1){
					if(j + 1 < yPanel && j > 0 && i > 0 && i < xPanel){
						if (terrain[i][j] > 0 && !(terrain[i][j + 1] > 0)){
							terrain[i][j+1] = terrain[i][j];
							terrain[i][j] = 0;
							repaint();
						}
					}
				}
			}
		}
		
		//damage any tanks if necessary
				for(int k = 0; k < drawable.size(); k++){
					if(drawable.get(k) instanceof manualTank || drawable.get(k) instanceof AITank){
						if(Math.abs((drawable.get(k).getX() + 19) - getX()) <= 19){
							drawable.get(k).setHealth(drawable.get(k).getHealth() - 3);
							if(drawable.get(k).getHealth() <= 0){
								drawable.remove(k);
								k -= 1;
							}
						}
						else if(Math.abs((drawable.get(k).getX() + 19) - getX()) <= 39){
							drawable.get(k).setHealth(drawable.get(k).getHealth() - 1);
							if(drawable.get(k).getHealth() <= 0){
								drawable.remove(k);
								k -= 1;
							}
						}
					}
				}
		repaint();
	}//end of the remove method

	/**
	 * Calls the super paintComponent to paint on the JPanel
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

	}//end of paintComponent method
	
	/**
	 * nextPlayerTurn
	 */
	public void nextPlayerTurn() {
		if (currentPlayer + 1 > maxPlayers) {
			currentPlayer = 1;
		} else {
			currentPlayer = currentPlayer + 1;
		}
		
		//Change the status bar to the information
		//of the current player
		power.setText("" + currentTank().v0);
		
	}

}// End of abstract Terrain Class