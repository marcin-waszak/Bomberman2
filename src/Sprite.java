import java.awt.Graphics;
import java.awt.Image;
/**
 * A sprite to de displayed on board.
 */
public class Sprite {
	/** The image to be drawn */
	private Image image;

	/**
	 * Create a new sprite using the image provided
	 *
	 * @param image The image that is this sprite
	 */
	public Sprite(Image image) {
		this.image = image;
	}

	/**
	 * Get the width of maintained image
	 * 
	 * @return The width in pixels of image
	 */
	public int getWidth() {
		return image.getWidth(null);
	}

	/**
	 * Get the height of maintained image
	 *
	 * @return The height in pixels of image
	 */
	public int getHeight() {
		return image.getHeight(null);
	}

	/**
	 * Draw the sprite onto the graphics provided
	 *
	 * @param g The graphics on which the sprite should be drawn
	 * @param x The x Axis localization where the sprite should be drawn
	 * @param 8 The y Axis localization where the sprite should be drawn
	 */
	public void draw(Graphics g, int x, int y) {
		g.drawImage(image, x, y, null);
	}
}