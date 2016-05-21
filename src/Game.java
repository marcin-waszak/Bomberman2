import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends Canvas {	
	private BufferStrategy strategy;
	private FPS fps;

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
		frame.setResizable(false);
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// add a key input system (defined below) to our canvas
		// so we can respond to key pressed
//		addKeyListener(new KeyInputHandler());
		
		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		// make FPS handler
		fps = new FPS();
		
		// initialize the entities in our game so there's something
		// to see at startup
//		initEntities();
	}
	
	public void gameLoop() {
		int m_fps;
		
		while(true)
		{
			m_fps = fps.measure();
			
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.black);
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setColor(Color.red);
			g.drawString("FPS: " + m_fps, 400, 600);
			
			g.dispose();
			strategy.show();
			
			fps.stabilize();
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
