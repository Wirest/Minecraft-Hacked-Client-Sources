// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.network.play.server.S02PacketChat;
import org.lwjgl.input.Keyboard;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.utils.ColorUtil;
import net.minecraft.client.Minecraft;

public class Module
{
    protected String name;
    private int bind;
    private Category category;
    private boolean isEnabled;
    public int color;
    protected String extraInfo;
    private boolean registeredEvents;
    public int defaultBind;
    private boolean savable;
    protected static Minecraft mc;
    
    static {
        Module.mc = Minecraft.getMinecraft();
    }
    
    public Module(final String name, final int bind, final Category category) {
        this.registeredEvents = true;
        this.name = name;
        this.bind = bind;
        this.defaultBind = bind;
        this.category = category;
        this.extraInfo = null;
        this.color = ColorUtil.generateRandomColorHex();
        this.savable = true;
        this.setup();
    }
    
    public Module(final String name, final int bind, final Category category, final boolean savable) {
        this.registeredEvents = true;
        this.name = name;
        this.bind = bind;
        this.category = category;
        this.extraInfo = null;
        this.color = ColorUtil.generateRandomColorHex();
        this.savable = savable;
        this.setup();
    }
    
    public Module(final String name, final Category category) {
        this.registeredEvents = true;
        this.name = name;
        this.bind = 0;
        this.category = category;
        this.extraInfo = null;
        this.color = ColorUtil.generateRandomColorHex();
        this.savable = true;
        this.setup();
    }
    
    public Module(final String name, final int bind, final Category category, final String exString) {
        this.registeredEvents = true;
        this.name = name;
        this.bind = bind;
        this.category = category;
        this.extraInfo = exString;
        this.color = ColorUtil.generateRandomColorHex();
        this.savable = true;
        this.setup();
    }
    
    public Module(final String name, final Category category, final String exString) {
        this.registeredEvents = true;
        this.name = name;
        this.bind = 0;
        this.category = category;
        this.extraInfo = exString;
        this.color = ColorUtil.generateRandomColorHex();
        this.savable = true;
        this.setup();
    }
    
    public String getExtraInfo() {
        if (this.extraInfo != null) {
            return String.valueOf(this.extraInfo) + " ";
        }
        return "";
    }
    
    public void setExtraInfo(final String extraInfo) {
        this.extraInfo = extraInfo;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setDisplayName(final String name) {
        this.name = name;
    }
    
    public int getBind() {
        return this.bind;
    }
    
    public void setBind(final int bind) {
        this.bind = bind;
        try {
            FusionX.theClient.fileManager.saveKeybinds();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setBind(final String bind) {
        this.bind = Keyboard.getKeyIndex(bind.toUpperCase());
        try {
            FusionX.theClient.fileManager.saveKeybinds();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setBind(final String bind, final boolean loading) {
        this.bind = Keyboard.getKeyIndex(bind.toUpperCase());
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public boolean getState() {
        return this.isEnabled;
    }
    
    public boolean onSendChatMessage(final String s) {
        return true;
    }
    
    public boolean onRecieveChatMessage(final S02PacketChat packet) {
        return true;
    }
    
    public void setState(final boolean state) {
        this.onToggle();
        if (state) {
            this.onEnable();
            this.isEnabled = true;
        }
        else {
            this.onDisable();
            this.isEnabled = false;
        }
        try {
            FusionX.theClient.fileManager.saveMods();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setState(final boolean state, final boolean loading) {
        this.isEnabled = state;
        this.onToggle();
        if (this.isEnabled) {
            this.registeredEvents = false;
        }
    }
    
    public void toggleModule() {
        this.setState(!this.getState());
    }
    
    public void onToggle() {
    }
    
    public void onEnable() {
        this.color = ColorUtil.generateRandomColorHex();
        EventManager.register(this);
    }
    
    public void onDisable() {
        EventManager.unregister(this);
    }
    
    public void onUpdate() {
        if (!this.registeredEvents && this.getState()) {
            this.onEnable();
            this.registeredEvents = true;
        }
    }
    
    public void onRender() {
    }
    
    public boolean isCategory(final Category i) {
        return i == this.category;
    }
    
    public boolean isSavable() {
        return this.savable;
    }
    
    public void setup() {
    }
}
