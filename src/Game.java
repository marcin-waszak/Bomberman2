import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * Main Game Class that extends Canvas. We need to use it only once, and only
 * in Main() method.
 *
 */

public class Game extends Canvas {
	/** Width of game window*/
	static final int R_WIDTH = 1024;
	/** Height of game window*/
	static final int R_HEIGHT = 720;
	/** Number of bonuses in the game*/
	static final int NUM_OF_DIFFERENT_BONUSES = 2;	
	
	/** Semaphore needed to synnc players at the beginning and at the end of game*/
	private Semaphore waitOnOtherPlayer;
	/** Handle for random*/
	private Random random;
	/** Handle for keyboard input*/
	private KeyInputHandler keyInputHandler;
	/** Buffer strategy used for double buffering*/
	private BufferStrategy strategy;
	/** Main FPS handle*/
	private FPS fps;
	/** Main sprite store handle*/
	private SpriteStore spriteStore;
	/** Handler for multiplayer game*/
	private Multiplayer multiplayer;
	/** Handle for messages thread*/
	private MessageListener messageListener;
	
	/** Handle for game playground*/
	private GameBoard gameBoard;
	/** Handle for statistics space*/
	private Board statusBoard;

	/** Handle for local player*/
	private PlayerEntity localPlayer;
	/** Handle for remote player*/
	private PlayerEntity remotePlayer;
	
	/** Handle for fps text*/
	private TextEntity fpsText;
	/** Handle for dynamites number text*/
	private TextEntity dynamitesText;
	/** Handle for range od dynamite text*/
	private TextEntity rangeText;
	/** Handle for entity number text*/
	private TextEntity entitiesText;
	/** Handle for points counter text*/
	private TextEntity pointsText;
	/** Handle for winning text*/
	private TextEntity wonGameText;
	/** Handle for loosing text*/
	private TextEntity lostGameText;
	/** Handle for drawing the round text*/
	private TextEntity drawGameText;
	/** Handle for awaiting fot another player text*/
	private TextEntity waitOnOtherPlayerText;
	
	/** Flag for indicate epilog text*/
	private boolean isEpilogTextSet;
	
	/** Variable containing current players points*/
	private int points;
	/** Flag for indicate end of game*/
	private boolean gameEnded;
	/** Flag for indicate a death of remote player*/
	private boolean remotePlayerDied;
	/** Flag for indicate a death of local player*/
	private boolean localPlayerDied;
	
	/** Main handle for MIDI player for background music*/
	private BackgroundMusic backgroundMusic;

