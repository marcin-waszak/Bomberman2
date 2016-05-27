import java.awt.Graphics2D;

public class BoxEntity extends Entity {
	SpriteStore spriteStore;
	Sprite sprite;

	public BoxEntity(double x, double y, SpriteStore spriteStore, String path) {
		super(x, y);
		this.spriteStore = spriteStore;
		this.sprite = spriteStore.getSprite(path);
	}

	@Override
	void draw(Graphics2D g2d) {
		sprite.draw(g2d, getActualX(), getActualY());
	}

}
