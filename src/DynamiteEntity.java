import java.awt.Graphics2D;

public class DynamiteEntity extends Entity {
	private Sprite sprite;
	private long plantTime;
	private long dynamiteTime = 2000;
	private PlayerEntity owner;
	private boolean lost;

	public DynamiteEntity(double x, double y, SpriteStore spriteStore, String path,
			PlayerEntity owner) {
		super(x, y);
		this.sprite = spriteStore.getSprite(path);
		this.rectangle.x = (int)x;
		this.rectangle.y = (int)y;
		this.rectangle.width = sprite.getWidth();
		this.rectangle.height = sprite.getHeight();
		this.owner = owner;
		this.lost = false;
		plantTime = System.currentTimeMillis();
	}
	
	public void tick(Game game) {
		if(System.currentTimeMillis() - plantTime > dynamiteTime)
			game.explodeDynamite(this);
		
		if(!lost && !this.collidesWith(owner))
			lost = true;
	}
	
	public PlayerEntity getOwner() {
		return owner;
	}
	
	public boolean isLost() {
		return lost;
	}

	@Override
	void draw(Graphics2D g2d) {
		sprite.draw(g2d, getActualX(), getActualY());
	}
}
