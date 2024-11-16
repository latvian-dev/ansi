package dev.latvian.apps.ansi.log;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class LogInstance implements Supplier<Log> {
	private Log log;

	public LogInstance(Log log) {
		this.log = log;
	}

	public void replace(Log log) {
		this.log = log;
	}

	public void replace(UnaryOperator<Log> log) {
		replace(log.apply(this.log));
	}

	@Override
	public Log get() {
		return log;
	}
}
