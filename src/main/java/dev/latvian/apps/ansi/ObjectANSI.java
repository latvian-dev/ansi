package dev.latvian.apps.ansi;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ObjectANSI {
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

	static ANSI ofObject(Object object) {
		return ofObject0(object, 0);
	}

	private static ANSI ofObject0(@Nullable Object object, int depth) {
		if (object == null || object == GSON_NULL || object == LSON_NULL) {
			return ANSI.darkRed("null");
		} else if (object instanceof Map<?, ?> map) {
			var builder = ANSI.empty(map.size() * 2 - 1 + 2).tree(depth);
			builder.append('{');
			boolean first = true;

			for (var entry : map.entrySet()) {
				if (first) {
					first = false;
				} else {
					builder.append(',');
				}

				builder.append(ANSI.cyan("\"" + entry.getKey() + "\""));
				builder.append(':');
				builder.append(ofObject0(entry.getValue(), depth + 1));
			}

			builder.append('}');
			return builder;
		} else if (object instanceof Iterable<?> itr) {
			var builder = ANSI.empty(10);
			builder.append('[');
			boolean first = true;

			for (var e : itr) {
				if (first) {
					first = false;
				} else {
					builder.append(',');
				}

				builder.append(ofObject0(e, depth + 1));
			}

			builder.append(']');
			return builder;
		} else if (object instanceof CharSequence) {
			return ANSI.orange("\"" + object + "\"");
		} else if (object instanceof Boolean b) {
			return b ? ANSI.lime("true") : ANSI.red("false");
		} else if (object instanceof Number) {
			return ANSI.purple(object);
		} else if (object.getClass().isRecord()) {
			var builder = ANSI.empty().tree(depth);
			builder.append('{');
			boolean first = true;

			for (var component : object.getClass().getRecordComponents()) {
				if (first) {
					first = false;
				} else {
					builder.append(',');
				}

				builder.append(ANSI.cyan("\"" + component.getName() + "\""));
				builder.append(':');

				try {
					builder.append(ofObject0(component.getAccessor().invoke(object), depth + 1));
				} catch (Exception ex) {
					builder.append(ANSI.of("<error>").error());
					ex.printStackTrace();
				}
			}

			builder.append('}');
			return builder;
		} else if (object.getClass().isEnum()) {
			return ANSI.orange(object);
		} else {
			return ANSI.lightGray(object);
		}
	}
}
