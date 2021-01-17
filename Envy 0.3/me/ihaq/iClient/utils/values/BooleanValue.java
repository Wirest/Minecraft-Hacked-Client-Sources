package me.ihaq.iClient.utils.values;


public class BooleanValue extends Value<Boolean> {
	
	/**
	 * Creates a new Value
	 * 
	 * @param object
	 *            - The Object that the Value belongs to
	 * @param name
	 *            - The name of the Values
	 * @param id
	 *            - The ID of the Value, used in data storage
	 */
	public BooleanValue(Object parent, String name, String id) {
		super(parent, name, id, false);
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
	 *            - The Value for the Object
	 */
	public BooleanValue(Object parent, String name, String id, Boolean value) {
		super(parent, name, id, value);
	}
	
	/**
	 * Toggles the Boolean Value
	 */
	public void toggle() {
		this.setValue(!this.getValue());
	}
}
