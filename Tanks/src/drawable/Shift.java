package drawable;

public class Shift {
/*paint(....)
* shift = call shift(...);
* then add shift in pointComponent(......y + shift..)
 */
//h=sqrt(v)/2
	
	protected Shift(boolean out, int y, int maxHeight){
		shifter(out, y, maxHeight);		
	}
	
	/**
	 * 
	 * @param out = tells if it is in or out of the screen
	 * @param y = y value
	 * @param maxHeight = when the projectile starts moving down
	 * @return the shift amount needed
	 */
	int shifter(boolean out, int y, int maxHeight){
		if(out){ 				// if out of screen
			if(y > maxHeight){
				return -10;		//shifting up
			}
			else{ 
				return 10;		//shift down
			}
		}
		else{					//if out of the screen
			return 0;
		}
	}
	
}