	/**
	 * Constructor method for Game class.
	 * It is main game class, we are using it only once.
	 */
	public Game() {		
		// create or join server
		multiplayer = new Multiplayer();
		
		waitOnOtherPlayer = new Semaphore(0);
		
		// create a thread for receiving messages
		messageListener = new MessageListener(this, multiplayer);
		new Thread(messageListener).start();
		
		// create a frame to contain our game
		JFrame frame = new JFrame("Bomberman 2");
		
		// get hold the content of the frame and set up the resolution of the game
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setPreferredSize(new Dimension(R_WIDTH, R_HEIGHT));
		panel.setLayout(null);
		
		// setup our canvas size and put it into the content of the frame
		setBounds(0, 0, R_WIDTH, R_HEIGHT);
		panel.add(this);
		
		// Tell AWT not to bother repainting our canvas since we're
		// going to do that our self in accelerated mode
		setIgnoreRepaint(true);
		
		// finally make the window visible 
		frame.setResizable(false);
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		random = new Random();
		points = 0;
		gameEnded = false;
		remotePlayerDied  = false;
		localPlayerDied = false;
		isEpilogTextSet = false;
		// add a key input system (defined below) to our canvas
		// so we can respond to key pressed
		keyInputHandler = new KeyInputHandler();
		addKeyListener(keyInputHandler);
		
		// request the focus so key events come to us
		requestFocus();

		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		// make FPS handler
		fps = new FPS();
		
		spriteStore = new SpriteStore();
		gameBoard = new GameBoard(8, 8, 832, 704);
		statusBoard = new Board(848, 8, 168, 704);
		
		initEntities();

		try {
			backgroundMusic = new BackgroundMusic();
			backgroundMusic.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SoundEffect.init();
		SoundEffect.volume = SoundEffect.Volume.HIGH;  // un-mute
	}
	
	/**
	 * Method for initialization of static entities only.
	 */
	private void initEntities() {	
		statusBoard.add(new BackgroundEntity(
				0, 0, statusBoard.getWidth(), statusBoard.getHeight(),
				Color.getHSBColor(1/3.f, 0.3f, 1.f)));
		
		fpsText = new TextEntity(0, 10, "FPS: 0");
		dynamitesText = new TextEntity(0, 24, "Dynamites: 0");
		rangeText = new TextEntity(0, 38, "Range: 0");
		entitiesText = new TextEntity(0, 52, "Entities: 0");
		pointsText = new TextEntity(0, 66, "Points: 0");
		
		wonGameText = new TextEntity(40, 357, "You Won! Press ENTER to start next round!", 40.F, Color.ORANGE);
		lostGameText = new TextEntity(40, 357, "You Lost! Press ENTER to start next round!", 40.F, Color.CYAN);
		drawGameText = new TextEntity(40, 357, "A Draw! Press ENTER to start next round!", 40.F, Color.WHITE);
		waitOnOtherPlayerText = new TextEntity(200, 407, "Waiting on other player...", 40.F, Color.YELLOW);

		statusBoard.add(fpsText);
		statusBoard.add(dynamitesText);
		statusBoard.add(rangeText);
		statusBoard.add(entitiesText);
		statusBoard.add(pointsText);
		
		initTemporaryEntities();
	}
	
	/**
	 * Method for initialization of dynamic entities only.
	 */
	private void initTemporaryEntities() {	
		gameBoard.add(new BackgroundEntity(
				0, 0, gameBoard.getWidth(), gameBoard.getHeight(),
				Color.getHSBColor(2/3.f, 0.3f, .1f)));
		
		if(multiplayer.getIs_server() == true) {
			localPlayer = new PlayerEntity(0, 0, Color.BLUE, multiplayer, false);
			remotePlayer = new PlayerEntity(832 - PlayerEntity.getPlayerSize(), 704 - PlayerEntity.getPlayerSize(), Color.GREEN, multiplayer, true);
		} else {
			localPlayer = new PlayerEntity(832 - PlayerEntity.getPlayerSize(), 704 - PlayerEntity.getPlayerSize(), Color.GREEN, multiplayer, false);
			remotePlayer = new PlayerEntity(0, 0, Color.BLUE, multiplayer, true);
		}
		
		synchronize();
		
		gameBoard.add(localPlayer);
		gameBoard.add(remotePlayer);
		
		for(int i = 0; i < 6; i++)
			for(int k = 0; k < 5; k++) 
				gameBoard.add(new BrickEntity(64*(1+2*i), 64*(1+2*k), spriteStore, "sprites/brick.png"));
		
		if(multiplayer.getIs_server() == true) {
			for(int x = 0; x < 13; x++) {
				for(int y = 0; y < 11; y += (x % 2 == 1 ? 2 : 1)) {
					if(!isSpawnPoint(x, y) && (random.nextInt(5) <= 3)) {
						spawnBox(x, y);
					}
				}			
			}
		}	
		synchronize();
	}

	/**
	 * Method for Graphics 2D handler initialization.
	 * @return Graphics2D handler.
	 */
	private Graphics2D initGraphics() {
		Graphics2D g2d = (Graphics2D)strategy.getDrawGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		return g2d;
	}
	
	/**
	 * Method used for refreshing statistics texts.
	 */
	private void updateTexts() {
		fpsText.setText("FPS: " + fps.getValue());
		dynamitesText.setText("Dynamites: " + localPlayer.getDynamitesCount());
		rangeText.setText("Range: " + localPlayer.getDynamiteRange());
		entitiesText.setText("Entities: " + gameBoard.entitiesCount());
		pointsText.setText("Points: " + points);
	}
	
	/**
	 * Method used for detecting and handling beam collisions.
	 */
	private void handleBeamCollisions() {
		for(Entity entity : gameBoard.getEntities())
			if(entity instanceof BeamEntity)
				for(Entity anotherEntity : gameBoard.getEntities()) {
					if(entity.collidesWith(anotherEntity)) {
						if(anotherEntity instanceof PlayerEntity) {
							SoundEffect.GIBS.play();
							if(((PlayerEntity)anotherEntity).isRemote() == true) {
								points++;
								remotePlayerDied = true;
							} else {
								localPlayerDied = true;
							}
							gameEnded = true;
							gameBoard.remove(anotherEntity);
						}
						else if(anotherEntity instanceof BoxEntity) {
							gameBoard.remove(anotherEntity);
							if(((BoxEntity)anotherEntity).getBonus() > 0) {
								gameBoard.add(new PickupEntity(anotherEntity.x,
								anotherEntity.y, spriteStore, "sprites/pickup.png", ((BoxEntity) anotherEntity).getBonus()));
							}
						}
						else if(anotherEntity instanceof PickupEntity
								&& !((PickupEntity)anotherEntity).getInvulnerability())
							gameBoard.remove(anotherEntity);
						else if(anotherEntity instanceof DynamiteEntity)
							explodeDynamite((DynamiteEntity)anotherEntity);
					}
				}
	}
	
	/**
	 * Method used for detecting and handing picking up items.
	 */
	private void handlePickingUp() {
		for(Entity entity : gameBoard.getEntities())
			if(entity instanceof PlayerEntity)
				for(Entity anotherEntity : gameBoard.getEntities()) {
					if(entity.collidesWith(anotherEntity)) {
						if(anotherEntity instanceof PickupEntity) {
							SoundEffect.PICKUP.play();
							switch(((PickupEntity)anotherEntity).getBonus()) {
							case 1:
								((PlayerEntity) entity).increaseDynamites();
								break;
							case 2:
								((PlayerEntity) entity).increaseDynamiteRange();
								break;
							default: break;
							}							
							gameBoard.remove(anotherEntity);
						}
					}
				}
	}	
	
	/**
	 * Method used for making logical tick for all entities in game.
	 */
	private void doLogic() {		
			for(Entity entity : gameBoard.getEntities())
				entity.tick(this);
			
			updateTexts();
			handleBeamCollisions();
			handlePickingUp();
						
			statusBoard.tick();
			gameBoard.tick();
	}
	
	/**
	 * Method used for clearing dynamic entities.
	 */
	private void clearTemporaryEntities() {
		gameBoard.removeAllEntities();
		
	}

	/**
	 * Method used for clearing Graphics2D handle.
	 * @param g2d Graphics2D handle.
	 */
	private void clear(Graphics2D g2d) {
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, getWidth(), getHeight());
	}
	
