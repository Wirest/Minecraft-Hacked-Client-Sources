package cedo.settings.impl;

import cedo.settings.Setting;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BooleanSetting extends Setting {

    @Expose
    @SerializedName("value")
    private boolean enabled;
    private ArrayList<Setting> children = new ArrayList<>();

    public BooleanSetting(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggle() {
        setEnabled(!isEnabled());
    }

    public ArrayList<Setting> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Setting> children) {
        this.children = children;
    }

    public void addChild(Setting child) {
        children.add(child);
    }

    public void addChildren(Setting... children) {
        for (Setting child : children)
            addChild(child);
    }

}
