package dev.latvian.apps.ansi.log;

import dev.latvian.apps.ansi.color.Color16;

public interface Log {
	LogInstance INSTANCE = new LogInstance(new DefaultLog(System.out, Color16.CYAN.fgStyle()));

	static void info(Object message) {
		INSTANCE.get().log(LogType.INFO, message);
	}

	static void warn(Object message) {
		INSTANCE.get().log(LogType.WARN, message);
	}

	static void error(Object message) {
		INSTANCE.get().log(LogType.ERROR, message);
	}

	static void debug(Object message) {
		INSTANCE.get().log(LogType.DEBUG, message);
	}

	static void success(Object message) {
		INSTANCE.get().log(LogType.SUCCESS, message);
	}

	static void fail(Object message) {
		INSTANCE.get().log(LogType.FAIL, message);
	}

	static void success(Object message, boolean success) {
		if (success) {
			success(message);
		} else {
			fail(message);
		}
	}

	static void important(Object message) {
		INSTANCE.get().log(LogType.IMPORTANT, message);
	}

	static void code(Object message) {
		INSTANCE.get().log(LogType.CODE, message);
	}

	void log(LogType type, Object message);
}
