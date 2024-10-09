package dev.latvian.apps.ansi.log;

import dev.latvian.apps.ansi.style.Style;

import java.util.function.UnaryOperator;

public enum LogType {
	INFO(UnaryOperator.identity()),
	WARN(Style::yellow),
	ERROR(Style::error),
	DEBUG(Style::lightGray),
	SUCCESS(Style::green),
	FAIL(Style::red),
	IMPORTANT(Style::orange),
	CODE(Style::teal);

	private final Style style;

	LogType(UnaryOperator<Style> style) {
		this.style = style.apply(Style.NONE);
	}

	public Style style() {
		return style;
	}
}
