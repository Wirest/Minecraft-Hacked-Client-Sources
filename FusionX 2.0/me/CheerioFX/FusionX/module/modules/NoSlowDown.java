// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import me.CheerioFX.FusionX.events.EventPostMotionUpdates;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.Packet;
import me.CheerioFX.FusionX.utils.NetUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class NoSlowDown extends Module
{
    public static boolean enabled;
    
    static {
        NoSlowDown.enabled = false;
    }
    
    public NoSlowDown() {
        super("NoSlowDown", 0, Category.MOVEMENT);
    }
    
    @Override
    public void onEnable() {
        NoSlowDown.enabled = true;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        NoSlowDown.enabled = false;
        super.onDisable();
    }
    
    @EventTarget
    public void onPreMotionUpdates(final EventPreMotionUpdates event) {
        if (NoSlowDown.mc.thePlayer.isBlocking() && (NoSlowDown.mc.thePlayer.motionX != 0.0 || NoSlowDown.mc.thePlayer.motionZ != 0.0)) {
            NetUtils.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }
    
    @EventTarget
    public void onPostMotionUpdates(final EventPostMotionUpdates event) {
        if (NoSlowDown.mc.thePlayer.isBlocking() && (NoSlowDown.mc.thePlayer.motionX != 0.0 || NoSlowDown.mc.thePlayer.motionZ != 0.0)) {
            NetUtils.sendPacket(new C08PacketPlayerBlockPlacement(NoSlowDown.mc.thePlayer.inventory.getCurrentItem()));
        }
    }
}
