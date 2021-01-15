package me.robbanrobbin.jigsaw.client.presets;

import java.util.ArrayList;

public class PresetMananger {

	private ArrayList<ModulePreset> presets;

	public PresetMananger() {
		presets = new ArrayList<ModulePreset>();
	}

	/**
	 * 
	 * @param name
	 *            filter
	 * @return null if no module found
	 */
	public ModulePreset getPresetByName(String name) {
		for (ModulePreset mp : presets) {
			if (mp.getName() == name) {
				return mp;
			}
		}
		return null;
	}

	public ArrayList<ModulePreset> getPresets() {
		return this.presets;
	}

	public void onToggle(ModulePreset updated) {

	}

}
