package drawable;

import Main.Main;
import Main.Ticker;
import terrain.Terrain;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongConsumer;

public abstract class Tank implements Drawable2 {
	private double barrelAngle = 0.0;
	private double tankAngle = 0.0;
	private int gas = 500;
	private Point location;
	private BufferedImage image;
	private int healthPercent;
	private String name;
	private int launchPower;

	private int motionTickerID = -1;
	private int cannonTickerID = -1;
	private boolean goLeft;
	private boolean counterClockwise;
	private int motionTarget;
	private double cannonTarget;
	private Consumer<Boolean> motionCompleteCallback;
	private Consumer<Boolean> cannonCompleteCallback;

	protected Color barrelColor;

	public Tank() {
		launchPower = 10;
		healthPercent = 100;
		name = "";
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/img/temporaryTank.png"));
		} catch (IOException e) {
			System.out.println("The tank file requested does not exist! Please fix this before continuing!");
		}
		location = new Point(100, 100000);
		barrelColor = new Color(Color.HSBtoRGB((float) Math.random(), 1.0f, 1.0f));
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

	public int getHealth() {
		return healthPercent;
	}

	public void setHealth(int healthPercent) {
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

	public void setBarrelAngle(double angle) { barrelAngle = angle; }

	public double getBarrelAngle() { return barrelAngle; }

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
		this.goLeft = goLeft;
		if (motionTickerID == -1) motionTickerID = Ticker.addMethod(this::moveTank);
		Main.sound.runLoop("movement");
	}

	public void startMotion(int target, Consumer<Boolean> callback) {
		motionTarget = Math.max(0, Math.min(target, Main.xLength - queryImage().getWidth()));
		if (motionTickerID == -1) {
			motionTickerID = Ticker.addMethod(this::moveTankTarget);
			motionCompleteCallback = callback;
			Main.sound.runLoop("movement");
		} else {
			callback.accept(false);
		}
	}

	public void stopMotion() {
		Ticker.removeMethod(motionTickerID);
		motionTickerID = -1;
		Main.sound.stop("movement");
	}

	public void aimCannon(boolean counterClockWise) {
		this.counterClockwise = counterClockWise;
		if (cannonTickerID == -1) cannonTickerID = Ticker.addMethod(this::rotateCannon);
		Main.sound.runLoop("turret");
	}

	public void aimCannon(double target, Consumer<Boolean> callback) {
		cannonTarget = Math.max(0, Math.min(target, Math.PI));
		if (cannonTickerID == -1) {
			cannonTickerID = Ticker.addMethod(this::rotateCannonTarget);
			cannonCompleteCallback = callback;
			Main.sound.runLoop("turret");
		} else {
			callback.accept(false);
		}
	}

	public void stopAimCannon() {
		Ticker.removeMethod(cannonTickerID);
		cannonTickerID = -1;
		Main.sound.stop("turret");
	}

	private void moveTank(long elapsedNanos) {
		double speed = 100.0 * ((double) elapsedNanos / 1000000000);
		double newX = location.getX() + (goLeft ? -speed : speed);
		if (newX > 0 && newX < Main.xLength - queryImage().getWidth()) location.setLocation(newX, 1000);
	}

	private void moveTankTarget(long elapsedNanos) {
		double speed = 100.0 * ((double) elapsedNanos / 1000000000);
		boolean left = getX() > motionTarget;
		double newX = location.getX() + (left ? -speed : speed);
		if ((left && newX <= motionTarget) || (!left && newX >= motionTarget)) {
			newX = motionTarget;
			Ticker.removeMethod(motionTickerID);
			motionTickerID = -1;
			motionCompleteCallback.accept(true);
			Main.sound.stop("movement");
		}
		location.setLocation(newX, 1000);
	}

	private void rotateCannon(long elapsedNanos) {
		double rate = 3.0 * ((double) elapsedNanos / 1000000000);
		double newAng = barrelAngle + (counterClockwise ? rate : -rate);
		if (newAng >= 0 && newAng <= Math.PI) barrelAngle = newAng;
	}

	private void rotateCannonTarget(long elapsedNanos) {
		double rate = 1.0 * ((double) elapsedNanos / 1000000000);
		boolean ccw = barrelAngle < cannonTarget;
		double newAng = barrelAngle + (ccw ? rate : -rate);
		if ((ccw && newAng >= cannonTarget) || (!ccw && newAng <= cannonTarget)) {
			newAng = cannonTarget;
			Ticker.removeMethod(cannonTickerID);
			cannonTickerID = -1;
			cannonCompleteCallback.accept(true);
			Main.sound.stop("turret");
		}
		barrelAngle = newAng;
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
		/*for(int i = 0; i < points[x].length; i += 1){
			if(points[x][i] > 0 && i++ < points[x].length && points[x][i++] <= 0){
				y1 = i;
				break;
			}
		}

		for(int i = 0; i < points[x].length; i += 1){
			if(points[x - 15][i] > 0 && i++ < points[x].length && points[x - 15][i++] <= 0){
				y2 = i;
				break;
			}
		}
		*/
		
		if(x > 0 && x < points.length){//find Y position from damage
			for(int i = 0; i < points[0].length; i ++ ){
				if(points[x][i] > 0){
					y1 = i;
					break;
				}
			}
		}
		
		if ( x - 15 < 0) {
			x = 0;
		} else {
			x = x - 15;
		}
		for(int i = 0; i < points[0].length; i ++ ){
			if(points[x][i] > 0){
				y2 = i;
				break;
			}
		}
		
		System.out.println(y1 + "     " + y2);
		if (y1 == 0 || y2 == 0 || y1 > 690 || y2 > 690){//y2 == 0 && y1 > (points[0].length - 20) || y1 == 0 && y2 > (points[0].length - 20)) {
			System.out.println("zero!");
			return 0;
		}
		
		double angle = Math.atan((y2 - y1) / -15.0);
		tankAngle = angle;
		return angle;
	}//end of angle method

	public Point getCenterPoint(Terrain terrain) {
		double angle = angle(getX() + 20 , terrain.getTerrain());
		int length = (int) (queryImage().getWidth() * Math.cos(angle));
		return new Point(getX() + (length/2), terrain.findY(getX() + (length/2)) - (queryImage().getHeight() /2) );
	}

	public Color getBarrelColor() {
		return barrelColor;
	}
}
