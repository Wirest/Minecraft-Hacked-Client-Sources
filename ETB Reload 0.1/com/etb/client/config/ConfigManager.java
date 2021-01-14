package com.etb.client.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.commons.io.FileUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.etb.client.Client;

public class ConfigManager {
    private File dir;
    private ArrayList<Config> configs = new ArrayList<>();
    public Config currentConfig;

    public ConfigManager(File dir) {
        this.dir = dir;
    }

    public ArrayList<Config> getConfigs() {
        return configs;
    }

    public void clear() throws IOException {
        FileUtils.cleanDirectory(new File(this.dir + "/"));
    }

    public void setCurrentConfig(Config config) {
        this.currentConfig = config;
    }

    public void load() {
        if (!this.dir.exists()) {
            this.dir.mkdirs();
        }
        File[] directories = dir.listFiles(File::isDirectory);
        if (this.dir != null && dir.listFiles() != null) {
            for (File dirs : directories) {
                Config config = new Config(dirs.getName().replace(" ",""));
                setCurrentConfig(config);
                this.configs.add(config);
            }
        }
    }
    public void deleteConfig(String cfgname) {
        File direc = new File(this.dir + "/" + cfgname + "/");
        File[] files = direc.listFiles();
        for (File file : files) {
            file.delete();
        }
        if (direc.exists()) {
            direc.delete();
        }
        Client.INSTANCE.getConfigManager().getConfigs().remove(getConfig(cfgname));
    }
    public void saveConfig(String cfgname) {
        File direc = new File(this.dir + "/" + cfgname + "/");
        File[] files = direc.listFiles();
        if (!direc.exists()) {
            direc.mkdir();
        } else if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        Client.INSTANCE.getModuleManager().getModuleMap().values().forEach(module -> {
            File file = new File(direc, module.getLabel() + ".json");
            JsonObject node = new JsonObject();
            module.save(node);
            if (node.entrySet().isEmpty()) {
                return;
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                return;
            }
            try (Writer writer = new FileWriter(file)) {
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(node));
            } catch (IOException e) {
                file.delete();
            }
        });
        files = direc.listFiles();
        if (files == null || files.length == 0) {
            direc.delete();
        }
    }

    public void loadModules(String cfgname) {
        File direc = new File(this.dir + "/" + cfgname + "/");
        Client.INSTANCE.getModuleManager().getModuleMap().values().forEach(module -> {
            final File file = new File(direc, module.getLabel() + ".json");
            if (!file.exists()) {
                return;
            }
            try (Reader reader = new FileReader(file)) {
                JsonElement node = new JsonParser().parse(reader);
                if (!node.isJsonObject()) {
                    return;
                }
                module.load(node.getAsJsonObject());
            } catch (IOException e) {
            }
        });
    }

    public Optional<Config> getPresetByName(String name) {
        return this.configs.stream().filter(p -> p.getName().equals(name)).findFirst();
    }
    public Config getConfig(String name) {
        for (Config cfg : configs) {
            if (cfg.getName().equalsIgnoreCase(name)) {
                return cfg;
            }
        }
        return null;
    }
    public boolean isConfig(String name) {
        return getConfig(name) != null;
    }
}
