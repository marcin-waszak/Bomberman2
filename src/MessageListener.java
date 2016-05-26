import java.util.concurrent.Semaphore;

public class  MessageListener implements Runnable {
	private Game game;
	private Multiplayer multiplayer;
	private Semaphore semaphore = new Semaphore(0);
	
	MessageListener(Game game, Multiplayer multiplayer) {
		this.game = game;
		this.multiplayer = multiplayer;
	}
	
	public void releaseSempahore()
	{
		semaphore.release();
	}
	
	public void run() {
		double i;
		double k;
	/*	try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		while(true) {
			
			while((i = multiplayer.readmsg()) != -1) {
				k = multiplayer.readmsg();
				game.enemy.setPosition(i, k);
			}
		}
	}

}
