package dev.latvian.apps.ansi.style;

public enum StyleFlag {
	BOLD("bold", 1, 22, new Style(null, null, Boolean.TRUE, null, null, null, null, null, null)),
	ITALIC("italic", 3, 23, new Style(null, null, null, Boolean.TRUE, null, null, null, null, null)),
	UNDERLINE("underline", 4, 24, new Style(null, null, null, null, Boolean.TRUE, null, null, null, null)),
	BLINK("blink", 5, 25, new Style(null, null, null, null, null, Boolean.TRUE, null, null, null)),
	REVERSE("reverse", 7, 27, new Style(null, null, null, null, null, null, Boolean.TRUE, null, null)),
	HIDDEN("hidden", 8, 28, new Style(null, null, null, null, null, null, null, Boolean.TRUE, null)),
	STRIKETHROUGH("strikethrough", 9, 29, new Style(null, null, null, null, null, null, null, null, Boolean.TRUE));

	public static final StyleFlag[] VALUES = values();

	public final String name;
	private final char[] pushCode;
	private final char[] popCode;
	private final Style style;

	StyleFlag(String name, int pushCode, int popCode, Style style) {
		this.name = name;
		this.pushCode = String.valueOf(pushCode).toCharArray();
		this.popCode = String.valueOf(popCode).toCharArray();
		this.style = style;
	}

	public Style style() {
		return style;
	}

	public void append(StringBuilder builder, boolean push) {
		builder.append(push ? pushCode : popCode);
	}
}
