package moonx.ohare.client.utils.value.clamper;

public class NumberClamper {
	private NumberClamper() {
	}

	public static <T extends Number> T clamp(final T value, final T min, final T max) {
		return (((Comparable) value).compareTo(min) < 0) ? min
				: ((((Comparable) value).compareTo(max) > 0) ? max : value);
	}
}
