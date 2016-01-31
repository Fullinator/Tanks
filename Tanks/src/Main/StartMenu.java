package Main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;


public class StartMenu extends JPanel implements ActionListener {
	private JTextField txtUsername;

	private int xLength = -1;
	private int yLength = -1;

	private JComboBox comboBox;
	private JComboBox comboBox_2;

	public StartMenu(int xDim, int yDim) {
		setBackground(Color.WHITE);
		xLength = xDim;
		yLength = yDim;

		String xDimension = new String("[" + xDim/2 + "][" + xDim/2 + "]");

		setLayout(new MigLayout("", "[0][0,grow]", "[][35][][35][][35][][35][][35][][][]"));

		JLabel lblPleaseEnterYour = new JLabel("Please enter your name");
		add(lblPleaseEnterYour, "cell 0 0, align center");
		this.setSize(1000, 700);
		//		JList<? extends E> list = new JList();
		//		add(list);

		txtUsername = new JTextField();
		txtUsername.setText("Username");
		add(txtUsername, "cell 1 0,alignx left,aligny center");
		txtUsername.setColumns(10);

		JLabel lblPleaseSelectThe = new JLabel("Please select the Terrain");
		add(lblPleaseSelectThe, "cell 0 2,align center");

		JCheckBox chckbxSand = new JCheckBox("Sand");
		//add(chckbxSand);

		comboBox = new JComboBox();
		add(comboBox, "cell 1 2,alignx left,aligny top");
		comboBox.addItem("Select");
		comboBox.addItem("Sand");
		comboBox.addItem("Forest");
		comboBox.addItem("Snow");


		comboBox.add("Test", chckbxSand);
		
		
		
				JLabel lblPleaseSelectThe_1 = new JLabel("Please select the number of human players");
				add(lblPleaseSelectThe_1, "cell 0 4,alignx trailing");
		
				comboBox_2 = new JComboBox();
				add(comboBox_2, "cell 1 4");
				comboBox_2.addItem("Select");
				comboBox_2.addItem("1");
				comboBox_2.addItem("2");
				comboBox_2.addItem("3");
		
				JButton btnStart = new JButton("Start!");
				add(btnStart, "cell 1 6");
				btnStart.addActionListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {
		//		g.setColor(Color.WHITE);
		//		g.fillRect(0,0,1000,700);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (comboBox.getSelectedItem().equals("Select")) {
			//Do something to warn the user that they have to actually select something
		}
		
//		if (comboBox_2.getSelectedItem().equals("select")) {
//			
//		}
		if (comboBox.getSelectedItem().equals("Sand") && comboBox_2.getSelectedItem().equals("Select") == false ) {
			if (comboBox_2.getSelectedItem().equals("1")) {
				Main.manualTanks = 1;
			}
			if (comboBox_2.getSelectedItem().equals("2")) {
				Main.manualTanks = 2;
			}
			if (comboBox_2.getSelectedItem().equals("3")) {
				Main.manualTanks = 3;
			}
			Main.startSand();
		}
		//Add the other starts and such in here

	}

}
