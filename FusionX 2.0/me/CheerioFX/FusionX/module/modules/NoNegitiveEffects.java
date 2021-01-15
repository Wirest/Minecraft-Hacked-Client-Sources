// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class NoNegitiveEffects extends Module
{
    int[] negitiveEffectsList;
    
    public NoNegitiveEffects() {
        super("NoNegitiveEffects", 0, Category.PLAYER);
        this.negitiveEffectsList = new int[] { 2, 4, 7, 9, 15, 17, 18, 19, 20 };
    }
    
    @Override
    public void onUpdate() {
        if (this.getState() && !NoNegitiveEffects.mc.thePlayer.getActivePotionEffects().isEmpty()) {
            for (int c = 0; c < this.negitiveEffectsList.length; ++c) {
                NoNegitiveEffects.mc.thePlayer.removePotionEffect(this.negitiveEffectsList[c]);
            }
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
}
