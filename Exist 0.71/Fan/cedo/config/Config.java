package cedo.config;

import cedo.Fan;
import cedo.modules.Module;
import cedo.settings.Setting;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.KeybindSetting;
import cedo.settings.impl.ModeSetting;
import cedo.settings.impl.NumberSetting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Config {

    private final String name;
    private final File file;

    public Config(String name) {
        this.name = name;
        this.file = new File(Minecraft.getMinecraft().mcDataDir + "/Fan/configs/" + name + ".json");
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public String serialize() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        for (Module module : Fan.modules) {
            List<ConfigSetting> settings = new ArrayList<>();
            for (Setting setting : module.settings) {
                if (setting instanceof KeybindSetting)
                    continue;

                ConfigSetting cfgSetting = new ConfigSetting(null, null);
                cfgSetting.name = setting.name;
                if (setting instanceof BooleanSetting) {
                    cfgSetting.value = ((BooleanSetting) setting).isEnabled();
                }
                if (setting instanceof ModeSetting) {
                    cfgSetting.value = ((ModeSetting) setting).getSelected();
                }
                if (setting instanceof NumberSetting) {
                    cfgSetting.value = ((NumberSetting) setting).getValue();
                }

                settings.add(cfgSetting);
            }
            module.cfgSettings = settings.toArray(new ConfigSetting[0]);
        }
        return gson.toJson(Fan.modules);
        
        /*JsonArray array = new JsonArray();
        for (Module module : Fan.modules) {
            JsonObject object = new JsonObject();
            JsonObject settingsObject = new JsonObject();
            object.addProperty("Name", module.getName());
            object.addProperty("Keybind", module.getKeyBind().getCode());
            object.addProperty("State", module.isToggled());
            for (Setting setting : module.getSettings()) {
                if (setting instanceof NumberSetting) {
                    NumberSetting s = (NumberSetting) setting;
                    settingsObject.addProperty(setting.name, s.getValue());
                }
                if (setting instanceof BooleanSetting) {
                    BooleanSetting s = (BooleanSetting) setting;
                    settingsObject.addProperty(setting.name, s.isEnabled());
                }
                if (setting instanceof ModeSetting) {
                    ModeSetting s = (ModeSetting) setting;
                    settingsObject.addProperty(setting.name, s.getSelected());
                }
            }
            object.add("Settings", settingsObject);
            array.add(object);
        }
        return array;*/
    }

}
