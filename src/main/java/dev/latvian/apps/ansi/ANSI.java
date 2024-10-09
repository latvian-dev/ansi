package dev.latvian.apps.ansi;

import dev.latvian.apps.ansi.color.ANSIColor;
import dev.latvian.apps.ansi.color.Color16;
import dev.latvian.apps.ansi.style.Style;
import dev.latvian.apps.ansi.style.StyleFlag;
import dev.latvian.apps.ansi.style.Styleable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.SequencedCollection;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public final class ANSI implements ANSISupplier, Styleable<ANSI> {
	/*
	// SEQ 1 - [s [9999;9999H [6n [u
	// SE1 2 - [2J [9999;9999H [6n [1;1H

	// [2J - clear screen
	// [s - save cursor position
	// [u - restore cursor position
	// [9999;9999H - move cursor to bottom right
	// [6n - get cursor position (R terminates)
	// [1;1H - move cursor to top left
	 */

	public record Part(String content, ANSIColor fg, ANSIColor bg, boolean b, boolean i, boolean u, boolean l, boolean r, boolean h, boolean s) {
		public boolean get(StyleFlag flag) {
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
	}

	public static final char CODE = '\u001B';
	public static final Pattern PATTERN = Pattern.compile(CODE + "\\[(?:\\d;)?\\d+[mD]");
	public static final char DEBUG_CODE = '§';
	private static final char[] RESET = "[0m".toCharArray();
	public static final ANSI EMPTY = new ANSI("", Style.NONE, true);
	public static final ANSI SPACE = new ANSI(" ", Style.NONE, true);
	public static final ANSI LINE = new ANSI("\n", Style.NONE, true);

	public static ANSI empty(int expectedPartCount) {
		var result = new ANSI("", Style.NONE, false);
		result.children = new ArrayList<>(expectedPartCount);
		return result;
	}

	public static ANSI empty() {
		return empty(3);
	}

	public static ANSI of(Object text, Style style) {
		return text instanceof ANSI a ? new ANSI(a.content, a.style.merge(style), false) : new ANSI(String.valueOf(text), style, false);
	}

	public static ANSI of(Object text) {
		return text instanceof ANSI a ? new ANSI(a.content, a.style, false) : new ANSI(String.valueOf(text), Style.NONE, false);
	}

	public static ANSI join(@Nullable ANSI delimiter, ANSI... ansi) {
		if (ansi.length == 0) {
			return empty();
		} else if (ansi.length == 1) {
			return ansi[0];
		}

		boolean d = delimiter != null && delimiter != EMPTY;
		var result = empty(d ? (ansi.length * 2 - 1) : ansi.length);

		for (int i = 0; i < ansi.length; i++) {
			if (i > 0 && d) {
				result.append(delimiter.copy());
			}

			result.append(ansi[i]);
		}

		return result;
	}

	public static ANSI join(@Nullable ANSI delimiter, Collection<ANSI> ansi) {
		if (ansi.isEmpty()) {
			return empty();
		} else if (ansi.size() == 1) {
			return ansi instanceof SequencedCollection<ANSI> c ? c.getFirst() : ansi.iterator().next();
		}

		boolean d = delimiter != null && delimiter != EMPTY;
		var result = empty(d ? (ansi.size() * 2 - 1) : ansi.size());
		boolean first = true;

		for (var a : ansi) {
			if (first) {
				first = false;
			} else if (d) {
				result.append(delimiter.copy());
			}

			result.append(a);
		}

		return result;
	}

	public static ANSI black(Object text) {
		return of(text, Color16.BLACK.style());
	}

	public static ANSI darkRed(Object text) {
		return of(text, Color16.DARK_RED.style());
	}

	public static ANSI green(Object text) {
		return of(text, Color16.GREEN.style());
	}

	public static ANSI orange(Object text) {
		return of(text, Color16.ORANGE.style());
	}

	public static ANSI navy(Object text) {
		return of(text, Color16.NAVY.style());
	}

	public static ANSI purple(Object text) {
		return of(text, Color16.PURPLE.style());
	}

	public static ANSI teal(Object text) {
		return of(text, Color16.TEAL.style());
	}

	public static ANSI lightGray(Object text) {
		return of(text, Color16.LIGHT_GRAY.style());
	}

	public static ANSI darkGray(Object text) {
		return of(text, Color16.DARK_GRAY.style());
	}

	public static ANSI red(Object text) {
		return of(text, Color16.RED.style());
	}

	public static ANSI lime(Object text) {
		return of(text, Color16.LIME.style());
	}

	public static ANSI yellow(Object text) {
		return of(text, Color16.YELLOW.style());
	}

	public static ANSI blue(Object text) {
		return of(text, Color16.BLUE.style());
	}

	public static ANSI magenta(Object text) {
		return of(text, Color16.MAGENTA.style());
	}

	public static ANSI cyan(Object text) {
		return of(text, Color16.CYAN.style());
	}

	public static ANSI white(Object text) {
		return of(text, Color16.WHITE.style());
	}

	private final String content;
	private final boolean immutable;
	private Style style;
	private List<ANSI> children;
	private int parts;

	private ANSI(String content, Style style, boolean immutable) {
		this.content = content;
		this.immutable = immutable;
		this.style = style;
		this.children = List.of();
		this.parts = 1;
	}

	@Override
	public ANSI getANSI() {
		return this;
	}

	public ANSI withStyle(Style style) {
		if (immutable) {
			return new ANSI(content, style, false);
		}

		this.style = style;
		return this;
	}

	public ANSI styled(Style merge) {
		return withStyle(style.merge(merge));
	}

	public ANSI styled(UnaryOperator<Style> restyle) {
		return styled(restyle.apply(style));
	}

	public ANSI append(ANSI child) {
		if (child == EMPTY) {
			return this;
		} else if (immutable) {
			return new ANSI(content, style, false).append(child);
		} else if (children.isEmpty()) {
			children = new ArrayList<>(1);
		}

		children.add(child);
		parts += child.parts;
		return this;
	}

	public ANSI append(Object text) {
		return append(of(text));
	}

	public ANSI space() {
		return append(SPACE);
	}

	public ANSI line() {
		return append(LINE);
	}

	public boolean immutable() {
		return immutable;
	}

	public String content() {
		return content;
	}

	public Style style() {
		return style;
	}

	public List<ANSI> children() {
		return children;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		var that = (ANSI) obj;
		return Objects.equals(this.content, that.content) &&
			Objects.equals(this.style, that.style) &&
			Objects.equals(this.children, that.children);
	}

	@Override
	public int hashCode() {
		return Objects.hash(content, style, children);
	}

	@Override
	public String toString() {
		return toANSIString();
	}

	public ANSI copy() {
		if (immutable) {
			return this;
		}

		var result = new ANSI(content, style, false);

		if (!children.isEmpty()) {
			result.children = new ArrayList<>(children.size());

			for (var c : children) {
				result.append(c.copy());
			}
		}

		return result;
	}

	public Part[] parts() {
		var result = new Part[parts];
		appendParts(Style.NONE, result, new int[1]);
		return result;
	}

	public void appendPartContent(StringBuilder builder) {
		builder.append(content);

		if (!children.isEmpty()) {
			for (var child : children) {
				child.appendPartContent(builder);
			}
		}
	}

	private void appendParts(Style s, Part[] parts, int[] index) {
		s = s.merge(style);
		parts[index[0]++] = new Part(content,
			s.fg() == null ? ANSIColor.NONE : s.fg(),
			s.bg() == null ? ANSIColor.NONE : s.bg(),
			s.b() != null && s.b(),
			s.i() != null && s.i(),
			s.u() != null && s.u(),
			s.l() != null && s.l(),
			s.r() != null && s.r(),
			s.h() != null && s.h(),
			s.s() != null && s.s()
		);

		if (!children.isEmpty()) {
			for (var child : children) {
				child.appendParts(s, parts, index);
			}
		}
	}

	public String build(ANSIContext ctx) {
		if (children.isEmpty()) {
			if (ctx.unformatted() || style.isDefault()) {
				return content;
			} else {
				var sb = new StringBuilder(6 + content.length() + 2);
				style.appendCode(sb, ctx);
				sb.append(content);
				sb.append(ctx.code());
				sb.append(RESET);
				return sb.toString();
			}
		}

		var sb = new StringBuilder();

		if (ctx.unformatted()) {
			appendPartContent(sb);
			return sb.toString();
		}

		ANSIColor foreground = ANSIColor.NONE;
		ANSIColor background = ANSIColor.NONE;
		var flags = new boolean[StyleFlag.VALUES.length];
		boolean hasFormatting = false;

		for (var p : parts()) {
			boolean code = false;
			boolean first = true;

			for (var flag : StyleFlag.VALUES) {
				var f = flag.ordinal();
				var v = p.get(flag);

				if (flags[f] != v) {
					flags[f] = v;

					if (!code) {
						code = true;
						hasFormatting = true;
						sb.append(ctx.code());
						sb.append('[');
					}

					if (first) {
						first = false;
					} else {
						sb.append(';');
					}

					flag.append(sb, v);
				}
			}

			if (!Objects.equals(foreground, p.fg())) {
				foreground = p.fg();

				if (!code) {
					code = true;
					hasFormatting = true;
					sb.append(ctx.code());
					sb.append('[');
				}

				if (first) {
					first = false;
				} else {
					sb.append(';');
				}

				foreground.push(sb, true);
			}

			if (!Objects.equals(background, p.bg())) {
				background = p.bg();

				if (!code) {
					code = true;
					hasFormatting = true;
					sb.append(ctx.code());
					sb.append('[');
				}

				if (!first) {
					sb.append(';');
				}

				background.push(sb, false);
			}

			if (code) {
				sb.append('m');
			}

			sb.append(p.content());
		}

		if (hasFormatting) {
			sb.append(ctx.code());
			sb.append(RESET);
		}

		return sb.toString();
	}

	@Override
	public ANSI set(StyleFlag flag, @Nullable Boolean value) {
		return withStyle(style.set(flag, value));
	}

	@Override
	@Nullable
	public Boolean get(StyleFlag flag) {
		return style.get(flag);
	}

	@Override
	public ANSI foreground(@Nullable ANSIColor value) {
		return withStyle(style.foreground(value));
	}

	@Override
	public ANSI background(@Nullable ANSIColor value) {
		return withStyle(style.background(value));
	}

	@Override
	public ANSI colors(@Nullable ANSIColor foregroundValue, @Nullable ANSIColor backgroundValue) {
		return withStyle(style.colors(foregroundValue, backgroundValue));
	}
}
