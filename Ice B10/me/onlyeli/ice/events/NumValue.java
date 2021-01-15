package me.onlyeli.ice.events;

import java.util.ArrayList;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;

public class NumValue {
	private double value;
	private double min;
	private double max;
	private String name;
	private ValueDisplay valueDisplay;
	private static ArrayList<NumValue> vals = new ArrayList<NumValue>();

	public static ArrayList<NumValue> getVals() {
		return NumValue.vals;
	}

	public NumValue(String name, double value, double min, double max, ValueDisplay valueDisplay) {
		this.setName(name);
		this.setValue(value);
		this.setMin(min);
		this.setMax(max);
		this.setValueDisplay(valueDisplay);
		getVals().add(this);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ValueDisplay getValueDisplay() {
		return this.valueDisplay;
	}

	public void setValueDisplay(ValueDisplay valueDisplay) {
		this.valueDisplay = valueDisplay;
	}

	public double getValue() {
		return this.value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getMin() {
		return this.min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return this.max;
	}

	public void setMax(double max) {
		this.max = max;
	}
}
