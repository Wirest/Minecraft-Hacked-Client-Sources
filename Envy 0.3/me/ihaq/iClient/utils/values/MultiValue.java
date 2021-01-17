package me.ihaq.iClient.utils.values;

public class MultiValue<T> extends Value<T> {
	
	/**
	 * Array of all possible String values
	 */
	private T[] values;
	
	/**
	 * Creates a new Value
	 * 
	 * @param object
	 *            - The Object that the Value belongs to
	 * @param name
	 *            - The name of the Values
	 * @param id
	 *            - The ID of the Value, used in data storage
	 * @param values
	 *            - All of the possible values the Object could posess
	 */
	public MultiValue(Object parent, String name, String id, T[] values) {
		this(parent, name, id, (values.length > 0 ? values[0] : null), values);
	}
	
	/**
	 * Creates a new Value
	 * 
	 * @param object
	 *            - The Object that the Value belongs to
	 * @param name
	 *            - The name of the Values
	 * @param id
	 *            - The ID of the Value, used in data storage
	 * @param value
	 *            - The Value for the Value Object
	 * @param values
	 *            - All of the possible values the Object could posess
	 */
	public MultiValue(Object parent, String name, String id, T value, T[] values) {
		super(parent, name, id, value);
		this.values = values;
		if (values.length == 0) {
			values = (T[]) new Object[] { value };
		}
	}
	
	public void next() {
		int index = ValueUtils.indexOf(value, values);
		index++;
		if (index >= values.length) {
			index = 0;
		}
		this.setValue(values[index]);
	}
	
	@Override
	public T setValue(T value) {
		value = ValueUtils.valueFrom(value, values);
		return super.setValue(value);
	}
	
	/**
	 * Returns all of the possible values
	 */
	public T[] getValues() {
		return this.values;
	}
}
