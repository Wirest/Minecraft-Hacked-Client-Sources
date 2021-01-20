package cedo.modules;

import cedo.config.ConfigSetting;
import cedo.events.Event;
import cedo.settings.Setting;
import cedo.settings.impl.KeybindSetting;
import cedo.ui.notifications.Notification;
import cedo.ui.notifications.NotificationType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class Module {
    public static Minecraft mc = Minecraft.getMinecraft();
    protected static int categoryCount;
    public String suffix;
    public Category category;
    public boolean expanded;
    public int index;
    public boolean disableOnLagback;
    public List<Setting> settings = new ArrayList<>();
    public boolean requiresDisabler;
    @Expose
    @SerializedName("keyCode")
    public int keyCode = -1;
    @Expose
    @SerializedName("settings")
    public ConfigSetting[] cfgSettings;
    @Expose
    @SerializedName("name")
    protected String name;
    @Expose
    @SerializedName("toggled")
    protected boolean toggled;
    String description;
    private KeybindSetting keyBind = new KeybindSetting("Keybind", 0);

    public Module(String name, int key, Category c) {
        this(name, "", key, c);
    }

    public Module(String name, String description, int key, Category c) {
        this.setName(name);
        this.description = description;
        this.getKeyBind().setCode(key);
        this.setCategory(c);
        this.addSetting(getKeyBind());
    }

    public void addSetting(Setting setting) {
        getSettings().add(setting);
    }

    public void addSettings(Setting... settings) {
        for (Setting setting : settings) {
            addSetting(setting);
        }
    }

    public boolean isEnabled() {
        return isToggled();
    }

    public int getKey() {
        return getKeyBind().getCode();
    }

    @SuppressWarnings("rawtypes")
    public void onEvent(Event e) {
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getDisplayName() {
        return suffix == null || suffix.isEmpty() ? name : name + " \2477" + suffix;
    }

    public void toggle() {
        setToggled(!isToggled());
        if (isToggled()) {
            Notification.post(NotificationType.SUCCESS, "Toggle", this.getName() + " was " + "\u00A7aenabled\r");
            onEnable();
        } else {
            Notification.post(NotificationType.DISABLE, "Toggle", this.getName() + " was " + "\u00A7cdisabled\r");
            onDisable();
        }
    }

    public void toggle(Notification notification) {
        setToggled(!isToggled());
        if (isToggled()) {
            onEnable();
        } else {
            onDisable();
        }
        notification.post();
    }

    public void toggleSilent() {
        setToggled(!isToggled());
        if (isToggled()) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public void setSettings(List<Setting> settings) {
        this.settings = settings;
    }

    public KeybindSetting getKeyBind() {
        return keyBind;
    }

    public void setKeyBind(KeybindSetting keyBind) {
        this.keyBind = keyBind;
    }


    public enum Category {
        COMBAT("Combat"),
        MOVEMENT("Movement"),
        RENDER("Render"),
        PLAYER("Player"),
        CUSTOMIZE("Customize"),
        EXPLOIT("Exploit");

        public String name;
        public int moduleIndex;
        public int posX, posY;
        public boolean expanded;

        Category(String name) {
            this.name = name;
            posX = 150 + (categoryCount * 95);
            posY = 85; //Should be 85.5
            expanded = true;
            categoryCount++;
        }
    }
}