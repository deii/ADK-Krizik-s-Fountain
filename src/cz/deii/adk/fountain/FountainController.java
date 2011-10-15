package cz.deii.adk.fountain;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class FountainController {

	private static final String TAG = "FountainController";
	
	private CommandLoader cmdLoader;
	private CommandsHub cmdHub;
    private CommandConsumer cmdConsumer;
	private CommandTimer cmdTimer;
	private MediaPlayer mediaPlayer;
	
	private ToggleButton fountainSwitch;

	public FountainController(Activity activity, UsbAccessoryService usb) {
		
		this.cmdLoader = new CommandLoader(activity);
		
		this.cmdHub = new CommandsHub(usb);
		
		this.cmdConsumer = new CommandConsumer(cmdLoader.getMessages(), cmdHub);
		
        this.cmdTimer = new CommandTimer(cmdHub);
	}
	
	public void onResume(Activity activity) {

        initSwitch(activity);
        
		// zrejme blbost
		if (cmdTimer == null) {
			Log.d(TAG, "This never happen!");
			cmdTimer = new CommandTimer(cmdHub);
		}
        if (cmdConsumer == null) {
        	Log.d(TAG, "This never happen!");
            cmdConsumer = new CommandConsumer(cmdLoader.getMessages(), cmdHub);
        }
	}
	
	public void onPause() {
		stop();
	}
	
	public void onDestroy() {
		stop();
	}
	
	private void start(Activity activity) {
		Log.d(TAG, "Start fountain.");
		cmdLoader.loadMessages();
        cmdTimer.start();
        cmdConsumer.start();
		initPlayer(activity);
	}
	
	private void stop() {
		Log.d(TAG, "Stop fountain.");
		destroyPlayer();
		cmdTimer.stop();
        cmdConsumer.stop();
        fountainSwitch.setChecked(false);
        cmdLoader.getMessages().clear();
	}
    
    private void initSwitch(Activity activity) {
    	fountainSwitch = (ToggleButton)activity.findViewById(R.id.fountainSwitch);
    	fountainSwitch.setOnCheckedChangeListener(new FountainSwitchListener(activity));
    }
    
    private void initPlayer(Activity activity) {
		mediaPlayer = MediaPlayer.create(activity, R.raw.martinu_music);
		ToggleButton button = (ToggleButton)activity.findViewById(R.id.fountainSwitch);
		mediaPlayer.setOnCompletionListener(new FountainCompletionListener(button));
		mediaPlayer.start();
		Log.d(TAG, "Player started.");
    }
	
	public void destroyPlayer() {
		if (mediaPlayer != null) {
			Log.d(TAG, "Stopping player.");
			if (mediaPlayer.isPlaying()) mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
    
    private class FountainSwitchListener implements CompoundButton.OnCheckedChangeListener {
    	private Activity activity;
    	
		public FountainSwitchListener(Activity activity) {
			super();
			this.activity = activity;
		}
		
		@Override
		public void onCheckedChanged(CompoundButton button, boolean isChecked) {
			if (isChecked) {
		        start(activity);
			} else {
				stop();
			}
		}
    	
    }
	
	public static class FountainCompletionListener implements OnCompletionListener {
		private ToggleButton button;
		
		public FountainCompletionListener(ToggleButton button) {
			this.button = button;
		}

		@Override
		public void onCompletion(MediaPlayer mp) {
			button.setChecked(false);
		}
	}
}
