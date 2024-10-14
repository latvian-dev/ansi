package dev.latvian.apps.ansi;

import dev.latvian.apps.ansi.color.ANSIColor;
import dev.latvian.apps.ansi.log.Log;
import dev.latvian.apps.ansi.style.Style;
import dev.latvian.apps.ansi.style.StyleFlag;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ANSITable implements ANSISupplier {
	public static class Col {
		public final ANSITable table;
		public final int x;
		private int align = -1;
		private Cell label;
		private ANSI padding = ANSI.SPACE;
		private int paddingSize = 1;

		private Col(ANSITable table, int x) {
			this.table = table;
			this.x = x;
		}

		public Col set(int y, Object value) {
			table.set(x, y, value);
			return this;
		}

		public Col set(int y, ANSI ansi) {
			table.set(x, y, ansi);
			return this;
		}

		public Col align(int align) {
			this.align = align;
			return this;
		}

		public Col alignRight() {
			return align(1);
		}

		public Col alignCenter() {
			return align(0);
		}

		public Col padding(ANSI p) {
			padding = p;
			paddingSize = p.toUnformattedString().length();
			return this;
		}
	}

	public static class Row {
		public final ANSITable table;
		public final int y;
		public final List<Cell> cells;

		public Row(ANSITable table, int y) {
			this.table = table;
			this.y = y;
			this.cells = new ArrayList<>(2);
		}

		public Row set(int x, Object value) {
			cells.get(x).value(ANSI.of(value));
			return this;
		}

		public Row set(int x, ANSI ansi) {
			cells.get(x).value(ansi);
			return this;
		}
	}

	public static class Cell {
		public final Col col;
		public final Row row;
		private ANSI value = ANSI.EMPTY;
		private String unformattedValue = "";

		private Cell(Col col, Row row) {
			this.col = col;
			this.row = row;
		}

		public Cell value(ANSI v) {
			value = v;
			unformattedValue = v.toUnformattedString();
			return this;
		}

		public Cell value(Object v) {
			return value(ANSI.of(v));
		}

		public String unformattedValue() {
			return unformattedValue;
		}

		public int unformattedLength() {
			return unformattedValue.length();
		}

		public String csvValue() {
			return escapeCSVSpecialCharacters(unformattedValue);
		}
	}

	public static String escapeCSVSpecialCharacters(@Nullable String data) {
		if (data == null) {
			return "null";
		}

		String escapedData = data.replaceAll("\\R", " ");
		if (data.contains(",") || data.contains("\"") || data.contains("'")) {
			data = data.replace("\"", "\"\"");
			escapedData = "\"" + data + "\"";
		}
		return escapedData;

		//return "\"" + data.replace("\"", "\"\"") + "\"";
	}

	public final Row headRow;
	private final List<Col> cols;
	private final List<Row> rows;
	private ANSIBorders borders;
	private ANSIColor backgroundColor;

	public ANSITable() {
		this.headRow = new Row(this, -1);
		this.cols = new ArrayList<>(2);
		this.rows = new ArrayList<>(2);
		this.borders = ANSIBorders.DEFAULT;
		this.backgroundColor = null;
	}

	public ANSITable(Object... labels) {
		this();

		for (var label : labels) {
			addCol(label);
		}
	}

	public void setBorders(ANSIBorders borders) {
		this.borders = borders;
	}

	public void setBorderStyle(Style style) {
		this.borders = new ANSIBorders(style);
	}

	public void setBackgroundColor(ANSIColor color) {
		this.backgroundColor = color;
	}

	public Col col(int x) {
		return cols.get(x);
	}

	public int cols() {
		return cols.size();
	}

	public Row row(int y) {
		return rows.get(y);
	}

	public int rows() {
		return rows.size();
	}

	public Col addCol(Object label) {
		var col = new Col(this, cols.size());
		cols.add(col);

		col.label = new Cell(col, headRow);
		headRow.cells.add(col.label);
		col.label.value(ANSI.of(label, StyleFlag.BOLD.style()));

		for (var row : rows) {
			row.cells.add(new Cell(col, row));
		}

		return col;
	}

	public Row addRow() {
		var row = new Row(this, rows.size());
		rows.add(row);

		for (var col : cols) {
			row.cells.add(new Cell(col, row));
		}

		return row;
	}

	public void addRow(Object... h) {
		var r = addRow();

		for (int i = 0; i < h.length; i++) {
			r.set(i, h[i]);
		}
	}

	public ANSITable set(int x, int y, Object value) {
		rows.get(y).set(x, value);
		return this;
	}

	public ANSITable set(int x, int y, ANSI ansi) {
		rows.get(y).set(x, ansi);
		return this;
	}

	private ANSI createLine(int[] max, int type) {
		var b = borders;

		var line = ANSI.empty(1 + max.length * 2).background(backgroundColor);
		line.append(type == 0 ? b.dr() : type == 1 ? b.udr() : b.ur());

		for (int i = 0; i < max.length; i++) {
			line.append(b.lr().repeat(max[i] + cols.get(i).paddingSize * 2));

			if (i == max.length - 1) {
				line.append(type == 0 ? b.dl() : type == 1 ? b.udl() : b.ul());
			} else {
				line.append(type == 0 ? b.dlr() : type == 1 ? b.udlr() : b.ulr());
			}
		}

		return line;
	}

	private ANSI createRow(int[] max, Row row) {
		var b = borders;

		var line = ANSI.empty(2 + row.cells.size() * 2).background(backgroundColor);
		line.append(b.ud());

		for (int i = 0; i < max.length; i++) {
			var c = row.cells.get(i);

			if (row == headRow) {
				line.append(ANSI.SPACE.repeat(c.col.paddingSize));
			} else {
				line.append(c.col.padding);
			}

			int l = max[i] - c.unformattedLength();

			if (c.col.align == 0) {
				int l2 = l / 2;

				if (l2 > 0) {
					line.append(ANSI.SPACE.repeat(l2));
				}

				line.append(c.value);

				if (l - l2 > 0) {
					line.append(ANSI.SPACE.repeat(l - l2));
				}
			} else if (c.col.align > 0) {
				if (l > 0) {
					line.append(ANSI.SPACE.repeat(l));
				}

				line.append(c.value);
			} else {
				line.append(c.value);

				if (l > 0) {
					line.append(ANSI.SPACE.repeat(l));
				}
			}

			if (row == headRow) {
				line.append(ANSI.SPACE.repeat(c.col.paddingSize));
			} else {
				line.append(c.col.padding);
			}

			line.append(b.ud());
		}

		return line;
	}

	public List<String> getCSVLines(boolean includeHead) {
		var lines = new ArrayList<String>();

		if (includeHead) {
			addCSVLine(lines, headRow);
		}

		for (var c : rows) {
			addCSVLine(lines, c);
		}

		return lines;
	}

	public byte[] getCSVBytes(boolean includeHead) {
		return String.join("\n", getCSVLines(includeHead)).getBytes(StandardCharsets.UTF_8);
	}

	private void addCSVLine(List<String> lines, Row row) {
		lines.add(row.cells.stream().map(Cell::csvValue).collect(Collectors.joining(",")));
	}

	private static final List<ANSI> EMPTY_LINES = List.of(
		ANSI.immutable("┌───────┐"),
		ANSI.immutable("│ Empty │"),
		ANSI.immutable("└───────┘")
	);

	public List<ANSI> getLines() {
		if (cols.isEmpty()) {
			if (backgroundColor == null && borders.style().isDefault()) {
				return EMPTY_LINES;
			} else {
				return List.of(
					ANSI.immutable("┌───────┐", borders.style().background(backgroundColor)),
					ANSI.immutable("│ Empty │", borders.style().background(backgroundColor)),
					ANSI.immutable("└───────┘", borders.style().background(backgroundColor))
				);
			}
		}

		var lines = new ArrayList<ANSI>(4 + rows.size());
		var max = new int[cols.size()];

		for (var col : cols) {
			max[col.x] = col.label.unformattedLength();
		}

		for (var row : rows) {
			for (var cell : row.cells) {
				max[cell.col.x] = Math.max(max[cell.col.x], cell.unformattedLength());
			}
		}

		lines.add(createLine(max, 0));
		lines.add(createRow(max, headRow));
		lines.add(createLine(max, 1));

		for (var row : rows) {
			lines.add(createRow(max, row));
		}

		lines.add(createLine(max, 2));
		return lines;
	}

	@Override
	public ANSI toANSI() {
		var join = ANSI.empty();

		for (var line : getLines()) {
			line.append(ANSI.LINE);
			join.append(line);
		}

		return join;
	}

	public void print() {
		Log.info(toANSI());
	}

	public void printLines() {
		getLines().forEach(Log::info);
	}
}