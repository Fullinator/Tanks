package Main;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.Random;

import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import drawable.Tank;
import drawable.UserTank;
import physics.Friction;
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
	//Maximum player
	int maxPlayer = 5;
	//The row in MigLayout to start adding the player name fields
	int nameRow = 5;

	private JTextField[] nameFields = new JTextField[maxPlayer];
	private JLabel[] nameLabels = new JLabel[maxPlayer];
	private double y = 0,a = 613.0530044043094,b = 0.5723935937530484,c = -0.0017677556908674563,d = .0000010360170037339734, x = 0;

	private Tank tank; 

	private int xLength = -1;
	private int yLength = -1;

	private JComboBox comboBox;
	private JComboBox humanSelect;
	private JComboBox AISelect;
	private int currentNumberofPlayers = 1;
	private int[][] terrain;
	String[] names;

	/**
	 * @name StartMenu
	 *
	 * @description Sets up the menu for the user to select options for their game
	 * @param xDim The length of the panel
	 * @param yDim The height of the panel
	 */
	public StartMenu(int xDim, int yDim) {
		//setBackground(Color.WHITE);
		xLength = xDim;
		yLength = yDim;
		this.setSize(xDim, yDim);
		tank = new UserTank(Color.WHITE);//null, xDim);

		createBackground();

		String xDimension = new String("[" + xDim/2 + "][" + xDim/2 + "]");

		setLayout(new MigLayout("fillx", "[" + yLength/5 + "][" + yLength/5 + "][" + yLength/5 + "][" + yLength/5 + "][" + yLength/5 + "]"
				,"[125][50][][][][][][][][][][][][][][][][][]"));//layout constraints, column, row

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
		//comboBox.addItem("Forest");
		comboBox.addItem("Snow");

		//add physics options
		//TODO

		//add number of AI player selection
		JLabel lblNumAI = new JLabel("Number of AI players");
		add(lblNumAI, "cell 2 3 , alignx center");

		AISelect = new JComboBox();
		add(AISelect, "cell 2 3 , alignx center");
		for (int i = 0; i <= maxPlayer; i++) AISelect.addItem("" + i);

		//add number of human player selection
		JLabel lblPleaseSelectThe_1 = new JLabel("Number of human players");
		add(lblPleaseSelectThe_1, "cell 2 4 , alignx center");

		humanSelect = new JComboBox();

		//Add an Action Listener to the human player selection so we can change the number of 
		//boxes needed to name each player
		humanSelect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeNumberOfPlayers(Integer.parseInt((String) humanSelect.getSelectedItem())); 
			}
		});

		add(humanSelect, "cell 2 4, alignx center");
		for (int i = 0; i < maxPlayer; i++) {
			humanSelect.addItem("" + (i+1));
		}

		for (int i = 0; i < maxPlayer; i++) {
			nameFields[i] = new JTextField();
			nameFields[i].setText("Player " + (i+1));
			nameFields[i].setColumns(10);
		}
		for (int i = 0; i < maxPlayer; i++) {
			nameLabels[i] = new JLabel("Player " + (i+1) + "'s name:");
		}
		//JLabel player1 = new JLabel("Player 1's name:");
		add(nameLabels[0], "cell 2 5, alignx center");
		add(nameFields[0], "cell 2 5, alignx left, alignx center");

		//Add back button
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.loadMenu();
			}
		});
		add(btnBack, "cell 2 10, alignx center");

		//Add start button
		JButton btnStart = new JButton("Start!");
		add(btnStart, "cell 2 10, alignx center");
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
			int row = nameRow;

			//remove all of the fields
			for (int i = 0; i < maxPlayer; i++) {
				remove(nameLabels[i]);
				remove(nameFields[i]);
			}
			//Add all fields needed
			for (int i = 0; i < currentNumberofPlayers; i++) {
				add(nameLabels[i], "cell 2 " + row + ", alignx center");
				add(nameFields[i], "cell 2 " + row + ", alignx left, alignx center");
				row++;
			}

			this.revalidate();
			repaint();
		}
	}//END OF CHANGENUMBEROFPLAYERS

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d=(Graphics2D)g;

		g2d.rotate(0, 0, 0);
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

		AffineTransform old = g2d.getTransform();// Saves a copy of the old transform so the rotation can be reset later

		int tankXPos = 750;
		int tankYPos = findY(600);
		//draw tank
		g2d.rotate(tank.angle(tankXPos, terrain), tankXPos, findY(tankXPos));// this takes a radian. It has to be a very small radian
		g2d.drawImage(tank.queryImage(), tankXPos, findY(tankXPos) - 16, null);

		//draws the barrel on the tank
		g2d.setColor(Color.BLACK);
		g2d.rotate(tank.getBarrelAngle(), tankXPos, findY(tankXPos) - 15 );
		g2d.fillRect(tankXPos, findY(tankXPos) - 17, 20, 4);

		g2d.setTransform(old);// resets the rotation back to how it was before the painting began
	}//END OF PAINT

	/**
	 * Returns the y coordinate of the top of the terrain from the boolean terrain array.
	 * @param x the x coordinate to check for the y coordinate
	 * @return returns the y coordinate of the terrain or -1 if one cannot be found
	 */
	public int findY(int x) {
		if(x > 0 && x < xLength){
			for(int i = 0; i < terrain[0].length; i += 1){
				if(terrain[x][i] > 0){
					return i;
				}
			}
		}
		return (int)(a + b * x + c * Math.pow(x, 2) + d * Math.pow(x, 3));
	}//end of findY method

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if ((Integer.parseInt((String) AISelect.getSelectedItem()) + Integer.parseInt((String) humanSelect.getSelectedItem())) > 1) {
			names = new String[currentNumberofPlayers];

			for (int i = 0; i < currentNumberofPlayers; i++) {
				if (nameFields[i].getText().length() > 10) {
					names[i] = nameFields[i].getText().substring(0, 10);
				}else {
					names[i] = nameFields[i].getText();
				}
			}

			if (comboBox.getSelectedItem().equals("Sand")) {
				physics.Friction.Sand();
				Main.manualTanks = Integer.parseInt((String) humanSelect.getSelectedItem());;
				Main.AITanks = Integer.parseInt((String) AISelect.getSelectedItem());
				Main.startSand(names);
				
			}else if (comboBox.getSelectedItem().equals("Snow")) {
				physics.Friction.Snow();
				Main.manualTanks = Integer.parseInt((String) humanSelect.getSelectedItem());;
				Main.AITanks = Integer.parseInt((String) AISelect.getSelectedItem());
				Main.startSnow(names);
				
			} else if (comboBox.getSelectedItem().equals("Forest")) {
				physics.Friction.Forest();
				Main.manualTanks = Integer.parseInt((String) humanSelect.getSelectedItem());;
				Main.AITanks = Integer.parseInt((String) AISelect.getSelectedItem());
				Main.startForrest(names);
				
			}

			Main.setTickerPause(false);
		}
	}

}
