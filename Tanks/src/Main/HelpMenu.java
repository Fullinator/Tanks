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
			"<li>Move Left:  		</li>" +
			"<li>Move Right: 		</li>" +
			"<li>Fire: 				</li>" +
			"<li>Move Turret Up: 	</li>" +
			"<li>Move Turret Down:	</li>" +
			"<li>Power Up: 			</li>" +
			"<li>Power Down: 		</li>" +
		"</ul></html>");
		
		add(controls, "cell 3 1, alignx 50%");
		
		JLabel about = new JLabel("<html><center><h1>About</h1><br><p>Tanks is a reimagining of an old flash 2-dimensional game where players could battle to be the last man standing.</p></center></html>");
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
	/*

	    java.net.URL helpURL = getClass( ).getResource("/html/help.html");

	    if (helpURL != null) {
	        try {
	            setPage(helpURL);
	        } catch (IOException e) {
	            System.err.println("Attempted to read a bad URL: " + helpURL);
	        }
	    } else {
	        System.err.println("Couldn't find file: help.html");
	    }

	    JScrollPane editorScrollPane = new JScrollPane(this);

	    editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    editorScrollPane.setPreferredSize(new Dimension(xDim, yDim));
	    editorScrollPane.setMinimumSize(new Dimension(xDim, yDim));
	*/
/*

	    JLabel emptyLabel = new JLabel("");

	    emptyLabel.setPreferredSize(new Dimension(300, 300));

	    JButton launch = new JButton("Launch!");

	    launch.setPreferredSize(new Dimension(350, 50));

	    JFrame f = new JFrame();

	    f.setTitle("Stonelore Launcher");
	    f.setSize(350, 400);
	    f.setLocationRelativeTo(null);
	    f.getContentPane().add(emptyLabel, BorderLayout.CENTER);
	    f.getContentPane().add(editorScrollPane, BorderLayout.NORTH);
	    f.getContentPane().add(launch, BorderLayout.SOUTH);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.setResizable(false);
	    f.setVisible(true);
	*/
	}

}//END OF HELPMENU