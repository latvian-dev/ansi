package dev.latvian.apps.ansi;

import dev.latvian.apps.ansi.style.Style;

public record ANSIBorders(
	Style style,
	ANSI ud,
	ANSI dr,
	ANSI udr,
	ANSI ur,
	ANSI lr,
	ANSI dlr,
	ANSI udlr,
	ANSI ulr,
	ANSI dl,
	ANSI udl,
	ANSI ul
) {
	public static final ANSIBorders DEFAULT = new ANSIBorders(Style.NONE);

	public ANSIBorders(Style style) {
		this(style,
			ANSI.immutable("│", style),
			ANSI.immutable("┌", style),
			ANSI.immutable("├", style),
			ANSI.immutable("└", style),
			ANSI.immutable("─", style),
			ANSI.immutable("┬", style),
			ANSI.immutable("┼", style),
			ANSI.immutable("┴", style),
			ANSI.immutable("┐", style),
			ANSI.immutable("┤", style),
			ANSI.immutable("┘", style)
		);
	}
}
