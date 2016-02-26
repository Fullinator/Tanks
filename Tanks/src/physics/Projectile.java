package physics;

import java.util.function.IntFunction;

import drawable.Tank;
import terrain.Terrain;
public  class Projectile{
	double intX;
	double intY;
	double x0;
	double y0;
	double g;
	double angle;
	double power;
	Terrain terrain;
	Wind wind;
	private static double windSpeed;
	public  double vX;
	public  double vY;
	public double height;
	public double[] points;
	double time;
	double mass;
	Tank tank;

	public Projectile(Tank tank,IntFunction<Integer> findY){
		this.tank = tank;

		
		System.out.println("X:" + intX);
		

		intX = tank.getX();
		intY = findY.apply((int)intX);
		System.out.println("X:" + intX);

		System.out.println("Y:" + intY);
		x0 = intX;
		y0 = intY;
		g = 1;
		angle = tank.getBarrelAngle();
		System.out.println("Angle:" + angle);
		windSpeed= wind.getWindSpeed();
		System.out.println("WindSpeed:" + windSpeed);

		double[] points = new double[2];
		points[0] = intX;
		points[1] = intY;
		System.out.println("Points:" + points);

		height = intY;
		time = 0;
		mass = 1;
		this.power = tank.getLaunchPower()/mass;
		setPower(this.power);

	}


	


	public double getPower() {
		return power;
	}

	public void setPower(double power){
		this.power = power/mass;
		vX = (double) this.power * Math.cos(angle);
		vY = (double) this.power* Math.sin(angle);
	}

	public void setAngle(double ang){
		angle = ang;
	}


	public void setX(double x){
		intX = x;

	}
	public void setY(double y){
		intY = y;

	}

	public double[] fire(long time){
		double Ttime = (time * Math.pow(10,-9));
		this.time = Ttime + this.time;
		//get time in seconds
		//deduct wind from x velocity
		vX = vX + windSpeed*this.time;
		points = new double[2];
		points[0] = x0 + vX * this.time;
		points[1] = y0 + vY * this.time + 0.5  * Math.pow(this.time, 2);


		System.out.println("Velocity: <" + vX + ", " + vY + ">\tLocation: (" + points[0] +", " + points[1] + ")");
		
		return points;
	}

	public double getAngle() {

		return angle;
	}

}
