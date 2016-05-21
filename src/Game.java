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
	private Board gameBoard;
	private Board statusBoard;

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
		
		// finally make the window visible 
		frame.setResizable(false);
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// add a key input system (defined below) to our canvas
		// so we can respond to key pressed
		addKeyListener(new KeyInputHandler());
		
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
		
		gameBoard = new Board(8, 8, 300, 200);
		statusBoard = new Board(340, 8, 64, 128);
		
		gameBoard.add(new BackgroundEntity(
				gameBoard.getX(), gameBoard.getY(),
				gameBoard.getWidth(), gameBoard.getHeight(),
				Color.green));
		
		statusBoard.add(new BackgroundEntity(
				statusBoard.getX(), statusBoard.getY(),
				statusBoard.getWidth(), statusBoard.getHeight(),
				Color.blue));
	}
	
	
	public void gameLoop() {
		int m_fps;

		while(true)
		{
			m_fps = fps.measure();
			
			Graphics2D g2d = (Graphics2D) strategy.getDrawGraphics();
			g2d.setColor(Color.black);
			g2d.fillRect(0, 0, getWidth(), getHeight());

			g2d.setColor(Color.red);
			g2d.drawString("FPS: " + m_fps, 400, 600);
			
			gameBoard.draw(g2d);
			statusBoard.draw(g2d);
			
			

			g2d.dispose();
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
