import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferStrategy;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends Canvas {
	static final int FPS = 60;
	
	private BufferStrategy strategy;

	public Game() {
		// create a frame to contain our game
		JFrame frame = new JFrame("Bomberman 2");
		
		// get hold the content of the frame and set up the resolution of the game
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setPreferredSize(new Dimension(800, 600));
		panel.setLayout(null);
		
		// setup our canvas size and put it into the content of the frame
		setBounds(0, 0, 800, 600);
		panel.add(this);
		
		// Tell AWT not to bother repainting our canvas since we're
		// going to do that our self in accelerated mode
		setIgnoreRepaint(true);
		
		// request the focus so key events come to us
		requestFocus();
		
		// finally make the window visible 
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// add a key input system (defined below) to our canvas
		// so we can respond to key pressed
//		addKeyListener(new KeyInputHandler());
		
		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		// initialize the entities in our game so there's something
		// to see at startup
//		initEntities();
	}

	public Game(GraphicsConfiguration config) {
		super(config);
		// TODO Auto-generated constructor stub
	}
	
	public void gameLoop() {
		String message = "xxxx";
		
		while(true)
		{
	    	long start = System.nanoTime();
	    	
	    	
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.black);
			g.fillRect(0, 0, getWidth(), getHeight());
	    	
	    	
			g.setColor(Color.red);
			//g.drawString(message,(800-g.getFontMetrics().stringWidth(message))/2,250);
			g.drawString(message, 400, 608);	    	
			
			g.dispose();
			strategy.show();
			
	    	// stabilize frames per second
			try {
				TimeUnit.NANOSECONDS.sleep(start + (long)(1E9/FPS) - System.nanoTime());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Game game = new Game();

		// Start the main game loop, note: this method will not
		// return until the game has finished running. Hence we are
		// using the actual main thread to run the game.
		game.gameLoop();

	}

}
