package me.robbanrobbin.jigsaw.client.presets;

import java.util.ArrayList;
import java.util.Collection;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.module.Module;

public class ModulePreset {

	private ArrayList<Module> modules;
	private boolean enabled;
	public String name;

	public ModulePreset() {
		this.modules = new ArrayList<Module>();
	}

	public ModulePreset(ArrayList<Object> preset, String name) {
		this.modules = new ArrayList<Module>();
		for (Object m : preset) {
			this.modules.add((Module) m);
		}
		this.name = name;
	}

	public ModulePreset(Object[] preset, String name) {
		this.modules = new ArrayList<Module>();
		for (Object m : preset) {
			this.modules.add((Module) m);
		}
		this.name = name;
	}

	public ArrayList<Module> getModules() {
		return this.modules;
	}

	public void addModules(Collection<Module> modules) {
		this.modules.addAll(modules);
	}

	public void addModule(Module module) {
		this.modules.add(module);
	}

	public void removeModule(Module module) {
		this.modules.remove(module);
	}

	public boolean isToggled() {

		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (enabled) {
			this.onEnable();
		} else {
			this.onDisable();
		}
	}

	public void toggle() {
		this.enabled = !this.enabled;
		if (enabled) {
			this.onEnable();
		} else {
			this.onDisable();
		}

	}

	public void onEnable() {
		for (Module m : Jigsaw.getToggledModules()) {
			m.setToggled(false, true);
		}
		for (Module m : this.modules) {
			m.setToggled(true, true);
		}
		Jigsaw.getPresetMananger().onToggle(this);
	}

	public void onDisable() {
		for (Module m : this.modules) {
			m.setToggled(false, true);
		}
		Jigsaw.getPresetMananger().onToggle(this);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
