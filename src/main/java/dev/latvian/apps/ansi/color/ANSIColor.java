package dev.latvian.apps.ansi.color;

import dev.latvian.apps.ansi.ANSISymbols;
import dev.latvian.apps.ansi.style.Style;

public interface ANSIColor {
	ANSIColor NONE = new NoColor();

	static ANSIColor of(int color) {
		return color >= 0 && color < 256 ? Color256.ALL[color] : new ColorRGB(color);
	}

	void push(StringBuilder builder, boolean foreground);

	default Style fgStyle() {
		return new Style(this, null, null, null, null, null, null, null, null);
	}

	default Style bgStyle() {
		return new Style(null, this, null, null, null, null, null, null, null);
	}

	default ANSISymbols symbols() {
		return new ANSISymbols(fgStyle());
	}
}
