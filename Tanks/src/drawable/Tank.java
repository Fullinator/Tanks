package drawable;

import javax.imageio.ImageIO;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Tank implements Drawable2 {
	private double barrelAngle = 0.0;
	private double tankAngle = 0.0;
	private int gas = 500;
	private Point location;
	private BufferedImage image;
	private double healthPercent;
	private String name;
	private int launchPower;

	public Tank() {
		healthPercent = 1.0f;
		name = "";
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/img/temporaryTank.png"));
		} catch (IOException e) {
			System.out.println("The tank file requested does not exist! Please fix this before continuing!");
		}
		location = new Point(100, 100000);
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public double getHealth() {
		return healthPercent;
	}

	public void setHealth(double healthPercent) {
		this.healthPercent = healthPercent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLaunchPower() {
		return launchPower;
	}

	public void setLaunchPower(int launchPower) {
		this.launchPower = launchPower;
	}

	public void adjustLaunchPower(int launchPower) {
		this.launchPower = Math.max(0, this.launchPower + launchPower);
	}

	public void setBarrelAngle(double angle) { barrelAngle = angle; }

	public double getBarrelAngle() { return barrelAngle; }

	public void adjustBarrelAngle(double angle) {
		barrelAngle += angle;
	}

	public void setTankAngle(double angle) { tankAngle = angle; }

	public double getTankAngle() { return tankAngle; }

	public void setGas(int gas) { this.gas = gas; }

	public int getGas() { return gas; }

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
		return image;
	}

	public void startMotion(boolean goLeft) {

	}

	public void stopMotion() {

	}

	public void aimCannon(boolean counterClockWise) {

	}

	public void stopAimCannon() {

	}

	/**
	 * Used to find the angle of rotation of the tank for keeping it level on the terrain
	 *
	 * @param x X position of the tank
	 * @param points Terrain array of points for determining slope
	 *
	 * @return the angle in radians
	 */
	public double angle(int x, int[][] points) {
		int y1 = 0;
		int y2 = 0;
		for(int i = 0; i < points[x].length; i += 1){
			if(points[x][i] > 0){
				y1 = i;
				break;
			}
		}

		for(int i = 0; i < points[x].length; i += 1){
			if(points[x - 15][i] > 0){
				y2 = i;
				break;
			}
		}
		double angle = Math.atan((y2 - y1) / -15.0);
		tankAngle = angle;
		return angle;
	}//end of angle method
}
