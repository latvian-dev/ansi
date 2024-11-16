package dev.latvian.apps.ansi.test;

import dev.latvian.apps.ansi.command.CLI;
import dev.latvian.apps.ansi.command.CommandManager;
import dev.latvian.apps.ansi.terminal.Terminal;

public class CLITest {
	public static void main(String[] args) throws Exception {
		Terminal.replaceSystemLog();
		boolean[] open = {true};

		var cmd = new CommandManager();
		cmd.add("close", () -> open[0] = false);
		CLI.start(cmd);

		while (open[0]) {
			Thread.sleep(1000L);
		}
	}
}
