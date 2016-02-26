package Main;
import java.awt.*;
import java.awt.event.ComponentEvent;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

import drawable.Clouds;
import terrain.*;


public class Main extends JFrame {

	public static int xLength = 1000;
	public static int yLength = 700;
	public static int manualTanks = -1;
	public static int AITanks = 0;
	
	private static Terrain map;// = new Sand(1000,700);
	private static JFrame frame = new JFrame();
	private static MainMenu menu;
	private static HelpMenu help;
	private static Ticker ticker;

	public static sounds sound = new sounds();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Tanks Alpha");
		frame.setSize(xLength, yLength);
		frame.setResizable(false);
		menu = new MainMenu(xLength, yLength);
		//menu.setBorder( BorderFactory.createEtchedBorder(Color.BLUE, Color.PINK));  
		//BorderFactory.
		frame.add(menu);
		menu.setFocusable(true);
		menu.requestFocusInWindow();
		menu.setVisible(true);
		frame.setFocusable(false);
		frame.setVisible(true);
		sound.runLoop("song");
		ticker = new Ticker(10);
		new Thread(ticker).start();
	}
	
	public static void setMaxHumans(int x) {
		manualTanks = x;
	}
	
	public static void loadMenu() {
		frame.getContentPane().removeAll();
		frame.setSize(xLength, yLength);
		menu = new MainMenu(xLength, yLength);
		frame.add(menu);
		menu.setFocusable(true);
		menu.requestFocusInWindow();
		menu.setVisible(true);
		frame.setFocusable(false);
		frame.setVisible(true);
		
	}
	
	public static void settings() {
		frame.getContentPane().removeAll();
		Settings settings = new Settings(xLength, yLength);
		frame.add(settings);
		frame.repaint();
		settings.setFocusable(true);
		settings.requestFocusInWindow();
		frame.setVisible(true);
		frame.revalidate();
	}
	public static void helpMenu() {
		frame.getContentPane().removeAll();
		frame.setSize(xLength, yLength);
		help = new HelpMenu(xLength, yLength);
		frame.add(help);
		menu.setFocusable(true);
		menu.requestFocusInWindow();
		menu.setVisible(true);
		frame.setFocusable(false);
		frame.setVisible(true);
	}
	
	public static void error(Exception e) {
		frame.getContentPane().removeAll();
		ErrorPage error = new ErrorPage(e, xLength, yLength);
		frame.add(error);
		frame.repaint();
		error.setFocusable(true);
		error.requestFocusInWindow();
		frame.setVisible(true);
		frame.revalidate();
	}

	public static void startMenu() {
		frame.getContentPane().removeAll();
		frame.repaint();
		StartMenu start = new StartMenu(xLength,yLength);
		frame.add(start);
		start.setFocusable(true);
		start.requestFocusInWindow();
		frame.setVisible(true);
		frame.revalidate();
	}
	
	public static void startSand(String[] names) {
		sound.stop("song");
		frame.getContentPane().removeAll();
		map = new Sand(xLength, yLength, manualTanks, names);
		frame.add(map);
		map.setFocusable(true);
		map.requestFocusInWindow();
		frame.setVisible(true);
		frame.revalidate();
			
	}
	
	public static void startSnow(String[] names) {
		sound.stop("song");
		frame.getContentPane().removeAll();
		map = new Snow(xLength, yLength, manualTanks, names);
		frame.add(map);
		map.setFocusable(true);
		map.requestFocusInWindow();
		frame.setVisible(true);
		frame.revalidate();
			
	}
	
	public static void startForrest(String[] names) {
		sound.stop("song");
		frame.getContentPane().removeAll();
		map = new Forest(xLength, yLength, manualTanks, names);
		frame.add(map);
		map.setFocusable(true);
		map.requestFocusInWindow();
		frame.setVisible(true);
		frame.revalidate();
			
	}
	
	public static void exitGame() {
		sound.stop("song");
		frame.dispose();
		ticker.stopTicking();
	}

	public static void setTickerPause(boolean pause) {
		ticker.setPaused(pause);
	}
	
}
// TO DO:


//create ammunition class that implments destruction


//add movement using the derivative to find the angle it's supposed to be at


