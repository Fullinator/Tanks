package physics;

public abstract class Friction {
	public static boolean forest = false;
	public static boolean sand = false;
	public static boolean snow= false;
	final static double sandCoef = .58;
	final static double forestCoef = .96; 
	final static double snowCoef = .89;
	
	public Friction(){
		forest = false;
		sand = false;
		snow = false;
	}
	
	public static void Forest(){
		forest = true;
		sand = false;
		snow = false;
	}
	public static void Sand(){
		forest = false;
		sand = true;
		snow = false;
	}
	public static void Snow(){
		forest = false;
		sand = false;
		snow  = true;
	}
	
	public static double getFriction(){
		if(forest == true){
			return forestCoef;
		}
		if(sand == true){
			return sandCoef;
		}
		if(snow == true){
			return snowCoef;
		}
		return 0;
	}
}
