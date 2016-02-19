package terrain;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLabel;

import buttons.*;
import drawable.*;
import Jama.Matrix;
import Main.sounds;
import net.miginfocom.swing.MigLayout;

/**
 * The sand terrain. This creates the random 2D terrain and coordinates all of the events and painting for the game to happen
 *
 * @author Joel Cherney
 *
 */
@SuppressWarnings("serial")
public class Sand extends Terrain implements KeyListener{

	/**
	 * Sand's constructor
	 *
	 * @param x The length of the panel
	 * @param y The height of the panel
	 */
	public Sand(int x, int y, int maxH, String[] names) {
		super(x, y, maxH, names);
		setLayout(new MigLayout("", "["+ ((getXTerrain() - 500)/2) +"][29][29][29][26][26][26]["+ ((getXTerrain() - 500)/2) +"]", "[35][35]"));

		
		RightButton angleUp = new RightButton("", this);
		this.add(angleUp, "cell 4 0");
		LeftButton angleDown = new LeftButton("",this);
		this.add(angleDown, "cell 2 0");
		angle = new JLabel("0.0");
		this.add(angle, "cell 3 0");

		UpButton powerUp = new UpButton("", this);
		this.add(powerUp, "cell 5 0");
		DownButton powerDown = new DownButton("", this);
		this.add(powerDown, "cell 7 0");
		power = new JLabel("" + currentTank().getLaunchPower());
		this.add(power, "cell 6 0");
		
		primary = new Color(0xe3bb1d);
		secondary = new Color(0xe7db8e);
	}// End of Sand constructor

}