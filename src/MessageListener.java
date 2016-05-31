import java.util.concurrent.Semaphore;
/**
 * This class is responsible for reading all messages sent to
 * the corresponding user. And then invoking right methods in game object.
 * 
 * The structure of the message protocol is defined as following:
 * 
 * 0x0000000F 4-bit Type of message
 * 0x0000FFF0 12-bit First message parameter
 * 0x0FFF0000 12-bit Second message parameter
 * 0xF0000000 4-bit Third message parameter
 *
 */
public class  MessageListener implements Runnable {
	/** Reference to game object required to invoke methods after receiving messages*/
	private Game game;
	/** Reference to multiplayer object required for reading messages*/
	private Multiplayer multiplayer;
	
	/**
	 * Basic MessageListener class constructor.
	 * @param game Reference to game object
	 * @param multiplayer Reference to multiplayer object
	 */
	MessageListener(Game game, Multiplayer multiplayer) {
		this.game = game;
		this.multiplayer = multiplayer;
	}
	
	/**
	 * Main loop of this thread.
	 * 
	 * First waits and then read an integer type message.
	 * Next, decodes the message and invokes the right game object method.
	 */
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
			case(4):
				game.notifyEndingGame((i & 0x0000FFF0) >> 4, (i & 0x0FFF0000));
				break;
			case(9):
				game.notifySemaphoreRelease();
			}
		}
	}
}

