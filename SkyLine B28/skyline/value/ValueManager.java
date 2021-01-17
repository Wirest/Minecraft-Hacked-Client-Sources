package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value;

import java.util.ArrayList;

public class ValueManager {

	private ArrayList<Value> values = new ArrayList<Value>();

	public static ValueManager INSTANCE = new ValueManager();
	
	public void register(Value<?> value) {
		values.add(value);
	}
	
	public ArrayList<Value> getValues(Object o) {
		ArrayList<Value> values = new ArrayList<Value>();
		for(Value v : this.values) {
			if(v.getParent() == o) {
				values.add(v);
			}
		}
		return values;
	}
	
}