	/**
	 * Method used showing up all items contained in boards.
	 * @param g2d Graphics2D handle.
	 */
	private void draw(Graphics2D g2d) 	{
		gameBoard.draw(g2d);
		statusBoard.draw(g2d);

		g2d.dispose();
		strategy.show();
	}
	
	/**
	 * Provides main key input handler.
	 * @return Main key input handler.
	 */
	public KeyInputHandler getKeyInputHandler() {
		return keyInputHandler;
	}
	
	/**
	 * Provides main key input handler.
	 * @return Main key input handler.
	 */
	public FPS getFPSHandler() {
		return fps;
	}
	
	/**
	 * Method used for checking if point is placed on spawnpoint.
	 * @param gx X coord.
	 * @param gy Y coord.
	 * @return The verdict.
	 */
	private boolean isSpawnPoint(int gx, int gy) {
		if(gx == 0 && gy == 0)
			return true;
		if(gx == 0 && gy == 1)
			return true;
		if(gx == 1 && gy == 0)
			return true;
		if(gx == 12 && gy == 10)
			return true;
		if(gx == 11 && gy == 10)
			return true;
		if(gx == 12 && gy == 9)
			return true;
		
		return false;
	}
	
	/**
	 * Method used for penetration of different entities.
	 * @param beam Beam handle.
	 * @param entity Entity handle.
	 * @return 0 for doing nothing,
	 * 1 for continue of current loop request,
	 * 2 for break outerloop request.
	 */
	private int beamPenetrator(BeamEntity beam, Entity entity) {		
		if(entity instanceof BackgroundEntity)
			return 1; // continue
		
		if(entity instanceof BeamEntity)
			return 1;
		
		if(!beam.collidesWith(entity))
			return 1;
		
		if(entity instanceof BrickEntity)
			return 2; // break outerloop		

		if(entity instanceof PlayerEntity)
			return 2;
		
		if(entity instanceof PickupEntity)
			return 2;
		
		if(entity instanceof BoxEntity)
			return 2;
		
		return 0; // do nothing
	}
	
