package dev.latvian.apps.ansi.log;

import java.util.Calendar;

public class TimestampFormat {
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

	private final Calendar calendar;
	private final StringBuilder builder;
	private final boolean date;
	private final boolean time;

	public TimestampFormat(boolean date, boolean time) {
		this.calendar = Calendar.getInstance();
		this.builder = new StringBuilder(21);
		this.date = date;
		this.time = time;
	}

	private void pad0(int num) {
		if (num < 10) {
			builder.append('0');
		}

		builder.append(num);
	}

	public StringBuilder format(StringBuilder builder, long millis) {
		calendar.setTimeInMillis(millis);

		if (date) {
			pad0(calendar.get(Calendar.DAY_OF_MONTH));
			builder.append('-');
			builder.append(MONTH_NAMES[calendar.get(Calendar.MONTH)]);
			builder.append('-');
			builder.append(calendar.get(Calendar.YEAR));
		}

		if (date && time) {
			builder.append(' ');
		}

		if (time) {
			pad0(calendar.get(Calendar.HOUR_OF_DAY));
			builder.append(':');
			pad0(calendar.get(Calendar.MINUTE));
			builder.append(':');
			pad0(calendar.get(Calendar.SECOND));
		}

		return builder;
	}

	public String format(long millis) {
		var str = format(builder, millis).toString();
		builder.setLength(0);
		return str;
	}

	@Override
	public String toString() {
		return date && time ? "yyyy-MMM-dd HH:mm:ss" : date ? "yyyy-MMM-dd" : time ? "HH:mm:ss" : "";
	}
}
