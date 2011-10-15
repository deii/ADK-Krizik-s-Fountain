package cz.deii.adk.fountain;

public class FountainMessage {
	private FountainCommand command;
	private long delta;
	private ArduinoPin target;
    private int value;
    private long velocity;

    public FountainCommand getCommand() {
        return command;
    }

    public void setCommand(FountainCommand command) {
        this.command = command;
    }

    public long getDelta() {
        return delta;
    }

    public void setDelta(long delta) {
        this.delta = delta;
    }

    public ArduinoPin getTarget() {
        return target;
    }

    public void setTarget(ArduinoPin target) {
        this.target = target;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public long getVelocity() {
        return velocity;
    }

    public void setVelocity(long velocity) {
        this.velocity = velocity;
    }

    @Override
	public String toString() {
		return "FountainMessage [command=" + command + ", delta=" + delta
				+ ", target=" + target + ", value=" + value + ", velocity="
				+ velocity + "]";
	}



	public static enum FountainCommand {
		GRADIENT, STATIC
	}
}
