package Main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import buttons.*;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;


@SuppressWarnings("serial")
public class ErrorPage extends JPanel {

	private int xLength = -1;
	private int yLength = -1;


	public ErrorPage(Exception e, int xL, int yL) {

		xLength = xL;
		yLength = yL;

		this.setSize(xLength, yLength);
		MigLayout layout = new MigLayout("", "[" + xLength + "]", "[20][" + (yLength - 50) + "]"); // Row constraints
		this.setLayout(layout);
		JLabel mainLabel = new JLabel("Team 2 would like to apologize for our blunder. Have a look at our mistake:");
		this.add(mainLabel, "cell 0 1, align center");

		// Create Scrolling Text Area in Swing
		JTextArea ta = new JTextArea(stackTraceToString(e), 5, 50);
		ta.setEditable(false);
		ta.setLineWrap(true);
		JScrollPane sbrText = new JScrollPane(ta);
		sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(sbrText, "cell 0 2,growx, growy");
		e.printStackTrace();
	}

	public String stackTraceToString(Throwable e) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : e.getStackTrace()) {
			sb.append(element.toString());
			sb.append(" \n ");
		}
		return sb.toString();
	}




	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.PINK);
		g.fillRect(0, 0, 1000, 700);

	}

}
