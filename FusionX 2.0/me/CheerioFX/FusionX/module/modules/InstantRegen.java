// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class InstantRegen extends Module
{
    public InstantRegen() {
        super("InstantRegen", 0, Category.COMBAT);
    }
    
    @Override
    public void onUpdate() {
        if (this.getState() && InstantRegen.mc.thePlayer.isPotionActive(Potion.regeneration) && InstantRegen.mc.thePlayer.getHealth() < InstantRegen.mc.thePlayer.getMaxHealth()) {
            for (int x = 0; x < 100 && InstantRegen.mc.thePlayer.getHealth() < InstantRegen.mc.thePlayer.getMaxHealth(); ++x) {
                InstantRegen.mc.getNetHandler().addToSendQueue(new C03PacketPlayer());
            }
        }
        super.onUpdate();
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
