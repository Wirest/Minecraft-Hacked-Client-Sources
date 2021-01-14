package cedo.config;

import cedo.Fan;
import cedo.modules.Module;
import cedo.settings.Setting;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.KeybindSetting;
import cedo.settings.impl.ModeSetting;
import cedo.settings.impl.NumberSetting;
import cedo.util.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager { //TODO ask user if visuals should be imported from config

    private static final List<Config> configs = new ArrayList<>();
    private final File file = new File(Minecraft.getMinecraft().mcDataDir, "/Fan/configs");
    public File config = new File(Minecraft.getMinecraft().mcDataDir + "/Fan/Config.json");
    String[] pathnames;

    public ConfigManager() {
        file.mkdirs();
    }

    public static Config getConfigByName(String name) {
        for (Config config : configs) {
            if (config.getName().equalsIgnoreCase(name)) return config;
        }
        return null;
    }

    public boolean load(String name) {
        Config config = new Config(name);
        try {
            String configString = new String(Files.readAllBytes(config.getFile().toPath()));
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            Module[] modules = gson.fromJson(configString, Module[].class);

            for (Module module : Fan.modules) {
                for (Module configModule : modules) {
                    if (module.getName().equals(configModule.getName())) {
                        try {
                            if (configModule.isToggled() && !module.isToggled())
                                module.toggle();
                            else if (!configModule.isToggled() && module.isToggled())
                                module.setToggled(false);

                            module.getKeyBind().setCode(configModule.keyCode);

                            for (Setting setting : module.settings) {
                                for (ConfigSetting cfgSetting : configModule.cfgSettings) {
                                    if (setting.name.equals(cfgSetting.name)) {
                                        if (setting instanceof BooleanSetting) {
                                            ((BooleanSetting) setting).setEnabled((boolean) cfgSetting.value);
                                        }
                                        if (setting instanceof ModeSetting) {
                                            ((ModeSetting) setting).setSelected((String) cfgSetting.value);
                                        }
                                        if (setting instanceof NumberSetting) {
                                            ((NumberSetting) setting).setValue((double) cfgSetting.value);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean loadConfig() {
        try {
            String configString = new String(Files.readAllBytes(config.toPath()));
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            Module[] modules = gson.fromJson(configString, Module[].class);

            for (Module module : Fan.modules) {
                for (Module configModule : modules) {
                    if (module.getName().equals(configModule.getName())) {
                        try {
                            if (configModule.isToggled() && !module.isToggled())
                                module.toggle();
                            else if (!configModule.isToggled() && module.isToggled())
                                module.setToggled(false);

                            module.getKeyBind().setCode(configModule.keyCode);

                            for (Setting setting : module.settings) {
                                for (ConfigSetting cfgSetting : configModule.cfgSettings) {
                                    if (setting.name.equals(cfgSetting.name)) {
                                        if (setting instanceof BooleanSetting) {
                                            ((BooleanSetting) setting).setEnabled((boolean) cfgSetting.value);
                                        }
                                        if (setting instanceof ModeSetting) {
                                            ((ModeSetting) setting).setSelected((String) cfgSetting.value);
                                        }
                                        if (setting instanceof NumberSetting) {
                                            ((NumberSetting) setting).setValue((double) cfgSetting.value);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean save(String name) {
       /* Config config = getConfigByName(name);
        if (config == null) {
            System.out.println("Cannot find " + name);
            return false;
        }*/


        Config config = new Config(name);

        try {
            //FileWriter fw = new FileWriter(config.getFile());
            //fw.write(config.serialize());
            //fw.close();
            config.getFile().getParentFile().mkdirs();
            Files.write(config.getFile().toPath(), config.serialize().getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save " + config);
            return false;
        }
    }

    public void saveConfig() {
        try {
            config.getParentFile().mkdirs();
            Files.write(config.toPath(), serialize().getBytes(StandardCharsets.UTF_8));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save " + config);
        }
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
    }

    public boolean save(Config config) {
        return this.save(config);
    }

    public void saveAll() {
        configs.forEach(config -> save(config.getName()));
    }

    public void loadConfigs() {
        for (File file : file.listFiles()) {
            configs.add(new Config(file.getName().replace(".json", "")));
        }
    }

    public List<Config> getConfigs() {
        return configs;
    }

    public void list() {
        pathnames = file.list();
        for (String pathname : pathnames) {
            // Print the names of files and directories
            Logger.ingameInfo(pathname.substring(0, pathname.length() - 5));
        }
    }

    public void delete(String configName) {
        Config config = new Config(configName);
        try {
            Files.deleteIfExists(config.getFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
