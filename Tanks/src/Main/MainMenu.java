package Main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import buttons.*;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import javax.swing.JComboBox;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;


@SuppressWarnings("serial")
public class MainMenu extends JPanel {
	
	private int xLength = -1;
	private int yLength = -1;
	
	public MainMenu(int yDim, int xDim) {

		xLength = xDim;
		yLength = yDim;
		
		String xDimension = new String("[" + (xDim - (49*5))+"][41][41][41][41]");
		
		MigLayout layout = new MigLayout("", "[]", xDimension); // Row constraints
		this.setLayout(layout);
		this.setSize(1000, 700);
		StartMenuButton start = new StartMenuButton("");
		this.add(start, "cell 0 1");
		SettingsMenuButton settings = new SettingsMenuButton("");
		this.add(settings, "cell 0 2");
		HelpMenuButton help = new HelpMenuButton("");
		this.add(help, "cell 0 3");
		ExitMenuButton exit = new ExitMenuButton("");
		this.add(exit, "cell 0 4");
	}

	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, yLength, xLength);
		
	}
	
}
