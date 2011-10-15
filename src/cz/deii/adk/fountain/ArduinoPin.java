package cz.deii.adk.fountain;

public enum ArduinoPin {
	// ANALOG IN
	ANALOG0(0),
	ANALOG1(1),
	ANALOGA2(2),
	ANALOGA3(3),
	ANALOGA4(4),
	ANALOGA5(5),
	ANALOGA6(6),
	ANALOGA7(7),
	ANALOGA8(8),
	ANALOGA9(9),
	ANALOGA10(10),
	ANALOGA11(11),
	ANALOGA12(12),
	ANALOGA13(13),
	ANALOGA14(14),
	ANALOGA15(15),
	// COMMUNICATION
	// CTX0(100),
	// CRX1(101),
	// PWM
	PWM2(102),
	PWM3(103),
	PWM4(104),
	PWM5(105),
	PWM6(106),
	PWM7(107),
	PWM8(108),
	PWM9(109),
	PWM10(110),
	PWM11(111),
	PWM12(112),
	PWM13(113),
	// COMMUNICATION
	// CTX14(114),
	// CRX15(115),
	// CTX16(116),
	// CRX17(117),
	// CTX18(118),
	// CRX19(119),
	// SDA20(120), serial data line
	// SCL21(121), serial clock line
	// DIGITAL
	// D22(122)-D53(153)
	;
	
	private final byte address;

	private ArduinoPin(int address) {
		this.address = (byte)address;
	}

	public byte getAddress() {
		return address;
	}
}
