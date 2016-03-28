package terrain;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

import physics.Projectile;
import physics.Wind;
import physics.terrainDestroyer;
import Jama.Matrix;
import Main.Main;
import Main.Ticker;
import Main.sounds;
import buttons.DownButton;
import buttons.FireButton;
import buttons.LeftButton;
import buttons.RightButton;
import buttons.UpButton;
import drawable.AITank;
import drawable.Animation;
import drawable.Cactus;
import drawable.Clouds;
import net.miginfocom.swing.MigLayout;
import drawable.Drawable2;
import drawable.Snowman;
//import drawable.Cactus;
import drawable.DayCycle;
import drawable.Tank;
import drawable.UserTank;


@SuppressWarnings("serial")
public abstract class Terrain extends JPanel implements KeyListener{
	protected int[][] terrain; // This will hold all of the points that will be painted
	protected int xLength = 0;// This will be set to the JPanels width
	protected int yLength = 0;// This will be set to the JPanels height
	public int maxPlayers;
	protected int currentPlayer = 1;
	protected double y;
	protected double a;
	protected double b;
	protected double c;
	protected double d;
	protected int numHuman = -1;
	protected int numAI = -1;
	public JLabel angle;
	public JLabel power;
	public JLabel playerName;
	Color primary;
	Color secondary;
	protected ArrayList<Drawable2> drawable;
	protected ArrayList<Tank> players;
	protected java.util.List<Long> downKeys = new ArrayList<>();
	private boolean paintLock;
	protected boolean paused = false;
	private boolean allowHumanInput = true;
	protected JLabel pauseTitle;
	protected MigLayout normalLayout;
	protected MigLayout pauseLayout;
	protected RightButton angleUp;
	protected LeftButton angleDown;
	protected UpButton powerUp;
	protected DownButton powerDown;
	protected JButton quit;
	protected JButton unPause;
	protected FireButton fire;
	protected boolean tabbed = false;
	Wind wind;
	Projectile projectile;
	private List<Projectile> projectiles;
	protected int nightShiftAmount;
	protected Color nightShiftColor;
	protected boolean nightShift;
	private BufferedImage currentTerrainImage;
	private boolean staleTerrainImage;
	protected JComboBox<String> weapons;



