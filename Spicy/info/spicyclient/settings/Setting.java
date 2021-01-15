package info.spicyclient.settings;

public class Setting {
	
	public String name;
	
	public transient boolean focused = false;
	
	public Setting getSetting() {
		return this;
	}
}
