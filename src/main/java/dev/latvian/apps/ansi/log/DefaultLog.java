package dev.latvian.apps.ansi.log;

import dev.latvian.apps.ansi.ANSI;
import dev.latvian.apps.ansi.style.Style;

import java.util.Calendar;
import java.util.function.Consumer;

public class DefaultLog implements FormattedTimeLog {
	public static final String[] MONTH_NAMES = {
		"Jan",
		"Feb",
		"Mar",
		"Apr",
		"May",
		"Jun",
		"Jul",
		"Aug",
		"Sep",
		"Oct",
		"Nov",
		"Dec",
	};

	private final Consumer<ANSI> output;
	private final Style timeStyle;
	private final Calendar calendar;
	private final StringBuilder builder;

	private static void pad0(StringBuilder sb, int num) {
		if (num < 10) {
			sb.append('0');
		}

		sb.append(num);
	}

	public static void appendDate(StringBuilder builder, Calendar calendar) {
		pad0(builder, calendar.get(Calendar.DAY_OF_MONTH));
		builder.append('-');
		builder.append(MONTH_NAMES[calendar.get(Calendar.MONTH)]);
		builder.append('-');
		builder.append(calendar.get(Calendar.YEAR));
		builder.append(' ');
		pad0(builder, calendar.get(Calendar.HOUR_OF_DAY));
		builder.append(':');
		pad0(builder, calendar.get(Calendar.MINUTE));
		builder.append(':');
		pad0(builder, calendar.get(Calendar.SECOND));
	}

	public DefaultLog(Consumer<ANSI> output, Style timeStyle) {
		this.output = output;
		this.timeStyle = timeStyle;
		this.calendar = Calendar.getInstance();
		this.builder = new StringBuilder(21);
		// ANSI.text(formatTime(now), TIME_STYLE)
	}

	@Override
	public synchronized ANSI prefix(long time) {
		calendar.setTimeInMillis(time);
		builder.setLength(0);
		appendDate(builder, calendar);
		builder.append(' ');
		return ANSI.of(builder.toString(), timeStyle);
	}

	@Override
	public void log(ANSI line) {
		output.accept(line);
	}
}