	/**
	 * Force to plant the dynamite.
	 * @param player Owner of the our new dynamite.
	 */
	public void PlantDynamite(PlayerEntity player) {		
		Point gPoint = gameBoard.getGridPixel(24+player.getX(), 24+player.getY());
		if(gameBoard.add(new DynamiteEntity(gPoint.x, gPoint.y,
				spriteStore, "sprites/dynamite.png", player))) {
			multiplayer.sendMessage(3 << 0 | gPoint.x << 4 | gPoint.y << 16 );
			player.decreaseDynamite();
		}
		
		SoundEffect.PLANT.play();
	}
	
	/**
	 * Force to dotanate the dynamite.
	 * @param dynamite Dynamite handle we want to detonate.
	 */
	public void explodeDynamite(DynamiteEntity dynamite) {
		PlayerEntity owner = dynamite.getOwner();
		int range = owner.getDynamiteRange();
		
		gameBoard.remove(dynamite);
		owner.increaseDynamites();
		
		SoundEffect.EXPLOSION.play();
		
		BeamEntity middle_beam = new BeamEntity(dynamite.getX(), dynamite.getY(),
				spriteStore, "sprites/beam.png");
		
		gameBoard.add(middle_beam);
		
		outerloop:
		for(int i = 1; i <= range; i++) {
			BeamEntity beam = new BeamEntity(dynamite.getX() + i*64, dynamite.getY(),
					spriteStore, "sprites/beam.png");
			
			gameBoard.add(beam);
			
			for(Entity entity : gameBoard.getEntities()) {				
				switch(beamPenetrator(beam, entity)) {
				case 1: continue;
				case 2: break outerloop;
				default: break;
				}
			}
		}
		
		outerloop:
		for(int i = 1; i <= range; i++) {
			BeamEntity beam = new BeamEntity(dynamite.getX() - i*64, dynamite.getY(),
					spriteStore, "sprites/beam.png");
			
			gameBoard.add(beam);
			
			for(Entity entity : gameBoard.getEntities()) {				
				switch(beamPenetrator(beam, entity)) {
					case 1: continue;
					case 2: break outerloop;
					default: break;
				}
			}
		}
		
		outerloop:
		for(int i = 1; i <= range; i++) {
			BeamEntity beam = new BeamEntity(dynamite.getX(), dynamite.getY() + i*64,
					spriteStore, "sprites/beam.png");
			
			gameBoard.add(beam);
			
			for(Entity entity : gameBoard.getEntities()) {				
				switch(beamPenetrator(beam, entity)) {
					case 1: continue;
					case 2: break outerloop;
					default: break;
				}
			}
		}
		
		outerloop:
		for(int i = 1; i <= range; i++) {
			BeamEntity beam = new BeamEntity(dynamite.getX(), dynamite.getY() - i*64,
					spriteStore, "sprites/beam.png");
			
			gameBoard.add(beam);
			
			for(Entity entity : gameBoard.getEntities()) {				
				switch(beamPenetrator(beam, entity)) {
					case 1: continue;
					case 2: break outerloop;
					default: break;
				}
			}
		}
	}
	
