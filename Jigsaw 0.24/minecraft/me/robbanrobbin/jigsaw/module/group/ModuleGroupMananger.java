package me.robbanrobbin.jigsaw.module.group;

import java.util.ArrayList;

import me.robbanrobbin.jigsaw.module.Module;

public class ModuleGroupMananger {

	ArrayList<ModuleGroup> groups;

	public ModuleGroupMananger() {
		groups = new ArrayList<ModuleGroup>();
	}

	public void addGroup(ModuleGroup moduleGroup) {
		groups.add(moduleGroup);
	}

	public ModuleGroup getModuleGroupForModule(Module module) {
		for (ModuleGroup moduleGroup : groups) {
			for (Module m : moduleGroup.modules) {
				if (m.equals(module)) {
					return moduleGroup;
				}
			}
		}
		return null;
	}

	public void disableGroupModsForModule(Module module) {
		ModuleGroup group = getModuleGroupForModule(module);
		if (group == null) {
			return;
		}
		for (Module m : group.modules) {
			if (!m.equals(module)) {
				if (m.isToggled()) {
					m.setToggled(false, true);
				}
			}
		}
	}

	public ModuleGroup getModuleGroupByName(String name) {
		for (ModuleGroup mg : groups) {
			if (mg.name.equalsIgnoreCase(name)) {
				return mg;
			}
		}
		return null;
	}

	public ArrayList<ModuleGroup> getModuleGroups() {
		return groups;
	}

}
