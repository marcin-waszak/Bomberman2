import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Board {
	protected Rectangle rectangle;
	protected ArrayList<Entity> entities;
	
	public Board(int x, int y, int width, int height) {
		rectangle = new Rectangle(x, y, width, height);
		entities = new ArrayList<Entity>();
	}
	
	public void add(Entity entity) {
		entity.setBoard(this);
		entities.add(entity);
	}
	
	public void draw(Graphics2D g2d) {
		for(Entity entity : entities)
			entity.draw(g2d);
	}
	
	public int getX() {
		return rectangle.x;
	}
	
	public int getY() {
		return rectangle.y;
	}
	
	public int getWidth() {
		return rectangle.width;
	}
	
	public int getHeight() {
		return rectangle.height;
	}

}
