package terrain;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import drawable.drawable;


@SuppressWarnings("serial")
public abstract class Terrain extends JPanel{
	protected int[][] terrain; // This will hold all of the points that will be painted
	protected int xPanel = 0;// This will be set to the JPanels width
	protected int yPanel = 0;// This will be set to the JPanels height
	public int maxPlayers = -1;
	public int currentPlayer = 1;
	protected double y;
	protected double a;
	protected double b;
	protected double c;
	protected double d;
	protected int maxHuman = -1;
	public JLabel angle;
	public JLabel power;
    protected ArrayList<drawable> drawable;
	
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

	public abstract void setDrawable(ArrayList<drawable> drawable);

	public abstract int findY(int x); 

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
	protected abstract void generate();

	/**
	 * fills the space underneath the cubic
	 */
	protected abstract void fill();


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
							terrain[i][j+1] = 1;
							terrain[i][j] = 0;
							repaint();
						}
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

}// End of abstract Terrain Class