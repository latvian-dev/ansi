package dev.latvian.apps.ansi.test;

import dev.latvian.apps.ansi.ANSI;
import dev.latvian.apps.ansi.JavaANSI;
import dev.latvian.apps.ansi.log.Log;
import dev.latvian.apps.ansi.log.LogType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class ANSITests {
	private static final ANSI TEST_123 = ANSI.empty()
		.foreground(224)
		.background(96)
		.append(" Text 1 ")
		.append(ANSI.of(" Text 2 ").reverse())
		.append(" Text 3 ");

	@Test
	public void single() {
		var ansi = ANSI.red("Red & Underlined").underline();

		Log.info(ansi);
		Log.info(ansi.toUnformattedString());
		Log.info(ansi.toDebugString());
	}

	@Test
	public void combo() {
		var ansi = ANSI.empty()
			.append("Default ")
			.append(ANSI.red("")
				.append("Red ")
				.append(ANSI.of("Red & Underlined").underline()
					.append(ANSI.of(ANSI.SPACE).underline(false))
					.append(ANSI.blue("(Blue)"))
				)
				.append(" Red")
			)
			.append(" Default ")
			.append(ANSI.of("Error!", LogType.ERROR.style()));

		Log.info(ansi);
		Log.info(ansi.toUnformattedString());
		Log.info(ansi.toDebugString());
	}

	@Test
	public void reverse() {
		var ansi = TEST_123;

		Log.info(ansi);
		Log.info(ansi.toUnformattedString());
		Log.info(ansi.toDebugString());
	}

	@Test
	public void object() {
		var ansi = JavaANSI.of(Map.of("inum", 10, "fnum", 30.4F, "arr", List.of("a", "b", "c"), "sub", Map.of("t", true, "f", false, "b", List.of()), "empty_map", Map.of()));
		// ansi = ANSI.empty(1).append(ansi).lightGray();

		Log.info(ansi);
		Log.info(ansi.toUnformattedString());
		Log.info(ansi.toDebugString());
	}

	@Test
	public void trim() {
		var ansi = ANSI.empty().append("<START>").append(TEST_123.trim(12)).append("<END>");

		Log.info(ansi);
		Log.info(ansi.toUnformattedString());
		Log.info(ansi.toDebugString());
	}

	@Test
	public void chars() {
		var ansi = ANSI.join(ANSI.of(", "), TEST_123.toCharacterArray());

		Log.info(ansi);
		Log.info(ansi.toUnformattedString());
		Log.info(ansi.toDebugString());
	}
}
