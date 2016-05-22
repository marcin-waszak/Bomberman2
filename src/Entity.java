import java.awt.Graphics2D;

public abstract class Entity {
	protected double x;
	protected double y;
	protected Board board;
	protected int offsetX;
	protected int offsetY;

	public Entity(double x, double y) {
		setPosition(x, y);
		board = null;
		offsetX = 0;
		offsetY = 0;
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
	
	protected int getActualX() {
		return (int)x + offsetX;
	}
	
	protected int getActualY() {
		return (int)y + offsetY;
	}
	
	abstract void draw(Graphics2D g2d);
}
