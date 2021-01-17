package me.ihaq.iClient.utils.values;

public class ValueUtils {
	
	/**
	 * Returns the Index of the specified Object in the Array, returns -1 if the
	 * object is not found in the array
	 * 
	 * @param object
	 *            - Object to be searched for
	 * @param array
	 *            - Array to search for object in
	 * @return Index of the Object in the Array
	 */
	public static <T> int indexOf(T object, T[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Returns if the specified object is present in the specified array
	 * 
	 * @param object
	 *            - Object to check if is present in Array
	 * @param array
	 *            - Array that may/may not contain the Object
	 * @return true if the specified object is in the specified array, false if
	 *         not
	 */
	public static <T> boolean arrayContains(T object, T[] array) {
		return indexOf(object, array) != -1;
	}
	
	/**
	 * Returns the specified Object from the Array, if the object is present in
	 * the array. If not, the first object present in the array will be
	 * returned, if the size of the array is greater than 1, if not, the
	 * specified Object will be returned.
	 * 
	 * @param object
	 *            - The value trying to be set
	 * @param array
	 *            - The array that may/may not contain the value
	 * @return The value found from the array
	 */
	public static <T> T valueFrom(T object, T[] array) {
		int index = indexOf(object, array);
		if (index != -1) {
			return array[index];
		} else {
			if (array.length > 0) {
				return array[0];
			} else {
				return object;
			}
		}
	}
	
	/**
	 * Clamps the Number type
	 * 
	 * @param value
	 *            - Value being clamped
	 * @param min
	 *            - Minimum value possible
	 * @param max
	 *            - Maximum value possible
	 * @return The clamped value
	 */
	public static Number clamp_number(Number value, Number min, Number max) {
		value = Math.max(min.doubleValue(), value.doubleValue());
		value = Math.min(max.doubleValue(), value.doubleValue());
		return value;
	}
	
	/**
	 * Snaps Value to Increment
	 * 
	 * @param value
	 *            - Value being snapped to increment
	 * @param increment
	 *            - Incrementation for Value
	 * @return The new value
	 */
	public static Number toIncrement(Number value, Number increment) {
		return Math.round(value.doubleValue() * (1.0 / increment.doubleValue())) / (1.0 / increment.doubleValue());
	}
}
