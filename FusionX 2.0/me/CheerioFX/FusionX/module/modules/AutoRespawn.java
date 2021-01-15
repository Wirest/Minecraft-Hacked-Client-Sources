// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class AutoRespawn extends Module
{
    boolean server;
    
    public AutoRespawn() {
        super("AutoRespawn", 0, Category.PLAYER);
        this.server = true;
    }
    
    @Override
    public void onUpdate() {
        if (this.getState() && !AutoRespawn.mc.thePlayer.isEntityAlive()) {
            if (!this.server) {
                AutoRespawn.mc.thePlayer.respawnPlayer();
            }
            else {
                AutoRespawn.mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
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
