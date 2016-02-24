package physics;

import java.awt.*;
import java.awt.image.BufferedImage;

import drawable.drawable;
import drawable.standardShell;

import java.math.*;
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

	public Projectile(Tank tank, IntFunction<Integer> findY){
		this.tank = tank;
		intX = tank.getX();
		intY = findY.apply((int)intX);
		System.out.println("X:" + intX);
		System.out.println("Y:" + intY);
		x0 = intX;
		y0 = intY;
		g = 1;
		angle = tank.getBarrelAngle();
		System.out.println("Angle:" + angle);
		wind = new Wind();
		windSpeed= wind.getWindSpeed();
		System.out.println("WindSpeed:" + windSpeed);
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
		vX = vX + windSpeed*this.time;
		points = new double[2];
		intX = x0 + vX * this.time;
		intY = y0 + vY * this.time + 0.5  * Math.pow(this.time, 2);


		System.out.println("Velocity: <" + vX + ", " + vY + ">\tLocation: (" + intX +", " + intY + ")");
		return points;
	}

	public double getAngle() {

		return angle;
	}

}
