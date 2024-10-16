package dev.latvian.apps.ansi;

import dev.latvian.apps.ansi.color.Color16;
import dev.latvian.apps.ansi.style.Style;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public interface JavaANSI {
	ANSI NULL = ANSI.immutable("null", Color16.DARK_RED.fgStyle());
	ANSI ERROR = ANSI.immutable("<error>", Style.NONE.error());
	ANSI TRUE = ANSI.immutable("true", Color16.LIME.fgStyle());
	ANSI FALSE = ANSI.immutable("false", Color16.RED.fgStyle());
	ANSI SEP = ANSI.immutable(", ");

	@Nullable
	private static Object find(String className, String fieldName) {
		try {
			return Class.forName(className).getField(fieldName).get(null);
		} catch (Exception e) {
			return null;
		}
	}

	Object GSON_NULL = find("com.google.gson.JsonNull", "INSTANCE");
	Object LSON_NULL = find("dev.latvian.apps.lson.JSON", "NULL");

	static ANSI of(Object object) {
		return of0(object, 0);
	}

	static ANSI of0(@Nullable Object object, int depth) {
		if (object == null || object == GSON_NULL || object == LSON_NULL) {
			return NULL;
		} else if (object instanceof Map<?, ?> map) {
			var builder = ANSI.empty(map.size() * 2 - 1 + 2).tree(depth);
			builder.append(ANSISymbols.DEFAULT.lc());
			boolean first = true;

			for (var entry : map.entrySet()) {
				if (first) {
					first = false;
					builder.append(entry.getKey() + ": ");
				} else {
					builder.append(", " + entry.getKey() + ": ");
				}

				builder.append(of0(entry.getValue(), depth + 1));
			}

			builder.append(ANSISymbols.DEFAULT.rc());
			return builder;
		} else if (object instanceof Iterable<?> itr) {
			var builder = ANSI.empty(itr instanceof Collection<?> c ? (c.size() * 2 - 1 + 2) : 10).tree(depth);
			builder.append(ANSISymbols.DEFAULT.ls());
			boolean first = true;

			for (var e : itr) {
				if (first) {
					first = false;
				} else {
					builder.append(SEP);
				}

				builder.append(of0(e, depth + 1));
			}

			builder.append(ANSISymbols.DEFAULT.rs());
			return builder;
		} else if (object instanceof Character) {
			return ANSI.yellow("'" + object + "'");
		} else if (object instanceof CharSequence) {
			return ANSI.yellow("\"" + object + "\"");
		} else if (object instanceof Boolean b) {
			return b ? TRUE : FALSE;
		} else if (object instanceof Number) {
			return ANSI.orange(object);
		} else if (object.getClass().isRecord()) {
			var components = object.getClass().getRecordComponents();
			var builder = ANSI.empty(components.length * 2 - 1 + 2).tree(depth);
			builder.append(ANSISymbols.DEFAULT.lc());
			boolean first = true;

			for (var component : components) {
				if (first) {
					first = false;
					builder.append(component.getName() + ": ");
				} else {
					builder.append(", " + component.getName() + ": ");
				}

				try {
					builder.append(of0(component.getAccessor().invoke(object), depth + 1));
				} catch (Exception ex) {
					builder.append(ERROR);
				}
			}

			builder.append(ANSISymbols.DEFAULT.rc());
			return builder;
		} else if (object instanceof ANSISupplier supplier) {
			return supplier.toANSI();
		} else if (object.getClass().isEnum()) {
			return ANSI.yellow(object);
		} else {
			return ANSI.lightGray(object);
		}
	}
}
