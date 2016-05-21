import java.util.concurrent.TimeUnit;

public class FPS {
	static final int FPS = 100;
	static final int FRAMES_PER_MEASURMENT = 16;
	
	private long lastTimeMeasurment;
	private int measurments;
	private double fpsAccumulator;
	private double fpsCurrent;
	
	public void stabilize() {
		try {
			TimeUnit.NANOSECONDS.sleep(lastTimeMeasurment + (long)(1E9/FPS) - System.nanoTime());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public int measure() {
		fpsAccumulator += 1E9 / (-lastTimeMeasurment + (lastTimeMeasurment = System.nanoTime()));
		measurments++;
		
		if(measurments == FRAMES_PER_MEASURMENT)
		{
			fpsCurrent = fpsAccumulator / FRAMES_PER_MEASURMENT;
			fpsAccumulator = 0;
			measurments = 0;
		}
		
		return (int)fpsCurrent;
	}
}