	/**
	 * Force to create BoxEntity and attach optional pickup.
	 * @param x X grid position.
	 * @param y Y grid position.
	 */
	private void spawnBox(int x, int y) {
		int i = ((random.nextInt(5) <= 0) ? (random.nextInt(NUM_OF_DIFFERENT_BONUSES) + 1) : 0);
		gameBoard.add(new BoxEntity(64*x, 64*y, spriteStore, "sprites/box.png", i));					
		multiplayer.sendMessage(1 << 0 | x << 4 | y << 16 | i << 28);
	}
	
	/**
	 * Handles epilog text.
	 */
	private void epilogText() {
		if(isEpilogTextSet == false) {
			if(localPlayerDied == true) {			
				localPlayerDied = false;	
				if(remotePlayerDied == true) {
					remotePlayerDied = false;				
					gameBoard.add(drawGameText);
				} else {
					gameBoard.add(lostGameText);
				}
			} else {
				remotePlayerDied = false;			
				gameBoard.add(wonGameText);
			}
			
			isEpilogTextSet = true;		
			gameBoard.tick();
			
			Graphics2D g2d = initGraphics();			
			clear(g2d);
			draw(g2d);
		}
	}
	
	/**
	 * Handles end of the round.
	 */
	private void endRound() {
			epilogText();
			
			if(keyInputHandler.isEnterPressed() == true) {
				gameEnded = false;
				isEpilogTextSet = false;
				
				gameBoard.add(waitOnOtherPlayerText);
				gameBoard.tick();
				
				Graphics2D g2d = initGraphics();			
				clear(g2d);
				draw(g2d);
				
				clearTemporaryEntities();
				initTemporaryEntities();
			}
	}
	
	/**
	 * 
	 */
	private void synchronize() {
		multiplayer.sendMessage(9);
		try {
			waitOnOtherPlayer.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add box notification.
	 * @param x X position.
	 * @param y Y position.
	 * @param i Bonus type, 0 when there's no bonus.
	 */
	public void notifyAddBox(int x, int y, int bonus_type) {
		gameBoard.add(new BoxEntity(64*x, 64*y, spriteStore, "sprites/box.png", bonus_type));
	}
	
	/**
	 * Force semaphore release.
	 */
	public void notifySemaphoreRelease() {
		waitOnOtherPlayer.release();
	}
	
	/**
	 * Force set player position.
	 * @param x X new postion.
	 * @param y Y new position.
	 */
	public void notifySetRemotePlayerPosition(int x, int y) {
		remotePlayer.setPosition(x, y);
	}
	
	/**
	 * Force to plant the bomb by remote.
	 * @param x X bomb position.
	 * @param y Y bomb position.
	 */
	public void notifyRemotePlayerSettingBomb(int x, int y) {
		gameBoard.add(new DynamiteEntity(x, y,
				spriteStore, "sprites/dynamite.png", remotePlayer));
	}
	
	/**
	 * Force to finish the beam.
	 * @param beam Beam handle we want to finish.
	 */
	public void finishBeam(BeamEntity beam) {
		gameBoard.remove(beam);
	}
	
	/**
	 * Main infinite game loop.
	 */
	public void gameLoop() {
		Graphics2D g2d;
		
		while(true) {
			fps.measure();	
			
			if(gameEnded == false)
				doLogic();
			else
				endRound();
			
			g2d = initGraphics();			
			clear(g2d);
			draw(g2d);
			fps.stabilize();
		}
	}
	
	/**
	 * The beginning of whole world.
	 * @param args
	 */
	public static void main(String[] args) {
		Game game = new Game();
		
		game.gameLoop();

	}

}
