package terrain;

import java.awt.Color;

/**
 * The sand terrain. This creates the random 2D terrain and coordinates all of the events and painting for the game to happen
 *
 * @author Joel Cherney
 *
 */
@SuppressWarnings("serial")
public class Sand extends Terrain{

	/**
	 * Sand's constructor
	 *
	 * @param x The length of the panel
	 * @param y The height of the panel
	 * @param names The List of names for the players
	 */
	public Sand(int x, int y, int maxH, String[] names) {
		super(x, y, maxH, names);

		primary = new Color(0xe3bb1d);
		secondary = new Color(0xe7db8e);
	}// End of Sand constructor

}