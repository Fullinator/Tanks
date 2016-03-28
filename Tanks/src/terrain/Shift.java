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
				System.out.println("OUT UP : y = "+ y + "prevY=" + prevY);
		//		shift = shift-x;
				return shift-x;		//shifting up
			}
			else if(y > prevY){ 
				System.out.println("OUT down");
			//	shift = shift+x;
				return shift+x;		//shift down
			}
			else 
		//		shift = shift;
				return shift;
		}
		else{					//if not out of the screen
			System.out.println("IN");
			return 0;
		}
	}
	
}

