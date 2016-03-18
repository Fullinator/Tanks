package terrain;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class Forest extends Terrain {

	public Forest(int x, int y, int maxH, int maxAI, String[] names) {
		super(x, y, maxH, maxAI, names);
		primary = new Color(20, 120, 30);
		secondary = new Color(30, 100, 10);
	}

	@Override
	protected void createTerrainSpecificItems(int amount) {
		// TODO Auto-generated method stub
		
	}

}
