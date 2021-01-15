// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.misc;

import me.aristhena.utils.ClientUtils;
import me.aristhena.event.Event;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.event.EventTarget;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import me.aristhena.event.events.PacketReceiveEvent;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(displayName = "World Time")
public class WorldTime extends Module
{
    @Option.Op(min = 0.0, max = 24000.0, increment = 250.0)
    private double time;
    
    public WorldTime() {
        this.time = 9000.0;
    }
    
    @EventTarget
    private void onPacketRecieve(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S03PacketTimeUpdate) {
            event.setCancelled(true);
        }
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (event.getState() == Event.State.POST) {
            ClientUtils.world().setWorldTime((long)this.time);
        }
    }
}
