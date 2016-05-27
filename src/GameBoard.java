import java.awt.Point;

public class GameBoard extends Board {
	int[][] grid;

	public GameBoard(int x, int y, int width, int height) {
		super(x, y, width, height);
		grid = new int[13][11];
		
		for(int i = 0; i < 13; i++)
			for(int k = 0; k < 11; k++)
				grid[i][k] = 0;
		
		grid[0][1] = 1;
		grid[1][0] = 1;
	}
	
	public boolean add(Entity entity) {
		Point gp = getGrid(entity.getX(), entity.getY());
		
		if(grid[gp.x][gp.y] != 0) 
			return false;
		
		entity.setBoard(this);
		entities.add(entity);
		
		if(!(entity instanceof BackgroundEntity))
			grid[gp.x][gp.y] = 1;
		
		return true;
	}
	
	public Point getGrid(double x, double y) {
		return new Point((int)(x/64.0), (int)(y/64.0));
	}
	
	public Point getGridPixel(double x, double y) {
		return new Point(
				rectangle.x + 64*(int)(x/64.0), rectangle.y + 64*(int)(y/64.0));
	}

}
