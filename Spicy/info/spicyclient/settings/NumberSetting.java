package info.spicyclient.settings;

import info.spicyclient.SpicyClient;
import info.spicyclient.settings.SettingChangeEvent.type;

public class NumberSetting extends Setting {
	
	public double value, minimum, maximum, increment;
	
	public boolean ClickGuiSelected = false;
	
	public NumberSetting(String name, double value, double minimum, double maximum, double increment) {
		
		this.name = name;
		this.value = value;
		this.minimum = minimum;
		this.maximum = maximum;
		this.increment = increment;
		
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		
		double precision = 1/increment;
		this.value = Math.round(Math.max(minimum,Math.min(maximum, value)) * precision) / precision;
		
		SettingChangeEvent settingNumber = new SettingChangeEvent(type.NUMBER, getSetting());
		SpicyClient.onSettingChange(settingNumber);
		
	}
	
	public void increment(Boolean positive) {
		
		setValue(getValue() + ((positive ? 1 : -1) * increment));
		
		SettingChangeEvent settingNumber = new SettingChangeEvent(type.NUMBER, getSetting());
		SpicyClient.onSettingChange(settingNumber);
		
	}
	
	public double getMinimum() {
		return minimum;
	}

	public void setMinimum(double minimum) {
		this.minimum = minimum;
	}

	public double getMaximum() {
		return maximum;
	}

	public void setMaximum(double maximum) {
		this.maximum = maximum;
	}

	public double getIncrement() {
		return increment;
	}

	public void setIncrement(double increment) {
		this.increment = increment;
	} 
	
}
