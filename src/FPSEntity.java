import java.awt.Color;
import java.awt.Graphics2D;

public class FPSEntity extends Entity {
	protected Color color;
	protected FPS fps;

	public FPSEntity(FPS fps, double x, double y, Color color) {
		super(x, y);
		this.fps = fps;
		this.color = color;
	}

	@Override
	void draw(Graphics2D g2d) {
		g2d.setColor(color);
		g2d.drawString("FPS: " + fps.getValue(), getActualX(), getActualY());
	}

}
