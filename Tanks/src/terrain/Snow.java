package terrain;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import drawable.Cactus;
import drawable.Snowman;

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
		
		createTerrainSpecificItems(3);
	}

	@Override
	protected void createTerrainSpecificItems(int amount) {
		Random random = new Random();
		int max = xLength;
		int min = 1;
		int type;
		int offset = 75;
		int[] placement = new int[3];
		for (int i = 0; i < amount; i++) {
			type = random.nextInt(max - min + 1) + min;
			if (type == placement[0] || type == placement[1] || type == placement[2] || (type - offset == placement[0] || type - offset == placement[1] || type - offset == placement[2]) || (type + offset == placement[0] || type + offset == placement[1] || type + offset == placement[2])) {
				i--;
				continue;
			}
			placement[i] = type;
			drawable.add(new Snowman(new Point(type, findY(type + 25) + 4)));
		}
	}

}
