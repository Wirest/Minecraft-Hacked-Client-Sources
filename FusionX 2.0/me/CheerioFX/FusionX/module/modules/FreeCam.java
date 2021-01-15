// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.util.AxisAlignedBB;
import me.CheerioFX.FusionX.events.EventBoundingBox;
import me.CheerioFX.FusionX.events.EventPacketSent;
import com.darkmagician6.eventapi.EventTarget;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.module.Category;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import me.CheerioFX.FusionX.module.Module;

public class FreeCam extends Module
{
    double x;
    double y;
    double z;
    private EntityOtherPlayerMP prayerCopy;
    private double startX;
    private double startY;
    private double startZ;
    private float startYaw;
    private float startPitch;
    public static boolean enabled;
    
    static {
        FreeCam.enabled = false;
    }
    
    public FreeCam() {
        super("FreeCam", 0, Category.PLAYER);
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        Wrapper.mc.thePlayer.setPosition(this.x, this.y, this.z);
        Wrapper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + 0.01, Wrapper.getPlayer().posZ, Wrapper.getPlayer().onGround));
        Wrapper.mc.thePlayer.capabilities.isFlying = false;
        Wrapper.mc.thePlayer.noClip = false;
        FreeCam.enabled = false;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        Wrapper.mc.thePlayer.noClip = true;
        this.x = Wrapper.mc.thePlayer.posX;
        this.y = Wrapper.mc.thePlayer.posY;
        this.z = Wrapper.mc.thePlayer.posZ;
        FreeCam.enabled = true;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotionUpdates e) {
        Wrapper.mc.thePlayer.capabilities.isFlying = true;
        Wrapper.mc.thePlayer.noClip = true;
        Wrapper.mc.thePlayer.capabilities.setFlySpeed(0.1f);
        e.setCancelled(true);
    }
    
    @EventTarget
    public void onPacketSend(final EventPacketSent e) {
        if (e.getPacket() instanceof C03PacketPlayer) {
            e.setCancelled(true);
        }
    }
    
    @EventTarget
    public void onBB(final EventBoundingBox e) {
        e.setBoundingBox(null);
    }
}
