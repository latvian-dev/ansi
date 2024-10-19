package dev.latvian.apps.ansi.log;

public record CombinedLog(Log first, Log second) implements Log {
	@Override
	public void log(LogType type, Object message) {
		first.log(type, message);
		second.log(type, message);
	}
}
