import java.util.concurrent.TimeUnit;

public class FPS {
	static final int FPS = 60;
	static final int framesPerMeasurment = 4;
	
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
		
		if(measurments == framesPerMeasurment)
		{
			fpsCurrent = fpsAccumulator / framesPerMeasurment;
			fpsAccumulator = 0;
			measurments = 0;
		}
		
		return (int)fpsCurrent;
	}
}
