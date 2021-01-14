package cedo.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfigSetting {

    @Expose
    @SerializedName("name")
    public String name;

    @Expose
    @SerializedName("value")
    public Object value;

    public ConfigSetting(String name, Object value) {
        this.name = name;
        this.value = value;
    }

}
