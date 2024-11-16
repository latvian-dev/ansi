package dev.latvian.apps.ansi.command;

import dev.latvian.apps.ansi.ANSI;
import dev.latvian.apps.ansi.log.Log;

public class CLI implements Runnable {
	public static void start(CommandManager commandManager) {
		var thread = new Thread(new CLI(commandManager));
		thread.setDaemon(true);
		thread.start();
	}

	public final CommandManager commandManager;

	private CLI(CommandManager commandManager) {
		this.commandManager = commandManager;
	}

	@Override
	public void run() {
		var inputBuffer = new StringBuilder();

		while (true) {
			try {
				var in = System.in.read();

				if (in == ANSI.CODE) {
					Log.warn("ANSI control code not supported!");
				} else if (in == '\n') {
					var inputLine = inputBuffer.toString();
					inputBuffer.setLength(0);
					commandManager.run(inputLine);
				} else if (in >= ' ' && in <= '~') {
					inputBuffer.append((char) in);
				} else {
					Log.warn("Invalid character: %04x / %s".formatted(in, (char) in));
				}
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
	}
}
