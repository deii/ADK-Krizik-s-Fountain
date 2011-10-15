package cz.deii.adk.fountain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

import cz.deii.adk.fountain.FountainMessage.FountainCommand;

import android.content.Context;
import android.util.Log;

/**
 * Nahrava prikazy a poskytuje jejich frontu.
 * Casem pridat postupne nacitani.
 */
public class CommandLoader {
	
	private static final String TAG = "CommandLoader";
	
	private final Queue<FountainMessage> messages = new LinkedList<FountainMessage>();
	private final Context ctx;

	public CommandLoader(Context ctx) {
		this.ctx = ctx;
	}

	public Queue<FountainMessage> getMessages() {
		return messages;
	}
	
	public void loadMessages() {
		messages.clear();
		InputStream inputStream = ctx.getResources().openRawResource(R.raw.martinu_schema);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				if (line.trim().length() == 0) continue;
				if (line.contains("#")) continue; // kometar
				FountainMessage msg = getMessage(line);
				if (msg != null) messages.add(msg);
			}
		} catch (IOException e) {
			Log.e(TAG, "Could not load input.", e);
		}
		Log.d(TAG, "Messages loaded: count=" + messages.size());
	}
	
	private static FountainMessage getMessage(String line) {
		FountainMessage msg = new FountainMessage();
		String[] parts = line.split("\\|");
		if (parts.length != 5) return null;
		try {
			msg.setCommand(FountainCommand.valueOf(parts[0])); // TODO osetrit
			msg.setTarget(ArduinoPin.valueOf(parts[1])); // TODO osetrit
			msg.setDelta(Long.parseLong(parts[2]));
			msg.setValue(Integer.parseInt(parts[3]));
			msg.setVelocity(Long.parseLong(parts[4]));
		} catch (Exception e) {
			Log.e(TAG, "Chyba pri zpracovani prikazu: " + line);
			return null;
		}
		return msg;
	}
	
}
