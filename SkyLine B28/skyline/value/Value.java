package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value;

import java.util.ArrayList;


public class Value<T> {

	private String name;
	private T defaultValue;
	public T value;
	private Object parent;
	private ArrayList<Value> values = new ArrayList<Value>();

	public Value(String name, T defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
		this.value = defaultValue;
		ValueManager.INSTANCE.register(this);
	}

	public T getValue() {
		return value;
	}

	public T setValue(T value) {
		this.value = value;
		return this.value;
	}

	public String getName() {
		return name;
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public Class getGenericClass() {
		return this.defaultValue.getClass();
	}

	public Object getParent() {
		return this.parent;
	}
	public ArrayList<Value> getValues(Object o) {
		ArrayList<Value> values = new ArrayList<Value>();
		for(Value v : values) {
			if(v.getParent() == o) {
				values.add(v);
			}
		}
		return values;
	}

}