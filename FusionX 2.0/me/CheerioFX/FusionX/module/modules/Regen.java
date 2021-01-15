// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class Regen extends Module
{
    public static int packets;
    
    static {
        Regen.packets = 200;
    }
    
    public Regen() {
        super("Regen", 0, Category.PLAYER);
    }
    
    @Override
    public void onUpdate() {
        if (this.getState() && Wrapper.isMoving() && Regen.mc.thePlayer.getHealth() < Regen.mc.thePlayer.getMaxHealth() && Wrapper.mc.thePlayer.getFoodStats().getFoodLevel() > 1) {
            for (int i = 0; i < Regen.packets; ++i) {
                Regen.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
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
