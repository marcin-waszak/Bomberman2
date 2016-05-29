import java.awt.Graphics2D;

public class BrickEntity extends Entity {
	private Sprite sprite;

	public BrickEntity(double x, double y, SpriteStore spriteStore, String path) {
		super(x, y);
		this.sprite = spriteStore.getSprite(path);
		this.rectangle.x = (int)x;
		this.rectangle.y = (int)y;
		this.rectangle.width = sprite.getWidth();
		this.rectangle.height = sprite.getHeight();
	}

	@Override
	void draw(Graphics2D g2d) {
		sprite.draw(g2d, getActualX(), getActualY());
	}

}
