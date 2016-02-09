package Main;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import buttons.*;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import javax.swing.JComboBox;
import javax.imageio.ImageIO;
import javax.swing.JButton;


@SuppressWarnings("serial")
public class MainMenu extends JPanel {
	
	private int xLength = -1;
	private int yLength = -1;
	private BufferedImage background;
	
	public MainMenu(int xDim, int yDim) {

		xLength = xDim;
		yLength = yDim;
		
		String xDimension = new String("[" + (xDim - (49*5))+"][41][41][41][41]");
		
		setLayout(new MigLayout("fillx", "[50][" + yLength/5 + "][" + yLength/5 + "][" + yLength/5 + "][" + yLength/5 + "]"
								,"[" + (xDim - (49*5))+"][41][41][41][41]"));//layout constraints, column, row
		//MigLayout layout = new MigLayout("", "[]", xDimension); // Row constraints
		//this.setLayout(layout);
		this.setSize(1000, 700);
		StartMenuButton start = new StartMenuButton("Start");
		this.add(start, "cell 0 1, alignx left");
		SettingsMenuButton settings = new SettingsMenuButton("Settings");
		this.add(settings, "cell 0 2, alignx left");
		HelpMenuButton help = new HelpMenuButton("Help");
		this.add(help, "cell 0 3, alignx left");
		ExitMenuButton exit = new ExitMenuButton("Exit");
		this.add(exit, "cell 0 4, alignx left");
		
		collectBackground();
		
	}

	private void collectBackground() {
		try {                
			background = ImageIO.read(getClass().getResourceAsStream("/img/MainMenu/MainMenu_Background.jpg"));
		} catch (IOException ex) {
			System.out.println("The tank file requested does not exist! Please fix this before contueing!");
			background = null;
		}
	}
	
	private BufferedImage getScaledImage(BufferedImage image) throws IOException {
	    int imageWidth  = image.getWidth();
	    int imageHeight = image.getHeight();

	    double scaleX = (double)xLength/imageWidth;
	    double scaleY = (double)yLength/imageHeight;
	    AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
	    AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

	    return bilinearScaleOp.filter(image, new BufferedImage(xLength, yLength, image.getType()));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		try {
			g.drawImage(getScaledImage(background), 0, 0, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
