package dev.latvian.apps.ansi.log;

import dev.latvian.apps.ansi.color.Color16;

public interface Log {
	Log OUT = new DefaultLog(System.out::println, Color16.CYAN.style());
	Log ERR = new DefaultLog(System.err::println, Color16.RED.style());

	default void info(Object message) {
		log(LogType.INFO, message);
	}

	default void warn(Object message) {
		log(LogType.WARN, message);
	}

	default void error(Object message) {
		log(LogType.ERROR, message);
	}

	default void debug(Object message) {
		log(LogType.DEBUG, message);
	}

	default void success(Object message) {
		log(LogType.SUCCESS, message);
	}

	default void fail(Object message) {
		log(LogType.FAIL, message);
	}

	default void success(Object message, boolean success) {
		if (success) {
			success(message);
		} else {
			fail(message);
		}
	}

	default void important(Object message) {
		log(LogType.IMPORTANT, message);
	}

	default void code(Object message) {
		log(LogType.CODE, message);
	}

	void log(LogType type, Object message);
}
