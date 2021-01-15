// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Render;

import net.minecraft.client.Minecraft;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.Mod;

public class Gamma extends Mod
{
    private float oldVal;
    
    public Gamma() {
        super("Gamma", Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        this.oldVal = Minecraft.gameSettings.gammaSetting;
        Minecraft.gameSettings.gammaSetting = 10000.0f;
    }
    
    @Override
    public void onDisable() {
        Minecraft.gameSettings.gammaSetting = this.oldVal;
    }
}
