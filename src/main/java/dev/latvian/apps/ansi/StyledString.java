package dev.latvian.apps.ansi;

import dev.latvian.apps.ansi.style.Style;

public record StyledString(String content, Style style) implements ANSISupplier {
	@Override
	public ANSI toANSI() {
		return new ANSI(content, style, false);
	}
}
