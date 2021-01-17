package me.rigamortis.faurax.module;

import net.minecraft.client.*;
import com.darkmagician6.eventapi.*;
import org.lwjgl.input.*;

public class Module
{
    private String name;
    private int key;
    private ModuleType type;
    public static Minecraft mc;
    private boolean isToggled;
    private int color;
    private String modInfo;
    private boolean isVisible;
    private long currentMS;
    protected long lastMS;
    
    static {
        Module.mc = Minecraft.getMinecraft();
    }
    
    public Module() {
        this.currentMS = 0L;
        this.lastMS = -1L;
    }
    
    public void onEnabled() {
        EventManager.register(this);
    }
    
    public void onDisabled() {
        EventManager.unregister(this);
    }
    
    public void onToggled() {
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public int getKey() {
        return this.key;
    }
    
    public void setKey(final String newKey) {
        this.key = getIntFromKey(newKey);
    }
    
    public static String getKeyFromInt(final int i) {
        return Keyboard.getKeyName(i);
    }
    
    public static int getIntFromKey(final String i) {
        return Keyboard.getKeyIndex(i);
    }
    
    public ModuleType getType() {
        return this.type;
    }
    
    public void setType(final ModuleType type) {
        this.type = type;
    }
    
    public boolean isToggled() {
        return this.isToggled;
    }
    
    public void toggle() {
        this.setToggled(!this.isToggled());
        this.onToggled();
    }
    
    public void setToggled(final boolean isToggled) {
        this.isToggled = isToggled;
        if (this.isToggled()) {
            this.onEnabled();
        }
        else {
            this.onDisabled();
        }
    }
    
    public final void updateMS() {
        this.currentMS = System.currentTimeMillis();
    }
    
    public final void updateLastMS() {
        this.lastMS = System.currentTimeMillis();
    }
    
    public final boolean hasTimePassedM(final long MS) {
        return this.currentMS >= this.lastMS + MS;
    }
    
    public final boolean hasTimePassedS(final float speed) {
        return this.currentMS >= this.lastMS + (long)(1000.0f / speed);
    }
    
    public int getColor() {
        return this.color;
    }
    
    public void setColor(final int color) {
        this.color = color;
    }
    
    public String getModInfo() {
        return this.modInfo;
    }
    
    public void setModInfo(final String modInfo) {
        this.modInfo = modInfo;
    }
    
    public boolean isVisible() {
        return this.isVisible;
    }
    
    public void setVisible(final boolean isVisible) {
        this.isVisible = isVisible;
    }
}
