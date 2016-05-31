import java.awt.Graphics2D;

public class BoxEntity extends Entity {
	/** Sprite handle*/
	private Sprite sprite;
	/**Bonus type*/
	private int bonus;
	
	/**
	 * Box entity constructor.
	 * @param x
	 * @param y
	 * @param spriteStore
	 * @param path Path to texture.
	 * @param bonus Bonus type.
	 */
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
	
	/**
	 * Provides bonus type.
	 * @return
	 */
	public int getBonus() {
		return bonus;
	}

	/**
	 * Draw Box entity.
	 */
	@Override
	void draw(Graphics2D g2d) {
		sprite.draw(g2d, getActualX(), getActualY());
	}
	
}
