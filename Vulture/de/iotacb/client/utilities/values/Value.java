package de.iotacb.client.utilities.values;

import java.util.Arrays;
import java.util.List;

public class Value {
	
	private final String valueName;
	
	private final ValueMinMax minMax;
	
	private double doubleValue;
	private final List<String> comboValue;
	private String currentComboValue;
	private boolean booleanValue;
	
	private boolean enabled;
	
	private ValueType valueType;
	
	public Value(String name, double value, ValueMinMax minMax) {
		this.valueName = name;
		this.doubleValue = value;
		this.valueType = ValueType.NUMBER;
		this.minMax = minMax;
		this.comboValue = null;
		this.enabled = true;
	}
	
	public Value(String name, String... value) {
		this.valueName = name;
		this.currentComboValue = value[0];
		this.comboValue = Arrays.asList(value);
		this.valueType = ValueType.COMBO;
		this.minMax = null;
		this.enabled = true;
	}
	
	public Value(String name, boolean value) {
		this.valueName = name;
		this.booleanValue = value;
		this.valueType = ValueType.BOOL;
		this.minMax = null;
		this.comboValue = null;
		this.enabled = true;
	}
	
	public final ValueType getValueType() {
		return valueType;
	}
	
	public final String getValueName() {
		return valueName;
	}
	
	public final ValueMinMax getMinMax() {
		return minMax;
	}
	
	public final Object getValue() {
		Object value = null;
		if (valueType == ValueType.NUMBER) {
			value = (float)doubleValue;
		} else if (valueType == ValueType.COMBO) {
			value = currentComboValue;
		} else if (valueType == ValueType.BOOL) {
			value = booleanValue;
		}
		return value;
	}
	
	public final double getNumberValue() {
		return doubleValue;
	}
	
	public final List<String> getComboValues() {
		return comboValue;
	}
	
	public final String getComboValue() {
		return currentComboValue;
	}
	
	public final boolean getBooleanValue() {
		return booleanValue;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public final void setNumberValue(double doubleValue) {
		this.doubleValue = doubleValue;
	}
	
	public final void setComboValue(String comboValue) {
		if (this.comboValue.contains(comboValue)) {
			this.currentComboValue = comboValue;
		}
	}
	
	public final void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void setEnabled(Value value, String...values) {
		boolean enabled = false;
		for (String v : values) {
			if (v.equalsIgnoreCase(value.getComboValue())) {
				enabled = true;
			}
		}
		setEnabled(enabled);
	}
	
	public final boolean isCombo(String value) {
		return this.currentComboValue.equalsIgnoreCase(value);
	}
	
	public final boolean isCombo(String...values) {
		for (String value : values) {
			if (this.currentComboValue.equalsIgnoreCase(value)) {
				return true;
			}
		}
		return false;
	}
}