package buttons;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import Main.*;
import javax.imageio.ImageIO;
import javax.swing.JButton;

import terrain.Terrain;

@SuppressWarnings("serial")
public class RightButton extends JButton implements ActionListener {

	private Terrain terrain;
	private BufferedImage button;

	public RightButton(String label, Terrain terrain) {
		super(label);
		collectButton();
		setBorder(null);
		setBorderPainted(false);
		setContentAreaFilled(false);
		setOpaque(false);
		addActionListener(this);
		this.terrain = terrain;
	}


	private void collectButton() {
		try {                
			button = ImageIO.read(getClass().getResourceAsStream("/img/mainGame/button_right.png"));
		} catch (Exception ex) {
			Main.error(ex);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(button, 0, 0, null);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(29, 26);
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			this.setFocusable(false);
			if (!terrain.getGameStatus()) {
				if (terrain.currentTank().getBarrelAngle() < Math.PI && !terrain.getGameStatus() && terrain.getHumanInputAllowed()) {
					terrain.currentTank().setBarrelAngle(terrain.currentTank().getBarrelAngle() + 0.1);
					terrain.angle.setText(String.format("%2.1f", terrain.currentTank().getBarrelAngle()));
					terrain.requestFocusInWindow();
				}
			}
		}
		catch (Exception e){Main.error(e);}
	}

}
