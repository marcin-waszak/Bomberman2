import java.awt.Color;
import java.awt.Graphics2D;

public class TextEntity extends Entity {
	private Color color;
	private String text;
	
	public TextEntity(double x, double y, Color color, String text) {
		super(x, y);
		this.color = color;
		this.text = text;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	@Override
	void draw(Graphics2D g2d) {
		g2d.setColor(color);
		g2d.drawString(text, getActualX(), getActualY());
	}

}
