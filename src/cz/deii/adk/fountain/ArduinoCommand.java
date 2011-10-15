package cz.deii.adk.fountain;

public enum ArduinoCommand {
	ANALOG_WRITE_COMMAND(2),
	DIGITAL_WRITE_COMMAND(3);
	
	private final byte address;

	private ArduinoCommand(int address) {
		this.address = (byte)address;
	}
	
	public byte getAddress() {
		return address;
	}
}