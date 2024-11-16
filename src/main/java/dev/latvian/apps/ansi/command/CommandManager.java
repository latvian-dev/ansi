package dev.latvian.apps.ansi.command;

import dev.latvian.apps.ansi.ANSI;
import dev.latvian.apps.ansi.log.Log;
import dev.latvian.apps.ansi.style.Style;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
	public final Map<String, Command> commands;
	public final List<String> commandHistory;

	public CommandManager() {
		this.commands = new LinkedHashMap<>();
		this.commandHistory = new ArrayList<>();

		add("help", this::help);
	}

	public void add(String name, List<String> args, CommandCallback callback) {
		commands.put(name, new Command(name, args, callback));
	}

	public void add(String name, SimpleCommandCallback callback) {
		commands.put(name, new Command(name, List.of(), callback));
	}

	public void run(String command) throws Throwable {
		var input = command.split(" ");
		var cmd0 = input[0].trim();

		if (cmd0.isEmpty()) {
			return;
		}

		commandHistory.remove(command);
		commandHistory.add(command);

		var cmd = commands.get(cmd0);

		if (cmd == null) {
			Log.warn("Unknown command: " + cmd0);
			return;
		}

		var args = new HashMap<String, String>(cmd.args().size());

		for (int i = 1; i < input.length; i++) {
			var a = i < cmd.args().size() - 1 ? cmd.args().get(i - 1) : null;

			if (a != null) {
				if (a.endsWith("...")) {
					var remaining = new String[input.length - i];
					System.arraycopy(input, i, remaining, 0, remaining.length);
					args.put(a, String.join(" ", remaining));
				} else {
					args.put(a, input[i]);
				}
			}
		}

		cmd.callback().run(args);
	}

	public void help() {
		for (var c : commands.values()) {
			if (c.args().isEmpty()) {
				Log.info(ANSI.of("- ").append(ANSI.of(" " + c.name()).styled(Style.NONE.bold())));
			} else {
				Log.info(ANSI.of("- ").append(ANSI.of(" " + c.name()).styled(Style.NONE.bold()).append(" ").append(ANSI.of(String.join(" ", c.args())).styled(Style.NONE.foreground(224)))));
			}
		}
	}
}
