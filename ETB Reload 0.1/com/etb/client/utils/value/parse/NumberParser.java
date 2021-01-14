package com.etb.client.utils.value.parse;

public final class NumberParser {

    public static <T extends Number> T parse(final String input, final Class<T> numberType) throws NumberFormatException {
        return NumberCaster.cast(numberType, Double.parseDouble(input));
    }
}
