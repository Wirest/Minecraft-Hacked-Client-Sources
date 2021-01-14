package cedo.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Setting {

    @Expose
    @SerializedName("name")
    public String name;

    public boolean focused;

}
