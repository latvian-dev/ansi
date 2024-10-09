package dev.latvian.apps.ansi.color;

public record Color256(int code) implements ANSIColor {
	static final ANSIColor[] ALL = new ANSIColor[256];
	private static final char[] FG_CODE = "38;5;".toCharArray();
	private static final char[] BG_CODE = "48;5;".toCharArray();

	static {
		System.arraycopy(Color16.VALUES, 0, ALL, 0, 16);

		for (int i = 16; i < 256; i++) {
			ALL[i] = new Color256(i);
		}
	}

	@Override
	public void push(StringBuilder builder, boolean foreground) {
		builder.append(foreground ? FG_CODE : BG_CODE);
		builder.append(code);
	}

	@Override
	public String toString() {
		return "#" + code;
	}
}
