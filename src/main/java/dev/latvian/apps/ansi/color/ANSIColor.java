package dev.latvian.apps.ansi.color;

public interface ANSIColor {
	ANSIColor NONE = new NoColor();

	static ANSIColor of(int color) {
		return color >= 0 && color < 256 ? Color256.ALL[color] : new ColorRGB(color);
	}

	void push(StringBuilder builder, boolean foreground);
}
