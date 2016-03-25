package drawable;

import Main.Ticker;
import terrain.Terrain;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.DoubleFunction;

/**
 * Artificially intelligent player.
 *
 * @author Nicholas Muggio
 */
public class AITank extends Tank {
	private List<Tank> tanks;
	private Terrain owner;

	private boolean motionComplete;
	private boolean cannonComplete;
	private Tank target;

	private int fireWhenReadyID;

	public AITank(Terrain owner, List<Tank> tanks) {
		this.owner = owner;
		this.tanks = tanks;
		barrelColor = Color.BLACK;
	}

	public void takeTurn() {
		System.out.println("Taking turn for AI " + getName());

		Tank minTank = null;
		double minCost = Double.POSITIVE_INFINITY;

		for (Tank t : tanks) {
			if (t == this) continue;
			double cost = Math.sqrt(Math.pow(getX() - t.getX(), 2) + Math.pow(getY() - t.getY(), 2)) * 3 + (100 - t.getHealth());// + (t instanceof AITank ? 500000 : 0) + (Math.random() * 100000 - 50000);
			System.out.println(getName() + " -> " + t.getName() + ": " + cost);
			if (cost < minCost) {
				minCost = cost;
				minTank = t;
			}
		}

		target = minTank;
		if (target == null) {
			System.out.println("========== NO TARGET FOUND ==========");
//			owner.nextPlayerTurn();
			owner.fire();
			return;
		}

		motionComplete = cannonComplete = false;

		int newX = Math.abs(getX() - target.getX()) < 100 ? getX() : (getX() < target.getX() ? target.getX() - 100 : target.getX() + 100);
		startMotion(newX, this::motionComplete, 0.5);

		// figure out workable angle
		double idealAngle = Math.PI;
		int power = 1;
		powerLoop:
		for (; power <= getHealth(); power++) {
			if (newX < target.getX()) {
				for (double a = 1; a > 0.5; a -= 0.05) {
					if (fuzzyTankIntersection(newX, a * Math.PI, power, target, 5)) {
						idealAngle = a * Math.PI;
						break powerLoop;
					}
				}
			} else {
				for (double a = 0; a < 0.5; a += 0.05) {
					if (fuzzyTankIntersection(newX, a * Math.PI, power, target, 5)) {
						idealAngle = a * Math.PI;
						break powerLoop;
					}
				}
			}
		}

		Random r = new Random();
//		power += r.nextGaussian() * 2;
//		idealAngle += r.nextGaussian() * 0.1;

		setLaunchPower(power);
		aimCannon(idealAngle, this::cannonComplete);

		fireWhenReadyID = Ticker.addMethod(this::fireWhenReady);
	}

	private void motionComplete(boolean success) {
		System.out.println("\t\t\t\tMotion complete = " + success);
		if (!success) {
			// figure out workable angle
			double idealAngle = Math.PI;
			int power = 1;
			powerLoop:
			for (; power <= getHealth(); power++) {
				if (getX() < target.getX()) {
					for (double a = 1; a > 0.5; a -= 0.05) {
						if (fuzzyTankIntersection(getX(), a * Math.PI, power, target, 5)) {
							idealAngle = a * Math.PI;
							break powerLoop;
						}
					}
				} else {
					for (double a = 0; a < 0.5; a += 0.05) {
						if (fuzzyTankIntersection(getX(), a * Math.PI, power, target, 5)) {
							idealAngle = a * Math.PI;
							break powerLoop;
						}
					}
				}
			}

			Random r = new Random();
//			power += r.nextGaussian() * 2;
//			idealAngle += r.nextGaussian() * 0.1;

			setLaunchPower(power);
			aimCannon(idealAngle, this::cannonComplete);
		}
		motionComplete = true;
	}

	private void cannonComplete(boolean success) {
//		Thread.dumpStack();
		System.out.println("\t\t\t\tCannon complete = " + success);
		cannonComplete = true;
	}

	private void fireWhenReady(long elapsedNanos) {
		if (cannonComplete && motionComplete) {
			owner.fire();
			Ticker.removeMethod(fireWhenReadyID);
			fireWhenReadyID = -1;
		}
	}

	private boolean fuzzyTankIntersection(int sourceX, double barrelAngle, int power, Tank target, double maxOffset) {
		// Micro helper class to make sure nothing gets abused
		class HelperTank extends Tank {}

		HelperTank t = new HelperTank();
		t.setLocation(new Point(sourceX, 0));
		t.setBarrelAngle(barrelAngle);
		t.setLaunchPower(power);
		physics.Projectile p = new physics.Projectile(t, owner);

		double[] p1 = p.fire(0, false);
		double[] p2 = p.fire(10000000000L, false);
		double[] p3 = p.fire(20000000000L, false);

		double a, b, c;

		double[][] mat =
				{
						{p1[0] * p1[0], p1[0], 1, p1[1]},
						{p2[0] * p2[0], p2[0], 1, p2[1]},
						{p3[0] * p3[0], p3[0], 1, p3[1]}
				};

		// Row-reduce matrix
		double coeff;

		// First-second
		coeff = -mat[1][0] / mat[0][0];
		mat[1][0] += mat[0][0] * coeff;
		mat[1][1] += mat[0][1] * coeff;
		mat[1][2] += mat[0][2] * coeff;
		mat[1][3] += mat[0][3] * coeff;

		// First-third
		coeff = -mat[2][0] / mat[0][0];
		mat[2][0] += mat[0][0] * coeff;
		mat[2][1] += mat[0][1] * coeff;
		mat[2][2] += mat[0][2] * coeff;
		mat[2][3] += mat[0][3] * coeff;

		// Second-third
		coeff = -mat[2][1] / mat[1][1];
		mat[2][1] += mat[1][1] * coeff;
		mat[2][2] += mat[1][2] * coeff;
		mat[2][3] += mat[1][3] * coeff;

		// Get a, b, c
		c = mat[2][3] / mat[2][2];
		b = (mat[1][3] - mat[1][2] * c) / mat[1][1];
		a = (mat[0][3] - mat[0][2] * c - mat[0][1] * b) / mat[0][0];

		// Check for rough intersection
		Point targetLoc = target.getCenterPoint(owner);
		DoubleFunction<Double> quad = value -> a * value * value + b * value + c;
//		System.out.println("Points: " + Arrays.toString(p1) + " " + Arrays.toString(p2) + " " + Arrays.toString(p3));
//		System.out.println("a: " + a + "\tb: " + b + "\tc: " + c);

		return quad.apply(targetLoc.x - maxOffset) >= targetLoc.y - maxOffset && quad.apply(targetLoc.x - maxOffset) <= targetLoc.y - maxOffset
				|| quad.apply(targetLoc.x) >= targetLoc.y - maxOffset && quad.apply(targetLoc.x) <= targetLoc.y + maxOffset
				|| quad.apply(targetLoc.x + maxOffset) >= targetLoc.y - maxOffset && quad.apply(targetLoc.x + maxOffset) <= targetLoc.y + maxOffset;
	}
}//end of AITank class
