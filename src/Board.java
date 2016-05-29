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
		if(entity.getX() < 0)
			return false;
		
		if(entity.getY() < 0)
			return false;
		
		if(entity.getX() >= getWidth())
			return false;
		
		if(entity.getY() >= getHeight())
			return false;
		
		entity.setBoard(this);
		
		to_add.add(entity);
		return true;
	}
	
	public boolean remove(Entity entity) {
		to_remove.add(entity);
		return true;
	}
	
	public int entitiesCount() {
		return entities.size();
	}
	
	public void tick() {
		entities.addAll(to_add);
		to_add.clear();
		entities.removeAll(to_remove);
		to_remove.clear();
		
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
