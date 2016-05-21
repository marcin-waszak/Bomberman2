import java.awt.Rectangle;
import java.util.ArrayList;

public class Board {
	protected Rectangle rectangle;
	protected ArrayList<Entity> entities;
	
	public Board(int x, int y, int width, int height) {
		rectangle = new Rectangle(x, y, width, height);
	}
	
	public void Add(Entity entity) {
		entity.setBoard(this);
		entities.add(entity);
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
