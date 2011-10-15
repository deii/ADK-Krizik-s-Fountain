package cz.deii.adk.fountain;


public interface UsbAccessoryService {
	
	/**
	 * Odesle prikaz do zarizeni.
	 * @param command prikaz pro zarizeni
	 * @param targetPin pin zarizeni
	 * @param value hodnota k nastaveni
	 */
    public void sendCommand(ArduinoCommand command, ArduinoPin targetPin, int value);
}
