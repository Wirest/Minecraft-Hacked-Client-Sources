/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.player;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPacketRecieve;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

@Module.Mod(category=Module.Category.PLAYER, description="Don't let the server force you to turn", key=0, name="NoRotateSet")
public class NoForceTurn
extends Module {
    public static String lastmsg = "";

    @EventTarget
    public void onPacketRecieve(EventPacketRecieve event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook count = (SPacketPlayerPosLook)event.getPacket();
            if (Wrapper.getPlayer() != null && Wrapper.getPlayer().rotationYaw != -180.0f && Wrapper.getPlayer().rotationPitch != 0.0f) {
                count.yaw = Wrapper.getPlayer().rotationYaw;
                count.pitch = Wrapper.getPlayer().rotationPitch;
            }
        }
        boolean count1 = false;
    }
}

