package dev.latvian.apps.ansi.log;

import dev.latvian.apps.ansi.ANSI;

public interface FormattedTimeLog extends Log {
	ANSI prefix(long time);

	default void log(ANSI line) {
		System.out.println(line.toANSIString());
	}

	@Override
	default void log(LogType type, Object message) {
		log(ANSI.empty(2).append(prefix(System.currentTimeMillis())).append(ANSI.of(message, type.style())));
	}
}
