package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import me.robbanrobbin.jigsaw.client.settings.ClientSettings;

public class SliderSetting<T extends Number> extends ModSetting {
	
	private String name;
	private double increment = 0.0;
	private T value;
	private double minValue;
	private double maxValue;
	private ValueFormat valueFormat;
	private Field valueField;
	
	public SliderSetting(String name, Integer value, double minValue, double maxValue, ValueFormat valueFormat) {
		this(name, (T) value, minValue, maxValue, 1.0, valueFormat);
	}
	
	public SliderSetting(String name, T value, double minValue, double maxValue, double increment, ValueFormat valueFormat) {
		this.name = name;
		this.value = value;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.increment = increment;
		this.valueFormat = valueFormat;
		Field[] fields = ClientSettings.class.getFields();
		for(Field field : fields) {
			try {
				if(value.hashCode() == (field.get(ClientSettings.class)).hashCode()) {
					this.valueField = field;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Component createComponent() {
		SliderTask task = new SliderTask() {
			@Override
			public void task(Slider slider) {
				if(value instanceof Integer) {
					try {
						valueField.setInt(value, slider.getValue().intValue());
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				if(value instanceof Double) {
					try {
						valueField.setDouble(value, slider.getValue().doubleValue());
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				if(value instanceof Float) {
					try {
						valueField.setFloat(value, slider.getValue().floatValue());
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		};
		return new Slider(name, value, minValue, maxValue, increment, valueFormat, task);
	}
	
}
