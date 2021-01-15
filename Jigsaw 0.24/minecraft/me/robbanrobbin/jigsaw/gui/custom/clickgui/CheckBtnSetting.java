package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import java.lang.reflect.Field;

import me.robbanrobbin.jigsaw.client.settings.ClientSettings;

public class CheckBtnSetting extends ModSetting {

	private String name;
	private Boolean value;
	private Field valueField;
	
	public CheckBtnSetting(String name, String fieldName) {
		this.name = name;
		Field[] fields = ClientSettings.class.getFields();
		for(Field field : fields) {
			try {
				if(fieldName.equals(field.getName())) {
					this.valueField = field;
					System.out.println(valueField.getName() + " " + name);
					this.value = valueField.getBoolean(ClientSettings.class);
					return;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		System.out.println("NO VALUE FOR: " + name);
	}
	
	@Override
	public Component createComponent() {
		CheckBtnTask task = new CheckBtnTask() {
			@Override
			public void task(SettingCheckBtn checkBtn) {
				try {
					valueField.setBoolean(value, checkBtn.getValue());
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		};
		return new SettingCheckBtn(name, value, task, this);
	}
	
	public boolean getValue() {
		try {
			return valueField.getBoolean(ClientSettings.class);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Field getValueField() {
		return valueField;
	}

}
