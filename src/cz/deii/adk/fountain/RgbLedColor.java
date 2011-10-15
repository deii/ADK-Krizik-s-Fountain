package cz.deii.adk.fountain;

public enum RgbLedColor {
//	AQUA(0, 255, 255),
	AQUAMARINE(127, 255, 212),
	BLACK(0, 0, 0),
	BLUE(0, 0, 255),
	CYAN(0, 255, 255),
	DARK_GRAY(54, 54, 54),
//	FUSCHIA(255, 0, 255),
	GOLD(255, 215, 0),
	GRAY(128, 128, 128),
	GREEN(0, 255, 0),
	GREEN2(0, 128, 0),
	INDIAN(255, 106, 106),
	LIGHT_GRAY(192, 192, 192),
//	LIME(0, 255, 0),
	MAGENTA(255, 0, 255),
	MAROON(128, 0, 0),
	NAVY(0, 0, 128),
	OLIVE(128, 128, 0),
	ORANGE(255, 165, 0),
	PINK(255, 181, 197),//?
	PURPLE(128, 0, 128),
	PURPLE2(155, 48, 255),
	RED(255, 0, 0),
	STEEL(198, 226, 255),
	WHITE(255, 255, 255),
	YELLOW(255, 255, 0);
	
	private final int red;
	private final int green;
	private final int blue;
	
	private RgbLedColor(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public int getRed() {
		return red;
	}
	
	public int getGreen() {
		return green;
	}
	
	public int getBlue() {
		return blue;
	}
}
