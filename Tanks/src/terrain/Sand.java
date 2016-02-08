package terrain;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLabel;

import buttons.*;
import drawable.*;
import Jama.Matrix;
import net.miginfocom.swing.MigLayout;

/**
 * The sand terrain. This creates the random 2D terrain and coordinates all of the events and painting for the game to happen
 *
 * @author Joel Cherney
 *
 */
@SuppressWarnings("serial")
public class Sand extends Terrain implements KeyListener{




	/**
	 * Sand's constructor
	 *
	 * @param x The length of the panel
	 * @param y The height of the panel
	 */
	public Sand(int x, int y, int maxH) {
		super(x, y, maxH);
		setLayout(new MigLayout("", "["+ ((getXTerrain() - 500)/2) +"][29][29][29][26][26][26]["+ ((getXTerrain() - 500)/2) +"]", "[35][35]"));
		RightButton angleUp = new RightButton("", this);
		this.add(angleUp, "cell 4 0");
		LeftButton angleDown = new LeftButton("",this);
		this.add(angleDown, "cell 2 0");
		angle = new JLabel("0.0");
		this.add(angle, "cell 3 0");

		UpButton powerUp = new UpButton("", this);
		this.add(powerUp, "cell 5 0");
		DownButton powerDown = new DownButton("", this);
		this.add(powerDown, "cell 7 0");
		power = new JLabel("" + currentTank().v0);
		this.add(power, "cell 6 0");
	}// End of Sand constructor

	/**
	 * Generates a random 2D cubic terrain based off of Cubic Regression using JAMA
	 * All credit remains with JAMA for their hard work.
	 * Stress Level Zero would like to thank them for their hard work and providing JAMA for open use
	 * JAMA: http://math.nist.gov/javanumerics/jama/
	 */
	@Override
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

