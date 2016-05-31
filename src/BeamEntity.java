import java.awt.Graphics2D;

/**
 * 
 * Beam Entity class. Appears when bomb is destroying.
 *
 */
public class BeamEntity extends Entity {
	/** Sprite handle*/
	private Sprite sprite;
	/** Moment of detonation*/
	private long explosionTime;
	/** Time of beam appearance*/
	private long beamTime = 500;
	
	/**
	 * Beam Entity constructor.
	 * @param x
	 * @param y
	 * @param spriteStore
	 * @param path
	 */
	public BeamEntity(double x, double y, SpriteStore spriteStore, String path) {
		super(x, y);
		this.sprite = spriteStore.getSprite(path);
		this.explosionTime = System.currentTimeMillis();
		this.rectangle.x = (int)x;
		this.rectangle.y = (int)y;
		this.rectangle.width = sprite.getWidth();
		this.rectangle.height = sprite.getHeight();
	}
	
	/** Tick of logic related to the Beam*/
	public void tick(Game game) {
		if(System.currentTimeMillis() - explosionTime > beamTime)
			game.finishBeam(this);
	}

	/**
	 * Draw Beam entity.
	 */
	@Override
	void draw(Graphics2D g2d) {
		sprite.draw(g2d, getActualX(), getActualY());
	}

}
