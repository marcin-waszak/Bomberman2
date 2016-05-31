import java.awt.Graphics2D;

/**
 * 
 * Dynamite entity class.
 *
 */
public class DynamiteEntity extends Entity {
	/**Sprite handle*/
	private Sprite sprite;
	/**Plant time*/
	private long plantTime;
	/**Time to explosion of dynamite*/
	private long dynamiteTime = 2000;
	/**Dynamite owner handle*/
	private PlayerEntity owner;
	/**Flag that informs if player gone away from his bomb*/
	private boolean lost;

	/*
	 * Bomb constructor.
	 */
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
		this.plantTime = System.currentTimeMillis();
	}
	
	/**
	 * Tick dynamite entity
	 */
	public void tick(Game game) {
		if(System.currentTimeMillis() - plantTime > dynamiteTime)
			game.explodeDynamite(this);
		
		if(!lost && !this.collidesWith(owner))
			lost = true;
	}
	
	/**
	 * provides dynamite owner.
	 * @return
	 */
	public PlayerEntity getOwner() {
		return owner;
	}
	
	/**
	 * Flag that informs if player gone away from his bomb.
	 * @return
	 */
	public boolean isLost() {
		return lost;
	}
	
	/**
	 * Draw dynamite entity.
	 */
	@Override
	void draw(Graphics2D g2d) {
		sprite.draw(g2d, getActualX(), getActualY());
	}
}
