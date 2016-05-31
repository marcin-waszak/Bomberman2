import java.awt.Point;
/**
 * GameBoard class is used to group all the game entities
 */
public class GameBoard extends Board {
	int[][] grid;

	/**
	 * GameBoard constructor
	 */
	public GameBoard(int x, int y, int width, int height) {
		super(x, y, width, height);
		grid = new int[13][11];
		
		for(int i = 0; i < 13; i++)
			for(int k = 0; k < 11; k++)
				grid[i][k] = 0;
	}
	
	/**
	 * Add entity to the game.
	 * @return Is operation succesfully added.
	 */
	public boolean add(Entity entity) {
		Point gp = getGrid(entity.getX(), entity.getY());
		
		if(entity.getX() < 0)
			return false;
		
		if(entity.getY() < 0)
			return false;
		
		if(entity.getX() >= getWidth())
			return false;
		
		if(entity.getY() >= getHeight())
			return false;
		
		if(grid[gp.x][gp.y] != 0) 
			return false;
		
		entity.setBoard(this);
		to_add.add(entity);
		
//		if(!(entity instanceof BackgroundEntity)
//				&& !(entity instanceof PlayerEntity))
//			grid[gp.x][gp.y] = 1;
		
		return true;
	}
	
	/**
	 * Remove entity from the game.
	 * @return Is operation succesfully removed.
	 */
	public boolean remove(Entity entity) {
		Point gp = getGrid(entity.getX(), entity.getY());
		
		to_remove.add(entity);		
		grid[gp.x][gp.y] = 0;
		
		return true;
	}
	
	/**
	 * Provides quantization of coordinates.
	 * @return 
	 */
	public Point getGrid(double x, double y) {
		return new Point((int)(x/64.0), (int)(y/64.0));
	}
	
	/**
	 * Provides quantization of coordinates back to pixel fotmat.
	 * @return 
	 */
	public Point getGridPixel(double x, double y) {
		return new Point(64*(int)(x/64.0), 64*(int)(y/64.0));
	}

}
