package drawable;

import java.awt.Point;
import java.awt.image.BufferedImage;

public abstract class Tank implements Drawable2 {
	private double barrelAngle = 0.0;
	private double tankAngle = 0.0;
	private int gas = 500;
	private Point location;
	private BufferedImage image;
	private float healthPercent;
	private String name;
	private int launchPower;

	public void setLocation(Point location) {
		this.location = location;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public float getHealthPercent() {
		return healthPercent;
	}

	public void setHealthPercent(float healthPercent) {
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

	public Tank() {
		healthPercent = 1.0f;
		name = "";
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
}
