package dev.latvian.apps.ansi.style;

import dev.latvian.apps.ansi.ANSIContext;
import dev.latvian.apps.ansi.color.ANSIColor;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public record Style(ANSIColor fg, ANSIColor bg, Boolean b, Boolean i, Boolean u, Boolean l, Boolean r, Boolean h, Boolean s) implements Styleable<Style> {
	public static final Style NONE = new Style(null, null, null, null, null, null, null, null, null);

	public Style(ANSIColor color, ANSIColor background, Map<StyleFlag, Boolean> flags) {
		this(
			color,
			background,
			flags.get(StyleFlag.BOLD),
			flags.get(StyleFlag.ITALIC),
			flags.get(StyleFlag.UNDERLINE),
			flags.get(StyleFlag.BLINK),
			flags.get(StyleFlag.REVERSE),
			flags.get(StyleFlag.HIDDEN),
			flags.get(StyleFlag.STRIKETHROUGH)
		);
	}

	public boolean isDefault() {
		return this == NONE || !(fg != null || bg != null || b != null || i != null || u != null || l != null || r != null || h != null || s != null);
	}

	public Style merge(Style other) {
		return other.isDefault() ? this : isDefault() ? other : new Style(
			other.fg != null ? other.fg : fg,
			other.bg != null ? other.bg : bg,
			other.b != null ? other.b : b,
			other.i != null ? other.i : i,
			other.u != null ? other.u : u,
			other.l != null ? other.l : l,
			other.r != null ? other.r : r,
			other.h != null ? other.h : h,
			other.s != null ? other.s : s
		);
	}

	@Override
	public Style set(StyleFlag flag, @Nullable Boolean value) {
		var v = value == null ? null : value ? Boolean.TRUE : Boolean.FALSE;

		return switch (flag) {
			case BOLD -> b == v ? this : v == Boolean.TRUE && isDefault() ? flag.style() : new Style(fg, bg, v, i, u, l, r, h, s);
			case ITALIC -> i == v ? this : v == Boolean.TRUE && isDefault() ? flag.style() : new Style(fg, bg, b, v, u, l, r, h, s);
			case UNDERLINE -> u == v ? this : v == Boolean.TRUE && isDefault() ? flag.style() : new Style(fg, bg, b, i, v, l, r, h, s);
			case BLINK -> l == v ? this : v == Boolean.TRUE && isDefault() ? flag.style() : new Style(fg, bg, b, i, u, v, r, h, s);
			case REVERSE -> r == v ? this : v == Boolean.TRUE && isDefault() ? flag.style() : new Style(fg, bg, b, i, u, l, v, h, s);
			case HIDDEN -> h == v ? this : v == Boolean.TRUE && isDefault() ? flag.style() : new Style(fg, bg, b, i, u, l, r, v, s);
			case STRIKETHROUGH -> s == v ? this : v == Boolean.TRUE && isDefault() ? flag.style() : new Style(fg, bg, b, i, u, l, r, h, v);
		};
	}

	@Override
	@Nullable
	public Boolean get(StyleFlag flag) {
		return switch (flag) {
			case BOLD -> b;
			case ITALIC -> i;
			case UNDERLINE -> u;
			case BLINK -> l;
			case REVERSE -> r;
			case HIDDEN -> h;
			case STRIKETHROUGH -> s;
		};
	}

	@Override
	public Style foreground(@Nullable ANSIColor value) {
		return Objects.equals(fg, value) ? this : value != null && isDefault() ? value.fgStyle() : new Style(value, bg, b, i, u, l, r, h, s);
	}

	@Override
	public Style background(@Nullable ANSIColor value) {
		return Objects.equals(bg, value) ? this : value != null && isDefault() ? value.bgStyle() : new Style(fg, value, b, i, u, l, r, h, s);
	}

	@Override
	public Style colors(@Nullable ANSIColor foreground, @Nullable ANSIColor background) {
		return Objects.equals(fg, foreground) && Objects.equals(bg, background) ? this : new Style(foreground, background, b, i, u, l, r, h, s);
	}

	@Override
	public String toString() {
		if (isDefault()) {
			return "none";
		}

		var sb = new StringBuilder("Style[");
		boolean sep = false;

		if (fg != null) {
			sb.append("fg=");
			sb.append(fg);
			sep = true;
		}

		if (bg != null) {
			if (sep) {
				sb.append(';');
				sb.append(' ');
			}

			sb.append("bg=");
			sb.append(bg);
			sep = true;
		}

		for (var flag : StyleFlag.VALUES) {
			var value = get(flag);

			if (value != null) {
				if (sep) {
					sb.append(';');
					sb.append(' ');
				}

				sb.append(value == Boolean.TRUE ? '+' : '-');
				sb.append(flag.name);
				sep = true;
			}
		}

		return sb.append(']').toString();
	}

	public void appendCode(StringBuilder sb, ANSIContext ctx) {
		sb.append(ctx.code());
		sb.append('[');
		boolean sub = false;

		for (var flag : StyleFlag.VALUES) {
			var value = get(flag);

			if (value == Boolean.TRUE) {
				if (sub) {
					sb.append(';');
				}

				flag.append(sb, true);
				sub = true;
			}
		}

		if (fg != null) {
			if (sub) {
				sb.append(';');
			}

			fg.push(sb, true);
			sub = true;
		}

		if (bg != null) {
			if (sub) {
				sb.append(';');
			}

			bg.push(sb, false);
		}

		sb.append('m');
	}
}
