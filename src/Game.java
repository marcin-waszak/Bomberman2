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

public class Game extends Canvas {
	static final int R_WIDTH = 1024;
	static final int R_HEIGHT = 720;
	static final int NUM_OF_DIFFERENT_BONUSES = 2;	
	
	private Semaphore waitOnOtherPlayer;
	private Random random;
	private KeyInputHandler keyInputHandler;
	private BufferStrategy strategy;
	private FPS fps;
	private SpriteStore spriteStore;
	
	private Multiplayer multiplayer;
	private MessageListener messageListener;
	
	private GameBoard gameBoard;
	private Board statusBoard;

	private PlayerEntity localPlayer;
	private PlayerEntity remotePlayer;
	
	private TextEntity fpsText;
	private TextEntity dynamitesText;
	private TextEntity rangeText;
	private TextEntity entitiesText;
	private TextEntity pointsText;
	private TextEntity wonGameText;
	private TextEntity lostGameText;
	private TextEntity waitOnOtherPlayerText;
	
	private int points;
	private boolean ended_game;
	private boolean lost_game;

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
		ended_game = false;
		lost_game  = false;
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
	}
	
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
		waitOnOtherPlayerText = new TextEntity(200, 407, "Waiting on other player...", 40.F, Color.YELLOW);

		statusBoard.add(fpsText);
		statusBoard.add(dynamitesText);
		statusBoard.add(rangeText);
		statusBoard.add(entitiesText);
		statusBoard.add(pointsText);
		
		initTemporaryEntities();
	}
	
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
				for(int y = 0; y < 11; y++) {
					if(!isSpawnPoint(x, y) && (random.nextInt(5) <= 3)) {
						int i;
						if(random.nextInt(5) <= 0) {
							i = random.nextInt(NUM_OF_DIFFERENT_BONUSES) + 1;
						} else {
							i = 0;
						}
						gameBoard.add(new BoxEntity(64*x, 64*y, spriteStore, "sprites/box.png", i));					
						multiplayer.sendMessage(1 << 0 | x << 4 | y << 16 | i << 28);
					}
					if(x % 2 == 1) {
						y++;
					}
				}			
			}
		}	
		synchronize();
	}

	private Graphics2D initGraphics() {
		Graphics2D g2d = (Graphics2D)strategy.getDrawGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		return g2d;
	}
	
	private void updateTexts() {
		fpsText.setText("FPS: " + fps.getValue());
		dynamitesText.setText("Dynamites: " + localPlayer.getDynamitesCount());
		rangeText.setText("Range: " + localPlayer.getDynamiteRange());
		entitiesText.setText("Entities: " + gameBoard.entitiesCount());
		pointsText.setText("Points: " + points);
	}
	
	private void handleBeamCollisions() {
		for(Entity entity : gameBoard.getEntities())
			if(entity instanceof BeamEntity)
				for(Entity anotherEntity : gameBoard.getEntities()) {
					if(entity.collidesWith(anotherEntity)) {
						if(anotherEntity instanceof PlayerEntity) {
							if(((PlayerEntity)anotherEntity).isRemote() == true) {
								lost_game = false;
								points++;
								gameBoard.add(wonGameText);
							} else {
								lost_game = true;
								gameBoard.add(lostGameText);
							}
							ended_game = true;
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
	
	private void handlePickingUp() {
		for(Entity entity : gameBoard.getEntities())
			if(entity instanceof PlayerEntity)
				for(Entity anotherEntity : gameBoard.getEntities()) {
					if(entity.collidesWith(anotherEntity)) {
						if(anotherEntity instanceof PickupEntity) {
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
	
	private void doLogic() {		
			for(Entity entity : gameBoard.getEntities())
				entity.tick(this);
			
			updateTexts();
			handleBeamCollisions();
			handlePickingUp();
			
			statusBoard.tick();
			gameBoard.tick();
	}
	
	private void clearTemporaryEntities() {
		gameBoard.removeAllEntities();
		
	}

	private void clear(Graphics2D g2d) {
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, getWidth(), getHeight());
	}
	
	private void draw(Graphics2D g2d) 	{
		gameBoard.draw(g2d);
		statusBoard.draw(g2d);

		g2d.dispose();
		strategy.show();
	}
	
	public KeyInputHandler getKeyInputHandler() {
		return keyInputHandler;
	}
	
	public FPS getFPSHandler() { //TODO
		return fps;
	}
	
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
	
	public void PlantDynamite(PlayerEntity player) {		
		Point gPoint = gameBoard.getGridPixel(24+player.getX(), 24+player.getY());
		if(gameBoard.add(new DynamiteEntity(gPoint.x, gPoint.y,
				spriteStore, "sprites/dynamite.png", player))) {
			multiplayer.sendMessage(3 << 0 | gPoint.x << 4 | gPoint.y << 16 );
			player.decreaseDynamite();
		}
	}
	
	public void explodeDynamite(DynamiteEntity dynamite) {
		PlayerEntity owner = dynamite.getOwner();
		int range = owner.getDynamiteRange();
		
		gameBoard.remove(dynamite);
		owner.increaseDynamites();
		
		outerloop:
		for(int i = 0; i <= range; i++) {
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
	
	private void synchronize() {
		multiplayer.sendMessage(9);
		try {
			waitOnOtherPlayer.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void notifyAddBox(int x, int y, int i) {
		gameBoard.add(new BoxEntity(64*x, 64*y, spriteStore, "sprites/box.png", i));
	}
	
	public void notifySemaphoreRelease() {
		waitOnOtherPlayer.release();
	}
	
	public void notifySetRemotePlayerPosition(int x, int y) {
		remotePlayer.setPosition(x, y);
	}
	
	public void notifyRemotePlayerSettingBomb(int x, int y) {
		gameBoard.add(new DynamiteEntity(x, y,
				spriteStore, "sprites/dynamite.png", remotePlayer));
	}
	
	public void finishBeam(BeamEntity beam) {
		gameBoard.remove(beam);
	}
	
	public void gameLoop() {
		Graphics2D g2d;
		
		while(true) {
			fps.measure();	
			
			if(ended_game == false) {
				doLogic();
				g2d = initGraphics();			
				clear(g2d);
				draw(g2d);
			} else {
				if(keyInputHandler.isEnterPressed() == true) {
					ended_game = false;
					gameBoard.add(waitOnOtherPlayerText);
					gameBoard.tick();
					g2d = initGraphics();			
					clear(g2d);
					draw(g2d);
					clearTemporaryEntities();
					initTemporaryEntities();
				}
			}
			fps.stabilize();
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		
		game.gameLoop();

	}

}
