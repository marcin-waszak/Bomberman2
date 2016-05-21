import java.awt.Graphics2D;

public abstract class Entity {
	protected double x;
	protected double y;

	public Entity(double x, double y) {
		SetPosition(x, y);
	}
	
	public void SetPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void Move(double dx, double dy) {
		this.x += dx;
		this.y += dy;
	}
	
	abstract void draw(Graphics2D g2d);
}
