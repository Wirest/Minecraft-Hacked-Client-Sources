package de.iotacb.cu.core.string;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class StringUtil {

	/**
	 * Will split the string at the end of a line and returns it as a array
	 * @param input
	 * @return
	 */
	public static final String[] makeArray(final String input) {
		return input.split("\n");
	}
	
	/**
	 * Will split the string at the end of a line and returns it as a list
	 * @param input
	 * @return
	 */
	public static final List<String> makeList(final String input) {
		return Arrays.asList(makeArray(input));
	}
	
	/**
	 * Will join multiple strings with a seperator
	 * @param input
	 * @param seperator
	 * @param begin
	 * @param end
	 * @return
	 */
	public static final String joinArray(final String[] input, final String seperator, int begin, int end) {
		if (begin < 0) begin = 0;
		if (end > input.length) end = input.length;
		
		final StringBuilder builder = new StringBuilder();
		
		for (int i = begin; i < end; i++) {
			builder.append(input[i]).append(seperator);
		}
		
		return builder.toString();
	}
	
	/**
	 * Will join multiple strings with a seperator
	 * @param input
	 * @param seperator
	 * @param begin
	 * @return
	 */
	public static final String joinArray(final String[] input, final String seperator, int begin) {
		return joinArray(input, seperator, begin, input.length);
	}
	
	/**
	 * Will join multiple strings with a seperator
	 * @param input
	 * @param seperator
	 * @return
	 */
	public static final String joinArray(final String[] input, final String seperator) {
		return joinArray(input, seperator, 0, input.length);
	}
	
	/**
	 * Will join multiple strings
	 * @param input
	 * @return
	 */
	public static final String joinArray(final String[] input) {
		return joinArray(input, "");
	}
	
	/**
	 * Will join multiple strings in a list with a seperator
	 * @param input
	 * @param seperator
	 * @param begin
	 * @param end
	 * @return
	 */
	public static final String joinList(final List<String> input, final String seperator, final int begin, final int end) {
		final String[] array = (String[]) input.toArray();
		return joinArray(array, seperator, begin, end);
	}
	
	/**
	 * Will join multiple strings in a list with a seperator
	 * @param input
	 * @param seperator
	 * @param begin
	 * @return
	 */
	public static final String joinList(final List<String> input, final String seperator, final int begin) {
		final String[] array = (String[]) input.toArray();
		return joinArray(array, seperator, begin);
	}
	
	/**
	 * Will join multiple strings in a list with a seperator
	 * @param input
	 * @param seperator
	 * @return
	 */
	public static final String joinList(final List<String> input, final String seperator) {
		final String[] array = (String[]) input.toArray();
		return joinArray(array, seperator);
	}
	
	/**
	 * Will join multiple strings in a list
	 * @param input
	 * @return
	 */
	public static final String joinList(final List<String> input) {
		final String[] array = (String[]) input.toArray();
		return joinArray(array);
	}
	
	/**
	 * Will return the given input without any Minecraft formatting codes
	 * @param input
	 * @return
	 */
	public static final String removeFormatting(final String input) {
		return Pattern.compile("(?i)§[0-9A-FK-OR]").matcher(input).replaceAll("");
	}
	
}
