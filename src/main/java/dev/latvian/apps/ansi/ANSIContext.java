package dev.latvian.apps.ansi;

public class ANSIContext {
	public static final ANSIContext UNFORMATTED = new ANSIContext(-1);
	public static final ANSIContext NONE = new ANSIContext(0);

	public int index;

	public ANSIContext(int index) {
		this.index = index;
	}

	public boolean unformatted() {
		return index == -1;
	}

	public boolean formatted() {
		return index != -1;
	}

	public boolean debug() {
		return index != 0;
	}

	public char code() {
		return index != 0 ? ANSI.DEBUG_CODE : ANSI.CODE;
	}
}
