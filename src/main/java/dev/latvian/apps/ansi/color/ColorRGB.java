package dev.latvian.apps.ansi.color;

public record ColorRGB(int rgb) implements ANSIColor {
	private static final char[] FG_CODE = "38;2;".toCharArray();
	private static final char[] BG_CODE = "48;2;".toCharArray();

	public ColorRGB(int rgb) {
		this.rgb = rgb & 0xFFFFFF;
	}

	@Override
	public void push(StringBuilder builder, boolean foreground) {
		builder.append(foreground ? FG_CODE : BG_CODE);
		builder.append((rgb >> 16) & 0xFF);
		builder.append(';');
		builder.append((rgb >> 8) & 0xFF);
		builder.append(';');
		builder.append(rgb & 0xFF);
	}

	@Override
	public String toString() {
		return "#%06X".formatted(rgb);
	}
}
