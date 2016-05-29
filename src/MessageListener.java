import java.util.concurrent.Semaphore;

public class  MessageListener implements Runnable {
	private Game game;
	private Multiplayer multiplayer;
	
	
	MessageListener(Game game, Multiplayer multiplayer) {
		this.game = game;
		this.multiplayer = multiplayer;
	}
	
	public void run() {
		while(true) {
			int i = multiplayer.readMessage();
			switch(i & 0x000000FF) {
			case(1):
				game.notifyAddBox((i & 0x0000FF00) >> 8, (i & 0x00FF0000) >> 16, (i & 0xFF000000) >> 24);
			case(2):
				game.notifySetRemotePlayerPosition((i & 0x0000FF00) >> 8, (i & 0x00FF0000) >> 16);
			case(9):
				game.notifySemaphoreRelease();
			}
		}
	}
}

