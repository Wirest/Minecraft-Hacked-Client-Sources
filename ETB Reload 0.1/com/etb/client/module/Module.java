package com.etb.client.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.greenrobot.eventbus.EventBus;

import com.google.gson.JsonObject;
import com.etb.client.utils.value.Value;

import net.minecraft.client.Minecraft;

public class Module {
    private String label, renderlabel, suffix, description = "No Description Found!";
    private boolean enabled, hidden;
    private int color, keybind;
    private Category category;
    private List<Value> values = new ArrayList<>();
    public Minecraft mc = Minecraft.getMinecraft();
    public Module(String label, Category category, int color) {
        this.label = label;
        this.category = category;
        this.color = color;
    }

    protected void addValues(Value... value) {
        values.addAll(Arrays.asList(value));
    }

    public Value find(String term) {
        for (Value value : values) {
            if (value.getLabel().equalsIgnoreCase(term)) {
                return value;
            }
        }
        return null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Value> getValues() {
        return this.values;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getRenderlabel() {
        return renderlabel;
    }

    public void setRenderlabel(String renderlabel) {
        this.renderlabel = renderlabel;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            if (hasSubscribers()) {
                EventBus.getDefault().register(this);
            }
            onEnable();
        } else {
            if (hasSubscribers()) {
                EventBus.getDefault().unregister(this);
            }
            onDisable();
        }
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public int getKeybind() {
        return keybind;
    }

    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Category getCategory() {
        return category;
    }

    public void toggle() {
        setEnabled(!isEnabled());
    }

    public enum Category {
        COMBAT,RENDER,MOVEMENT,PLAYER,WORLD
    }

    public void save(JsonObject directory) {
        directory.addProperty("key", getKeybind());
        directory.addProperty("enabled", isEnabled());
        directory.addProperty("hidden", isHidden());
        values.forEach(val -> {
            directory.addProperty(val.getLabel(), val.getValue().toString());
        });
    }

    public void load(JsonObject directory) {
        directory.entrySet().forEach(data -> {
            switch (data.getKey()) {
                case "key":
                    setKeybind(data.getValue().getAsInt());
                    break;
                case "enabled":
                    if (!(isEnabled() && data.getValue().getAsBoolean()) && !(!isEnabled() && !data.getValue().getAsBoolean()))
                        setEnabled(data.getValue().getAsBoolean());
                    break;
                case "hidden":
                    setHidden(data.getValue().getAsBoolean());
                    break;
            }
            Value val = find(data.getKey());
            if (val != null) val.setValue(data.getValue().getAsString());
        });
    }

    public boolean hasSubscribers() {
        return true;
    }
}
