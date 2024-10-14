package dev.latvian.apps.ansi.test;

import dev.latvian.apps.ansi.ANSI;
import dev.latvian.apps.ansi.ANSITable;
import dev.latvian.apps.ansi.color.Color16;
import dev.latvian.apps.ansi.style.Style;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class TableTest {
	private static String randomString(Random random) {
		var chars = new char[1 + random.nextInt(20)];

		for (int j = 0; j < chars.length; j++) {
			chars[j] = (char) ('0' + random.nextInt(10));
		}

		return new String(chars);
	}

	@Test
	public void table() {
		var table = new ANSITable("Default", ANSI.green("Green Label"), "Colored Values");
		table.col(1).alignCenter().padding(ANSI.of(" - "));
		table.col(2).alignRight();
		table.setBackgroundColor(Color16.BLACK);
		table.setBorderStyle(Style.NONE.foreground(218));

		var random = new Random();

		for (int i = 0; i < 15; i++) {
			table.addRow(
				"Left " + randomString(random),
				"Center " + randomString(random),
				ANSI.of("Right " + randomString(random)).foreground(104 + random.nextInt(151))
			);
		}

		table.addCol("Post-init col");
		table.addRow("Post-init row");

		table.printLines();
	}
}
