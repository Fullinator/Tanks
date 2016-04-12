package terrain;

import Jama.Matrix;
import Main.Main;
import Main.Ticker;
import buttons.*;
import drawable.*;
import net.miginfocom.swing.MigLayout;
import physics.Projectile;
import physics.RiskTaker;
import physics.TerrainDestroyer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.function.LongConsumer;

@SuppressWarnings("serial")
public abstract class Terrain extends JPanel implements KeyListener{
	protected int[][] terrain; // This will hold all of the points that will be painted
	protected int xLength = 0;// This will be set to the JPanels width
	private int yLength = 0;// This will be set to the JPanels height
	private int maxPlayers;
	private int currentPlayer = 1;
	protected double y;
	private double a;
	private double b;
	private double c;
	private double d;
	public JLabel angle;
	public JLabel power;
	private JLabel playerName;
	Color primary;
	Color secondary;
	protected ArrayList<Drawable> drawable;
	private ArrayList<Tank> players;
	private java.util.List<Long> downKeys = new ArrayList<>();
	private boolean paintLock;
	private boolean paused = false;
	private boolean allowHumanInput = true;
	private JLabel pauseTitle;
	private MigLayout normalLayout;
	private MigLayout pauseLayout;
	private RightButton angleUp;
	private LeftButton angleDown;
	private UpButton powerUp;
	private DownButton powerDown;
	private JButton quit;
	private JButton unPause;
	private FireButton fire;
	private JLabel fuelLabel;
	private boolean tabbed = false;
	private Projectile projectile;
	private List<Projectile> projectiles;
	private int nightShiftAmount;
	private boolean nightShift;
	private BufferedImage currentTerrainImage;
	private boolean staleTerrainImage;
	private JComboBox<String> weapons;



	/**
	 *	Terrain's constructor
	 *
	 * @param x width of JPanel
	 * @param y height of JPanel
	 */
	protected Terrain(int x, int y, int numHuman, int numAI, String[] names) {
		xLength = x;

		yLength = y;
		maxPlayers = numHuman + numAI;
		generate();
		fill();// calls a method that fills in the points underneath the cubic
		createTanks(numHuman, numAI, names);
		createClouds(2);
		createTopMenu();
		paintLock = false;
		Ticker.addMethod(this::render);
		drawable.add(new DayCycle(xLength,yLength));
		projectiles = new ArrayList<>();

		staleTerrainImage = true;
		//		screenMove();
	}


	/**
	 * Creates the unique element on the terrain
	 * 
	 * @param amount number of objects to create
	 */
	protected abstract void createTerrainSpecificItems(int amount);

	private void render(long elapsedNanos) {
		if (!paintLock) {
			paintLock = true;
			repaint();
			paintLock = false;
		}
	}

	/**
	 * returns the tank who's turn it currently it
	 * 
	 * @return The current tank who is taking its' turn
	 */
	public Tank currentTank() {
		return players.get(currentPlayer - 1);
	}

	/**
	 * used to get the array of points used to draw the terrain
	 * 
	 * @return boolean array of points
	 */
	public int[][] getTerrain(){
		return terrain;
	}

	/**
	 * Sets the drawable list equal to the list sent as a parameter
	 * 
	 * @param drawable ArrayList of drawable objects for the paint class to draw
	 */
	public void setDrawable(ArrayList<Drawable> drawable) {
		this.drawable = drawable;
	}

	/**
	 * Returns the y coordinate of the top of the terrain from the boolean terrain array.
	 * 
	 * @param x the x coordinate to check for the y coordinate
	 * @return returns the y coordinate of the terrain or -1 if one cannot be found
	 */
	public int findY(int x){
		if(x > 0 && x < xLength){//find Y position from damage
			for(int i = 0; i < terrain[0].length; i += 1){
				if(terrain[x][i] > 0){
					return i;
				}
			}
		}
		//return (int)(a + b * x + c * Math.pow(x, 2) + d * Math.pow(x, 3));	
		return yLength - 20;
	}


	/**
	 * Returns the length of the JPanel
	 *
	 * @return the JPanels width
	 */
	public int getXTerrain() {
		return xLength;
	}

