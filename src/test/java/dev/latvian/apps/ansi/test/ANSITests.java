package dev.latvian.apps.ansi.test;

import dev.latvian.apps.ansi.ANSI;
import dev.latvian.apps.ansi.ObjectANSI;
import dev.latvian.apps.ansi.log.LogType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class ANSITests {
	@Test
	public void single() {
		var ansi = ANSI.red("Red & Underlined").underline();

		System.out.println(ansi);
		System.out.println(ansi.toUnformattedString());
		System.out.println(ansi.toDebugString());
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

		System.out.println(ansi);
		System.out.println(ansi.toUnformattedString());
		System.out.println(ansi.toDebugString());
	}

	@Test
	public void reverse() {
		var ansi = ANSI.empty()
			.lime()
			.darkGrayBG()
			.append(" Text 1 ")
			.append(ANSI.of(" Text 2 ").reverse())
			.append(" Text 3 ");

		System.out.println(ansi);
		System.out.println(ansi.toUnformattedString());
		System.out.println(ansi.toDebugString());
	}

	@Test
	public void object() {
		var ansi = ObjectANSI.ofObject(Map.of("inum", 10, "fnum", 30.4F, "arr", List.of("a", "b", "c"), "sub", Map.of("t", true, "f", false, "b", List.of()), "empty_map", Map.of()));

		System.out.println(ansi);
		System.out.println(ansi.toUnformattedString());
		System.out.println(ansi.toDebugString());
	}
}
