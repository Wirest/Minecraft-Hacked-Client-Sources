package VenusClient.online.Module;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import VenusClient.online.Client;
import VenusClient.online.Module.Category;
import VenusClient.online.Ui.notification.Notification;

import org.lwjgl.input.Keyboard;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;

public abstract class Module {

    public static Minecraft mc = Minecraft.getMinecraft();

    public String name;
    public String DisplayName;
    public Category category;
    public int KeyCode;

    private boolean enabled;
    private boolean isshowed;

    public Module(String name, String displayName, int key, Category category) {
        this(name, displayName, category, key);
    }

    public Module(String name, String displayName, Category category, int keyCode) {
        this.name = name;
        this.DisplayName = displayName;
        this.category = category;
        this.KeyCode = keyCode;
        setup();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getKeyCode() {
        return KeyCode;
    }

    public void setKeyCode(int keyCode) {
        KeyCode = keyCode;
    }

    public boolean isshowed() {
        return isshowed;
    }

    public void setshowed(boolean isshowed) {
        this.isshowed = isshowed;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggle() {
        //if (this.getClass().equals(Checker.class)) return;
        setEnabled(!isEnabled());
        if (enabled == true) {
            onEnable();
        }
        else
        {
            onDisable();
        }
    }


    public void setup() {}

    protected void onEnable()  {
        Client.instance.eventManager.register(this);
    }

    protected void onDisable() {
    	Client.instance.eventManager.unregister(this);
    }

    public void setState(boolean state) {
        setEnabled(state);
        if (state == true) {
            onEnable();
        }
        else
        {
            onDisable();
        }
    }
}
