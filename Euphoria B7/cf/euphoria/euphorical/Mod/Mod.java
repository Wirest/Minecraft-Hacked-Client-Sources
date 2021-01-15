// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod;

import net.minecraft.client.Minecraft;
import java.awt.Color;

public class Mod
{
    private String name;
    private String renderName;
    private String realName;
    private int bind;
    private Category cat;
    private Color color;
    private boolean isEnabled;
    protected Minecraft mc;
    
    public Mod(final String name, final Category cat) {
        this.mc = Minecraft.getMinecraft();
        this.name = name;
        this.setBind(0);
        this.cat = cat;
        this.setRenderName(this.getModName());
        this.setName(name);
    }
    
    public void setName(final String name) {
        this.realName = name;
    }
    
    public String getName() {
        return this.realName;
    }
    
    public int getBind() {
        return this.bind;
    }
    
    public void setBind(final int keyBind) {
        this.bind = keyBind;
    }
    
    public String getModName() {
        return this.name;
    }
    
    public Category getCategory() {
        return this.cat;
    }
    
    public Color getRandColor() {
        return this.color;
    }
    
    public String getRenderName() {
        return this.renderName;
    }
    
    public void setRenderName(final String renderName) {
        this.renderName = renderName;
    }
    
    public boolean isEnabled() {
        return this.isEnabled;
    }
    
    public void toggle() {
        this.setEnabled(!this.isEnabled);
    }
    
    public void onEnable() {
    }
    
    public void onDisable() {
    }
    
    public void onLeftClick() {
    }
    
    public void onRightClick() {
    }
    
    public void setEnabled(final boolean enabled) {
        this.color = new Color((float)(Math.random() * 1.0), (float)(Math.random() * 1.0), (float)(Math.random() * 1.0), 1.0f);
        if (enabled) {
            this.isEnabled = true;
            this.onEnable();
        }
        else {
            this.isEnabled = false;
            this.onDisable();
        }
    }
    
    public boolean isCategory(final Category category) {
        return this.cat == category;
    }
}
