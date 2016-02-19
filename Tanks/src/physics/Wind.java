package physics;

public class Wind {
	protected double windSpeed;



	public Wind(){
		windSpeed = 0;
	}
	public Wind (double speed){
		windSpeed = speed;
	}
	public double getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed (double speed){
		windSpeed = speed;
	}
}