package drawable;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Particle {
	private Vector2d loc;
	private Vector2d vel;
	private Vector2d acc;
	private Vector2d size;
	private Vector2d maxSize;
	private Vector2d growth;
	private Vector2d life;
	private Color color;

	private boolean ultSize = false;
	private boolean defaultSize = false;

	public Particle(int x, int y, int dx, int dy, int size, int life, Color c){
		this.loc = new Vector2d(x,y);
		this.vel = new Vector2d(dx,dy);
		this.acc = new Vector2d(0,0);
		this.life = new Vector2d(life,life);
		this.size = new Vector2d(size,size);
		this.growth = new Vector2d(0,0);
		this.maxSize = new Vector2d(0,0);
		this.color = c;
	}

	public boolean update(){
		vel.add(acc);
		loc.add(vel);
		size.add(growth);
		life.x--;

		if(life.x <= 0)
			return true;

		if(defaultSize){
			if(size.x >= maxSize.x){
				if(size.y >= maxSize.y)
					return true;
				else
					size.x = maxSize.x;
			}
			if(size.y >= maxSize.y) //Note: we already checked if both x and y are bigger.
				size.y = maxSize.y;
			if(size.x <= 0)
				if(size.y <= 0)
					return true;
				else
					size.x = 1;
			if(size.y <= 0)
				size.y = 1;
			return false; // we are done
		}

		if(ultSize){ // We will shrink and grow back and forth
			if(size.x > maxSize.x){
				size.x = maxSize.x;
				growth.x *= -1;
			}
			if(size.y > maxSize.y){
				size.y = maxSize.y;
				growth.y *= -1;
			}
			if(size.x <= 0){
				size.x = 1;
				growth.x *= -1;
			}
			if(size.y <= 0){
				size.y = 1;
				growth.y *= -1;
			}
		}
		else{ //We stop growing or shrinking.
			if(size.x > maxSize.x)
				size.x = maxSize.x;
			if(size.y > maxSize.y)
				size.y = maxSize.y;
			if(size.x <= 0)
				size.x = 1;
			if(size.y <= 0)
				size.y = 1;
		}
		return false;
	}

	public void render(Graphics g){
		Graphics2D g2d = (Graphics2D) g.create();

		g2d.setColor(color);
		g2d.fillRect((int)(loc.x-(size.x/2)), (int)(loc.y-(size.y/2)), (int)size.x, (int)size.y);

		g2d.dispose();
	}

	public void setLoc(double x,  double y){
		loc.x = x;
		loc.y = y;
	}

	public void setVel(double x,  double y){
		vel.x = x;
		vel.y = y;
	}

	public void setAcc(double x,  double y){
		acc.x = x;
		acc.y = y;
	}

	public void setSize(double x,  double y){
		size.x = x;
		size.y = y;
	}

	public void setMaxSize(double x,  double y){
		maxSize.x = x;
		maxSize.y = y;
	}

	public void setGrowth(double x,  double y){
		growth.x = x;
		growth.y = y;
	}

	public void setLife(double num){
		life.x = num;
		life.y = num;
	}

	public void setSizeDeault(boolean c){
		defaultSize = c;
	}

	public void setUltSize(boolean c){
		defaultSize = false;
		ultSize = c;
	}

	public Vector2d getLoc(){
		return loc;
	}

	public Vector2d getVel(){
		return vel;
	}
	
	boolean isDead(){
		if(life.x < 0.0){
			return true;
		} else{
			return false;
		}
	}
}

