package drawable;

import terrain.Terrain;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.DoubleFunction;

/**
 * Artificially intelligent player.
 * 
 * @author Nicholas Muggio
 *
 */
public class AITank extends Tank {
	private List<Tank> tanks;
	private Terrain owner;

	private boolean motionComplete;
	private boolean cannonComplete;

	public AITank(Terrain owner, List<Tank> tanks) {
		this.owner = owner;
		this.tanks = tanks;
	}

	public void takeTurn() {
		System.out.println("Taking turn for AI " + getName());

		Tank minTank = null;
		double minCost = Double.POSITIVE_INFINITY;

		for (Tank t : tanks) {
			if (t == this) continue;
			double cost = Math.sqrt(Math.pow(getX() - t.getX(), 2) + Math.pow(getY() - t.getY(), 2)) * 3 + (100 - t.getHealth()) + (t instanceof AITank ? 1000 : 0);
			if (cost < minCost) {
				minCost = cost;
				minTank = t;
			}
		}

		// Select target
//		Tank target = tanks.parallelStream()
//				.filter(tank -> tank != this)
//				.min((o1, o2) -> o1.getHealth() - o2.getHealth()).get();
		Tank target = minTank;
		if (target == null) {
			owner.nextPlayerTurn();
			return;
		}

		motionComplete = cannonComplete = false;

		int newX = Math.abs(getX() - target.getX()) < 100 ? getX() : (getX() < target.getX() ? target.getX() - 100 : target.getX() + 100);
		startMotion(newX, this::motionComplete);

		// figure out workable angle
		double idealAngle = Math.PI;
		int power = getLaunchPower() / 5;
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

		setLaunchPower(power);
		aimCannon(idealAngle, this::cannonComplete);

	}

	private void motionComplete(boolean success) {
		motionComplete = true;
		if (cannonComplete) owner.fire();
	}

	private void cannonComplete(boolean success) {
		cannonComplete = true;
		if (motionComplete) owner.fire();
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
		double[] p2 = p.fire(1000000000, false);
		double[] p3 = p.fire(2000000000, false);

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
		coeff = -mat[0][0] / mat[1][0];
		mat[1][0] += mat[0][0] * coeff;
		mat[1][1] += mat[0][1] * coeff;
		mat[1][2] += mat[0][2] * coeff;
		mat[1][3] += mat[0][3] * coeff;

		// First-third
		coeff = -mat[0][0] / mat[2][0];
		mat[2][0] += mat[0][0] * coeff;
		mat[2][1] += mat[0][1] * coeff;
		mat[2][2] += mat[0][2] * coeff;
		mat[2][3] += mat[0][3] * coeff;

		// Second-third
		coeff = -mat[1][1] / mat[2][1];
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

		return quad.apply(targetLoc.x - maxOffset) >= targetLoc.y - maxOffset && quad.apply(targetLoc.x - maxOffset) <= targetLoc.y - maxOffset
				|| quad.apply(targetLoc.x) >= targetLoc.y - maxOffset && quad.apply(targetLoc.x) <= targetLoc.y + maxOffset
				|| quad.apply(targetLoc.x + maxOffset) >= targetLoc.y - maxOffset && quad.apply(targetLoc.x + maxOffset) <= targetLoc.y + maxOffset;
	}
}//end of AITank class
