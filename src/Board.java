import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * 
 * Board class. Is used to group entities on the screen.
 *
 */
public class Board {
	/** Rectangle of the board*/
	protected Rectangle rectangle;
	/** Current entites list of the board*/
	protected ArrayList<Entity> entities;
	/** List of entities to add*/
	protected ArrayList<Entity> to_add;
	/** List of entities to remove*/
	protected ArrayList<Entity> to_remove;
	
	/**
	 * Board constructor.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Board(int x, int y, int width, int height) {
		rectangle = new Rectangle(x, y, width, height);
		entities = new ArrayList<Entity>();
		to_add = new ArrayList<Entity>();
		to_remove = new ArrayList<Entity>();
	}
	
	/**
	 * Add new entity to the board.
	 * @param entity
	 * @return
	 */
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
	
	/**
	 * Remove list of entities from the board
	 */
	public boolean removeAllEntities() {
		entities.clear();
		return true;
	}
	
	/**
	 * Remove entity from the board
	 * @param entity
	 * @return
	 */
	public boolean remove(Entity entity) {
		to_remove.add(entity);
		return true;
	}
	
	/**
	 * Get intities count.
	 * @return
	 */
	public int entitiesCount() {
		return entities.size();
	}
	
	/*
	 * Tick (refresh) all elements in the board.
	 */
	public void tick() {
		entities.addAll(to_add);
		to_add.clear();
		entities.removeAll(to_remove);
		to_remove.clear();
		
	}
	
	/**
	 * Draw all entities in buffer.
	 * @param g2d
	 */
	public void draw(Graphics2D g2d) {
		for(Entity entity : entities)
			entity.draw(g2d);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getX() {
		return rectangle.x;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getY() {
		return rectangle.y;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getWidth() {
		return rectangle.width;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getHeight() {
		return rectangle.height;
	}
	
	/**
	 * Get entities list from the board.
	 * @return
	 */
	public ArrayList<Entity> getEntities() {
		return entities;
	}
}
