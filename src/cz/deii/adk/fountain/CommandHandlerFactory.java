package cz.deii.adk.fountain;


public class CommandHandlerFactory {
	
	public static CommandHandler createHandler(FountainMessage message, float current) {
		switch (message.getCommand()) {
			case GRADIENT: return new GradientController(message, current);
			case STATIC: return new StaticController(message, current);
			default: throw new IllegalStateException("Programmers fault - koukej to spravit.");
		}
	}
}
