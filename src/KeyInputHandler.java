import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent; 
/**
 * An entity responsible for handling the keyboard input.
 */
 
public class KeyInputHandler extends KeyAdapter {
		/** Flag indicates left key pressed*/
		boolean left_pressed;
		/** Flag indicates right key pressed*/
		boolean right_pressed;
		/** Flag indicates up key pressed*/
		boolean up_pressed;
		/** Flag indicates down key pressed*/
		boolean down_pressed;
		/** Flag indicates space key pressed*/
		boolean space_pressed;
		/** Flag indicates enter key pressed*/
		boolean enter_pressed;
		
		/**
		 * Key pressed handler 
		 */
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
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				enter_pressed = true;
			}
		} 

		/**
		 * Key released handler 
		 */
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
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				enter_pressed = false;
			}
		}

		/**
		 * Key typed handler 
		 */
		public void keyTyped(KeyEvent e) {
			// if we hit escape, then quit the game
			if (e.getKeyChar() == 27) {
				System.exit(0);
			}
		}
		
		/**
		 * Method tells us about presssed key.
		 * @return 
		 */
		public boolean isLeftPressed() {
			return left_pressed;
		}

		/**
		 * Method tells us about presssed key.
		 * @return 
		 */		
		public boolean isRightPressed() {
			return right_pressed;
		}

		/**
		 * Method tells us about presssed key.
		 * @return 
		 */		
		public boolean isUpPressed() {
			return up_pressed;
		}

		/**
		 * Method tells us about presssed key.
		 * @return 
		 */		
		public boolean isDownPressed() {
			return down_pressed;
		}

		/**
		 * Method tells us about presssed key.
		 * @return 
		 */		
		public boolean isSpacePressed() {
			return space_pressed;
		}
		
		/**
		 * Method tells us about presssed key.
		 * @return 
		 */		
		public boolean isEnterPressed() {
			return enter_pressed;
		}
	}