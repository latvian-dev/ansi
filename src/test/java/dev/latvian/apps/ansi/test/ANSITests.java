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

	private static void log(ANSI ansi) {
		Log.info(ansi);
		Log.info(ansi.toUnformattedString());
		Log.info(ansi.toDebugString());
	}

	@Test
	public void single() {
		log(ANSI.red("Red & Underlined").underline());
	}

	@Test
	public void combo() {
		log(ANSI.empty()
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
			.append(ANSI.of("Error!", LogType.ERROR.style())));
	}

	@Test
	public void reverse() {
		log(TEST_123);
	}

	@Test
	public void object() {
		var ansi = JavaANSI.of(Map.of("inum", 10, "fnum", 30.4F, "arr", List.of("a", "b", "c"), "sub", Map.of("t", true, "f", false, "b", List.of()), "empty_map", Map.of()));
		// ansi = ANSI.empty(1).append(ansi).lightGray();
		log(ansi);
	}

	@Test
	public void trim() {
		log(ANSI.empty().append("<START>").append(TEST_123.trim(12)).append("<END>"));
	}

	@Test
	public void chars() {
		log(ANSI.join(ANSI.COMMA_SPACE, TEST_123.toCharacterArray()));
	}

	@Test
	public void lines() {
		var lines = ANSI.empty()
			.foreground(224)
			.background(96)
			.append(" Te\nxt 1 ")
			.append(ANSI.of(" Text 2 ").reverse())
			.append(" Text 3 ")
			.line()
			.append(" Text 4 ")
			.lines(1000);

		Log.info(lines.size() + " lines:");

		for (int i = 0; i < lines.size(); i++) {
			var ansi = ANSI.empty();

			for (var part : lines.get(i)) {
				ansi.append(part);
			}

			Log.info(ANSI.empty().append(ANSI.white(" Line " + i + " ").limeBG()).space().append(ansi));
		}
	}
}
