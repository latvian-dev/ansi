package dev.latvian.apps.ansi;

@FunctionalInterface
public interface ANSISupplier {
	ANSI getANSI();

	default String toANSIString() {
		return getANSI().build(ANSIContext.NONE);
	}

	default String toDebugString() {
		return getANSI().build(new ANSIContext(1));
	}

	default String toUnformattedString() {
		return getANSI().build(ANSIContext.UNFORMATTED);
	}
}
