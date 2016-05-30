import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class TextEntity extends Entity {
	private Color color;
	private String text;
	private float size;
	
	public TextEntity(double x, double y, String text) {
		super(x, y);
		this.color = Color.black;
		this.text = text;
		this.size = 12.f;
	}
	
	public TextEntity(double x, double y, String text, float size, Color color) {
		super(x, y);
		this.color = color;
		this.text = text;
		this.size = size;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setSize(float size) {
		this.size = size;
	}

	@Override
	void draw(Graphics2D g2d) {
		Font currentFont = g2d.getFont();
		Font newFont = currentFont.deriveFont(size);
		g2d.setFont(newFont);
		
		g2d.setColor(color);
		
		g2d.drawString(text, getActualX(), getActualY());
	}

}
