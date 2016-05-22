import java.awt.Color;
import java.awt.Graphics2D;

public class PlayerEntity extends Entity {
	public PlayerEntity(double x, double y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}
	
	void tick(Game game) {
		KeyInputHandler keyHandler = game.getKeyInputHandler();
		double speed = 4.0;

		if(keyHandler.isUpPressed())
			move(0.0, -speed);
		
		if(keyHandler.isDownPressed())
			move(0.0, speed);
	
		if(keyHandler.isLeftPressed())
			move(-speed, 0.0);
		
		if(keyHandler.isRightPressed())
			move(speed, 0.0);
	}

	@Override
	void draw(Graphics2D g2d) {
		g2d.setColor(Color.red);
		g2d.fillRect(getActualX(), getActualY(), 48, 48);
	}

}
