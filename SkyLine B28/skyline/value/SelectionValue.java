package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value;

import java.util.Arrays;

public class SelectionValue<T> extends Value<T> {

	private T[] selections;
	
	public SelectionValue(String name, T defaultValue, T[] selections) {
		super(name, defaultValue);
		this.selections = selections;
	}

	public T[] getSelections() {
		return selections;
	}

	public void addSelection(T selection){
        selections = Arrays.copyOf(selections, selections.length + 1);
        selections[selections.length - 1] = selection;
	}
	
}
