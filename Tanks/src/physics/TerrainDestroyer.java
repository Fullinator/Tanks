package physics;

import drawable.Tank;
import terrain.Terrain;

public class TerrainDestroyer extends Projectile{

	public TerrainDestroyer(Tank tank,Terrain terrain) {
		super(tank,terrain);
		damage = 5;
		terrainMag = 75;
	}

}
