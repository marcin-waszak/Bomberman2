import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Entity {
	protected double x;
	protected double y;
	protected Board board;
	protected int offsetX;
	protected int offsetY;
	protected Rectangle rectangle;
	
	public Entity(double x, double y) {
		setPosition(x, y);
		board = null;
		offsetX = 0;
		offsetY = 0;
		rectangle = new Rectangle();
	}
	
	public void setBoard(Board board) {
		this.board = board;
		offsetX = board.getX();
		offsetY = board.getY();
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void move(double dx, double dy) {
		this.x += dx;
		this.y += dy;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	protected int getActualX() {
		return (int)x + offsetX;
	}
	
	protected int getActualY() {
		return (int)y + offsetY;
	}
	
	public Rectangle getRectangle() {
		return rectangle;
	}
	
	public boolean collidesWith(Entity entity) {
		if(rectangle.intersects(entity.getRectangle()))
			return true;
		return false;
	}
	
	public void tick(Game game) {
		
	}
	
	abstract void draw(Graphics2D g2d);
}
