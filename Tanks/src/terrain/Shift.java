package terrain;

public class Shift {
/*paint(....)
* shift = call shift(...);
* then add shift in pointComponent(......y + shift..)
 */
	//get this to take just projectile list of shots
	

	static int Y;
	static int prevY;
	static int shift;
	int x = 10;
	public Shift(double[] points){
		shift = shifter(points);		
	}
	
	/**
	 * 
	 * @param out = tells if it is in or out of the screen
	 * @param y = y value
	 * @param maxHeight = when the projectile starts moving down
	 * @return the shift amount needed
	 */
	int shifter(double[] points){
		prevY = Y;
		Y = (int) points[1];
		if(physics.Projectile.outOfScreen){ 				// if out of screen
			if(Y < prevY){
				//System.out.println("OUT UP : y = "+ Y + "prevY=" + prevY);
				//shift = shift+x;
				shift=shift+Math.abs((int)physics.Projectile.vY);
				return shift;		//shifting up
			}
			else if(Y > prevY){ 
				//System.out.println("OUT down");
				//shift = shift-x;
				shift=shift-Math.abs((int)physics.Projectile.vY);
				return shift;		//shift down
			}
			else 
				return shift;
		}
		else{					//if not out of the screen
		//	System.out.println("IN");
			shift = 0;
			return shift;
		}
	}
	
}

