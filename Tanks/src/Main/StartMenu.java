package Main;
import java.awt.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import drawable.manualTank;

import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;

/**
 * Start Menu
 *
 * @author Joel Cherney
 */
public class StartMenu extends JPanel implements ActionListener {
	private JLabel player2 = new JLabel("Player 2's name:");
	private JLabel player3 = new JLabel("Player 3's name:");
	private JLabel player4 = new JLabel("Player 4's name:");
	private JLabel player5 = new JLabel("Player 5's name:");
	private JTextField player1Name = new JTextField();
	private JTextField player2Name = new JTextField();
	private JTextField player3Name = new JTextField();
	private JTextField player4Name = new JTextField();
	private JTextField player5Name = new JTextField();

	private int xLength = -1;
	private int yLength = -1;

	private JComboBox comboBox;
	private JComboBox humanSelect;
	private JComboBox AISelect;
	private int currentNumberofPlayers = 1;
	private int[][] terrain;

	/**
	 * @name StartMenu
	 *
	 * @description Sets up the menu for the user to select options for their game
	 * @param xDim The length of the panel
	 * @param yDim The height of the panel
	 */
	public StartMenu(int xDim, int yDim) {
		setBackground(Color.WHITE);
		xLength = xDim;
		yLength = yDim;

		createBackground();
		
		String xDimension = new String("[" + xDim/2 + "][" + xDim/2 + "]");

		setLayout(new MigLayout("fillx", "[" + yLength/5 + "][" + yLength/5 + "][" + yLength/5 + "][" + yLength/5 + "][" + yLength/5 + "]"
							    ,"[125][50][][][][][][][][][][][]"));//layout constraints, column, row

		//add the title
		JLabel pageTitle = new JLabel("Game Options");
		pageTitle.setFont(new Font("Arial", Font.BOLD, 42));
		//pageTitle.setSize(50, 50);
		add(pageTitle, "cell 2 1, align center");
		
		
		//add the terrain selection
		JLabel lblPleaseSelectThe = new JLabel("Terrain style");
		add(lblPleaseSelectThe, "cell 2 2, alignx center");
		
		comboBox = new JComboBox();
		add(comboBox, "cell 2 2, alignx center");
		comboBox.addItem("Sand");
		comboBox.addItem("Forest");
		comboBox.addItem("Snow");
		
		//add physics options
		//TODO
		
		//add number of AI player selection
		//TODO
		
		//add number of human player selection
		JLabel lblPleaseSelectThe_1 = new JLabel("Number of human players");
		add(lblPleaseSelectThe_1, "cell 2 3 , alignx center");

		humanSelect = new JComboBox();
		
		//Add an Action Listener to the human player selection so we can change the number of 
		//boxes needed to name each player
		humanSelect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					changeNumberOfPlayers(Integer.parseInt((String) humanSelect.getSelectedItem())); 
			}
	    });
		
		add(humanSelect, "cell 2 3, alignx center");
		humanSelect.addItem("1");
		humanSelect.addItem("2");
		humanSelect.addItem("3");
		humanSelect.addItem("4");
		humanSelect.addItem("5");
		
		
		JLabel player1 = new JLabel("Player 1's name:");
		add(player1, "cell 2 4, alignx center");
		this.setSize(1000, 700);

		player1Name.setText("Username");
		player2Name.setText("Username");
		player3Name.setText("Username");
		player4Name.setText("Username");
		player5Name.setText("Username");
		player1Name.setColumns(10);
		player2Name.setColumns(10);
		player3Name.setColumns(10);
		player4Name.setColumns(10);
		player5Name.setColumns(10);
		
		add(player1Name, "cell 2 4,alignx left, alignx center");
		
		//Add back button
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.loadMenu();
			}
		});
		add(btnBack, "cell 2 9, alignx center");
		
		//Add start button
		JButton btnStart = new JButton("Start!");
		add(btnStart, "cell 2 9, alignx center");
		btnStart.addActionListener(this);
	}

	/**
	 * @name createBackground
	 *
	 * @description Creates the array of land from a preset polynomial
	 * 
	 */
	private void createBackground() {
		//Polynomial variables for the terrain
		double y = 0,a = 613.0530044043094,b = 0.5723935937530484,c = -0.0017677556908674563,d = .0000010360170037339734, x = 0;
		
		//Polynomial equation
		y = a+b*x+c*Math.pow(x, 2)+d*Math.pow(x, 3);
		
		//Array to hold the terrain
		terrain = new int[xLength][yLength];
		
		//Put points in the array that will form the cubic line
		while ((int)x <= xLength) {
			y = a+b*x+c*Math.pow(x, 2)+d*Math.pow(x, 3);
			if ( ((int)x < xLength) && ((int)x >= 0) && ((int)Math.round(y) >= 0)  && ((int)Math.round(y) < yLength)) {

				terrain[(int) x][(int) Math.round(y)] = 1; 
			} 
			x++; 
		} 

		//Fill the area under the cubic line
		//And generate noise
		int k = 0;
		Random random = new Random();
		while (k < xLength - 1) {
			for (int i = 0; i < yLength - 1;i++) {
				if (terrain[k][i] == 1) {
					for (int j = i; j < yLength - 1; j++) {
						if (random.nextInt(2 - 1 + 1) + 1 == 1){
							terrain[k][j] = 1;
						} else { 
							terrain[k][j] = 2;
						}
					}
					break;
				}
			}
			k++;
		}
	}//END OF CREATE BACKGROUND
	
	/**
	 * @name changeNumberOfPlayers
	 *
	 * @description Changes the amount of player name inputs and redraws the panel
	 * @param numberOfPlayers The current number of players that are selected
	 * 
	 */
	public void changeNumberOfPlayers(int numberOfPlayers) {
		if (numberOfPlayers != currentNumberofPlayers) {
			currentNumberofPlayers = numberOfPlayers;
			
			if (numberOfPlayers == 1) {
				remove(player2);
				remove(player3);
				remove(player4);
				remove(player5);
				remove(player2Name);
				remove(player3Name);
				remove(player4Name);
				remove(player5Name);
			} else if (numberOfPlayers == 2) {
				//Add the relevant fields
				add(player2, "cell 2 5, alignx center");
				add(player2Name, "cell 2 5, alignx center");
				
				//Remove the relevant fields
				remove(player3);
				remove(player4);
				remove(player5);
				remove(player3Name);
				remove(player4Name);
				remove(player5Name);
			} else if (numberOfPlayers == 3) {
				//Add the relevant fields
				add(player2, "cell 2 5, alignx center");
				add(player2Name, "cell 2 5, alignx center");
				add(player3, "cell 2 6, alignx center");
				add(player3Name, "cell 2 6, alignx center");
				
				//Remove the relevant fields
				remove(player4);
				remove(player5);
				remove(player4Name);
				remove(player5Name);
			} else if (numberOfPlayers == 4) {
				//Add the relevant fields
				add(player2, "cell 2 5, alignx center");
				add(player2Name, "cell 2 5, alignx center");
				add(player3, "cell 2 6, alignx center");
				add(player3Name, "cell 2 6, alignx center");
				add(player4, "cell 2 7, alignx center");
				add(player4Name, "cell 2 7, alignx center");
				
				//Remove the relevant fields
				remove(player5);
				remove(player5Name);
			}else if (numberOfPlayers == 5) {
				//Add the relevant fields
				add(player2, "cell 2 5, alignx center");
				add(player2Name, "cell 2 5, alignx center");
				add(player3, "cell 2 6, alignx center");
				add(player3Name, "cell 2 6, alignx center");
				add(player4, "cell 2 7, alignx center");
				add(player4Name, "cell 2 7, alignx center");
				add(player5, "cell 2 8, alignx center");
				add(player5Name, "cell 2 8, alignx center");
			} 
			this.revalidate();
			repaint();
		}
	}//END OF CHANGENUMBEROFPLAYERS
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d=(Graphics2D)g;
		
		g2d.setColor(new Color(0x21a1cb));
		g.fillRect(0,0,(int) xLength,(int) yLength);
		
		Color tan = new Color(0xe3bb1d);
		Color darkTan = new Color(0xe7db8e);
		
		// draws the terrain from the terrain array
		for (int i = 0; i < xLength ; i++) {
			for (int j = 0; j < yLength; j++) {
				//Change the color and draw the pixel
				if (terrain[i][j] == 1) {
					g2d.setColor(tan);// The sand color
					g.drawRect(i, j, 1, 1);
				} else if (terrain[i][j] == 2) {
					g.setColor(darkTan);// The sand color
					g2d.drawRect(i, j, 1, 1);
				}
			}
		}
	}//END OF PAINT

	@Override
	public void actionPerformed(ActionEvent arg0) {

		
		if (comboBox.getSelectedItem().equals("Select") && comboBox.getSelectedItem().equals("Select")) {
			//Do something to warn the user that they have to actually select something
		}

		
		
		//		if (comboBox_2.getSelectedItem().equals("select")) {
		//			
		//		}
		if (comboBox.getSelectedItem().equals("Sand") && humanSelect.getSelectedItem().equals("Select") == false ) {
			if (humanSelect.getSelectedItem().equals("1")) {
				Main.manualTanks = 1;
			}
			if (humanSelect.getSelectedItem().equals("2")) {
				Main.manualTanks = 2;
			}
			if (humanSelect.getSelectedItem().equals("3")) {
				Main.manualTanks = 3;
			}
			Main.startSand();
		}
		//Add the other starts and such in here

	}

}
