package drawable;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Animation implements Drawable2{

	private Point location;
	String type;
	public int tick = 0;
	BufferedImage[] smoke = new BufferedImage[10];
	BufferedImage bigImg = null;
	int width, height, rows, cols;

	public Animation(String type){

		this.type = type;
		width = 128;
		height = 128;
		rows = 1;
		cols = 10;
		if(type == "smoke"){
			try {
				bigImg = ImageIO.read(getClass().getResourceAsStream("/img/smoke.png"));
			} catch (IOException e) {
				System.out.println("The animation file requested does not exist! Please fix this before continuing!");
			}

			for(int i = 0; i < rows; i++){
				for(int j = 0; j < cols; j++){
					smoke[(i*cols)+j] = bigImg.getSubimage(j*width, i*height, width, height);
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
		if(type == "smoke"){
			if(tick >= 10){
				tick = 0;
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
		return null;
	}

}
