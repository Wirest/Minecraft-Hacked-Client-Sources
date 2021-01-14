package me.memewaredevs.client.option;

import me.memewaredevs.client.module.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * No longer using streams (as of 5.6.0)
 */
public class OptionManager {

    private ArrayList<Option<?>> options = new ArrayList<Option<?>>();

    public List<Option<?>> getSettingsByMod(Module module) {
        ArrayList<Option<?>> options = new ArrayList<>();
        for (Option option : this.options) {
            if (option.getModule() == module) {
                options.add(option);
            }
        }
        return options;
    }

    public List<Option<?>> getSettingsByMod(Class<?> clazz) {
        ArrayList<Option<?>> options = new ArrayList<>();
        for (Option option : this.options) {
            if (option.getModule().getClass() == clazz) {
                options.add(option);
            }
        }
        return options;
    }

    public Option<?> getSettingByMod(Class<?> moduleClass, String optionName) {
        for (Option option : this.getSettingsByMod(moduleClass)) {
            if (option.getParentModuleMode() == null && option.getName().equalsIgnoreCase(optionName) && option.getModule().getClass() == moduleClass) {
                return option;
            }
        }
        return null;
    }

    public Option<?> getSettingByMod(Class<?> moduleClass, String moduleMode, String optionName) {
        for (Option option : this.getSettingsByMod(moduleClass)) {
            if (option.getParentModuleMode() != null && option.getParentModuleMode().equalsIgnoreCase(moduleMode) && option.getName().equalsIgnoreCase(optionName) && option.getModule().getClass() == moduleClass) {
                return option;
            }
        }
        return null;
    }

    public void addSetting(Option<?> option) {
        this.options.add(option);
    }

}
