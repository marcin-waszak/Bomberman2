import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends Canvas {
	static final int R_WIDTH = 1024;
	static final int R_HEIGHT = 720;
	
	private Random random;
	private KeyInputHandler keyInputHandler;
	private BufferStrategy strategy;
	private FPS fps;
	private SpriteStore spriteStore;
	private GameBoard gameBoard;
	private Board statusBoard;
	private PlayerEntity localPlayer;
	private TextEntity fpsText;
	private TextEntity dynamitesText;	
	private TextEntity entitiesText;

	public Game() {
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
		
		// initialize the entities in our game so there's something
		// to see at startup
		// initEntities();
		
		spriteStore = new SpriteStore();
		gameBoard = new GameBoard(8, 8, 832, 704);
		statusBoard = new Board(848, 8, 168, 704);
		
		initEntities();
	}
	
	private void initEntities() {
		gameBoard.add(new BackgroundEntity(
				0, 0, gameBoard.getWidth(), gameBoard.getHeight(),
				Color.getHSBColor(2/3.f, 0.3f, .1f)));
		
		statusBoard.add(new BackgroundEntity(
				0, 0, statusBoard.getWidth(), statusBoard.getHeight(),
				Color.getHSBColor(1/3.f, 0.3f, 1.f)));
		
		fpsText = new TextEntity(0, 10, "FPS: 0");
		dynamitesText = new TextEntity(0, 24, "Dynamites: 0");
		entitiesText = new TextEntity(0, 38, "Entities: 0");

		statusBoard.add(fpsText);
		statusBoard.add(dynamitesText);
		statusBoard.add(entitiesText);
		
		localPlayer = new PlayerEntity(0, 0);
		gameBoard.add(localPlayer);
		
		for(int i = 0; i < 6; i++)
			for(int k = 0; k < 5; k++) 
				gameBoard.add(new BrickEntity(64*(1+2*i), 64*(1+2*k), spriteStore, "sprites/brick.png"));
		
		for(int i = 0; i < 23; i++) {
			int kx = random.nextInt((12 - 0) + 1) + 0;
			int ky = random.nextInt((10 - 0) + 1) + 0;
			
			if(isSpawnPoint(kx, ky) || !gameBoard.add(new BoxEntity(64*kx, 64*ky,
					spriteStore, "sprites/box.png")))  {
				i--;
				continue;
			}
		}
	}
	
	private Graphics2D initGraphics() {
		Graphics2D g2d = (Graphics2D)strategy.getDrawGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		return g2d;
	}
	
	private void doLogic() {		
		for(Entity entity : gameBoard.getEntities())
			entity.tick(this);
		
		fpsText.setText("FPS: " + fps.getValue());
		dynamitesText.setText("Dynamites: " + localPlayer.getDynamitesCount());
		entitiesText.setText("Entities: " + gameBoard.entitiesCount());
		
		statusBoard.tick();
		gameBoard.tick();
	}
	
	private void clear(Graphics2D g2d)
	{
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, getWidth(), getHeight());
	}
	
	private void draw(Graphics2D g2d)
	{
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
	
	public void PlantDynamite(PlayerEntity player) {		
		Point gPoint = gameBoard.getGridPixel(24+player.getX(), 24+player.getY());
		if(gameBoard.add(new DynamiteEntity(gPoint.x, gPoint.y,
				spriteStore, "sprites/dynamite.png", player)))
			player.decreaseDynamite();
	}
	
	public void explodeDynamite(DynamiteEntity dynamite) {
		gameBoard.remove(dynamite);
		dynamite.getOwner().increaseDynamite();
		
		BeamEntity beam = new BeamEntity(dynamite.getX(), dynamite.getY(),
				spriteStore, "sprites/beam.png", dynamite.getOwner());
		
		gameBoard.add(beam);
	}
	
	public void finishBeam(BeamEntity beam) {
		gameBoard.remove(beam);
	}
	
	public void gameLoop() {
		Graphics2D g2d;
		
		while(true)
		{
			fps.measure();	
			
			doLogic();			
			g2d = initGraphics();			
			clear(g2d);
			draw(g2d);
			
			fps.stabilize();
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		
		game.gameLoop();

	}

}