	/**
	 * Returns the height of the JPanel
	 *
	 * @return the JPanels height
	 */
	public int getYTerrain() {
		return yLength;
	}

	/**
	 * Generates a random 2D cubic terrain based off of Cubic Regression using the JAMA library
	 * JAMA: http://math.nist.gov/javanumerics/jama/
	 */
	private void generate() {

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
			if (temp < (getYTerrain() - 150) && temp > 300) {
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

	/**
	 * Creates cloud objects and adds them to the list of drawables
	 * 
	 * @param numberOfClouds number of clouds to create
	 */
	private void createClouds(int numberOfClouds) {
		for (int i = 0; i < numberOfClouds; i++) {
			Clouds c = new Clouds(this, getXTerrain(), getYTerrain());
			drawable.add(c);
		}
	}

	/**
	 * Creates tank objects and adds them to the list of players and drawables
	 * 
	 * @param numHumans number of human players
	 * @param numAI number of AI players
	 * @param names names of the tanks 
	 */
	private void createTanks(int numHumans, int numAI, String[] names) {
		players  = new ArrayList<>();
		drawable = new ArrayList<>();
		Color[] c = new Color[] {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.ORANGE};
		for (int i = 0; i < numHumans; i++) {
			Tank t = new UserTank(c[i]);
			t.setLocation(new Point((int) (Math.random() * (getXTerrain() - 100) + 50), 0));
			t.setName(names[i]);
			drawable.add(t);
			players.add(t);
		}
		List<String> aiNames = new ArrayList<>();
		Scanner s;
		try {
			s = new Scanner(new File("names.txt"));
			while (s.hasNext()) aiNames.add(s.next());
		} catch (IOException e) {
			s = null;
		}
		Random r = new Random();
		for (int i = 0; i < numAI; i++) {
			Tank t = new AITank(this, players);
			if (aiNames.size() > 0) t.setName(aiNames.get(r.nextInt(aiNames.size())));
			else t.setName("AI " + (i + 1));
			t.setLocation(new Point((int) (Math.random() * (getXTerrain() - 100) + 50), 0));
			drawable.add(t);
			players.add(t);
		}
		if (s != null) s.close();
		setFocusTraversalKeysEnabled(false);
		addKeyListener(this);

	}

	/**
	 * fills the space underneath the cubic
	 */
	private void fill() {
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
	 * detects whether the projectile collides with a player or the terrain and then calls the damage method
	 * 
	 * @param shot a Projectile object that contain the information of the standard shot
	 */
	public void collisionDetection(Projectile shot) {
		boolean tankDestroyed = false;
		int radius = 15;//radius around tank in pixels to check collision
		boolean tankHit = false;
		//check against all tanks
		for (Tank t : players) {
			//We need to skip the outbound shot on the current tank

			//find center of tank
			double angle = t.angle(t.getX() + 20 , terrain);
			int length = (int) (t.queryImage().getWidth() * Math.cos(angle));
			Point center = new Point(t.getX() + (length/2), findY(t.getX() + (length/2)) - (t.queryImage().getHeight() /2) );

			//check if our shot is within a hit of tank
			if (shot.getX() >= center.getX() - radius && shot.getX() <= center.getX() + radius) {//check X
				if (shot.getY() >= center.getY() - radius && shot.getY() <= center.getY() + radius) {//check Y
					//then we have a hit

					//call for damage
					projectiles.remove(shot);
					Ticker.removeMethod(shot.getTickerID());
					tankHit = true;
					Main.sound.run("impact");   //Impact sound

					//Explode animation
					Animation ani = new Animation("explode");
					Point a = new Point(shot.getX()-64, shot.getY()+64);
					ani.setLocation(a);
					drawable.add(ani);

					//pause();
				}
			}
		}
		if (tankHit) {
			tankDestroyed = damage(shot, true, radius) == currentPlayer;
		}

		boolean terrainHit = false;

		//check against terrain
		if (shot.getX() > xLength || shot.getX() < 0 || shot.getY() > yLength) {
			projectiles.remove(shot);
			Ticker.removeMethod(shot.getTickerID());
			terrainHit = true;
		} else if (shot.getY() >= 0 && terrain[shot.getX()][shot.getY()] > 0) {
			projectiles.remove(shot);
			Ticker.removeMethod(shot.getTickerID());
			//call for damage
			Main.sound.run("impact");   //Impact sound
			Animation ani = new Animation("explode"); 
			Point p = new Point(shot.getX()-64, shot.getY()+64);
			ani.setLocation(p);
			drawable.add(ani);

			tankDestroyed = damage(shot, false, radius) == currentPlayer;
			terrainHit = true;
		}


		if (tankHit || terrainHit) {
			allowHumanInput = true;
			nextPlayerTurn(!tankDestroyed);
		}

	}

	/**
	 * damges the terrain and tanks
	 * @param shot the projectile that is colliding
	 * @param tank a boolean that's true if it's colliding with a tank and false if not
	 * @param tankCollisionRadius the radius that a tank will collide within
	 */
	private int damage(Projectile shot, boolean tank, int tankCollisionRadius) {
		//check which shot it is so we can tell what kind of damage
		//For now we only have one kind
		int remTank = -1;

		if (!tank && shot instanceof RiskTaker) {//checks to see if the risk of the RiskTaker projectile was mitigtated
			currentTank().setHealth((currentTank().getHealth() - ((RiskTaker)shot).riskDamage));
			if (currentTank().getHealth() <= 0) {
				Tank toRemove = currentTank();
				remTank = currentPlayer;
				maxPlayers = maxPlayers - 1;
				if (currentPlayer > maxPlayers) currentPlayer = maxPlayers;
				players.remove(toRemove);
				drawable.remove(toRemove);
				checkEndOfGame();
			}
		}

		if (tank) {//damage tank first
			for (Tank t : players) {
				//check if our shot is within a hit of tank
				//Point center = new Point(t.getX() + 20, findY(t.getX() + 10) - (t.queryImage().getHeight()) );
				double angle = t.angle(t.getX() + 20 , terrain);
				int length = (int) (t.queryImage().getWidth() * Math.cos(angle));
				Point center = new Point(t.getX() + (length/2), findY(t.getX() + (length/2)) - (t.queryImage().getHeight() /2) );
				if (shot.getX() >= center.getX() - tankCollisionRadius && shot.getX() <= center.getX() + tankCollisionRadius) {//check X
					if (shot.getY() >= center.getY() - tankCollisionRadius && shot.getY() <= center.getY() + tankCollisionRadius) {//check Y
						t.setHealth(t.getHealth() - shot.damage);
						if (t.getHealth() <= 0) {
							remTank = players.indexOf(t) + 1;
							maxPlayers = maxPlayers - 1;
							if (currentPlayer > maxPlayers) currentPlayer = maxPlayers;
							players.remove(t);
							drawable.remove(t);
							checkEndOfGame();
							damage(shot,true,tankCollisionRadius);
							return remTank;
						}
					}
				}
			}
		} else {//damage terrain
//			int mag = shot.terrainMag;
			int mag = shot.terrainMag + (shot instanceof TerrainDestroyer ? 20 : 10);
			int x = shot.getX();
//			int y = findY(x);
			int y = findY(x) - (shot instanceof TerrainDestroyer ? 30 : 10);
			for(int i = y + mag; i > y - mag; i -= 1){
				int low = (int)(-Math.sqrt(Math.pow(mag, 2) - Math.pow(i - y, 2)) + x);// Finds the lower x coordinate for the given y coordinate
				int high = (int)(Math.sqrt(Math.pow(mag, 2) - Math.pow(i - y, 2)) + x);// Finds the upper x coordiante for the given y corrdinate
				for(int j = low; j < high; j += 1){// loops from the lower x to the upper x
					if(j >= 0 && j < xLength && i >= 0 && i < yLength){
						terrain[j][i] = 0;//sets points equal to false
					}
				}
			}
			terrarinElementRemove(shot);
			staleTerrainImage = true;

			//implement gravity
			//for (int i = x - (mag + shot.terrainMag + 50); i <= x + (mag + shot.terrainMag + 50); i++) {
			for (int i = 0; i <xLength; i++) {
			if (i < 0 || i >= xLength) {
					continue;
				}
				for (int j = 0; j < (yLength - 1); j++) {

					if (terrain[i][j] > 0 && !(terrain[i][j + 1] > 0)){
						terrain[i][j+1] = terrain[i][j];
						terrain[i][j] = 0;
						staleTerrainImage = true;
						j = 0;
					}
				}
			}
			staleTerrainImage = true;
		}
		return remTank;
	}

	/**
	 * removes a terrain specific element from the map. This might be a cactus or snow man
	 * @param shot the shot that is colliding with the terrain
	 */
	private void terrarinElementRemove(Projectile shot) {
		for (Drawable object: drawable) {
			if ( (object instanceof Cactus  || object instanceof Snowman)  && ((object.getX() + object.queryImage().getWidth()/2) >= (shot.getX() - shot.terrainMag) && (object.getX() + object.queryImage().getWidth()/2) <= (shot.getX() + shot.terrainMag))) {
				drawable.remove(object);
				terrarinElementRemove(shot);
				return;
			}
		}
	}

	/**
	 * checks whether the game has finished. IE if there is only one player left
	 */
	private void checkEndOfGame() {
		if (maxPlayers == 1) {
			playerWin();
		}
	}

	/**
	 * Calls the super paintComponent to paint on the JPanel
	 * This also handles all standard terrain drawing and drawables drawing.
	 */

	private Shift shift1 = new Shift(Projectile.points);
	private int shift = 0;
	public void paintComponent(Graphics g) {
		ArrayList<Projectile> projectiles = new ArrayList<>(this.projectiles.size());
		projectiles.addAll(this.projectiles);
		ArrayList<Drawable> drawable = new ArrayList<>(this.drawable.size());
		drawable.addAll(this.drawable);

		//int shift = 0;
		shift =shift1.shifter(Projectile.points);

		Graphics2D g2d=(Graphics2D)g;
		super.paintComponent(g);// prevents older objects from staying on the screen
		GradientPaint gp;
		if(shift > 400){
			gp = new GradientPaint(0,0,Color.BLACK,0,getYTerrain()-50 ,new Color(0x21a1cb));
		}
		else{
			gp = new GradientPaint(0,0,new Color(0x21a1cb),0,getYTerrain()-50 ,new Color(0xAFEEEE));
		}
		g2d.setPaint(gp);// The skies color
		g2d.fillRect(0, 0, getXTerrain(), getYTerrain());// fills the entire background with the sky       
		AffineTransform old = g2d.getTransform();// Saves a copy of the old transform so the rotation can be reset later

		for (int i = 0; i < drawable.size(); i++) {
			if (drawable.get(i) instanceof DayCycle) {//Make sure to draw the sun/moon first.
				g2d.drawImage(drawable.get(i).queryImage(), drawable.get(i).getX(), drawable.get(i).getY() - drawable.get(i).queryImage().getHeight()+shift, null);
				nightShiftAmount = ((DayCycle) drawable.get(i)).shiftNightAmount();
				nightShift = ((DayCycle) drawable.get(i)).shiftNight();
			}
		}

		for (int i = 0; i < drawable.size(); i++) {// draws the clouds and tanks and eventually trees and whatever else needs to be drawn
			if (drawable.get(i) instanceof Clouds) {// draws clouds
				g2d.drawImage(drawable.get(i).queryImage(), drawable.get(i).getX(), drawable.get(i).getY()+shift, null);
			} else {
				g2d.drawImage(drawable.get(i).queryImage(), drawable.get(i).getX(), drawable.get(i).getY() - drawable.get(i).queryImage().getHeight()+shift, null);
			}

		}// End of loop to draw objects

		drawable.forEach(c -> {
			if (c instanceof Tank) {
				g2d.rotate(((Tank)c).angle((int) (c.getX() + c.queryImage().getWidth() *.5), terrain), c.getX(), findY(c.getX()) - c.queryImage().getHeight());// this takes a radian. It has to be a very small radian
				g2d.drawImage(c.queryImage(), c.getX(), findY(c.getX()) - c.queryImage().getHeight() +shift, null);

				//draws the barrel on the tank

				g2d.setColor(((Tank) c).getBarrelColor());
				g2d.rotate(((Tank)c).getBarrelAngle(), c.getX() + c.queryImage().getWidth() *.5, findY(c.getX()) - c.queryImage().getHeight() +2+shift );
				g2d.fillRect(c.getX(), findY(c.getX()) - c.queryImage().getHeight() +shift , (int) (c.queryImage().getWidth()*.5) , 4);
				g2d.setTransform(old);// resets the rotation back to how it was before the painting began
				//g2d.drawOval((int)center.getX(), (int)center.getY(), 35, 35);
			}
		});

		drawable.forEach(a -> {
			if (a instanceof Animation) g2d.drawImage(a.queryImage(), a.getX(), a.getY() - a.queryImage().getHeight()+shift, null);
		});

		if (staleTerrainImage) {
			currentTerrainImage = new BufferedImage(xLength, yLength, BufferedImage.TYPE_INT_ARGB);
			Graphics2D terrainGraphics = currentTerrainImage.createGraphics();
			terrainGraphics.setColor(new Color(0, 0, 0, 0));
			terrainGraphics.fillRect(0, 0, xLength, yLength);
			for (int i = 0; i < getXTerrain(); i++) {// draws the terrain from the boolean terrain array
				for (int j = 0; j < getYTerrain(); j++) {
					if (terrain[i][j] == 1) {
						terrainGraphics.setColor(primary);// The sand color
						terrainGraphics.drawRect(i, j, 1, 1);
					} else if (terrain[i][j] == 2) {
						terrainGraphics.setColor(secondary);// The sand color
						terrainGraphics.drawRect(i, j, 1, 1);
					}


				}
			}
			staleTerrainImage = false;
		}
		g2d.drawImage(currentTerrainImage, 0, shift, null);

		//Draw the projectile in the list
		projectiles.forEach(p -> g2d.drawImage(p.queryImage(), p.getX(), p.getY(), null));

		fuelLabel.setText("Fuel: " + (int) currentTank().getGas());

		//draw night shift

		if (nightShift) {
			g2d.setColor(new Color(66,98,255,nightShiftAmount));
			g2d.fillRect(0, 0, xLength, yLength);
		}



		if (tabbed) {
			//g2d.setColor(new Color(0x21a1cb));// The skies color
			//g2d.fillRect(0, 0, getXTerrain(), 60);
			if (nightShift) {
				g2d.setColor(new Color(66,98,255,nightShiftAmount));
				g2d.fillRect(0, 0, getXTerrain(), 60);
			}
			g2d.setColor(new Color(0,0,0,180));
			g2d.fillRect(0, 0, xLength, yLength);
		} else {
			g2d.setColor(new Color(0xdfdfdf));
			g2d.fillRect(0, 0, getXTerrain(), 60);// draws the top menu bar
		}

		if (paused) {
			g2d.setColor(new Color(0x21a1cb));// The skies color
			g2d.fillRect(0, 0, getXTerrain(), 60);
			if (nightShift) {
				g2d.setColor(new Color(66,98,255,nightShiftAmount));
				g2d.fillRect(0, 0, getXTerrain(), 60);
			}
			g2d.setColor(new Color(0,0,0,180));
			g2d.fillRect(0, 0, xLength, yLength);
		}
	}//end of paintComponent method

	private boolean lockNextPlayerTurnCalls = false;
	
	/**
	 * Sets the turn to the next player
	 */
	public void nextPlayerTurn(boolean advancePlayer) {
		if (lockNextPlayerTurnCalls) return;
		lockNextPlayerTurnCalls = true;
		try { Thread.sleep(100); } catch (InterruptedException ignored) {}
		lockNextPlayerTurnCalls = false;
		currentTank().completeFirstTurn();
		if (advancePlayer) {
			if (currentPlayer + 1 > maxPlayers) {
				currentPlayer = 1;
			} else {
				currentPlayer = currentPlayer + 1;
			}
		}

		currentTank().setLaunchPower(Math.min(currentTank().getHealth() / 2, currentTank().getLaunchPower()));

		//Change the status bar to the information
		//of the current player
		power.setText("" + currentTank().getLaunchPower());
		playerName.setText(currentTank().getName());
		playerName.setForeground(currentTank().getBarrelColor());

		weapons.setSelectedItem(currentTank().getProjectileType());

		if (currentTank() instanceof AITank) {
			allowHumanInput = false;
			((AITank) currentTank()).takeTurn();
		} else {
			allowHumanInput = true;
		}

		if (currentTank().isFirstTurnTaken())
			currentTank().setGas(currentTank().getGas() + Math.max(10, currentTank().getHealth() / 2));
	}

	/**
	 * Creates the top menu of controls 
	 */
	private void createTopMenu() {
		normalLayout = new MigLayout("aligny -7px", "[150][40][20][40][30][20][30][100][80][60][60][60][60]", "[60]"  + (yLength - 70) + "");
		setLayout(normalLayout);

		//Player Name
		playerName = new JLabel();
		playerName.setText(currentTank().getName());
		playerName.setFont(new Font("Arial", Font.PLAIN, 35));
		playerName.setForeground(currentTank().getBarrelColor());
		add(playerName, "cell 0 0");

		//Barrel Angle Up
		angleUp = new RightButton("", this);
		add(angleUp, "cell 3 0, alignx center");
		//Barrel Angle Down
		angleDown = new LeftButton("",this);
		add(angleDown, "cell 1 0, alignx center");
		//Angle Label
		angle = new JLabel("0.0");
		add(angle, "cell 2 0, alignx center");

		//Power Up
		powerUp = new UpButton("", this);
		add(powerUp, "cell 4 0, alignx center");
		//Power Down
		powerDown = new DownButton("", this);
		add(powerDown, "cell 6 0, alignx center");
		//Power Label
		power = new JLabel("" + currentTank().getLaunchPower());
		add(power, "cell 5 0, alignx center");

		//Weapon selection
		weapons = new JComboBox<>();
		weapons.addItem("Standard Shot");
		weapons.addActionListener(e -> {
			if (allowHumanInput) currentTank().setProjectileType((String) weapons.getSelectedItem());
			this.requestFocus();
		});
		weapons.addItem("Terrain Destroyer");
		weapons.addItem("Risk Taker");

		add(weapons, "cell 8 0, alignx center");

		//Fire Button
		fire = new FireButton("", this);
		add(fire, "cell 7 0, alignx center");
		//Health Label

		//Buy weapons

		// Fuel label
		fuelLabel = new JLabel("Fuel: " + (int)currentTank().getGas());
		add(fuelLabel, "cell 10 0, alignx center");
	}

	private boolean allowFire = true;
	private int fireCooldownId;

	/**
	 * Stops tank movement and creates the requested shot from current parameters and then changes the turn
	 */
	public void fire() {
		if (!allowFire) return;
		allowFire = false;
		fireCooldownId = Ticker.addMethod(new LongConsumer() {
			long cumulative = 0;
			@Override
			public void accept(long value) {
				cumulative += value;
				if (cumulative > 1000000000) {
					allowFire = true;
					Ticker.removeMethod(fireCooldownId);
					fireCooldownId = -1;
				}
			}
		});
		Tank tank = players.get(currentPlayer - 1);
		tank.stopAimCannon();
		tank.stopMotion();
		allowHumanInput = false;

		//		String weapon = (String) weapons.getSelectedItem();
		String weapon = currentTank().getProjectileType();

		switch (weapon) {
		case "Standard Shot": 
			projectile = new Projectile(currentTank(), this);
			projectiles.add(projectile);
			break;

		case "Terrain Destroyer":
			projectile = new TerrainDestroyer(currentTank(), this);
			projectiles.add(projectile);
			break;

		case "Risk Taker":
			projectile = new RiskTaker(currentTank(), this);
			projectiles.add(projectile);
			break;
		}




		projectile.setTickerID(Ticker.addMethod(projectile::fire));

		Main.sound.run("shot1");

		Animation ani = new Animation("smoke");
		Point t = new Point((int)projectile.intX-16, (int)projectile.intY+12);
		ani.setLocation(t);
		drawable.add(ani);
	}

	/**
	 * Hides the top menu of player controls
	 */
	private void hideTopMenu() {
		remove(playerName);
		remove(angleUp);
		remove(angleDown);
		remove(angle);
		remove(powerUp);
		remove(powerDown);
		remove(power);
		remove(weapons);
		remove(fuelLabel);
		removeAll();

	}

	/**
	 * Shows the top menu of player controls
	 */
	private void showTopMenu() {
		add(playerName, "cell 0 0");
		add(angleUp, "cell 3 0, alignx center");
		add(angleDown, "cell 1 0, alignx center");
		add(angle, "cell 2 0, alignx center");
		add(powerUp, "cell 4 0, alignx center");
		add(powerDown, "cell 6 0, alignx center");
		add(power, "cell 5 0, alignx center");
		add(fire, "cell 7 0, alignx center");
		add(weapons, "cell 8 0, alignx center");
		add(fuelLabel, "cell 10 0, alignx center");
		revalidate();
		//add(power, "cell 5 0, alignx center");
	}

	/**
	 * Returns whether the game is paused or not
	 * 
	 * @return returns true if the game is paused and false if not
	 * 
	 */
	public boolean getGameStatus() {
		return paused;
	}

	/**
	 * Creates all needed objects to show player stats on the "tab" menu
	 */
	private void showPlayerStats() {
		tabbed = true;
		hideTopMenu();
		removeAll();
		pauseLayout = new MigLayout("", "[300][][][][][]", "[150][][]");
		setLayout(pauseLayout);
		for (int i = 0; i < players.size(); i ++) {
			JLabel name = new JLabel(players.get(i).getName());
			name.setFont(new Font("Arial", Font.CENTER_BASELINE, 30));
			name.setForeground(Color.WHITE);
			add(name, "cell 1 " + i + 1);

			UIManager.put("ProgressBar.selectionForeground",Color.WHITE);  //colour of precentage counter on red background
			JProgressBar health = new JProgressBar(0,100);
			health.setForeground(Color.orange);
			health.setBackground(Color.green);
			health.setValue( players.get(i).getHealth());
			health.setStringPainted(true);
			add(health , "cell 3 " + i + 1);
		}
		revalidate();
	}

	/**
	 * Hides all objects from the player stats menu
	 */
	private void hidePlayerStats() {
		tabbed = false;
		if (!getGameStatus()) {//Make sure we don't bring back the top control menu 
			//While the game is paused
			setLayout(normalLayout);
			removeAll();
			showTopMenu();
			revalidate();
		}
	}

	/**
	 * Ends the game and shows the end game screen
	 */
	private void playerWin() {
		hidePlayerStats();
		paused = true;
		hideTopMenu();
		tabbed = false;
		pauseLayout = new MigLayout("", "["+ ((getXTerrain() - 500)/2) +"][29][29][26][26][26]["+ ((getXTerrain() - 500)/2) +"]", "[150][35][40][][][][][][]");
		setLayout(pauseLayout);
		quit = new JButton("Quit to Menu");
		quit.addActionListener(e -> {
			Main.sound.runLoop("song");
			Main.loadMenu();
			Main.setTickerPause(true);
		});
		pauseTitle = new JLabel(players.get(0).getName() + " Wins!");
		pauseTitle.setFont(new Font("Arial", Font.BOLD, 35));
		pauseTitle.setForeground(Color.white);
		add(pauseTitle,"cell 5 2, alignx center");
		add(quit, "cell 5 4, alignx center");
		revalidate();
		Main.setTickerPause(true);
	}

	/**
	 * Creates all needed  objects to pause the game and temporarily stops the ticker
	 */
	private void pause() {
		hidePlayerStats();
		Main.setTickerPause(true);
		paused = true;
		pauseLayout = new MigLayout("", "["+ ((getXTerrain() - 500)/2) +"][29][29][26][26][26]["+ ((getXTerrain() - 500)/2) +"]", "[150][35][40][][][][][][]");
		setLayout(pauseLayout);
		hideTopMenu();
		pauseTitle = new JLabel("Game Paused");
		pauseTitle.setFont(new Font("Arial", Font.BOLD, 35));
		pauseTitle.setForeground(Color.white);
		quit = new JButton("Quit to Menu");
		quit.addActionListener(e -> {
			Main.sound.runLoop("song");
			Main.loadMenu();
			Main.setTickerPause(true);
		});
		unPause = new JButton("UnPause");
		unPause.addActionListener(e -> unPause());
		add(unPause, "cell 5 4, alignx center");
		add(quit, "cell 5 5, alignx center");
		add(pauseTitle,"cell 5 2, alignx center");
		tabbed = false;//Makes sure the tab and pause menus don't overlay
		revalidate();
	}

	/**
	 * Removes all objects to pause the game and restarts the ticker
	 */
	private void unPause() {
		Main.setTickerPause(false);
		paused = false;
		remove(pauseTitle);
		remove(unPause);
		remove(quit);
		setLayout(normalLayout);
		showTopMenu();
		revalidate();
		requestFocusInWindow();
	}

	/**
	 * is called when a key is pressed
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (downKeys.contains((long) e.getKeyCode())) return;
		else downKeys.add((long) e.getKeyCode());

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (paused) {
				unPause();
			} else {
				pause();
			}
		}
		if (!paused && allowHumanInput) {


			if (e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == KeyEvent.VK_EQUALS) {
				if (currentTank().getLaunchPower() < currentTank().getHealth() / 2) {
					currentTank().setLaunchPower(currentTank().getLaunchPower() + 1);
					power.setText("" + currentTank().getLaunchPower());
					requestFocusInWindow();
				}
			}

			if (e.getKeyCode() == KeyEvent.VK_MINUS) {
				if (currentTank().getLaunchPower() > 0) {
					currentTank().setLaunchPower(currentTank().getLaunchPower() - 1);
					power.setText("" + currentTank().getLaunchPower());
					requestFocusInWindow();
				}
			}

			if (e.getKeyCode() == KeyEvent.VK_TAB) {
				showPlayerStats();
			}

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
				fire();

			}
		}
		if (!paused) {
			if (e.getKeyCode() == KeyEvent.VK_BACK_QUOTE) {
				Tank t = currentTank();
				t.setHealth(-100);
				maxPlayers = maxPlayers - 1;
				if (currentPlayer > maxPlayers) currentPlayer = maxPlayers;
				players.remove(t);
				drawable.remove(t);
				checkEndOfGame();
				nextPlayerTurn(false);
			}
		}
	}//end of keyPressed method

	/**
	 * is called when a key is released
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		downKeys.remove(((long) e.getKeyCode()));

		Tank t = players.get(currentPlayer - 1);

		switch (e.getKeyCode()) {
		// Stop moving tank when motion key released
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_RIGHT:
			if (allowHumanInput) t.stopMotion();
			break;

			// Stop adjusting cannon angle when aim key released
		case KeyEvent.VK_UP:
		case KeyEvent.VK_DOWN:
			if (allowHumanInput) t.stopAimCannon();
			break;
		case KeyEvent.VK_TAB:
			hidePlayerStats();
			break;
		case KeyEvent.VK_PLUS:

			break;
		case KeyEvent.VK_MINUS:

			break;
		}
	}
	
	/**
	 * is called when a key is typed
	 */
	@Override
	public void keyTyped(KeyEvent e) {

	}
	
	/**
	 * returns the drawable array list
	 * @return srray lsit of drawables
	 */
	public ArrayList<Drawable> getDrawable(){
		return drawable;
	}

	/**
	 * returns if human input is allowed
	 * @return boolean true if input is allowed and false if it is not
	 */
	public boolean getHumanInputAllowed() {
		return allowHumanInput;
	}
}// End of abstract Terrain Class
