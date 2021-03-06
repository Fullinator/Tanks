package Main;

import java.util.HashMap;
import java.util.Map;
import java.util.function.LongConsumer;

/**
 * This class is used to fire events regularly and rapidly.
 *
 * @author Nicholas Muggio
 */
public class Ticker implements Runnable {

	private static final Map<Integer, LongConsumer> managedMethods = new HashMap<>();
	private boolean stop;
	private boolean pause;
	private long sleepMillis;
	private static int id = -1;

	Ticker(long sleepMillis) {
		stop = false;
		pause = false;
		this.sleepMillis = sleepMillis;
	}

	void stopTicking() {
		stop = true;
	}

	void setPaused(boolean pause) {
		this.pause = pause;
	}

	public static int addMethod(LongConsumer consumer) {
		id++;
		synchronized (managedMethods) {
			managedMethods.put(id, consumer);
			return id;
		}
	}

	public static void removeMethod(int index) {
		synchronized (managedMethods) {
			managedMethods.remove(index);
		}
	}

	@Override
	public void run() {
		long lastRunTime = System.nanoTime();
		while (!stop) {
			// This temp final needed for lambda use
			final long finalLastRunTime = lastRunTime;
			final long curTime = System.nanoTime();
			if (!pause) {
				synchronized (managedMethods) {
					managedMethods.values().forEach(f -> new Thread(() -> {
						f.accept(curTime - finalLastRunTime);
					}).start());
				}
			}
			lastRunTime = curTime;
			try {
				Thread.yield();
				Thread.sleep(sleepMillis);
			} catch (InterruptedException ignored) {}
		}
	}
}
