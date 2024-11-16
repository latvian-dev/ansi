package dev.latvian.apps.ansi.command;

import java.util.Map;

@FunctionalInterface
public interface CommandCallback {
	void run(Map<String, String> args) throws Throwable;
}
