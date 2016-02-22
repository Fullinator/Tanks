package terrain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import drawable.drawable;

public class Snow extends Terrain {
	
	public Snow(int x, int y, int maxH, String[] names) {
		super(x, y, maxH, names);
		
		primary = new Color(0xFFFFFF);
		secondary = new Color(0xf2f2f2);
	}

}
