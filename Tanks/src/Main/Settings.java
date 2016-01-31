package Main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;


public class Settings extends JPanel implements ActionListener {

	private int xLength = -1;
	private int yLength = -1;
	private JComboBox comboBox_1;

	public Settings(int xDim, int yDim) {
		setBackground(Color.WHITE);
		xLength = xDim;
		yLength = yDim;

		String xDimension = new String("[" + xDim/2 + "][" + xDim/2 + "]");

		setLayout(new MigLayout("", "[0][0,grow]", "[][35][][35][][35][][35][][35][][][]"));
		this.setSize(1000, 700);
		
				JLabel lblPleaseSelectYour = new JLabel("Please select your resolution");
				add(lblPleaseSelectYour, "cell 0 1,alignx center");
		
		
				comboBox_1 = new JComboBox();
				add(comboBox_1, "cell 1 1");
				comboBox_1.addItem("Select");
				comboBox_1.addItem("800 x 600");
				comboBox_1.addItem("1000 x 700 (TEST RESOLUTION ONLY)");
				comboBox_1.addItem("1280 x 760");
				comboBox_1.addItem("1440 x 900");
				comboBox_1.addItem("1920 x 1080");
				comboBox_1.addItem("2560 x 1600");
				
				JButton btnSave = new JButton("Save");
				add(btnSave, "cell 1 3");
				btnSave.addActionListener(this);
				
				BackButton back = new BackButton("Back");
				add(back, "cell 1 4");
	}

	@Override
	public void paintComponent(Graphics g) {
		//		g.setColor(Color.WHITE);
		//		g.fillRect(0,0,1000,700);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (comboBox_1.getSelectedItem().equals("")) {
			//Do something to warn the user that they have to actually select something
		}
		if (comboBox_1.getSelectedItem().equals("800 x 600")) {
			Main.xLength = 800;
			Main.yLength = 600;
		}
		if (comboBox_1.getSelectedItem().equals("1000 x 700 (TEST RESOLUTION ONLY)")) {
			Main.xLength = 1000;
			Main.yLength = 700;
		}
		if (comboBox_1.getSelectedItem().equals("1280 x 760")) {
			Main.xLength = 1280;
			Main.yLength = 760;
		}
		if (comboBox_1.getSelectedItem().equals("1440 x 900")) {
			Main.xLength = 1440;
			Main.yLength = 900;
		}
		if (comboBox_1.getSelectedItem().equals("1920 x 1080")) {
			Main.xLength = 1920;
			Main.yLength = 1080;
		}
		if (comboBox_1.getSelectedItem().equals("2560 x 1600")) {
			Main.xLength = 2560;
			Main.yLength = 1600;
		}
		//Add the other starts and such in here

	}
	
	public class BackButton extends JButton implements ActionListener {

		public BackButton(String string) {
			super(string);
			this.addActionListener(this);
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Main.loadMenu();
			
		}
		
	}

}
