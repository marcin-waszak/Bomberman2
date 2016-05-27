import java.awt.Graphics2D;

public class DynamiteEntity extends Entity {
	private SpriteStore spriteStore;
	private Sprite sprite;

	public DynamiteEntity(double x, double y, SpriteStore spriteStore, String path) {
		super(x, y);
		this.spriteStore = spriteStore;
		this.sprite = spriteStore.getSprite(path);
	}

	@Override
	void draw(Graphics2D g2d) {
		sprite.draw(g2d, getActualX(), getActualY());
	}

}
