package physics;

import java.awt.*;
import java.awt.image.BufferedImage;

import drawable.drawable;
import drawable.standardShell;

import java.math.*;

import terrain.Terrain;

public  class Projectile{
	protected int power;
	protected static double intX;
	protected static double intY;
	protected static double x0;
	protected static double y0;
	protected static double g;
	protected double angle;
	protected double maxPower;
	private Terrain terrain;
	private standardShell std;
	private static double wind;
	public static double vX;
	public static double vY;
	public double height;
	public static double[] points;

	public Projectile(double x, double y, Terrain terr, Wind wind){
		terrain = terr;
		intX = x;
		intY= y;
		x0 = x;
		y0 = y;
		g = 1;
		angle = Math.tan(y/x);
		maxPower = 100;
		this.wind = wind.getWindSpeed();
		vX = 0;
		vY = 0;
		double[] points = new double[2];
		height = 0;
		
	}

	public Projectile() {
		// TODO Auto-generated constructor stub
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power){
		this.power = power;
		vX = (double) this.power * Math.cos(angle);
		vY = (double) this.power* Math.sin(angle);
	}
	
	public void setAngle(double ang){
		angle = ang;
	}
	
	public void setAngle(){
		angle = Math.tan(y0/x0);
	}
	
	public double[] fire(int tick,double freq, double x, double y){
		//freq is frequency of calls per second: ie freq = 10, 10 ticks per sec
		System.out.println("do I get here?");
		double time = tick/freq;
		
		vX = vX - wind;
		x = (double) x + vX * (double) time;
		y = (double) y + vY * (double) time + 0.5  * Math.pow(time, 2);
		points = new double[2];
		points[0] = x;
		points[1] = y;
		System.out.println(points[0] +" " + points[1]);
		return points;
	}

	public double getAngle() {
		
		return angle;
	}

	
}
