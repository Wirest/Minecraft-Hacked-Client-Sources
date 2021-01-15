package dev.astroclient.client.configuration.impl;

import com.google.gson.JsonObject;

import dev.astroclient.client.Client;
import dev.astroclient.client.configuration.IConfiguration;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.property.Property;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.property.impl.ColorProperty;
import dev.astroclient.client.property.impl.MultiSelectableProperty;
import dev.astroclient.client.property.impl.StringProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * made by Xen for Astro
 * at 12/8/2019
 **/
public class BasicConfiguration implements IConfiguration {
    private ToggleableFeature toggleableFeature;
    private String name;

    public BasicConfiguration(String name) {
        this.name = name;
    }

    public BasicConfiguration(ToggleableFeature feature) {
        this.toggleableFeature = feature;
    }

    @Override
    public void loadConfig(JsonObject jsonObject) {
        if(jsonObject != null) {
            ToggleableFeature toggleModule = getFeature();

            if (jsonObject.has("State")) {
                toggleModule.setState(jsonObject.get("State").getAsBoolean());
            }
            if(jsonObject.has("Hidden")) {
                toggleModule.setHidden(jsonObject.get("Hidden").getAsBoolean());
            }
            if(jsonObject.has("Bind")) {
                toggleModule.setBind(jsonObject.get("Bind").getAsInt());
            }

            if (jsonObject.has("Properties")) {
                jsonObject.get("Properties").getAsJsonObject().entrySet().forEach(entry -> {
                    Property property = Client.INSTANCE.propertyManager.getProperty(getFeature(), entry.getKey());

                    if (property != null) {

                        if (property instanceof ColorProperty) {
                            ((ColorProperty) property).setColor(new Color(entry.getValue().getAsInt()));
                        } else if(property instanceof BooleanProperty) {
                            ((BooleanProperty) property).setValue(entry.getValue().getAsBoolean());
                        } else if(property instanceof StringProperty) {
                            ((StringProperty) property).setValue(entry.getValue().getAsString());
                        } else if(property instanceof NumberProperty) {
                            ((NumberProperty) property).setValue(entry.getValue().getAsString());
                        } else if(property instanceof MultiSelectableProperty) {
                            ((MultiSelectableProperty) property).setObjects(new ArrayList<>(Arrays.asList(entry.getValue().getAsString())));
                        }
                    }
                });
            }
        }
    }

    @Override
    public JsonObject saveConfig() {
        JsonObject object = new JsonObject();
        ToggleableFeature toggleableFeature = getFeature();

        object.addProperty("State", toggleableFeature.getState());
        object.addProperty("Hidden", toggleableFeature.isHidden());
        object.addProperty("Bind", toggleableFeature.getBind());

        if (Client.INSTANCE.propertyManager.getPropertiesForFeature(toggleableFeature) != null) {
            JsonObject propertiesObject = new JsonObject();
            for (Property property : Client.INSTANCE.propertyManager.getPropertiesForFeature(toggleableFeature)) {
                if (property instanceof NumberProperty) {
                    propertiesObject.addProperty(property.getName(), ((NumberProperty) property).getValue());
                } else if (property instanceof BooleanProperty) {
                    propertiesObject.addProperty(property.getName(), ((BooleanProperty) property).getValue());
                } else if (property instanceof ColorProperty) {
                    propertiesObject.addProperty(property.getName(), ((ColorProperty) property).getColor().getRGB());
                } else if(property instanceof MultiSelectableProperty) {
                    propertiesObject.addProperty(property.getName(), ((MultiSelectableProperty) property).getSelectedObjects().toString()
                            .replaceAll("\\[","").replaceAll("\\]",""));
                } else if(property instanceof StringProperty) {
                    propertiesObject.addProperty(property.getName(), ((StringProperty) property).getValue());
                }
            }

            object.add("Properties", propertiesObject);
        }

        return object;
    }

    public String getName() {
        return name;
    }

    public ToggleableFeature getFeature() {
        return toggleableFeature;
    }
}
