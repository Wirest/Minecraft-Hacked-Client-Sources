// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import me.CheerioFX.FusionX.utils.EntityUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.network.Packet;
import me.CheerioFX.FusionX.utils.NetUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.events.EventPostMotionUpdates;
import com.darkmagician6.eventapi.EventTarget;
import me.CheerioFX.FusionX.events.EventUpdate;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.utils.TimeHelper2;
import me.CheerioFX.FusionX.module.Module;

public class Phase extends Module
{
    private TimeHelper2 timer;
    public static boolean newPhase;
    double mx2;
    double mz2;
    double x;
    double z;
    
    static {
        Phase.newPhase = true;
    }
    
    public Phase() {
        super("Phase", 0, Category.MOVEMENT);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.mx2 = Math.cos(Math.toRadians(Phase.mc.thePlayer.rotationYaw + 90.0f));
        this.mz2 = Math.sin(Math.toRadians(Phase.mc.thePlayer.rotationYaw + 90.0f));
        this.x = Phase.mc.thePlayer.movementInput.moveForward * 0.3 * this.mx2 + Phase.mc.thePlayer.movementInput.moveStrafe * 0.3 * this.mz2;
        this.z = Phase.mc.thePlayer.movementInput.moveForward * 0.3 * this.mz2 - Phase.mc.thePlayer.movementInput.moveStrafe * 0.3 * this.mx2;
    }
    
    @EventTarget
    public void onEventPost(final EventPostMotionUpdates event) {
        if (Phase.mc.thePlayer.isCollidedHorizontally && !Phase.mc.thePlayer.isOnLadder() && !Wrapper.isInsideBlock()) {
            NetUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX + this.x, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ + this.z, true));
            if (Phase.newPhase) {
                for (int i = 0; i < 1; ++i) {
                    NetUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX + this.x, Double.MIN_VALUE, Phase.mc.thePlayer.posZ + this.z, true));
                }
                Phase.mc.thePlayer.setPosition(Phase.mc.thePlayer.posX + this.x, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ + this.z);
            }
            else if (!Phase.newPhase) {
                EntityUtils.blinkToPos(new double[] { Phase.mc.thePlayer.posX, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ }, new BlockPos(Phase.mc.thePlayer.posX + this.x, Double.MIN_VALUE, Phase.mc.thePlayer.posZ + this.z), 0.0, new double[] { 0.0, 0.0 });
            }
            this.timer.reset();
        }
    }
}
