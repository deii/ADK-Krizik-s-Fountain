package cz.deii.adk.fountain;

import android.util.Log;

public class GradientController implements CommandHandler {

	private static final String TAG = "GradientController";
	
	private final GradientState state;
	private final ArduinoPin pin;
	
	public GradientController(FountainMessage message, float current) {
		this.state = new GradientState();
		state.setGoal(message.getValue());
		state.setCurrent(current);
		state.setVelocity(message.getVelocity());
		this.pin = message.getTarget();
	}

	@Override
	public void update(long delta /*refresh*/, UsbAccessoryService usb) {
		if (state.getCurrent() == state.getGoal()) return;
		// FIXME osetrit deleni nulou, klasika, vole!
		float toChange = (state.getGoal() - state.getCurrent()) / (state.getVelocity() / delta);
        updateState(state, toChange);
        usb.sendCommand(ArduinoCommand.ANALOG_WRITE_COMMAND, pin, (int)state.getCurrent());
	}
	
    private void updateState(GradientState state, float delta) {
        float newValue = state.getCurrent() + delta;
        if ((delta > 0 && state.getCurrent() > state.getGoal())
                || (delta < 0 && state.getCurrent() < state.getGoal())) {
            newValue = state.getGoal();
        }
        state.setCurrent(newValue);
//        Log.d(TAG, "Value updated: [state=" + state + "]");
    }

	@Override
	public float getCurrent() {
		return state.getCurrent();
	}

    private static class GradientState {
        private int goal;
        private float current;
        private float velocity;

        public float getCurrent() {
            return current;
        }

        public void setCurrent(float current) {
            this.current = current;
        }

        public float getVelocity() {
            return velocity;
        }

        public void setVelocity(float velocity) {
            this.velocity = velocity;
        }

        public int getGoal() {
            return goal;
        }

        public void setGoal(int goal) {
            this.goal = goal;
        }

		@Override
		public String toString() {
			return "RgbLedState [goal=" + goal + ", current=" + current
					+ ", velocity=" + velocity + "]";
		}
    }
}
