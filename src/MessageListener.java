
public class  MessageListener implements Runnable {
	private Game game;
	private Multiplayer multiplayer;
	
	MessageListener(Game game, Multiplayer multiplayer) {
		this.game = game;
		this.multiplayer = multiplayer;
	}
	
	public void run() {
		while(true) {
			double i;
			while((i = multiplayer.readmsg()) != -1) {
				double k = multiplayer.readmsg();
				game.enemy.setPosition(i, k);
			}
		}
	}

}
