package terrain;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import drawable.Cactus;

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
	public Sand(int x, int y, int maxH, int maxAI, String[] names) {
		super(x, y, maxH, maxAI, names);

		primary = new Color(0xe3bb1d);
		secondary = new Color(0xe7db8e);
		
		createTerrainSpecificItems(3);
	}// End of Sand constructor

	/**
	 * Creates the unique element on the terrain
	 * 
	 * @param amount number of objects to create
	 */
	@Override
	protected void createTerrainSpecificItems(int amount) {
		Random random = new Random();
		int max = xLength;
		int min = 1;
		int type;
		for (int i = 0; i < amount; i++) {
			type = random.nextInt(max - min + 1) + min;
			drawable.add(new Cactus(new Point(type, findY(type))));
		}
	}

}