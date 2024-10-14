package dev.latvian.apps.ansi;

@FunctionalInterface
public interface ANSISupplier {
	ANSI toANSI();

	default String toANSIString() {
		return toANSI().build(ANSIContext.NONE);
	}

	default String toDebugString() {
		return toANSI().build(new ANSIContext(1));
	}

	default String toUnformattedString() {
		return toANSI().build(ANSIContext.UNFORMATTED);
	}
}
