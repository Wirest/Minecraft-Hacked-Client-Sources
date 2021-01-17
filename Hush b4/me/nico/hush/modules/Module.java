// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules;

import net.minecraft.client.Minecraft;

public class Module
{
    private String name;
    private String displayname;
    private Category category;
    private int color;
    private int keyBind;
    public boolean visible;
    public static boolean colormode;
    public static Minecraft mc;
    protected boolean toggled;
    
    static {
        Module.colormode = false;
        Module.mc = Minecraft.getMinecraft();
    }
    
    public Module(final String name, final String displayname, final int color, final int keyBind, final Category category) {
        this.name = name;
        this.displayname = name;
        this.category = category;
        this.keyBind = keyBind;
        this.color = color;
        this.setup();
        this.register();
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDisplayname() {
        return this.displayname;
    }
    
    public void setDisplayname(final String displayname) {
        this.displayname = displayname;
    }
    
    public int getColor() {
        return this.color;
    }
    
    public void setColor(final int color) {
        this.color = color;
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public void setCategory(final Category category) {
        this.category = category;
    }
    
    public boolean isCategory(final Category category) {
        return this.category == category;
    }
    
    public int getKeyBind() {
        return this.keyBind;
    }
    
    public void setKeyBind(final int keyBind) {
        this.keyBind = keyBind;
    }
    
    public boolean isEnabled() {
        return this.toggled;
    }
    
    public boolean isDisabled() {
        return this.toggled;
    }
    
    public void toggle() {
        if (this.toggled) {
            this.toggled = false;
            this.onDisable();
        }
        else {
            this.toggled = true;
            this.onEnable();
        }
    }
    
    public void setup() {
    }
    
    public void onEnable() {
    }
    
    public void onDisable() {
    }
    
    public void onRender() {
    }
    
    private void register() {
    }
}
