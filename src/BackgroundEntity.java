import java.awt.Color;
import java.awt.Graphics2D;

/**
 * 
 * Background entity.
 *
 */
public class BackgroundEntity extends Entity {
	/** Height of the background*/
	protected double height;
	/** Width of the background*/
	protected double width;
	/** Color of the background*/
	protected Color color;
	
	/**
	 * Background Entity constructor.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 */
	public BackgroundEntity(double x, double y, double width, double height, Color color) {
		super(x, y);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}

	/**
	 * Draw Background entity.
	 */
	@Override
	void draw(Graphics2D g2d) {
		g2d.setColor(color);
		g2d.fillRect(getActualX(), getActualY(), (int)width, (int)height);
	}
}
