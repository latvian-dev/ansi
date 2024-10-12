package dev.latvian.apps.ansi.color;

import dev.latvian.apps.ansi.ANSISymbols;
import dev.latvian.apps.ansi.style.Style;

public final class Color256 implements ANSIColor {
	static final ANSIColor[] ALL = new ANSIColor[256];
	private static final char[] FG_CODE = "38;5;".toCharArray();
	private static final char[] BG_CODE = "48;5;".toCharArray();

	private final int code;
	private Style foregroundStyle;
	private Style backgroundStyle;
	private ANSISymbols symbols;

	public Color256(int code) {
		this.code = code;
	}

	static {
		System.arraycopy(Color16.VALUES, 0, ALL, 0, 16);

		for (int i = 16; i < 256; i++) {
			ALL[i] = new Color256(i);
		}
	}

	public int code() {
		return code;
	}

	@Override
	public Style fgStyle() {
		if (foregroundStyle == null) {
			foregroundStyle = new Style(this, null, null, null, null, null, null, null, null);
		}

		return foregroundStyle;
	}

	@Override
	public Style bgStyle() {
		if (backgroundStyle == null) {
			backgroundStyle = new Style(null, this, null, null, null, null, null, null, null);
		}

		return backgroundStyle;
	}

	@Override
	public ANSISymbols symbols() {
		if (symbols == null) {
			symbols = new ANSISymbols(fgStyle());
		}

		return symbols;
	}

	@Override
	public void push(StringBuilder builder, boolean foreground) {
		builder.append(foreground ? FG_CODE : BG_CODE);
		builder.append(code);
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this || obj instanceof Color256 c && c.code == code;
	}

	@Override
	public int hashCode() {
		return code;
	}

	@Override
	public String toString() {
		return "#" + code;
	}
}
