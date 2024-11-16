package dev.latvian.apps.ansi;

import dev.latvian.apps.ansi.style.Style;

public record StyledCharacter(char content, Style style) implements ANSISupplier {
	@Override
	public ANSI toANSI() {
		return new ANSI(String.valueOf(content), style, false);
	}
}
