import java.awt.Graphics2D;

public class BeamEntity extends Entity {
	private Sprite sprite;
	private long explosionTime;
	private long beamTime = 500;
	private PlayerEntity owner;
	
	public BeamEntity(double x, double y, SpriteStore spriteStore, String path,
			PlayerEntity owner) {
		super(x, y);
		this.sprite = spriteStore.getSprite(path);
		this.owner = owner;
		this.explosionTime = System.currentTimeMillis();
		
	}
	
	public void tick(Game game) {
		if(System.currentTimeMillis() - explosionTime > beamTime)
			game.finishBeam(this);
	}

	@Override
	void draw(Graphics2D g2d) {
		sprite.draw(g2d, getActualX(), getActualY());
	}

}
