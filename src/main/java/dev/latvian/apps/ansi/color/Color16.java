package dev.latvian.apps.ansi.color;

import dev.latvian.apps.ansi.style.Style;

public enum Color16 implements ANSIColor {
	BLACK("black", 30, 40),
	DARK_RED("dark_red", 31, 41),
	GREEN("green", 32, 42),
	ORANGE("orange", 33, 43),
	NAVY("navy", 34, 44),
	PURPLE("purple", 35, 45),
	TEAL("teal", 36, 46),
	LIGHT_GRAY("light_gray", 37, 47),
	DARK_GRAY("dark_gray", 90, 100),
	RED("red", 91, 101),
	LIME("lime", 92, 102),
	YELLOW("yellow", 93, 103),
	BLUE("blue", 94, 104),
	MAGENTA("magenta", 95, 105),
	CYAN("cyan", 96, 106),
	WHITE("white", 97, 107);

	public static final Color16[] VALUES = values();
	private static final Color16[] TREE = {RED, GREEN, BLUE, YELLOW};

	public static Color16 tree(int index) {
		return TREE[index & 3];
	}

	private final String name;
	private final char[] foreground;
	private final char[] background;
	private final Style style;

	Color16(String name, int fg, int bg) {
		this.name = name;
		this.foreground = String.valueOf(fg).toCharArray();
		this.background = String.valueOf(bg).toCharArray();
		this.style = new Style(this, null, null, null, null, null, null, null, null);
	}

	@Override
	public void push(StringBuilder builder, boolean f) {
		builder.append(f ? foreground : background);
	}

	public Style style() {
		return style;
	}

	@Override
	public String toString() {
		return name;
	}
}