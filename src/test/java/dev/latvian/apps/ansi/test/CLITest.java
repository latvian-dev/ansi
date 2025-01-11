package dev.latvian.apps.ansi.test;

import dev.latvian.apps.ansi.command.CLI;
import dev.latvian.apps.ansi.command.CommandManager;
import dev.latvian.apps.ansi.log.Log;
import dev.latvian.apps.ansi.terminal.Terminal;

import java.util.List;

public class CLITest {
	public static void main(String[] args0) throws Exception {
		Terminal.replaceSystemLog();
		boolean[] open = {true};

		var cmd = new CommandManager();
		cmd.add("close", () -> open[0] = false);
		cmd.add("args", List.of("a"), args -> Log.info("Arg: " + args));
		cmd.add("args2", List.of("a", "b"), args -> Log.info("Arg: " + args));
		cmd.add("print", List.of("channel", "text..."), args -> Log.info(args.get("channel") + ": " + args.get("text")));
		CLI.start(cmd);

		while (open[0]) {
			Thread.sleep(1000L);
		}
	}
}
