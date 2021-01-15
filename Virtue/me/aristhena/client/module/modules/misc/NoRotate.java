// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.misc;

import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
import me.aristhena.event.EventTarget;
import me.aristhena.event.events.PacketReceiveEvent;
import me.aristhena.utils.ClientUtils;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@Mod(displayName = "No Rotate")
public class NoRotate extends Module
{
    @EventTarget
    private void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.getPacket();
            packet.field_148936_d = ClientUtils.yaw();
            packet.field_148937_e = ClientUtils.pitch();
        }
    }
}
