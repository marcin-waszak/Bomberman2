import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInputHandler extends KeyAdapter {
		boolean left_pressed;
		boolean right_pressed;
		boolean up_pressed;
		boolean down_pressed;
		boolean space_pressed;
		
		public void keyPressed(KeyEvent e) {					
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				left_pressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				right_pressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				up_pressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				down_pressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				space_pressed = true;
			}
		} 
		
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				left_pressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				right_pressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				up_pressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				down_pressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				space_pressed = false;
			}
		}

		public void keyTyped(KeyEvent e) {
			// if we hit escape, then quit the game
			if (e.getKeyChar() == 27) {
				System.exit(0);
			}
		}
		
		public boolean isLeftPressed() {
			return left_pressed;
		}
		
		public boolean isRightPressed() {
			return right_pressed;
		}
		
		public boolean isUpPressed() {
			return up_pressed;
		}
		
		public boolean isDownPressed() {
			return down_pressed;
		}
		
		public boolean isSpacePressed() {
			return space_pressed;
		}
	}