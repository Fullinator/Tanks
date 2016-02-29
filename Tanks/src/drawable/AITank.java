package drawable;

import java.util.Comparator;
import java.util.List;

/**
 * Artifically intelligent player.
 * 
 * @author Nicholas Muggio
 *
 */
public class AITank extends Tank {
	private List<Tank> tanks;

	public AITank(List<Tank> tanks) {
		this.tanks = tanks;
	}

	public void takeTurn() {
		System.out.println("Take AI turn");

		// Select target
		Tank target = tanks.parallelStream().min((o1, o2) -> o1.getHealth() - o2.getHealth()).get();
		if (target == null) {
			target = tanks.parallelStream().filter(tank -> tank != this).findAny().get();
		}

		System.out.println("Target health: " + target.getHealth());
	}
}//end of AITank class
