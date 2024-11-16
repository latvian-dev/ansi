package dev.latvian.apps.ansi.log;

import dev.latvian.apps.ansi.ANSI;
import dev.latvian.apps.ansi.style.Style;

import java.io.PrintStream;
import java.util.function.Consumer;

public class DefaultLog implements FormattedTimeLog {
	private final Consumer<ANSI> output;
	private final Style timeStyle;
	private final TimestampFormat timestampFormat;

	public DefaultLog(Consumer<ANSI> output, Style timeStyle) {
		this.output = output;
		this.timeStyle = timeStyle;
		this.timestampFormat = new TimestampFormat(true, true);
	}

	public DefaultLog(PrintStream output, Style timeStyle) {
		this(output::println, timeStyle);
	}

	@Override
	public synchronized ANSI prefix(long time) {
		return ANSI.of(timestampFormat.format(time) + " ", timeStyle);
	}

	@Override
	public void log(ANSI line) {
		output.accept(line);
	}
}
