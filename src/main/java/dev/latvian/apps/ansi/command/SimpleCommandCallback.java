package dev.latvian.apps.ansi.command;

import java.util.Map;

@FunctionalInterface
public interface SimpleCommandCallback extends CommandCallback {
	void run() throws Throwable;

	@Override
	default void run(Map<String, String> args) throws Throwable {
		run();
	}
}
