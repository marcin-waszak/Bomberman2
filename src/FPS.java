import java.util.concurrent.TimeUnit;

/**
 * 
 * FPS counter class.
 *
 */
public class FPS {
	/** FPS limit*/
	static final int FPS = 60;
	/** Frames used to calculation of average fps*/
	static final int FRAMES_PER_MEASURMENT = 16;
	
	/** Time of last measurment*/
	private long lastTimeMeasurment;
	/** Current number of measurments*/
	private int measurments;
	/** Current sum of FPS accumulator*/
	private double fpsAccumulator;
	/** Current FPS (not averaged)*/
	private double fpsCurrent;
	/** Current time between frames*/
	private long delta;
	
	/**
	 * Method used to stabilization of FPS in the game.
	 */
	public void stabilize() {
		try {
			TimeUnit.NANOSECONDS.sleep(lastTimeMeasurment + (long)(1E9/FPS) - System.nanoTime());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method used to measure FPS.
	 * @return Current average FPS.
	 */
	public int measure() {
		delta = -lastTimeMeasurment + (lastTimeMeasurment = System.nanoTime());
		fpsAccumulator += 1E9 / (delta);
		measurments++;
		
		if(measurments == FRAMES_PER_MEASURMENT) {
			fpsCurrent = fpsAccumulator / FRAMES_PER_MEASURMENT;
			fpsAccumulator = 0;
			measurments = 0;
		}
		
		return (int)fpsCurrent;
	}
	
	/**
	 * Provides current FPS averaged value.
	 * @return Current average FPS.
	 */
	public int getValue() {
		return (int)fpsCurrent;
	}
	
	/**
	 * Provides frame time.
	 * @return frame time.
	 */
	public double getFrameTime() {
		return delta / 1E9;
	}
}
