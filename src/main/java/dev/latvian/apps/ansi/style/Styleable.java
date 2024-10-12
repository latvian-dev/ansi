package dev.latvian.apps.ansi.style;

import dev.latvian.apps.ansi.color.ANSIColor;
import dev.latvian.apps.ansi.color.Color16;
import org.jetbrains.annotations.Nullable;

public interface Styleable<T extends Styleable<T>> {
	T set(StyleFlag flag, @Nullable Boolean value);

	@Nullable
	Boolean get(StyleFlag flag);

	T foreground(@Nullable ANSIColor value);

	T background(@Nullable ANSIColor value);

	default T foreground(int color) {
		return foreground(ANSIColor.of(color));
	}

	default T background(int color) {
		return background(ANSIColor.of(color));
	}

	default T colors(@Nullable ANSIColor foregroundValue, @Nullable ANSIColor backgroundValue) {
		return foreground(foregroundValue).background(backgroundValue);
	}

	default T bold(@Nullable Boolean value) {
		return set(StyleFlag.BOLD, value);
	}

	default T bold() {
		return bold(Boolean.TRUE);
	}

	default T italic(@Nullable Boolean value) {
		return set(StyleFlag.ITALIC, value);
	}

	default T italic() {
		return italic(Boolean.TRUE);
	}

	default T underline(@Nullable Boolean value) {
		return set(StyleFlag.UNDERLINE, value);
	}

	default T underline() {
		return underline(Boolean.TRUE);
	}

	default T blink(@Nullable Boolean value) {
		return set(StyleFlag.BLINK, value);
	}

	default T blink() {
		return blink(Boolean.TRUE);
	}

	default T reverse(@Nullable Boolean value) {
		return set(StyleFlag.REVERSE, value);
	}

	default T reverse() {
		return reverse(Boolean.TRUE);
	}

	default T hidden(@Nullable Boolean value) {
		return set(StyleFlag.HIDDEN, value);
	}

	default T hidden() {
		return hidden(Boolean.TRUE);
	}

	default T strikethrough(@Nullable Boolean value) {
		return set(StyleFlag.STRIKETHROUGH, value);
	}

	default T strikethrough() {
		return strikethrough(Boolean.TRUE);
	}

	default T rgb(int color) {
		return foreground(ANSIColor.of(color));
	}

	default T black() {
		return foreground(Color16.BLACK);
	}

	default T darkRed() {
		return foreground(Color16.DARK_RED);
	}

	default T green() {
		return foreground(Color16.GREEN);
	}

	default T orange() {
		return foreground(Color16.ORANGE);
	}

	default T navy() {
		return foreground(Color16.NAVY);
	}

	default T purple() {
		return foreground(Color16.PURPLE);
	}

	default T teal() {
		return foreground(Color16.TEAL);
	}

	default T lightGray() {
		return foreground(Color16.LIGHT_GRAY);
	}

	default T darkGray() {
		return foreground(Color16.DARK_GRAY);
	}

	default T red() {
		return foreground(Color16.RED);
	}

	default T lime() {
		return foreground(Color16.LIME);
	}

	default T yellow() {
		return foreground(Color16.YELLOW);
	}

	default T blue() {
		return foreground(Color16.BLUE);
	}

	default T magenta() {
		return foreground(Color16.MAGENTA);
	}

	default T cyan() {
		return foreground(Color16.CYAN);
	}

	default T white() {
		return foreground(Color16.WHITE);
	}

	default T rgbBG(int color) {
		return background(ANSIColor.of(color));
	}

	default T blackBG() {
		return background(Color16.BLACK);
	}

	default T darkRedBG() {
		return background(Color16.DARK_RED);
	}

	default T greenBG() {
		return background(Color16.GREEN);
	}

	default T orangeBG() {
		return background(Color16.ORANGE);
	}

	default T navyBG() {
		return background(Color16.NAVY);
	}

	default T purpleBG() {
		return background(Color16.PURPLE);
	}

	default T tealBG() {
		return background(Color16.TEAL);
	}

	default T lightGrayBG() {
		return background(Color16.LIGHT_GRAY);
	}

	default T darkGrayBG() {
		return background(Color16.DARK_GRAY);
	}

	default T redBG() {
		return background(Color16.RED);
	}

	default T limeBG() {
		return background(Color16.LIME);
	}

	default T yellowBG() {
		return background(Color16.YELLOW);
	}

	default T blueBG() {
		return background(Color16.BLUE);
	}

	default T magentaBG() {
		return background(Color16.MAGENTA);
	}

	default T cyanBG() {
		return background(Color16.CYAN);
	}

	default T whiteBG() {
		return background(Color16.WHITE);
	}

	default T error() {
		return colors(Color16.WHITE, Color16.DARK_RED);
	}

	default T tree(int index) {
		return foreground(Color16.tree(index));
	}
}
