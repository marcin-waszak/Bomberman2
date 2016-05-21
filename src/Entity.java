import java.awt.Graphics2D;

public abstract class Entity {
	protected double x;
	protected double y;
	protected Board board;

	public Entity(double x, double y) {
		setPosition(x, y);
		board = null;
	}
	
	public void setBoard(Board board) {
		this.board = board;
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void move(double dx, double dy) {
		this.x += dx;
		this.y += dy;
	}
	
	abstract void draw(Graphics2D g2d);
}
