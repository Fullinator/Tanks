package physics;

import java.awt.*;
import java.awt.image.BufferedImage;

import drawable.drawable;
import drawable.standardShell;

import java.math.*;
import java.util.function.IntFunction;

import drawable.Tank;
import terrain.Terrain;
import java.util.function.*;
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

	public Projectile(Tank tank,int X, int Y){
		this.tank = tank;
		intX = X;
		System.out.println("X:" + intX);
		intY = Y;
		System.out.println("Y:" + intY);
		x0 = intX;
		y0 = intY;
		g = 1;
		angle = tank.getBarrelAngle();
		System.out.println("Angle:" + angle);
		wind = new Wind();
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
		System.out.println("do I get here?");
		double Ttime = (time * Math.pow(10,-9));
		this.time = Ttime + this.time;
		//get time in seconds
		vX = vX + windSpeed*this.time;
		System.out.println(vX);
		System.out.println(vY);
		points = new double[2];
		intX = (double) x0 + vX * (double) this.time;
		intY = (double) y0 + vY * (double) this.time + 0.5  * Math.pow(this.time, 2);


		System.out.println(intX +" " + intY);
		return points;
	}

	public double getAngle() {

		return angle;
	}
	public void run(long time){
		System.out.println(points);
		fire(time);
		System.out.println(points);


	}

}
