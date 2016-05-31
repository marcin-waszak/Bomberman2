import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * 
 * General entity class.
 *
 */
public abstract class Entity {
	/** X position*/
	protected double x;
	/** Y position*/
	protected double y;
	/** Board handle*/
	protected Board board;
	/** X offset*/
	protected int offsetX;
	/** Y offset*/
	protected int offsetY;
	/** Ractangle for collisions*/
	protected Rectangle rectangle;
	
	/**
	 * Constructor of our entity.
	 * @param x X position.
	 * @param y Y position.
	 */
	public Entity(double x, double y) {
		setPosition(x, y);
		board = null;
		offsetX = 0;
		offsetY = 0;
		rectangle = new Rectangle();
	}
	
	/**
	 * Set containing board.
	 * @param board Board handle.
	 */
	public void setBoard(Board board) {
		this.board = board;
		offsetX = board.getX();
		offsetY = board.getY();
	}
	
	/**
	 * Set positon of entity.
	 * @param x
	 * @param y
	 */
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Move entitiy by dx and dy.
	 * @param dx
	 * @param dy
	 */
	public void move(double dx, double dy) {
		this.x += dx;
		this.y += dy;
	}
	
	/**
	 *
	 * @return
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Return X position with containing board offset.
	 * @return
	 */
	protected int getActualX() {
		return (int)x + offsetX;
	}
	
	/**
	 * Return Y position with containing board offset.
	 * @return
	 */
	protected int getActualY() {
		return (int)y + offsetY;
	}
	
	/*
	 * Provides collision rectangle.
	 */
	public Rectangle getRectangle() {
		return rectangle;
	}
	
	/*
	 * Check the collision with other entity.
	 */
	public boolean collidesWith(Entity entity) {
		if(rectangle.intersects(entity.getRectangle()))
			return true;
		return false;
	}
	
	/*
	 * Tick the entity (refresh)
	 */
	public void tick(Game game) {
		
	}
	
	/*
	 * Draw the entity.
	 */
	abstract void draw(Graphics2D g2d);
}
