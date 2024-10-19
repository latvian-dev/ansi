package dev.latvian.apps.ansi.log;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;

public class CallbackPrintStream extends PrintStream {
	public final Consumer<Object> callback;

	public CallbackPrintStream(Consumer<Object> callback) {
		super(OutputStream.nullOutputStream());
		this.callback = callback;
	}

	@Override
	public void println(int x) {
		callback.accept(x);
	}

	@Override
	public void println(char x) {
		callback.accept(x);
	}

	@Override
	public void println(long x) {
		callback.accept(x);
	}

	@Override
	public void println(float x) {
		callback.accept(x);
	}

	@Override
	public void println(char[] x) {
		callback.accept(new String(x));
	}

	@Override
	public void println(double x) {
		callback.accept(x);
	}

	@Override
	public void println(String x) {
		callback.accept(x);
	}

	@Override
	public void println(boolean x) {
		callback.accept(x);
	}

	@Override
	public void println(Object x) {
		callback.accept(x);
	}
}
