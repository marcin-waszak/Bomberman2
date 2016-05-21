import java.awt.Color;
import java.awt.Graphics2D;

public class BackgroundEntity extends Entity {
	protected int x;
	protected int y;
	protected int height;
	protected int width;
	protected Color color;
	
	public BackgroundEntity(double x, double y, double width, double height, Color color) {
		super(x, y);
		this.x = (int)x;
		this.y = (int)y;
		this.width = (int)width;
		this.height = (int)height;
		this.color = color;
	}

	@Override
	void draw(Graphics2D g2d) {
		g2d.setColor(color);
		g2d.fillRect(x, y, width, height);
	}

}
