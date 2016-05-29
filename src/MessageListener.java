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
			switch(i & 0x0000000F) {
			case(1):
				game.notifyAddBox((i & 0x0000FFF0) >> 4, (i & 0x0FFF0000) >> 16, (i & 0xF0000000) >> 28);
				break;
			case(2):
				game.notifySetRemotePlayerPosition((i & 0x0000FFF0) >> 4, (i & 0x0FFF0000) >> 16);
				break;
			case(3):
				game.notifyRemotePlayerSettingBomb((i & 0x0000FFF0) >> 4, (i & 0x0FFF0000) >> 16);
				break;
			case(9):
				game.notifySemaphoreRelease();
			}
		}
	}
}

