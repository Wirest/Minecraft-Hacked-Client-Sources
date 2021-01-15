package dev.astroclient.client.configuration;

import com.google.gson.*;
import dev.astroclient.client.Client;
import dev.astroclient.client.configuration.impl.BasicConfiguration;
import dev.astroclient.client.feature.Feature;
import dev.astroclient.client.feature.ToggleableFeature;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;

import java.util.Set;

/**
 * made by Xen for Astro
 * at 12/8/2019
 **/
public class ConfigurationManager {
    private File dir;
    private final Set<IConfiguration> configurations = new HashSet<>();
    public ArrayList<IConfiguration> loadConfigs = new ArrayList<>();

    public ConfigurationManager(File dir) {
        this.dir = new File(dir, "/configs/");
    }

    public void register(IConfiguration configuration) {
        configurations.add(configuration);
    }

    public void load() {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (dir != null) {
            File[] files = dir.listFiles(f -> !f.isDirectory() && FilenameUtils.getExtension(f.getName()).equals("json"));
            for (File f : files) {
                BasicConfiguration config = new BasicConfiguration(FilenameUtils.removeExtension(f.getName()).replace(" ", ""));
                loadConfigs.add(config);
            }
        }
    }

    public void load(String name) {
        File file = new File(dir, name + ".json");

        for (Feature feature : Client.INSTANCE.featureManager.getFeatures()) {
            if (feature instanceof ToggleableFeature) {
                ToggleableFeature toggleableFeature = (ToggleableFeature) feature;
                if (Files.exists(file.toPath())) {
                    try (Reader reader = new FileReader(file.toPath().toFile())) {
                        JsonElement object = new JsonParser().parse(reader);
                        if (object.isJsonObject()) {
                            BasicConfiguration configuration = getConfiguration(toggleableFeature);
                            if (configuration != null) {
                                configuration.loadConfig(object.getAsJsonObject().getAsJsonObject(toggleableFeature.getLabel()));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void save(String name) {
        File file = new File(dir, name + ".json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
            }
        }

        JsonObject jsonObject = new JsonObject();

        for (Feature feature : Client.INSTANCE.featureManager.getFeatures()) {
            if (feature instanceof ToggleableFeature) {
                ToggleableFeature toggleableFeature = (ToggleableFeature) feature;
                BasicConfiguration configuration = getConfiguration(toggleableFeature);
                if (configuration != null)
                    jsonObject.add(toggleableFeature.getLabel(), configuration.saveConfig());
            }
        }

        try (Writer writer = new FileWriter(file)) {
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject));
        } catch (IOException e) {
            file.delete();
        }

    }

    public void delete(String name) {
        File file = new File(dir, name + ".json");
        if (file.exists()) {
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
            }
        }
        loadConfigs.remove(getConfig(name));
    }


    private BasicConfiguration getConfiguration(ToggleableFeature module) {
        for (IConfiguration configuration : configurations) {
            if (configuration instanceof BasicConfiguration) {
                BasicConfiguration basicConfiguration = (BasicConfiguration) configuration;
                if (basicConfiguration.getFeature() == module)
                    return basicConfiguration;
            }
        }

        return null;
    }

    public boolean isConfig(String name) {
        for (IConfiguration configuration : loadConfigs) {
            if (configuration instanceof BasicConfiguration) {
                if (((BasicConfiguration) configuration).getName().equalsIgnoreCase(name))
                    return true;
            }
        }

        return false;
    }

    public BasicConfiguration getConfig(String name) {
        for (IConfiguration configuration : loadConfigs) {
            if (configuration instanceof BasicConfiguration) {
                if (((BasicConfiguration) configuration).getName().equalsIgnoreCase(name))
                    return (BasicConfiguration) configuration;
            }
        }

        return null;
    }

    public Set<IConfiguration> getConfigurations() {
        return configurations;
    }

}
