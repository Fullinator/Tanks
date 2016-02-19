package terrain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import drawable.drawable;

public class Snow extends Terrain {
	
	public Snow(int x, int y, int maxH) {
		super(x, y, maxH);
		
		primary = new Color(0xe3bb1d);
		secondary = new Color(0xe7db8e);
		
		generate();
	}

	@Override
	public void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


}
