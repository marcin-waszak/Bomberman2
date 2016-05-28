import java.awt.Graphics2D;

public class DynamiteEntity extends Entity {
	private SpriteStore spriteStore;
	private Sprite sprite;
	private long plantTime;
	private long dynamiteTime = 2000;
	private PlayerEntity owner;

	public DynamiteEntity(double x, double y, SpriteStore spriteStore, String path,
			PlayerEntity owner) {
		super(x, y);
		this.spriteStore = spriteStore;
		this.sprite = spriteStore.getSprite(path);
		this.owner = owner;
		
		plantTime = System.currentTimeMillis();
	}
	
	public void tick(Game game) {
		if(System.currentTimeMillis() - plantTime > dynamiteTime)
			game.explodeDynamite(this);
	}
	
	public PlayerEntity getOwner() {
		return owner;
	}

	@Override
	void draw(Graphics2D g2d) {
		sprite.draw(g2d, getActualX(), getActualY());
	}

}
