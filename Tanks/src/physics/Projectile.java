package physics;

import java.awt.*;
import java.awt.geom.AffineTransform;
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
	public static  double vX;
	public static  double vY;
	public double height;
	public static double[] points = new double[2];
	double time;
	double mass;
	Tank tank;
	public double barrelAngle;
	private BufferedImage image;
	public static boolean outOfScreen;
	private int tickerID;
	public double tankAngle;

	public int damage = 20;
	public double MaxHeight;

//	public int damage = 1;
	public int terrainMag = 5;


	public Projectile(Tank tank,Terrain terrain){
		this.tank = tank;

		this.terrain = terrain;
		//System.out.println("X:" + intX);
		
//		intY = tank.queryImage().getHeight() + tank.getY();
		intX = tank.queryImage().getWidth()+tank.getX();
		// drawable.get(i).getX(), findY(drawable.get(i).getX()) - 17, 20, 4
		Point tankLoc = tank.getLocation();
		double tankX = tank.getX();
		double tankY = terrain.findY((int)tankX);
		g = 1;
		tankAngle = tank.angle((int)intX, terrain.getTerrain());
		double tankHeight = tank.queryImage().getHeight();
		double tankWidth = tank.queryImage().getWidth();
		double innerTankAngle = Math.atan((terrain.findY(tank.getX())-15)/(tank.getX()+20));
		double tankH = Math.pow(Math.pow(tankHeight,2) + Math.pow(tankWidth,2), .5);
		barrelAngle = tank.getBarrelAngle();
		angle = barrelAngle + tankAngle;

//		System.out.println("Angle:" + angle);
		
//		g2d.rotate(((Tank)drawable.get(i)).getBarrelAngle(), drawable.get(i).getX() + 20, findY(drawable.get(i).getX()) - 15 );
//		g2d.fillRect(drawable.get(i).getX(), findY(drawable.get(i).getX()) - 17, 20, 4);
//		g2d.setTransform(old);// resets the rotation back to how it was before the painting began

		intX = tank.getX() -20 + (terrain.findY(tank.getX()))*Math.tan(innerTankAngle)/Math.tan(innerTankAngle);
		intY = terrain.findY(tank.getX()) + 15 + (tank.getX() + 20)*Math.tan(innerTankAngle);
//		if(tankAngle < Math.PI/2){
////			intY += 20*Math.sin( tankAngle);
////			intX += 20*Math.cos( tankAngle);
//			intX += .5*tank.queryImage().getWidth()*Math.cos(innerTankAngle + Math.PI/2);
//			intY -= tank.queryImage().getHeight()*Math.sin( innerTankAngle - Math.PI/2);
//	}
//		if(tankAngle > -Math.PI/2){
////			intY -= 20*Math.sin(tankAngle);
////			intX -= 20*Math.cos( tankAngle);
//			intX += .5*tank.queryImage().getWidth()*Math.cos(innerTankAngle - Math.PI/2);
//			intY -= tank.queryImage().getHeight()*Math.sin(innerTankAngle + Math.PI/2);
//		}
//		System.out.println("X:" + intX);

		//System.out.println("Angle:" + angle);
		intX = .5*tank.queryImage().getWidth() + tank.getX();
		intY = terrain.findY(tank.getX())- tank.queryImage().getHeight();
		double[] point = getEndBarrel(intX,intY,angle);
		intX = point[0];
		intY = point[1];
//		System.out.println("PI: " + Math.PI);
//		System.out.println("ang: " + angle);
//		System.out.println("X: " + intX);
//		System.out.println("y: " + intY);
		
		
		//System.out.println("X:" + intX);


		//System.out.println("Y:" + intY);
//		if(tankAngle < Math.PI/2){
//			intY -= 20*Math.sin( tankAngle);
//			intX += 20*Math.cos( tankAngle);
//		}
//		if(tankAngle > -Math.PI/2){
//			intY -= 20*Math.sin(tankAngle);
//			intX -= 20*Math.cos( tankAngle);
//		}
		x0 = intX;
		y0 = intY;
		//windSpeed= wind.getWindSpeed();
		//System.out.println("WindSpeed:" + windSpeed);

		points = new double[2];

		points[0] = intX;
		points[1] = intY;
		//System.out.println("Points:" + points);

		height = intY;
		time = 0;
		mass = 1;
		this.power = tank.getLaunchPower()/mass;
		setPower(this.power);
		
		image = new BufferedImage(5, 5, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
//		g2d.setColor(new Color(0, 0, 0, 0));
//		g2d.drawRect(0, 0, 5, 5);
		g2d.setColor(tank.getBarrelColor());
		g2d.fillOval(0, 0, 5, 5);
		AffineTransform old = g2d.getTransform();
		g2d.rotate((tank).getBarrelAngle(), tank.getX() + 20, terrain.findY(tank.getX()) - 15 );
		

	}



	public double getPower() {
		return power;
	}

	public void setPower(double power){
		this.power = power/mass;
		vX = (double) this.power * Math.cos(angle+ Math.PI);
		vY = (double) this.power* Math.sin(angle+ Math.PI);
		
		MaxHeight = (vY*vY)/2;
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
		return fire(time, true);
	}

	public double[] fire(long time, boolean collide) {
		
		double Ttime = (time * Math.pow(10,-7.75));
		this.time = Ttime + this.time;
		//get time in seconds
		//deduct wind from x velocity
		vX = vX + windSpeed*this.time;
		points = new double[2];
		points[0] = (x0 + vX * this.time);
		points[1] = y0 + vY * this.time + 0.5  * Math.pow(this.time, 2);
		
		//System.out.println("Velocity: <" + vX + ", " + vY + ">\tLocation: (" + points[0] +", " + points[1] + ")");

		if(points[1] < 20){
			outOfScreen = true;
		}
		else{
			outOfScreen = false;
		}
		//System.out.println("Out Of Screen:"+outOfScreen);

		if (collide) terrain.collisionDetection(this);
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
	
	public double[] getEndBarrel(double x, double y, double ang){
		x += 20*Math.cos(ang - Math.PI);
		y -= 20*Math.sin(ang);
		if(ang > Math.PI){
			y = y + 4;
		}
		if(ang < 0){
			y = y + 4;
		}
		double[] newPoint = {x,y};
		return newPoint;
		
	}

	@Override
	public BufferedImage queryImage() {
		return image;
	}
	
	public void setTickerID(int ticker) {
		tickerID = ticker;
	}
	
	public int getTickerID() {
		return tickerID;
	}
	
}