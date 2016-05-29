import java.awt.Graphics2D;

public class BeamEntity extends Entity {
	private Sprite sprite;
	private long explosionTime;
	private long beamTime = 500;
	
	public BeamEntity(double x, double y, SpriteStore spriteStore, String path) {
		super(x, y);
		this.sprite = spriteStore.getSprite(path);
		this.explosionTime = System.currentTimeMillis();
		this.rectangle.x = (int)x;
		this.rectangle.y = (int)y;
		this.rectangle.width = sprite.getWidth();
		this.rectangle.height = sprite.getHeight();
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
