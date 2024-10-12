package dev.latvian.apps.ansi;

import dev.latvian.apps.ansi.style.Style;

public record ANSISymbols(
	Style style,
	ANSI lc,
	ANSI rc,
	ANSI ls,
	ANSI rs,
	ANSI lp,
	ANSI rp,
	ANSI lt,
	ANSI gt,
	ANSI comma,
	ANSI col,
	ANSI dot
) {

	public ANSISymbols(Style style) {
		this(style,
			ANSI.immutable("{", style),
			ANSI.immutable("}", style),
			ANSI.immutable("[", style),
			ANSI.immutable("]", style),
			ANSI.immutable("(", style),
			ANSI.immutable(")", style),
			ANSI.immutable("<", style),
			ANSI.immutable(">", style),
			ANSI.immutable(",", style),
			ANSI.immutable(":", style),
			ANSI.immutable(".", style)
		);
	}
}
