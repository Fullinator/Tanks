package drawable;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Main.Main;

public class Animation implements Drawable{

	private Point location;
	String type;
	public int tick = 0;
	BufferedImage[] smoke = new BufferedImage[10];
	BufferedImage[] explode = new BufferedImage[10];
	BufferedImage bigImg = null;
	int width, height, rows, cols;

	public Animation(String type){

		this.type = type;
		
		if(type.equals("smoke")){ //check if smoke is needed
			width = 32;
			height = 32;
			rows = 1;
			cols = 10;
			try {
				//load the image
				bigImg = ImageIO.read(getClass().getResourceAsStream("/img/smoke1.png"));
			} catch (IOException e) {
				System.out.println("The animation file requested does not exist! Please fix this before continuing!");
			}

			for(int i = 0; i < rows; i++){ //load the array with the stages of smoke
				for(int j = 0; j < cols; j++){
					smoke[(i*cols)+j] = bigImg.getSubimage(j*width, i*height, width, height);
				}

			}
		}
		
		if(type.equals("explode")){ //check is an explosion is needed
			width = 128;
			height = 128;
			rows = 1;
			cols = 10;
			try {
				//load the image
				bigImg = ImageIO.read(getClass().getResourceAsStream("/img/explode.png"));
			} catch (IOException e) {
				System.out.println("The animation file requested does not exist! Please fix this before continuing!");
			}

			for(int i = 0; i < rows; i++){ //load the array with the stages of explosion
				for(int j = 0; j < cols; j++){
					explode[(i*cols)+j] = bigImg.getSubimage(j*width, i*height, width, height);
				}

			}
		}
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public Point getLocation() {
		return location;
	}

	public int getX() {
		return (int) location.getX();
	}

	public int getY() {
		return (int) location.getY();
	}

	public BufferedImage queryImage() {
		if(type.equals("smoke")){ //start sending smoke animation
			if(tick >= 99){
				BufferedImage temp = smoke[tick/10];
				tick = 0;
				ArrayList<Drawable> d = Main.getTerrain().getDrawable();
				d.remove(this); //animation removes itself
				Main.getTerrain().setDrawable(d);
				return temp;
			}
			else{
				BufferedImage temp = smoke[tick/10];
				tick++;
				return temp;
			}
		}
		
		if(type.equals("explode")){ //start sending explosion animation
			if(tick >= 29){
				BufferedImage temp = explode[tick/3];
				tick = 0;
				ArrayList<Drawable> d = Main.getTerrain().getDrawable();
				d.remove(this); //animation removes itself
				Main.getTerrain().setDrawable(d);
				return temp;
			}
			else{
				BufferedImage temp = explode[tick/3];
				tick++;
				return temp;
			}
		}
		return null;
	}
}