	/**
	 *	Terrain's constructor
	 *
	 * @param x width of JPanel
	 * @param y height of JPanel
	 */
	protected Terrain(int x, int y, int numHuman, int numAI, String[] names) {
		xLength = x;

		yLength = y;
		this.numHuman = numHuman;
		this.numAI = numAI;
		maxPlayers = this.numHuman + this.numAI;
		generate();
		fill();// calls a method that fills in the points underneath the cubic
		createTanks(this.numHuman, this.numAI, names);
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
	public void setDrawable(ArrayList<Drawable2> drawable) {
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


	/**
	 * findPlacement returns the x and y position of where to place an image
	 * @param width the width of the base of the image to place
	 */
	protected int[] findPlacement(int width) {
		int start = -1, end = -1, tempStart = 0, tempEnd = 0, pastHeight = -1, length = 0;
		int[] location = new int[2];
		//loop through terrain
		for (int i = 0; i < xLength; i++) {
			for (int j = 0; j < yLength; j++) {
				if (terrain[i][j] > 0) {
					if (j == pastHeight) {//The heights are equal
						break;
					} else  {//The heights are not equal
						tempEnd = i;
						if (length < (tempEnd - tempStart)) {//The new length is greater than the old
							start = tempStart;
							end = tempEnd;
							pastHeight = j;
							length = (tempEnd - tempStart);
							tempStart = i++;
						} else {//The new length is not greater than the old
							tempStart = i;
						}
						break;
					}
				}
			}
		}
		//find the longest flattest area
		location[0] = start;
		location[1] = end;
		//System.out.println(location[0] + "   end:"  + location[1]);
		return location;
	}

	/**
	 * Creates cloud objects and adds them to the list of drawables
	 * 
	 * @param numberOfClouds number of clouds to create
	 */
	protected void createClouds(int numberOfClouds) {
		for (int i = 0; i < numberOfClouds; i++) {
			Clouds c = new Clouds(this, getXTerrain(), getYTerrain(), getXTerrain() - (getXTerrain() + 1));
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
	protected void createTanks(int numHumans, int numAI, String[] names) {
		players  = new ArrayList<Tank>();
		drawable = new ArrayList<Drawable2>();
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


	public void collisionDetection(Projectile shot) {
		int radius = 15;//radius around tank in pixels to check collision
		boolean tankHit = false;
		//check against all tanks
		for (Tank t : players) {
			//We need to skip the outbound shot on the current tank

			//find center of tank
			//Point center = new Point(t.getX() + 20, findY(t.getX() + 10) - (t.queryImage().getHeight()) );
			//g2d.rotate(((Tank)drawable.get(i)).angle(drawable.get(i).getX() + 20, terrain), drawable.get(i).getX(), findY(drawable.get(i).getX()));// this takes a radian. It has to be a very small radian

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
					ani.setLocation(center);
					drawable.add(ani);

					//pause();
				}
			}
		}
		if (tankHit) {
			damage(shot, tankHit, radius);
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

			damage(shot, false, radius);
			terrainHit = true;
		}


		if (tankHit || terrainHit) {
			allowHumanInput = true;
			nextPlayerTurn();
		}

	}

	public void damage(Projectile shot, boolean tank, int tankCollisionRadius) {
		//check which shot it is so we can tell what kind of damage
		//For now we only have one kind

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
						//System.out.println("damage!");
						if (t.getHealth() <= 0) {
							players.remove(t);
							drawable.remove(t);
							maxPlayers = maxPlayers - 1;
							checkEndOfGame();
							damage(shot,true,tankCollisionRadius);
							return;
						}
					}
				}
			}
		} else {//damage terrain
			int mag = shot.terrainMag;
			int x = shot.getX();
			int y = findY(x);
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
			System.out.println("left: " + (x - (mag + shot.terrainMag )) + "    Right: " + (x + mag + shot.terrainMag));
			for (int i = x - (mag + shot.terrainMag + 50); i <= x + (mag + shot.terrainMag + 50); i++) {
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
	}

	protected void terrarinElementRemove(Projectile shot) {
		for (Drawable2 object: drawable) {
			if ( (object instanceof Cactus  || object instanceof Snowman)  && ((object.getX() + object.queryImage().getWidth()/2) >= (shot.getX() - shot.terrainMag) && (object.getX() + object.queryImage().getWidth()/2) <= (shot.getX() + shot.terrainMag))) {
				drawable.remove(object);
				terrarinElementRemove(shot);
				return;
			}
		}
	}
	
	protected void checkEndOfGame() {
		if (maxPlayers == 1) {
			playerWin();
		}
	}

	/**
	 * Calls the super paintComponent to paint on the JPanel
	 * This also handles all standard terrain drawing and drawables drawing.
	 */
	
	Shift shift1 = new Shift(Projectile.outOfScreen, (int)Projectile.vY);
	public void paintComponent(Graphics g) {
		int shift =0;
		shift =shift1.shifter(Projectile.outOfScreen, (int)Projectile.vY);

		Graphics2D g2d=(Graphics2D)g;
		super.paintComponent(g);// prevents older objects from staying on the screen

		g2d.setColor(new Color(0x21a1cb));// The skies color
		g2d.fillRect(0, 0, getXTerrain(), getYTerrain());// fills the entire background with the sky       

		AffineTransform old = g2d.getTransform();// Saves a copy of the old transform so the rotation can be reset later
//		Shift shift1 = new Shift(Projectile.outOfScreen, (int)Projectile.vY);

		for (int i = 0; i < drawable.size(); i++) {
			//System.out.println("Shift = " + shift);
			if (drawable.get(i) instanceof DayCycle) {//Make sure to draw the sun/moon first.
				g2d.drawImage(drawable.get(i).queryImage(), drawable.get(i).getX(), drawable.get(i).getY() - drawable.get(i).queryImage().getHeight(), null);
				nightShiftAmount = ((DayCycle) drawable.get(i)).shiftNightAmount();
				nightShift = ((DayCycle) drawable.get(i)).shiftNight();
			}
		}

		for (int i = 0; i < drawable.size(); i++) {// draws the clouds and tanks and eventually trees and whatever else needs to be drawn
			if (drawable.get(i) instanceof DayCycle) {
				//We already drew this
			} 
			else if (drawable.get(i) instanceof Tank) {// draws player controlled tanks
				g2d.rotate(((Tank)drawable.get(i)).angle((int) ((int)drawable.get(i).getX() + (int) drawable.get(i).queryImage().getWidth()*.5), terrain), drawable.get(i).getX(), findY(drawable.get(i).getX()) - drawable.get(i).queryImage().getHeight());// this takes a radian. It has to be a very small radian
				g2d.drawImage(drawable.get(i).queryImage(), drawable.get(i).getX(), findY(drawable.get(i).getX()) - (int) drawable.get(i).queryImage().getHeight(), null);
				
				//System.out.println(findY(drawable.get(i).getX()) - 18);
				
				//draws the barrel on the tank
				g2d.setColor(((Tank) drawable.get(i)).getBarrelColor());
				g2d.rotate(((Tank)drawable.get(i)).getBarrelAngle(), drawable.get(i).getX() + (int) drawable.get(i).queryImage().getWidth()*.5, findY(drawable.get(i).getX()) - (int) drawable.get(i).queryImage().getHeight() );
				g2d.fillRect(drawable.get(i).getX(), findY(drawable.get(i).getX()) - drawable.get(i).queryImage().getHeight() , (int) (drawable.get(i).queryImage().getWidth()*.5) , 4);
				g2d.setTransform(old);// resets the rotation back to how it was before the painting began
				//g2d.translate(i,i);


				//REMOVE THIS:
				//Draws the center of the hit box on the tank
				//double angle = ((Tank) drawable.get(i)).angle(drawable.get(i).getX() + 20 , terrain);
				//int length = (int) (drawable.get(i).queryImage().getWidth() * Math.cos(angle));
				//Point center = new Point(drawable.get(i).getX() + (length/2), findY(drawable.get(i).getX() + (length/2)) - (drawable.get(i).queryImage().getHeight() /2) );
				//g2d.setColor(Color.PINK);
				//g2d.drawRect((int)center.getX(), (int)center.getY(), 10, 10);
				//g2d.drawOval((int)center.getX(), (int)center.getY(), 35, 35);


			} //else if (drawable.get(i) instanceof standardShell) {// draws the missile
			//g2d.fillOval(drawable.get(i).getX(), drawable.get(i).getY(), 5, 5);
			else if (drawable.get(i) instanceof Clouds) {// draws clouds
				g2d.drawImage(drawable.get(i).queryImage(), drawable.get(i).getX(), drawable.get(i).getY(), null);
			} else {
				g2d.drawImage(drawable.get(i).queryImage(), drawable.get(i).getX(), drawable.get(i).getY() - drawable.get(i).queryImage().getHeight(), null);
			}

		}// End of loop to draw objects

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
		g2d.drawImage(currentTerrainImage, 0, 0, null);

		//Draw the projectile in the list
		projectiles.forEach(p -> {
			g2d.drawImage(p.queryImage(), p.getX(), p.getY(), null);
		});

		//draw night shift

		if (nightShift) {
			g2d.setColor(new Color(66,98,255,nightShiftAmount));
			g2d.fillRect(0, 0, xLength, yLength);
		}

		g2d.setColor(new Color(0xdfdfdf));
		g2d.fillRect(0, 0, getXTerrain(), 60);// draws the top menu bar

		if (tabbed) {
			g2d.setColor(new Color(0x21a1cb));// The skies color
			g2d.fillRect(0, 0, getXTerrain(), 60);
			if (nightShift) {
				g2d.setColor(new Color(66,98,255,nightShiftAmount));
				g2d.fillRect(0, 0, getXTerrain(), 60);
			}
			g2d.setColor(new Color(0,0,0,180));
			g2d.fillRect(0, 0, xLength, yLength);
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

	
	/**
	 * Sets the turn to the next player
	 */
	public void nextPlayerTurn() {
		System.out.println("nextPlayerTurn() --> " + System.currentTimeMillis());
//		Thread.dumpStack();
		if (currentPlayer + 1 > maxPlayers) {
			currentPlayer = 1;
		} else {
			currentPlayer = currentPlayer + 1;
		}
		System.out.println("\t\tAdvancing to player " + currentPlayer);

		//Change the status bar to the information
		//of the current player
		power.setText("" + currentTank().getLaunchPower());
		playerName.setText(currentTank().getName());
		playerName.setForeground(currentTank().getBarrelColor());

		if (currentTank() instanceof AITank) {
			allowHumanInput = false;
			((AITank) currentTank()).takeTurn();
		} else {
			allowHumanInput = true;
		}
	}

	/**
	 * Creates the top menu of controls 
	 */
	protected void createTopMenu() {
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
		weapons = new JComboBox<String>();
		weapons.addItem("Standard Shot");
		weapons.addActionListener(e -> {
			this.requestFocus();
		});
		weapons.addItem("Terrain Destroyer");

		add(weapons, "cell 7 0, alignx center");

		//Fire Button
		fire = new FireButton("", this);
		add(fire, "cell 8 0, alignx center");
		//Health Label

		//Buy weapons

	}

	/**
	 * Stops tank movement and creates the requested shot from current parameters and then changes the turn
	 */
	public void fire() {
		Tank tank = players.get(currentPlayer - 1);
		tank.stopAimCannon();
		tank.stopMotion();
		allowHumanInput = false;

		String weapon = (String) weapons.getSelectedItem();
		Projectile Projectile;
		
		switch (weapon) {
		case "Standard Shot": 
			projectile = new Projectile(currentTank(), this);
			projectiles.add(projectile);
			break;
			
		case "Terrain Destroyer":
			projectile = new terrainDestroyer(currentTank(), this);
			projectiles.add(projectile);
			break;
		}
		
		


		projectile.setTickerID(Ticker.addMethod(projectile::fire));

				Main.sound.run("shot1");
//		nextPlayerTurn();

		Animation ani = new Animation("smoke");
		Point t = new Point(tank.getX(), findY(tank.getX()));
		ani.setLocation(t);
		drawable.add(ani);
	}

	/**
	 * Hides the top menu of player controls
	 */
	protected void hideTopMenu() {
		remove(playerName);
		remove(angleUp);
		remove(angleDown);
		remove(angle);
		remove(powerUp);
		remove(powerDown);
		remove(power);
		removeAll();
	}

	/**
	 * Shows the top menu of player controls
	 */
	protected void showTopMenu() {
		add(playerName, "cell 0 0");
		add(angleUp, "cell 3 0, alignx center");
		add(angleDown, "cell 1 0, alignx center");
		add(angle, "cell 2 0, alignx center");
		add(powerUp, "cell 4 0, alignx center");
		add(powerDown, "cell 6 0, alignx center");
		add(power, "cell 5 0, alignx center");
		add(fire, "cell 7 0, alignx center");
		revalidate();
		weapons.addItem("Standard Shot");
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
	protected void showPlayerStats() {
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
	protected void hidePlayerStats() {
		tabbed = false;
		if (!getGameStatus()) {//Make sure we don't bring back the top control menu 
			//While the game is paused
			setLayout(normalLayout);
			removeAll();
			showTopMenu();
			revalidate();
		}
	}


	protected void playerWin() {
		hidePlayerStats();
		paused = true;
		hideTopMenu();
		tabbed = false;
		pauseLayout = new MigLayout("", "["+ ((getXTerrain() - 500)/2) +"][29][29][26][26][26]["+ ((getXTerrain() - 500)/2) +"]", "[150][35][40][][][][][][]");
		setLayout(pauseLayout);
		quit = new JButton("Quit to Menu");
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
											Main.sound.runLoop("song");
				Main.loadMenu();
				Main.setTickerPause(true);
			}
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
	protected void pause() {
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
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
											Main.sound.runLoop("song");
				Main.loadMenu();
				Main.setTickerPause(true);
			}
		});
		unPause = new JButton("UnPause");
		unPause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				unPause();
			}
		});
		add(unPause, "cell 5 4, alignx center");
		add(quit, "cell 5 5, alignx center");
		add(pauseTitle,"cell 5 2, alignx center");
		tabbed = false;//Makes sure the tab and pause menus don't overlay
		revalidate();
	}

	/**
	 * Removes all objects to pause the game and restarts the ticker
	 */
	protected void unPause() {
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
				if (currentTank().getLaunchPower() < currentTank().getHealth()) {
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
	}//end of keyPressed method


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

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public ArrayList<Drawable2> getDrawable(){
		return drawable;
	}

}// End of abstract Terrain Class
