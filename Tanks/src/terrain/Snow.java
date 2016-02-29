package terrain;

import java.awt.Color;

/**
 * The Snow terrain. This creates the random 2D terrain and coordinates all of the events and painting for the game to happen
 *
 * @author Joel Cherney
 *
 */
@SuppressWarnings("serial")
public class Snow extends Terrain {
	
	/**
	 * Snow's constructor
	 *
	 * @param x The length of the panel
	 * @param y The height of the panel
	 * @param names The List of names for the players
	 */
	public Snow(int x, int y, int maxH, int maxAI, String[] names) {
		super(x, y, maxH, maxAI, names);
		
		primary = new Color(0xFFFFFF);
		secondary = new Color(0xf2f2f2);
	}

}
