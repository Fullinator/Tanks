package Main;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

public class HelpMenu extends JEditorPane{


	public HelpMenu(int xDim, int yDim) {
		
		setLayout(new MigLayout("fillx, filly", "[]"
								,"[]"));//layout constraints, column, row
		
		
	    setEditable(false);

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