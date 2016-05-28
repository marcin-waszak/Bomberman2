import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Board {
	protected Rectangle rectangle;
	protected ArrayList<Entity> entities;
	protected ArrayList<Entity> to_add;
	protected ArrayList<Entity> to_remove;
	
	public Board(int x, int y, int width, int height) {
		rectangle = new Rectangle(x, y, width, height);
		entities = new ArrayList<Entity>();
		to_add = new ArrayList<Entity>();
		to_remove = new ArrayList<Entity>();
	}
	
	public boolean add(Entity entity) {
		entity.setBoard(this);
		entities.add(entity);
		return true;
	}
	
	public void toAdd(Entity entity) {
		to_add.add(entity);
	}
	
	public void toRemove(Entity entity) {
		to_remove.add(entity);
	}
	
	public void tick() {
		entities.removeAll(to_remove);
		entities.addAll(to_add);
		
		for(Entity entity :  to_add)
			entity.setBoard(this);
		
		to_remove.clear();
		to_add.clear();
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
	
	public ArrayList<Entity> getEntities() {
		return entities;
	}
}
