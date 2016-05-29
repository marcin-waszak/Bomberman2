import java.awt.Graphics2D;

public class PickupEntity extends Entity {
	private Sprite sprite;
	private int bonus;
	private boolean invulnerability;
	private long spawnTime;

	public PickupEntity(double x, double y, SpriteStore spriteStore, String path, int bonus) {
		super(x, y);
		this.sprite = spriteStore.getSprite(path);
		this.bonus = bonus;
		this.invulnerability = true;
		this.spawnTime = System.currentTimeMillis();
		this.rectangle.x = (int)x;
		this.rectangle.y = (int)y;
		this.rectangle.width = sprite.getWidth();
		this.rectangle.height = sprite.getHeight();
	}
	
	public void tick(Game game) {
		if(invulnerability && System.currentTimeMillis() - spawnTime > 501)
			invulnerability = false;
	}
	
	public int getBonus() {
		return bonus;
	}
	
	public boolean getInvulnerability() {
		return invulnerability;
	}

	@Override
	public void draw(Graphics2D g2d) {
		sprite.draw(g2d, getActualX(), getActualY());
	}

}
