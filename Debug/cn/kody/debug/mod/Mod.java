package cn.kody.debug.mod;

import cn.kody.debug.Client;
import cn.kody.debug.mod.Category;
import cn.kody.debug.utils.file.FileUtils;
import cn.kody.debug.value.Value;
import com.darkmagician6.eventapi.EventManager;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;

public class Mod {
    protected Minecraft mc = Minecraft.getMinecraft();
    private Category category;
    private String name;
    private String rendername;
    private int key;
    public String displayName;
    private boolean isEnabled;
    public float hoverOpacity;
    public int valueSize;

    public Mod(String name, String renderName, Category category) {
        this.name = name;
        this.rendername = renderName;
        this.key = -1;
        this.category = category;
    }

    public Mod(String name, Category category) {
        this.name = name;
        this.rendername = name;
        this.key = -1;
        this.category = category;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return this.name;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getRenderName() {
        return this.rendername;
    }

    public Category getCategory() {
        return this.category;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onToggle() {
    }

    public void toggle() {
        try {
            if (this.isEnabled()) {
                this.set(false);
            } else {
                this.set(true);
            }
        }
        catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    public void set(boolean state) {
        this.set(state, false);
        Client.instance.fileMgr.saveMods();
    }

    public void set(boolean state, boolean safe) {
        this.isEnabled = state;
        this.onToggle();
        if (state) {
            if (this.mc.theWorld != null) {
                this.onEnable();
            }
            EventManager.register(this);
        } else {
            if (this.mc.theWorld != null) {
                this.onDisable();
            }
            EventManager.unregister(this);
        }
        if (safe) {
            Client.instance.fileMgr.saveMods();
        }
    }

    public boolean hasValues() {
        for (Value value : Value.list) {
            String name = value.getValueName().split("_")[0];
            if (!name.equalsIgnoreCase(this.getName())) continue;
            return true;
        }
        return false;
    }

    public void onFinalLoad() {
    }
}

