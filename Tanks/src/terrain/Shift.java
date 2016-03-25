package terrain;

public class Shift {
/*paint(....)
* shift = call shift(...);
* then add shift in pointComponent(......y + shift..)
 */

	static int Y;
	static int prevY;
	static int shift;
	public Shift(boolean out, int y){
		prevY = Y;
		Y = y;
		shift = shifter(out, Y);		
	}
	
	/**
	 * 
	 * @param out = tells if it is in or out of the screen
	 * @param y = y value
	 * @param maxHeight = when the projectile starts moving down
	 * @return the shift amount needed
	 */
	int shifter(boolean out, int y){
		if(out){ 				// if out of screen
			if(y < prevY){
				return shift-10;		//shifting up
			}
			else if(y > prevY){ 
				return shift+10;		//shift down
			}
			else 
				return shift;
		}
		else{					//if out of the screen
			return 0;
		}
	}
	
}

