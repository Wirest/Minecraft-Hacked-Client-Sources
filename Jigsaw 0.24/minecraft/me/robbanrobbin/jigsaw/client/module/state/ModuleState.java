package me.robbanrobbin.jigsaw.client.module.state;

import org.json.simple.JSONObject;

import com.google.gson.JsonObject;

import me.robbanrobbin.jigsaw.module.Module;

public class ModuleState {

	private String mode;
	private boolean toggled;
	private int keyBind;

	public ModuleState(Module module) {
		this.mode = module.getCurrentMode();
		this.toggled = module.isToggled();
		this.keyBind = module.getKeyboardKey();
	}

	public ModuleState() {
	}

	public String getMode() {
		return mode;
	}

	public boolean getToggled() {
		return toggled;
	}

	public int getKeyBind() {
		return keyBind;
	}

	public void saveToJson(JsonObject json) {
		json.addProperty("keyBind", this.keyBind);
		json.addProperty("toggled", this.toggled);
		json.addProperty("mode", this.mode);
	}

	public static ModuleState createStateFromJson(int def, JSONObject json) {
		ModuleState state = new ModuleState();
		try {
			long key = ((Long) json.get("keyBind"));
			state.keyBind = (int) key;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("No keybind saved, continuing...");
			state.keyBind = def;
		}
		try {
			boolean toggled = (Boolean) json.get("toggled");
			state.toggled = toggled;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("No toggle saved, continuing...");
			state.toggled = false;
		}
		try {
			String mode = (String) json.get("mode");
			state.mode = mode;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("No mode saved, continuing...");
		}
		return state;
	}
}
