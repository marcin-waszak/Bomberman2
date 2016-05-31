import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
/**
 * An entity representing the player objects in the game.
 * Used for both local and remote players. Maintains all information
 * about current state of player abilities and status. Also this has
 * a reference to multiplayer object which is required for invoking methods
 * sending messages about current player position or setting a bomb.
 */
public class PlayerEntity extends Entity {
	/** The size of width and height of rectangle which represents a player */
	private static final int playerSize = 36;
	/** This speed of player indicating how many pixels it can travel in fixed period of time */
	private static final int PLAYER_SPEED = 200;
	/** A value used for indicate how much time has passed since planting last bomb. This forbids the player from setting many bombs with just one click */
	private long lastPlantedDynamite;
	/** This value tells how much time player must wait before setting next bomb after planting one */
	private long plantDynamiteInterval = 400;
	/** Number of bombs which player has currently in inventory. This is NOT the maximum number of bomb which player can plant. */
	private int numberOfDynamites = 1;
	/** This indicates how far the beam can reach after bomb explodes */ 
	private int dynamiteRange = 1;
	/** The color of rectangle which represents a player */
	private Color color;
	/** Used for invoking send message method */
	private Multiplayer multiplayer;
	/** Used for telling if this player is remote or not from client point of view */
	private boolean remote;
	
	/**
	 * Create an entity with provided parameters.
	 * Setting x and y axis is done by Entity class constructor
	 * which this class inherits from.
	 *
	 * @param x The value of x Axis starting position
	 * @param y The value of y Axis starting position
	 * @param color The color of player
	 * @param multiplayer Reference to multiplayer object
	 * @param remote The boolean value telling if player is remote from caller point of view
	 */
	public PlayerEntity(double x, double y, Color color, Multiplayer multiplayer, boolean remote) {
		super(x, y);
		this.color = color;
		this.multiplayer = multiplayer;
		this.remote = remote;
	}
	
	/**
	 * Used for handling player logic in every frame.
	 *
	 * First checks which arrows are pressed and then calculates
	 * how far a player can move in this frame. If the player in new position
	 * would collide with other entity, which forbids moving on it, the move
	 * method is not called. If the move method was called, this then send a message
	 * to other user telling the new position of this player.
	 *
	 * Next, this checks if the user wishes to plant a bomb and if he/she does
	 * the TryPlantDynamite method is called.
	 *
	 * @param game The game on which the events should take place
	 */
	public void tick(Game game) {
		if(remote == false) {
			// Movement
			KeyInputHandler keyHandler = game.getKeyInputHandler();
			double step = PLAYER_SPEED * game.getFPSHandler().getFrameTime(); //TODO
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
	
			if(x + playerSize + dx > board.getWidth())
				dx = 0;
	
			if(y + playerSize + dy > board.getHeight())
				dy = 0;
	
			for(Entity entity : board.entities) {
				if(entity == this)
					continue;
				if(entity instanceof BackgroundEntity)
					continue;
				if(entity instanceof BeamEntity)
					continue;
				if(entity instanceof PickupEntity)
					continue;
				if(entity instanceof DynamiteEntity &&
					((DynamiteEntity) entity).getOwner() != entity &&
						!((DynamiteEntity)entity).isLost())
					continue;			
	
				Rectangle2D.Double r_player;
				Rectangle2D.Double r_brick;
				 
				r_player = new Rectangle2D.Double(x, y, playerSize, playerSize);
				if(entity instanceof PlayerEntity)
					r_brick = new Rectangle2D.Double(entity.x, entity.y, playerSize, playerSize);
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
			
			multiplayer.sendMessage(2 << 0 | (int)x << 4 | (int)y << 16);
			
			// Plant the dynamite
			if(keyHandler.isSpacePressed())
				TryPlantDynamite(game);
		}	
			
		this.rectangle.x = (int)x;
		this.rectangle.y = (int)y;
		this.rectangle.width = playerSize;
		this.rectangle.height = playerSize;
	}

	/**
	 * Plants a dynamite if two conditions are fulfilled
	 * 
	 * The first one checks is enough time has passed since planting last bomb.
	 * The second one checks if player still has some bombs in his inventory.
	 *
	 * @param game The game on which the bomb should be planted
	 */
	private void TryPlantDynamite(Game game) {
		if(System.currentTimeMillis() - lastPlantedDynamite < plantDynamiteInterval)
			return;
		
		if(numberOfDynamites < 1)
			return;
		
		lastPlantedDynamite = System.currentTimeMillis();
		game.PlantDynamite(this);	
	}
	
	/**
	 * Increases number of available dynamites in player inventory.
	 */
	public void increaseDynamites() {
		numberOfDynamites++;
	}
	
	/**
	 * Decreases number of available dynamites in player inventory.
	 */
	public void decreaseDynamite() {
		numberOfDynamites--;
	}
	
	/**
	 * Tells how many dynamites the player has left in inventory.
	 */
	public int getDynamitesCount() {
		return numberOfDynamites;
	}
	
	/**
	 * Tells the beam range of player bombs.
	 */
	public int getDynamiteRange() {
		return dynamiteRange;
	}
	
	/**
	 * Increases the beam range of player bombs.
	 */
	public void increaseDynamiteRange() {
		dynamiteRange++;
	}
	
	/**
	 * Tells the width/height of rectangle representing a player
	 */
	public static int getPlayerSize() {
		return playerSize;
	}
	
	/**
	 * Tells if the player is remote or not from caller point of view
	 */
	public boolean isRemote() {
		return remote;
	}
	
	/**
	 * Draws this entity to the graphics provided
	 *
	 * @param g2d The graphics on which to draw
	 */
	public void draw(Graphics2D g2d) {
		g2d.setColor(color);
		g2d.fillRect(getActualX(), getActualY(), playerSize, playerSize);
	}
}
