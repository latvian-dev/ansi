package dev.latvian.apps.ansi;

import dev.latvian.apps.ansi.color.ANSIColor;
import dev.latvian.apps.ansi.color.Color16;
import dev.latvian.apps.ansi.style.Style;
import dev.latvian.apps.ansi.style.StyleFlag;
import dev.latvian.apps.ansi.style.Styleable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.SequencedCollection;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public final class ANSI implements ANSISupplier, Styleable<ANSI> {
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

		public boolean hasStyle() {
			return fg != ANSIColor.NONE || bg != ANSIColor.NONE || b || i || u || l || r || h || s;
		}

		public Style style() {
			return new Style(
				fg == ANSIColor.NONE ? null : fg,
				bg == ANSIColor.NONE ? null : bg,
				b ? Boolean.TRUE : null,
				i ? Boolean.TRUE : null,
				u ? Boolean.TRUE : null,
				l ? Boolean.TRUE : null,
				r ? Boolean.TRUE : null,
				h ? Boolean.TRUE : null,
				s ? Boolean.TRUE : null
			);
		}
	}

	public static final char CODE = '\u001B';
	public static final Pattern PATTERN = Pattern.compile(CODE + "\\[(?:\\d;)?\\d+[mD]");
	public static final char DEBUG_CODE = '§';
	private static final char[] RESET = "[0m".toCharArray();
	public static final ANSI EMPTY = immutable("");
	public static final ANSI SPACE = immutable(" ");
	public static final ANSI LINE = immutable("\n");
	public static final ANSI COMMA = immutable(",");
	public static final ANSI COMMA_SPACE = immutable(", ");

	public static ANSI immutable(String content, Style style) {
		return new ANSI(content, style, true);
	}

	public static ANSI immutable(String content) {
		return immutable(content, Style.NONE);
	}

	public static ANSI empty(int expectedPartCount) {
		var result = new ANSI("", Style.NONE, false);
		result.children = new ArrayList<>(expectedPartCount);
		return result;
	}

	public static ANSI empty() {
		return empty(3);
	}

	public static ANSI of(Object text, Style style) {
		if (text instanceof ANSISupplier a) {
			return style.isDefault() ? a.toANSI() : a.toANSI().copy().styled(style);
		} else {
			return new ANSI(String.valueOf(text), style, false);
		}
	}

	public static ANSI of(Object text) {
		if (text instanceof ANSISupplier a) {
			return a.toANSI();
		} else {
			return new ANSI(String.valueOf(text), Style.NONE, false);
		}
	}

	public static ANSI join(@Nullable ANSI delimiter, ANSISupplier... ansi) {
		if (ansi.length == 0) {
			return empty();
		} else if (ansi.length == 1) {
			return ansi[0].toANSI();
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
		return of(text, Color16.BLACK.fgStyle());
	}

	public static ANSI darkRed(Object text) {
		return of(text, Color16.DARK_RED.fgStyle());
	}

	public static ANSI green(Object text) {
		return of(text, Color16.GREEN.fgStyle());
	}

	public static ANSI orange(Object text) {
		return of(text, Color16.ORANGE.fgStyle());
	}

	public static ANSI navy(Object text) {
		return of(text, Color16.NAVY.fgStyle());
	}

	public static ANSI purple(Object text) {
		return of(text, Color16.PURPLE.fgStyle());
	}

	public static ANSI teal(Object text) {
		return of(text, Color16.TEAL.fgStyle());
	}

	public static ANSI lightGray(Object text) {
		return of(text, Color16.LIGHT_GRAY.fgStyle());
	}

	public static ANSI darkGray(Object text) {
		return of(text, Color16.DARK_GRAY.fgStyle());
	}

	public static ANSI red(Object text) {
		return of(text, Color16.RED.fgStyle());
	}

	public static ANSI lime(Object text) {
		return of(text, Color16.LIME.fgStyle());
	}

	public static ANSI yellow(Object text) {
		return of(text, Color16.YELLOW.fgStyle());
	}

	public static ANSI blue(Object text) {
		return of(text, Color16.BLUE.fgStyle());
	}

	public static ANSI magenta(Object text) {
		return of(text, Color16.MAGENTA.fgStyle());
	}

	public static ANSI cyan(Object text) {
		return of(text, Color16.CYAN.fgStyle());
	}

	public static ANSI white(Object text) {
		return of(text, Color16.WHITE.fgStyle());
	}

	private final String content;
	private final boolean immutable;
	private Style style;
	private List<ANSI> children;
	private int parts;

	ANSI(String content, Style style, boolean immutable) {
		this.content = content;
		this.immutable = immutable;
		this.style = style;
		this.children = List.of();
		this.parts = content.isEmpty() ? 0 : 1;
	}

	@Override
	public ANSI toANSI() {
		return this;
	}

	@Override
	public String toANSIString() {
		return build(ANSIContext.NONE);
	}

	@Override
	public String toDebugString() {
		return build(new ANSIContext(1));
	}

	@Override
	public String toUnformattedString() {
		return build(ANSIContext.UNFORMATTED);
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
		if (child.immutable && child.content.isEmpty() && child.children.isEmpty()) {
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

	public ANSI append(String text) {
		if (text.isEmpty()) {
			return this;
		} else if (text.length() == 1) {
			return append(switch (text.charAt(0)) {
				case ' ' -> SPACE;
				case '\n' -> LINE;
				case ',' -> COMMA;
				default -> new ANSI(text, Style.NONE, true);
			});
		} else {
			return append(new ANSI(text, Style.NONE, true));
		}
	}

	public ANSI append(ANSISupplier ansi) {
		return append(ansi.toANSI());
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

	public StyledString[] styledParts() {
		var result = new StyledString[parts];
		appendStyledParts(Style.NONE, result, new int[1]);
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

		if (!content.isEmpty()) {
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
		}

		if (!children.isEmpty()) {
			for (var child : children) {
				child.appendParts(s, parts, index);
			}
		}
	}

	private void appendStyledParts(Style s, StyledString[] parts, int[] index) {
		s = s.merge(style);

		if (!content.isEmpty()) {
			parts[index[0]++] = new StyledString(content, s);
		}

		if (!children.isEmpty()) {
			for (var child : children) {
				child.appendStyledParts(s, parts, index);
			}
		}
	}

	public String build(ANSIContext ctx) {
		if (children.isEmpty()) {
			if (content.isEmpty() || ctx.unformatted() || style.isDefault()) {
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
			if (hasFormatting && !p.hasStyle()) {
				sb.append(ctx.code());
				sb.append(RESET);
				sb.append(p.content);
				foreground = ANSIColor.NONE;
				background = ANSIColor.NONE;
				Arrays.fill(flags, false);
				hasFormatting = false;
				continue;
			}

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

			if (!Objects.equals(foreground, p.fg)) {
				foreground = p.fg;

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

			if (!Objects.equals(background, p.bg)) {
				background = p.bg;

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

			sb.append(p.content);
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

	public ANSI repeat(int times) {
		return times <= 0 && immutable && style.isDefault() ? EMPTY : new ANSI(times <= 0 ? "" : content.repeat(times), style, false);
	}

	public ANSI flatten() {
		if (children.isEmpty()) {
			return copy();
		}

		var result = new StringBuilder();

		for (var part : parts()) {
			result.append(part.content);
		}

		return new ANSI(result.toString(), style, false);
	}

	public int length() {
		int len = content.length();

		if (!children.isEmpty()) {
			for (var child : children) {
				len += child.length();
			}
		}

		return len;
	}

	public ANSI trim(int toLength) {
		if (toLength <= 0) {
			return EMPTY;
		} else if (children.isEmpty()) {
			if (toLength >= content.length()) {
				return this;
			} else {
				return new ANSI(content.substring(0, toLength), style, false);
			}
		} else if (content.length() == toLength) {
			return new ANSI(content, style, false);
		} else if (content.length() > toLength) {
			return new ANSI(content.substring(0, toLength), style, false);
		} else {
			var result = new ANSI(content, style, false);

			for (var c : children) {
				int len = c.length();

				if (toLength - len >= 0) {
					result.append(c);
					toLength -= len;
				} else {
					result.append(c.trim(toLength));
					break;
				}
			}

			return result;
		}
	}

	public StyledCharacter[] toCharacterArray() {
		var chars = new StyledCharacter[length()];
		toCharacterArray0(Style.NONE, chars, new int[1]);
		return chars;
	}

	private void toCharacterArray0(Style s, StyledCharacter[] chars, int[] index) {
		s = s.merge(style);

		if (!content.isEmpty()) {
			if (content.length() == 1) {
				chars[index[0]++] = new StyledCharacter(content.charAt(0), s);
			} else {
				for (var c : content.toCharArray()) {
					chars[index[0]++] = new StyledCharacter(c, s);
				}
			}
		}

		if (!children.isEmpty()) {
			for (var child : children) {
				child.toCharacterArray0(s, chars, index);
			}
		}
	}

	public List<List<StyledString>> lines(int maxLength) {
		if (maxLength <= 0 || children.isEmpty() && content.isEmpty()) {
			return List.of();
		}

		var result = new ArrayList<List<StyledString>>(1);
		var currentString = new StringBuilder();
		Style currentStyle = null;
		int len = 0;

		var currentLine = new ArrayList<StyledString>(1);

		for (var ch : toCharacterArray()) {
			if (ch.content() == '\n') {
				if (currentStyle != null && !currentString.isEmpty()) {
					currentLine.add(new StyledString(currentString.toString(), currentStyle));
				}

				result.add(currentLine);
				currentLine = new ArrayList<>(1);
				currentString.setLength(0);
				currentStyle = null;
				len = 0;
			} else {
				if (currentStyle == null || !currentStyle.equals(ch.style())) {
					if (!currentString.isEmpty()) {
						currentLine.add(new StyledString(currentString.toString(), currentStyle == null ? Style.NONE : currentStyle));
						currentString.setLength(0);
					}

					currentStyle = ch.style();
				}

				currentString.append(ch.content());
				len++;

				if (len == maxLength) {
					if (currentStyle != null && !currentString.isEmpty()) {
						currentLine.add(new StyledString(currentString.toString(), currentStyle));
					}

					result.add(currentLine);
					currentLine = new ArrayList<>(1);
					currentString.setLength(0);
					currentStyle = null;
					len = 0;
				}
			}
		}

		if (currentStyle != null && !currentString.isEmpty()) {
			currentLine.add(new StyledString(currentString.toString(), currentStyle));
			result.add(currentLine);
		}

		return result;
	}
}
