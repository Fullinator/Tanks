package drawable;

import terrain.Terrain;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

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

		// Select target
		Tank target = tanks.parallelStream()
				.filter(tank -> tank != this)
				.min((o1, o2) -> o1.getHealth() - o2.getHealth()).get();

		if (target == null) {
			target = tanks.parallelStream().filter(tank -> tank != this).findAny().get();
		}

		motionComplete = cannonComplete = false;

		Random r = new Random();
		int newX = r.nextInt(owner.getXTerrain());
		System.out.println(newX);
		boolean left = getX() > newX;
		startMotion(newX, this::motionComplete);
		double idealAngle = Math.PI * r.nextDouble();
		aimCannon(idealAngle, this::cannonComplete);

	}

	private void motionComplete(boolean success) {
		motionComplete = true;
		if (cannonComplete) owner.fire();
	}

	private void cannonComplete(boolean success) {
		cannonComplete = true;

		System.out.println("cannonComplete for " + getName() + " - " + success);

		if (motionComplete) owner.fire();
	}
}//end of AITank class
