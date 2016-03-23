package drawable;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Main.Main;

public class Animation implements Drawable2{

	private Point location;
	String type;
	public int tick = 0;
	BufferedImage[] smoke = new BufferedImage[10];
	BufferedImage[] explode = new BufferedImage[10];
	BufferedImage bigImg = null;
	int width, height, rows, cols;

	public Animation(String type){

		this.type = type;
		
		if(type.equals("smoke")){
			width = 32;
			height = 32;
			rows = 1;
			cols = 10;
			try {
				bigImg = ImageIO.read(getClass().getResourceAsStream("/img/smoke1.png"));
			} catch (IOException e) {
				System.out.println("The animation file requested does not exist! Please fix this before continuing!");
			}

			for(int i = 0; i < rows; i++){
				for(int j = 0; j < cols; j++){
					smoke[(i*cols)+j] = bigImg.getSubimage(j*width, i*height, width, height);
				}

			}
		}
		
		if(type.equals("explode")){
			width = 128;
			height = 128;
			rows = 1;
			cols = 10;
			try {
				bigImg = ImageIO.read(getClass().getResourceAsStream("/img/explode.png"));
			} catch (IOException e) {
				System.out.println("The animation file requested does not exist! Please fix this before continuing!");
			}

			for(int i = 0; i < rows; i++){
				for(int j = 0; j < cols; j++){
					explode[(i*cols)+j] = bigImg.getSubimage(j*width, i*height, width, height);
				}

			}
		}
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	@Override
	public Point getLocation() {
		return location;
	}

	@Override
	public int getX() {
		return (int) location.getX();
	}

	@Override
	public int getY() {
		return (int) location.getY();
	}

	@Override
	public BufferedImage queryImage() {
		if(type.equals("smoke")){
			if(tick >= 99){
				BufferedImage temp = smoke[tick/10];
				tick = 0;
				ArrayList<Drawable2> d = Main.getTerrain().getDrawable();
				d.remove(this);
				Main.getTerrain().setDrawable(d);
				return temp;
			}
			else if(tick >= 90){
				BufferedImage temp = smoke[tick/10];
				tick++;
				return temp;
				
			}
			else{
				BufferedImage temp = smoke[tick/10];
				tick++;
				return temp;

			}
		}
		
		if(type.equals("explode")){
			if(tick >= 99){
				BufferedImage temp = explode[tick/10];
				tick = 0;
				ArrayList<Drawable2> d = Main.getTerrain().getDrawable();
				d.remove(this);
				Main.getTerrain().setDrawable(d);
				return temp;
			}
			else if(tick >= 90){
				BufferedImage temp = explode[tick/10];
				tick++;
				return temp;
				
			}
			else{
				BufferedImage temp = explode[tick/10];
				tick++;
				return temp;

			}
		}
		return null;
	}

}
