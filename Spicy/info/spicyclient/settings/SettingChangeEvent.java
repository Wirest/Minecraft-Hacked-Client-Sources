package info.spicyclient.settings;

public class SettingChangeEvent {
	
	public SettingChangeEvent(type type, Setting setting) {
		
		this.settingType = type;
		this.setting = setting;
		
	}
	
	public type settingType = type.NONE;
	public Setting setting = null;
	
	public static enum type{
		NONE,
		MODE,
		NUMBER,
		BOOLEAN,
		KEYBIND;
	}
	
}
