import java.awt.Graphics2D;

public class BoxEntity extends Entity {
	private Sprite sprite;
	private int bonus;
	
	public BoxEntity(double x, double y, SpriteStore spriteStore, String path,
			int bonus) {
		super(x, y);
		this.sprite = spriteStore.getSprite(path);
		this.bonus = bonus;
		this.rectangle.x = (int)x;
		this.rectangle.y = (int)y;
		this.rectangle.width = sprite.getWidth();
		this.rectangle.height = sprite.getHeight();
	}
	
	public int getBonus() {
		return bonus;
	}

	@Override
	void draw(Graphics2D g2d) {
		sprite.draw(g2d, getActualX(), getActualY());
	}

}
