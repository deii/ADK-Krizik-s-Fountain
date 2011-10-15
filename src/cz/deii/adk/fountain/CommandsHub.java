package cz.deii.adk.fountain;

import java.util.HashMap;
import java.util.Map;

/**
 * Postara se, ze se bude pro jeden pin provadet pouze jeden command.
 * Plus vyzjisti ktery handler umi command zpracovat.
 */
public class CommandsHub {
	
	private final Map<ArduinoPin, CommandHandler> pinHub = new HashMap<ArduinoPin, CommandHandler>();
	private UsbAccessoryService usb;
	
	public CommandsHub(UsbAccessoryService usb) {
		this.usb = usb;
	}
	
	/**
	 * Zaregistruje command.
	 * Starsi commandy jsou nemilosrdne odeslany na vecnost (do garbage collectoru). 
	 * @param command registrovany prikaz
	 */
	public void registerCommand(FountainMessage command) {
		ArduinoPin target = command.getTarget();
		float current = (pinHub.containsKey(target)) ? pinHub.get(target).getCurrent() : 0;
		CommandHandler handler = CommandHandlerFactory.createHandler(command, current);
		pinHub.put(target, handler);
	}
	
	/**
	 * Provede update vsech commandu.
	 * @param delta cas od posledniho updatu
	 */
	public void update(long delta) {
		for (Map.Entry<ArduinoPin, CommandHandler> e : pinHub.entrySet()) {
			e.getValue().update(delta, usb);
		}
	}
	
	public void setUsb(UsbAccessoryService usb) {
		this.usb = usb;
	}
}
