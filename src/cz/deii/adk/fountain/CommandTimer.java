package cz.deii.adk.fountain;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

/**
 * Casovac odesilani prikazu.
 */
public class CommandTimer {

	private static final String TAG = "CommandTimer";
	
	private static final long REFRESH = 100L;

	private final CommandsHub hub;
	private final Handler handler;
	
	private final Runnable task = new Runnable(){
		private long nextRun = 0L;

		public void run() {
//			Log.d(TAG, "run():: start");

			hub.update(REFRESH); // FIXME vylepsit delta time?
			
			if (nextRun == 0L) {
				this.nextRun = SystemClock.uptimeMillis();
			} else {
				this.nextRun = nextRun + REFRESH;
			}
//			Log.d(TAG, "Next run: [nextTime=" + nextRun + "]");
			handler.postAtTime(this, nextRun);
		}
	};

	public CommandTimer(CommandsHub hub) {
		this.hub = hub;
		this.handler = new Handler();
	}
	
	public void start() {
		// pro jistotu
		handler.removeCallbacks(task);
		handler.postAtTime(task, SystemClock.uptimeMillis());
	}
	
	public void stop() {
		Log.d(TAG, "Stopping task: [task=" + task + "]");
		handler.removeCallbacks(task);
	}
}
