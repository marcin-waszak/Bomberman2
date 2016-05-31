import java.awt.Graphics2D;
/**
 * An entity representing a bonus panel in game.
 * If the player steps on it, it disappears and his/her state
 * is changed depending of type of bonus it was.
 */
public class PickupEntity extends Entity {
	/** A sprite, which will be displayed on screen, representing this entity */
	private Sprite sprite;
	/** 
	 * This tells what type of event will take place when a player steps on this entity 
	 * 1 - Increase by 1 number of bombs in player inventory
	 * 2 - Increase by 1 number of player's bomb beam range
	 */
	private int bonus;
	/** Is set to true this entity can't by destroyed by bombs beam */
	private boolean invulnerability;
	/** Tells the creation time of this entity. Used in changing the invulnerability flag */
	private long spawnTime;

	/**
	 * Creates a new pickup entity with provided position, sprite
	 * and type of bonus.
	 *
	 * @param x xAxis coordinates
	 * @param y yAxis coordinates
	 * @param spriteStore 
	 * @param path 
	 * @param bonus type of bonus
	 */
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
	
	/**
	 * Used for handling logic of this entity in every frame
	 * 
	 * Checks if enough time has passed since creation of this entity.
	 * If yes, then this entity loses its invulnerability status.
	 *
	 * @param game reference to the current game
	 */
	public void tick(Game game) {
		if(invulnerability && System.currentTimeMillis() - spawnTime > 501)
			invulnerability = false;
	}
	
	/**
	 * Returns type of bonus this entity provides.
	 */
	public int getBonus() {
		return bonus;
	}
	
	/**
	 * Returns true if this entity is still invulnerable
	 */
	public boolean getInvulnerability() {
		return invulnerability;
	}

	/**
	 * Draws this entity on the graphics provided
	 *
	 * @param g2d the graphics on which the sprite should be drawed
	 */
	public void draw(Graphics2D g2d) {
		sprite.draw(g2d, getActualX(), getActualY());
	}

}
