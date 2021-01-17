package me.ihaq.iClient.utils.values;

public class NumberValue extends Value<Number> {
	
	/**
	 * The Maximum Value for the Object
	 */
	private Number max;
	
	/**
	 * The Minimum Value for the Object
	 */
	private Number min;
	
	/**
	 * The Increment Value for the Object
	 */
	private Number increment;
	
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
	 *            - The Value for the Object
	 * @param min
	 *            - The Minimum Value for the Object
	 * @param max
	 *            - The Minimum Value for the Object
	 * @param increment
	 *            - The Incrementation of the Value
	 */
	public NumberValue(Object parent, String name, String id, Number value, Number min, Number max, Number increment) {
		super(parent, name, id, value);
		this.max = max;
		this.min = min;
		this.increment = increment;
	}
	
	@Override
	public Number setValue(Number value) {
		value = ValueUtils.clamp_number(value, min, max);
		value = ValueUtils.toIncrement(value, increment);
		return super.setValue(value);
	}
	
	/**
	 * Returns the Maximum value for the Object
	 */
	public Number getMax() {
		return this.max;
	}
	
	/**
	 * Returns the Maximum value for the Object
	 */
	public Number getMin() {
		return this.min;
	}
	
	/**
	 * Returns the Increment value for the Object
	 */
	public Number getIncrement() {
		return this.increment;
	}
}
