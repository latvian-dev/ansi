package dev.latvian.apps.ansi.terminal;

import dev.latvian.apps.ansi.color.Color16;
import dev.latvian.apps.ansi.log.CallbackPrintStream;
import dev.latvian.apps.ansi.log.DefaultLog;
import dev.latvian.apps.ansi.log.Log;

import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

public class Terminal {
	private static boolean rawModeEnabled = false;

	public static void enableRawMode() throws IOException, InterruptedException {
		rawModeEnabled = true;
		// TODO: Support other OS
		Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "stty raw -echo </dev/tty"}).waitFor(10L, TimeUnit.SECONDS);
	}

	public static void disableRawMode() throws IOException, InterruptedException {
		rawModeEnabled = false;
		// TODO: Support other OS
		Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "stty cooked echo </dev/tty"}).waitFor(10L, TimeUnit.SECONDS);
	}

	public static boolean isRawModeEnabled() {
		return rawModeEnabled;
	}

	@SuppressWarnings("UnusedReturnValue")
	public static PrintStream replaceSystemLog() {
		var out = System.out;
		Log.INSTANCE.replace(new DefaultLog(out, Color16.CYAN.fgStyle()));
		System.setOut(new CallbackPrintStream(Log::info));
		System.setErr(new CallbackPrintStream(Log::error));
		return out;
	}
}