		drawable =  new ArrayList<drawable>();
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
	}//End of Generate

	/**
	 * Allows the drawable array to be set from outside this object. It's used primarily so weapons can be added from outside the object
	 */
	@Override
	public void setDrawable(ArrayList<drawable> drawable) {
		this.drawable = drawable;
	}//end of setDrawable method


	/**
	 *  Fills the area underneath the line
	 */
	@Override
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
	}// End of fill()

	/**
	 * Paint Component. This handles all of the painting for the game part of Tanks
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d=(Graphics2D)g;
		super.paintComponent(g);// prevents older objects from staying on the screen

		g2d.setColor(new Color(0x21a1cb));// The skies color
		g2d.fillRect(0, 0, getXTerrain(), getYTerrain());// fills the entire background with the sky


		Color tan = new Color(0xe3bb1d);
		Color darkTan = new Color(0xe7db8e);       

		for (int i = 0; i < getXTerrain() ; i++) {// draws the terrain from the boolean terrain array
			for (int j = 0; j < getYTerrain(); j++) {
				if (terrain[i][j] == 1) {
					g2d.setColor(tan);// The sand color
					g.drawRect(i, j, 1, 1);
				} else if (terrain[i][j] == 2) {
					g2d.setColor(darkTan);// The sand color
					g.drawRect(i, j, 1, 1);
				}


			}
		}

		AffineTransform old = g2d.getTransform();// Saves a copy of the old transform so the rotation can be reset later

		for (int i = 0; i < drawable.size(); i++) {// draws the clouds and tanks and eventually trees and whatever else needs to be drawn

			if (drawable.get(i) instanceof manualTank) {// draws player controlled tanks
				g2d.rotate(((manualTank)drawable.get(i)).angle(drawable.get(i).getX() + 20, terrain), drawable.get(i).getX(), findY(drawable.get(i).getX()));// this takes a radian. It has to be a very small radian
				g2d.drawImage(drawable.get(i).queryImage(), drawable.get(i).getX(), findY(drawable.get(i).getX()) - 18, null);

				//draws the barrel on the tank
				g2d.setColor(Color.BLACK);
				g2d.rotate(((manualTank)drawable.get(i)).barrelAngle(), drawable.get(i).getX() + 20, findY(drawable.get(i).getX()) - 15 );
				g2d.fillRect(drawable.get(i).getX(), findY(drawable.get(i).getX()) - 17, 20, 4);
			}

			if (drawable.get(i) instanceof standardShell) {// draws the missile
				g2d.fillOval(drawable.get(i).getX(), drawable.get(i).getY(), 5, 5);
			}

			g2d.setTransform(old);// resets the rotation back to how it was before the painting began

			if (drawable.get(i) instanceof Clouds) {// draws clouds
				g2d.drawImage(drawable.get(i).queryImage(), drawable.get(i).getX(), drawable.get(i).getY(), null);
			}

		}// End of loop to draw objects

		g2d.setColor(new Color(0xdfdfdf));
		g2d.fillRect(0, 0, getXTerrain(), 70);// draws the top menu bar

	}// End of Paint Component


	/**
	 * Returns the y coordinate of the top of the terrain from the boolean terrain array.
	 * @param x the x coordinate to check for the y coordinate
	 * @return returns the y coordinate of the terrain or -1 if one cannot be found
	 */
	@Override
	public int findY(int x) {
		if(x > 0 && x < xPanel){
			for(int i = 0; i < terrain[0].length; i += 1){
				if(terrain[x][i] > 0){
					return i;
				}
			}
		}
		return (int)(a + b * x + c * Math.pow(x, 2) + d * Math.pow(x, 3));
	}//end of findY method

	@Override
	public void keyPressed(KeyEvent e) {
		//draws all of the drawables in the drawable array
		for ( int i = 0; i < drawable.size(); i++) {
			if (drawable.get(i).playerNumber() == currentPlayer  && (drawable.get(i) instanceof manualTank || drawable.get(i) instanceof AITank)) {// PLAYER NUMBER HERE FOR THE EVENTUAL TURNS

				//moves the tank to the left
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					if (drawable.get(i).startX > 0 && drawable.get(i).getGas() > 0) {
						drawable.get(i).startX -= 5;
						drawable.get(i).setMove(drawable.get(i).getMove() + 5);
						if(drawable.get(i).getMove() % 30 == 0){
							drawable.get(i).setGas(drawable.get(i).getGas() - 1);
						}
					}
					repaint();
				}

				//moves the tank to the right
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					if (drawable.get(i).startX < drawable.get(i).maxX - 55 && drawable.get(i).getGas() > 0) {
						drawable.get(i).startX += 5;
						drawable.get(i).setMove(drawable.get(i).getMove() + 5);
						if(drawable.get(i).getMove() % 30 == 0){
							drawable.get(i).setGas(drawable.get(i).getGas() - 1);
						}
					}
					repaint();
				}

				//moves the turret in a clockwise direction
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					if (drawable.get(i).barrelAngle < Math.PI) {
						drawable.get(i).barrelAngle += 0.1;
						angle.setText(String.format("%2.1f", drawable.get(i).barrelAngle));
					}
					repaint();
				}

				//moves the turret in a counterclockwise direction
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					if (drawable.get(i).barrelAngle >= 0 ) {
						drawable.get(i).barrelAngle -= 0.1;
						angle.setText(String.format("%2.1f", drawable.get(i).barrelAngle));
					}
					repaint();
				}
				//fires a shell
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					currentPlayer = currentPlayer * -1;
					double theta = Math.PI - drawable.get(i).barrelAngle;					
					int barrelX = (int)(drawable.get(i).getX() + 20 + 20 * Math.cos(theta));
					int barrelY = (int)(findY(drawable.get(i).getX()) - 18 - 20 * Math.sin(theta));
					standardShell shell = new standardShell(this, terrain, getXTerrain(), getYTerrain(), barrelX, barrelY, drawable.get(i).barrelAngle + drawable.get(i).tankAngle+ Math.PI, drawable, drawable.get(i).v0);
					drawable.add(shell);
					setDrawable(drawable);
					if(Math.abs((drawable.get(i).getX() + 19) - shell.getX()) <= 19){
						drawable.get(i).setHealth(drawable.get(i).getHealth() - 3);
						System.out.println("1");
					}
					else if(Math.abs((drawable.get(i).getX() + 19) - shell.getX()) <= 39){
						drawable.get(i).setHealth(drawable.get(i).getHealth() - 1);
						System.out.println("2");
					}
					System.out.println(drawable.get(i).getHealth());
					repaint();
					break;
				}
			}			
		}
	}//end of keyPressed method


	@Override
	public void keyReleased(KeyEvent e) {
		// Unused

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Unused

	}

}