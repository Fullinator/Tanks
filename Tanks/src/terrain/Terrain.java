package terrain;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

import Jama.Matrix;
import Main.Ticker;
import buttons.DownButton;
import buttons.LeftButton;
import buttons.RightButton;
import buttons.UpButton;
import drawable.AITank;
import drawable.Clouds;
import drawable.drawable;
import drawable.manualTank;
import drawable.standardShell;
import net.miginfocom.swing.MigLayout;
import drawable.Drawable2;
import drawable.Tank;
import drawable.UserTank;


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
	protected java.util.List<Long> downKeys = new ArrayList<>();
	private boolean paintLock;
	protected boolean paused = false;
	protected JLabel pauseTitle;
	protected MigLayout normalLayout;
	protected MigLayout pauseLayout;
	RightButton angleUp;
	LeftButton angleDown;
	UpButton powerUp;
	DownButton powerDown;
	
	/**
	 *
	 * @param x width of JPanel
	 * @param y height of JPanel
	 */
	protected Terrain(int x, int y, int maxH, String[] names) {
		setXTerrain(x);
		setYTerrain(y);
		maxHuman = maxH;
		maxPlayers = maxHuman; //ADD THE AI PLAYERS TO THIS LATER
		pauseTitle = new JLabel("Game Paused");
		pauseTitle.setFont(new Font("Arial", Font.BOLD, 35));
		pauseTitle.setForeground(Color.white);
		generate();
		fill();// calls a method that fills in the points underneath the cubic
		createTanks(maxHuman, names);
		createClouds(2);
		createTopMenu();
		paintLock = false;
		Ticker.addMethod(this::render);
	}

	private void render(long elapsedNanos) {
		if (!paintLock) {
			paintLock = true;
			repaint();
			paintLock = false;
		}
	}

	public Tank currentTank() {
		return players.get(currentPlayer - 1);
		//		for ( int i = 0; i < drawable.size(); i++) {
		//			if (drawable.get(i).playerNumber() == currentPlayer) {
		//				return drawable.get(i);
		//			}
		//		}
		//		assert false; //Execution should never reach this point
		//		return null;
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

	/**
	 * Returns the y coordinate of the top of the terrain from the boolean terrain array.
	 * @param x the x coordinate to check for the y coordinate
	 * @return returns the y coordinate of the terrain or -1 if one cannot be found
	 */
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
	 * Generates a random 2D cubic terrain based off of Cubic Regression using JAMA
	 * All credit remains with JAMA for their hard work.
	 * Stress Level Zero would like to thank them for their hard work and providing JAMA for open use
	 * JAMA: http://math.nist.gov/javanumerics/jama/
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

		//System.out.println("A: " + a + " B: " + b +" C: " + c + " D: " + d);

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
	}//END OF GENERATE

	protected void createClouds(int numberOfClouds) {
		for (int i = 0; i < numberOfClouds; i++) {
			Clouds c = new Clouds(this, getXTerrain(), getYTerrain(), getXTerrain() - (getXTerrain() + 1));
			drawable.add(c);
		}
	}

	protected void createTanks(int numberOfTanks, String[] names) {
		players  = new ArrayList<Tank>();
		drawable = new ArrayList<Drawable2>();

		for (int i = 0; i < maxHuman; i++) {
			Tank t = new UserTank();
			t.setName(names[i]);
			drawable.add(t);
			players.add(t);
		}
		setFocusTraversalKeysEnabled(false);
		addKeyListener(this);
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
					players.get(k).setHealth(players.get(k).getHealth() - 3);
					if(players.get(k).getHealth() <= 0){
						drawable.remove(k);
						k -= 1;
					}
				}
				else if(Math.abs((drawable.get(k).getX() + 19) - getX()) <= 39){
					players.get(k).setHealth(players.get(k).getHealth() - 1);
					if(players.get(k).getHealth() <= 0){
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
	 * This also handles all standard terrain drawing and drawables drawing.
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2d=(Graphics2D)g;
		super.paintComponent(g);// prevents older objects from staying on the screen

		g2d.setColor(new Color(0x21a1cb));// The skies color
		g2d.fillRect(0, 0, getXTerrain(), getYTerrain());// fills the entire background with the sky       

		for (int i = 0; i < getXTerrain() ; i++) {// draws the terrain from the boolean terrain array
			for (int j = 0; j < getYTerrain(); j++) {
				if (terrain[i][j] == 1) {
					g2d.setColor(primary);// The sand color
					g.drawRect(i, j, 1, 1);
				} else if (terrain[i][j] == 2) {
					g2d.setColor(secondary);// The sand color
					g.drawRect(i, j, 1, 1);
				}


			}
		}

		AffineTransform old = g2d.getTransform();// Saves a copy of the old transform so the rotation can be reset later

		for (int i = 0; i < drawable.size(); i++) {// draws the clouds and tanks and eventually trees and whatever else needs to be drawn

			if (drawable.get(i) instanceof Tank) {// draws player controlled tanks
				g2d.rotate(((Tank)drawable.get(i)).angle(drawable.get(i).getX() + 20, terrain), drawable.get(i).getX(), findY(drawable.get(i).getX()));// this takes a radian. It has to be a very small radian
				g2d.drawImage(drawable.get(i).queryImage(), drawable.get(i).getX(), findY(drawable.get(i).getX()) - 18, null);

				//draws the barrel on the tank
				g2d.setColor(Color.BLACK);
				g2d.rotate(((Tank)drawable.get(i)).getBarrelAngle(), drawable.get(i).getX() + 20, findY(drawable.get(i).getX()) - 15 );
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

		if (paused) {
			g2d.setColor(new Color(0x21a1cb));// The skies color
			g2d.fillRect(0, 0, getXTerrain(), 70);
			g2d.setColor(new Color(0,0,0,160));
			g2d.fillRect(0, 0, xPanel, yPanel);
		}
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
		power.setText("" + currentTank().getLaunchPower());
		playerName.setText(currentTank().getName());
	}

	protected void createTopMenu() {
		normalLayout = new MigLayout("", "["+ ((getXTerrain() - 600)/2) +"][26][26][26][26][26][26]["+ ((getXTerrain() - 500)/2) +"]", "[35][35][150][][][]");
		setLayout(normalLayout);

		//Player Name
		playerName = new JLabel();
		playerName.setText(currentTank().getName());
		playerName.setFont(new Font("Arial", Font.PLAIN, 35));
		add(playerName, "cell 0 0");

		//Barrel Angle Up
		angleUp = new RightButton("", this);
		add(angleUp, "cell 4 0");
		//Barrel Angle Down
		angleDown = new LeftButton("",this);
		add(angleDown, "cell 2 0");
		//Angle Label
		angle = new JLabel("0.0");
		add(angle, "cell 3 0");

		//Power Up
		powerUp = new UpButton("", this);
		add(powerUp, "cell 5 0");
		//Power Down
		powerDown = new DownButton("", this);
		add(powerDown, "cell 7 0");
		//Power Label
		power = new JLabel("" + currentTank().getLaunchPower());
		add(power, "cell 6 0");
		
		//Health Label
		
		//Fire Button

	}

	protected void hideTopMenu() {
		remove(playerName);
		remove(angleUp);
		remove(angleDown);
		remove(angle);
		remove(powerUp);
		remove(powerDown);
		remove(power);
	}
	
	protected void showTopMenu() {
		add(playerName, "cell 0 0");
		this.add(angleUp, "cell 4 0");
		add(angleDown, "cell 2 0");
		add(angle, "cell 3 0");
		add(powerUp, "cell 5 0");
		add(powerDown, "cell 7 0");
		add(power, "cell 6 0");
	}
	
	/**
	 * @ getGameStatus
	 * @params none
	 * @return returns true if the game is paused and false if not
	 * 
	 */
	public boolean getGameStatus() {
		return paused;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (downKeys.contains((long) e.getKeyCode())) return;
		else downKeys.add((long) e.getKeyCode());

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (paused) {
				paused = false;
				remove(pauseTitle);
				setLayout(normalLayout);
				showTopMenu();
				revalidate();
			} else {
				paused = true;
				pauseLayout = new MigLayout("", "["+ ((getXTerrain() - 500)/2) +"][29][29][26][26][26]["+ ((getXTerrain() - 500)/2) +"]", "[35][35][150][][][][][][]");
				setLayout(pauseLayout);
				hideTopMenu();
				add(pauseTitle,"cell 2 4, alignx center");
				revalidate();	
			}
		}
		if (!paused) {
			//draws all of the drawables in the drawable array
			Tank t = players.get(currentPlayer - 1);

			// move left
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				t.startMotion(true);
			}

			// move right
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				t.startMotion(false);
			}

			// rotate cannon counter clockwise
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				t.aimCannon(true);
			}

			// rotate cannon clockwise
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				t.aimCannon(false);
			}

			// fire projectile
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				System.out.println("fire");
				Main.Main.sound.loadSound("sounds/Shot1.wav");
				Main.Main.sound.run();

				nextPlayerTurn();
			}
		}
		/*for ( int i = 0; i < players.size(); i++) {
			if (i == currentPlayer && (drawable.get(i) instanceof manualTank || drawable.get(i) instanceof AITank)) {// PLAYER NUMBER HERE FOR THE EVENTUAL TURNS
				//fires a shell
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					double theta = Math.PI - drawable.get(i).barrelAngle;				
					sound.loadSound("sounds/Shot1.wav");
					sound.run();
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

					nextPlayerTurn();
					repaint();
					break;
				}
			}
		}*/
	}//end of keyPressed method


	@Override
	public void keyReleased(KeyEvent e) {
		downKeys.remove(((long) e.getKeyCode()));

		Tank t = players.get(currentPlayer - 1);

		switch (e.getKeyCode()) {
		// Stop moving tank when motion key released
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_RIGHT:
			t.stopMotion();
			break;

			// Stop adjusting cannon angle when aim key released
		case KeyEvent.VK_UP:
		case KeyEvent.VK_DOWN:
			t.stopAimCannon();
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}


}// End of abstract Terrain Class
