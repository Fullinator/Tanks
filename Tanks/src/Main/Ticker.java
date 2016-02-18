package Main;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongConsumer;

/**
 * Created by nick on 2/18/16.
 */
public class Ticker implements Runnable {

	private static final List<LongConsumer> managedMethods = new ArrayList<>();
	private boolean stop;
	private long sleepMillis;

	public Ticker(long sleepMillis) {
		stop = false;
		this.sleepMillis = sleepMillis;
	}

	public void stopTicking() {
		stop = true;
	}

	public synchronized static void addMethod(LongConsumer consumer) {
		managedMethods.add(consumer);
	}

	public synchronized static void removeMethod(LongConsumer consumer) {
		managedMethods.remove(consumer);
	}

	@Override
	public void run() {
		long lastRunTime = System.nanoTime();
		while (!stop) {
			// This temp final needed for lambda use
			final long finalLastRunTime = lastRunTime;
			final long curTime = System.nanoTime();
			synchronized (managedMethods) {
				managedMethods.forEach(f -> f.accept(curTime - finalLastRunTime));
			}
			lastRunTime = curTime;
			try {
				Thread.sleep(sleepMillis);
			} catch (InterruptedException e) {}
		}
	}
}
