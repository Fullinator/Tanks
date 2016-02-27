package drawable;

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
		// select target
	}
}//end of AITank class
