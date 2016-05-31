import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
/**
 * An entity used for maintaining data about texts
 * which will be printed on boards throughout the game.
 */
public class TextEntity extends Entity {
	/** Color of text */
	private Color color;
	/** String to be printed as text */
	private String text;
	/** Size of font */
	private float size;
	
	/**
	 * Creates a TextEntity with provided parameters
	 *
	 * @param x xAxis coordinate of text position
	 * @param y yAxis coordinate of text position
	 * @param text text to be printed
	 */
	public TextEntity(double x, double y, String text) {
		super(x, y);
		this.color = Color.black;
		this.text = text;
		this.size = 12.f;
	}
	
	/**
	 * Creates a TextEntity with provided parameters
	 *
	 * @param x xAxis coordinate of text position
	 * @param y yAxis coordinate of text position
	 * @param text text to be printed
	 * @param size size of font
	 * @param color color of font
	 */
	public TextEntity(double x, double y, String text, float size, Color color) {
		super(x, y);
		this.color = color;
		this.text = text;
		this.size = size;
	}
	
	/**
	 * Sets a new text
	 * @param text A string type text
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Sets a new color for font
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Sets a new size of font
	 * @param size
	 */
	public void setSize(float size) {
		this.size = size;
	}

	/**
	 * Draws the font on the graphics provided
	 * 
	 * @param g The graphics on which to draw the font
	 */
	 void draw(Graphics2D g2d) {
		Font currentFont = g2d.getFont();
		Font newFont = currentFont.deriveFont(size);
		g2d.setFont(newFont);
		
		g2d.setColor(color);
		
		g2d.drawString(text, getActualX(), getActualY());
	}

}
