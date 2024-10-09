package dev.latvian.apps.ansi.color;

public class NoColor implements ANSIColor {
	private static final char[] FG_CODE = "39".toCharArray();
	private static final char[] BG_CODE = "49".toCharArray();

	NoColor() {
	}

	@Override
	public void push(StringBuilder builder, boolean foreground) {
		builder.append(foreground ? FG_CODE : BG_CODE);
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public String toString() {
		return "none";
	}
}
