package physics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.IntFunction;
import drawable.Drawable2;

import drawable.Tank;
import terrain.Terrain;

public  class Projectile implements Drawable2 {
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
	private BufferedImage image;

	public Projectile(Tank tank,Terrain terrain){
		this.tank = tank;

		this.terrain = terrain;
		//System.out.println("X:" + intX);
		
//		intY = tank.queryImage().getHeight() + tank.getY();
		intX = tank.queryImage().getWidth()+tank.getX();
		// drawable.get(i).getX(), findY(drawable.get(i).getX()) - 17, 20, 4
		
		g = 1;
		double tankAngle = tank.angle((int)intX, terrain.getTerrain());
		angle = tank.getBarrelAngle() + tank.angle((int)intX, terrain.getTerrain());
		System.out.println("Angle:" + angle);
		intX = .5*tank.queryImage().getWidth() + tank.getX()- 20*Math.cos(angle);
		intY = terrain.findY(tank.getX())- 20*Math.sin(angle)- tank.queryImage().getHeight();
		if(tankAngle < Math.PI/2){
			intY += 20*Math.sin( tankAngle);
			intX += tank.queryImage().getWidth()*Math.sin( tankAngle);
		}
		if(tankAngle > Math.PI/2){
			intY -= 20*Math.sin(tankAngle);
			intX -= tank.queryImage().getWidth()*Math.cos( tankAngle);
		}
		System.out.println("X:" + intX);

		System.out.println("Y:" + intY);
		x0 = intX;
		y0 = intY;
		//windSpeed= wind.getWindSpeed();
		System.out.println("WindSpeed:" + windSpeed);

		double[] points = new double[2];
		points[0] = intX + 5;
		points[1] = intY + 30;
		System.out.println("Points:" + points);

		height = intY;
		time = 0;
		mass = 1;
		this.power = tank.getLaunchPower()/mass;
		setPower(this.power);

		image = new BufferedImage(5, 5, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(new Color(0, 0, 0, 0));
		g2d.drawRect(0, 0, 5, 5);
		g2d.setColor(Color.BLACK);
		g2d.fillOval(0, 0, 5, 5);
	}


	


	public double getPower() {
		return power;
	}

	public void setPower(double power){
		this.power = power/mass;
		vX = (double) this.power * Math.cos(angle+ Math.PI);
		vY = (double) this.power* Math.sin(angle+ Math.PI);
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
		double Ttime = (time * Math.pow(10,-8));
		this.time = Ttime + this.time;
		//get time in seconds
		//deduct wind from x velocity
		vX = vX + windSpeed*this.time;
		points = new double[2];
		points[0] = (x0 + vX * this.time);
		points[1] = y0 + vY * this.time + 0.5  * Math.pow(this.time, 2);


		System.out.println("Velocity: <" + vX + ", " + vY + ">\tLocation: (" + points[0] +", " + points[1] + ")");
		
		return points;
	}

	public double getAngle() {

		return angle;
	}

	@Override
	public Point getLocation() {
		return new Point((int) points[0], (int) points[1]);
	}

	@Override
	public int getX() {
		return (int) points[0];
	}

	@Override
	public int getY() {
		return (int) points[1];
	}

	@Override
	public BufferedImage queryImage() {
		return image;
	}
	
	
}
