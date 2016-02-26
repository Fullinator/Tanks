package physics;

public abstract class Wind {
	 double windSpeed = 0;



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