package cz.deii.adk.fountain;

import java.util.Queue;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

/**
 * Pozira zpravy z fronty.
 * Cyklus:
 * - preda hubu zpravu (ke zpracovani) nactenou z fronty v predchozim behu
 * - odebere zpravu z fronty
 * - ulozi pro predani hubu
 * - nastavi pristi spusteni v aktualni uptime + delta cas ze zpravy
 */
public class CommandConsumer implements Runnable {

    private static final String TAG = "FountainMessageConsumer";
	
	private static final long WAIT_DELTA = 100L;
	
	private final Queue<FountainMessage> messages;
	private final Handler taskHandler;
	private final CommandsHub hub;

	private FountainMessage current;
	
	public CommandConsumer(Queue<FountainMessage> messages, CommandsHub hub) {
		this.messages = messages;
		this.hub = hub;

		this.taskHandler = new Handler();
	}

    public void start() {
        Log.d(TAG, "Starting consumer.");
		postNextCommand();
    }

    public void stop() {
        Log.d(TAG, "Stopping consumer.");
        taskHandler.removeCallbacks(this);
    }

	public void run() {
		if (current != null) {
			hub.registerCommand(current);
		}

		postNextCommand();
	}
	
	private void postNextCommand() {
		this.current = (messages == null || messages.isEmpty()) ? null : messages.remove();
		long delta = (current == null) ? WAIT_DELTA : current.getDelta();
        Log.d(TAG, "Scheduling new message: [message=" + current + "]");
		taskHandler.postAtTime(this, SystemClock.uptimeMillis() + delta);
	}
}
