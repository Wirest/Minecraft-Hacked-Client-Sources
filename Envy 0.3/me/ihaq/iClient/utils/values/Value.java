package me.ihaq.iClient.utils.values;

public class Value<T> {
	
	/**
	 * Object that the value belongs to
	 */
	private Object parent;
	
	/**
	 * Name of the Value
	 */
	private String name;
	
	/**
	 * ID of the Value, used in files
	 */
	private String id;
	
	/**
	 * The Value for the Object
	 */
	protected T value;
	
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
	 */
	public Value(Object parent, String name, String id, T value) {
		this.parent = parent;
		this.name = name;
		this.id = id;
		this.value = value;
		
		ValueManager.INSTANCE.register(this);
	}
	
	/**
	 * Returns the Object that the value belongs to
	 */
	public Object getParent() {
		return this.parent;
	}
	
	/**
	 * Returns the Name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns the ID
	 */
	public String getID() {
		return this.id;
	}
	
	/**
	 * Sets the Current Value
	 * 
	 * @param value
	 *            - The new Value for the Object
	 * @return The Set Value
	 */
	public T setValue(T value) {
		this.value = value;
		return this.value;
	}
	
	/**
	 * Returns the Current Value
	 */
	public T getValue() {
		return this.value;
	}
}
