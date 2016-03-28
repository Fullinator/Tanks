package physics;

import drawable.Tank;
import terrain.Terrain;

public class terrainDestroyer extends Projectile{

	public terrainDestroyer(Tank tank,Terrain terrain) {
		super(tank,terrain);
		damage = 5;
		terrainMag = 75;
	}

}
