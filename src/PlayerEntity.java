import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class PlayerEntity extends Entity {
	private static final int PLAYER_SPEED = 400;
	private long lastPlantedDynamite;
	private long plantDynamiteInterval = 500;
	private int numberOfDynamites = 3;

	public PlayerEntity(double x, double y) {
		super(x, y);
	}

	public void tick(Game game) {
		// Movement
		KeyInputHandler keyHandler = game.getKeyInputHandler();
		double step = PLAYER_SPEED * game.getFPSHandler().getFrameTime();
		double dx = 0;
		double dy = 0;		

		if(keyHandler.isUpPressed())
			dy -= step;

		if(keyHandler.isDownPressed())
			dy += step;

		if(keyHandler.isLeftPressed())
			dx -= step;

		if(keyHandler.isRightPressed())
			dx += step;

		if(x + dx < 0)
			dx = 0;

		if(y + dy < 0)
			dy = 0;

		if(x+48 + dx > board.getWidth())
			dx = 0;

		if(y+48 + dy > board.getHeight())
			dy = 0;

		for(Entity entity : board.entities) {
			if(entity == this)
				continue;
			if(entity instanceof BackgroundEntity)
				continue;			
			if(entity instanceof DynamiteEntity)
				continue;

			Rectangle2D.Double r_player;
			Rectangle2D.Double r_brick;
			
			r_player = new Rectangle2D.Double(x, y, 48, 48);
			if(entity instanceof PlayerEntity)
				r_brick = new Rectangle2D.Double(entity.x, entity.y, 48, 48);
			else
				r_brick = new Rectangle2D.Double(entity.x, entity.y, 64, 64);

			r_player.x += dx;
			if(r_player.intersects(r_brick)) {
				r_player.x -= dx;
				dx = 0;
			}

			r_player.y += dy;
			if(r_player.intersects(r_brick)) {
				r_player.y -= dy;
				dy = 0;
			}
		}

		move(dx, dy);
		
		// Plant the dynamite
		
		if(keyHandler.isSpacePressed())
			TryPlantDynamite(game);
	}

	private void TryPlantDynamite(Game game) {
		if(System.currentTimeMillis() - lastPlantedDynamite < plantDynamiteInterval)
			return;
		
		if(numberOfDynamites < 1)
			return;
		
		lastPlantedDynamite = System.currentTimeMillis();
		game.PlantDynamite(this);
		numberOfDynamites--;		
	}
	
	public void gainDynamite() {
		numberOfDynamites++;
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.red);
		g2d.fillRect(getActualX(), getActualY(), 48, 48);
	}

}
