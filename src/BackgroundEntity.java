import java.awt.Color;
import java.awt.Graphics2D;

public class BackgroundEntity extends Entity {
	protected double height;
	protected double width;
	protected Color color;
	
	public BackgroundEntity(double x, double y, double width, double height, Color color) {
		super(x, y);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}

	@Override
	void draw(Graphics2D g2d) {
		g2d.setColor(color);
		g2d.fillRect(getActualX(), getActualY(), (int)width, (int)height);
	}
}
