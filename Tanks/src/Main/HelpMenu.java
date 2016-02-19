package Main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

public class HelpMenu extends JPanel{


	public HelpMenu(int xDim, int yDim) {
		
		setLayout(new MigLayout("fillx, filly", "[" + yDim/5 + "][" + yDim/5 + "][20][" + yDim/5 + "][" + yDim/5 + "][" + yDim/5 + "]"
			    ,"[][][][]"));//layout constraints, column, row
		
		JLabel pageTitle = new JLabel("Tanks Help Center");
		pageTitle.setFont(new Font("Arial", Font.BOLD, 42));
		add(pageTitle, "cell 3 0, alignx 50%");
		
		JLabel controls = new JLabel("<html><center><h1>Controls</h1></center>" + 
		"<ul>" + 
			"<li>Move Left: Left Arrow Key</li>" +
			"<li>Move Right: Right Arrow Key</li>" +
			"<li>Fire: Space</li>" +
			"<li>Move Turret Up: Up Arrow Key</li>" +
			"<li>Move Turret Down: Down Arrow Key</li>" +
			"<li>Power Up: 			</li>" +
			"<li>Power Down: 		</li>" +
		"</ul></html>");
		
		add(controls, "cell 3 1, alignx 50%");
		
		JLabel about = new JLabel("<html><center><h1>About</h1><br><p>Tanks is a reimagining of an old flash 2-dimensional game where players could battle to be the last man standing.<br>" + 
								  "Tanks uses JAMA 1.0.3 and MigLayout 4.0:<br>" + 
								  "http://www.miglayout.com<br>" + 
								  "http://math.nist.gov/javanumerics/jama/</p></center></html>");
		add(about, "cell 3 2, aligny -100%, alignx 50%");
		
		//Add back button
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.loadMenu();
			}
		});
		add(btnBack, "cell 3 3, alignx center");
	}

}//END OF HELPMENU