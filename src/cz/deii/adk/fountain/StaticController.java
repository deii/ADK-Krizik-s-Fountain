package cz.deii.adk.fountain;

import android.util.Log;

public class StaticController implements CommandHandler {

	private static final String TAG = "GradientController";
	
	private float current = 0;
	private int goal = 0;
	private final ArduinoPin pin;
	
	public StaticController(FountainMessage message, float current) {
		this.current = current;
		this.goal = (message.getValue() > 0) ? 255 : 0;
		this.pin = message.getTarget();
	}

	@Override
	public void update(long delta, UsbAccessoryService usb) {
		if (current == goal) return;
		this.current = goal;
		ArduinoCommand cmd = (pin.getAddress() < 100) ? ArduinoCommand.ANALOG_WRITE_COMMAND : ArduinoCommand.DIGITAL_WRITE_COMMAND;
		usb.sendCommand(cmd, pin, (int)current);
//		Log.d(TAG, "Value updated: [current=" + current + ", goal=" + goal + "]");
	}

	@Override
	public float getCurrent() {
		return current;
	}

}
