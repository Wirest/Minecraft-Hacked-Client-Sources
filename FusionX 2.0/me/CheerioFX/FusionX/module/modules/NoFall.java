// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class NoFall extends Module
{
    public NoFall() {
        super("NoFall", 0, Category.PLAYER);
    }
    
    @Override
    public void onUpdate() {
        if (this.getState() && NoFall.mc.thePlayer.fallDistance > 2.0f && NoFall.mc.thePlayer.fallDistance < 20.0f) {
            NoFall.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
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
