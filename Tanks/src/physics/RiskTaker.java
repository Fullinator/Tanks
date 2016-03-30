package physics;

import drawable.Tank;
import terrain.Terrain;

public class RiskTaker extends Projectile{

	public int riskDamage = 50;
	
	public RiskTaker(Tank tank,Terrain terrain) {
			super(tank,terrain);
			damage = 50;
			terrainMag = 5;
		}

}
