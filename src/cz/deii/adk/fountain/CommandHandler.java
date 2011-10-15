package cz.deii.adk.fountain;

public interface CommandHandler {
	public void update(long delta, UsbAccessoryService usb);
	public float getCurrent();
}
