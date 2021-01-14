package cedo.settings.impl;

import cedo.settings.Setting;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BooleanSetting extends Setting {

    @Expose
    @SerializedName("value")
    private boolean enabled;

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

}
