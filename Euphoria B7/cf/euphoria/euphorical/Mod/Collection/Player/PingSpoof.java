// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.status.client.C01PacketPing;
import cf.euphoria.euphorical.Events.EventPacketSend;
import com.darkmagician6.eventapi.EventManager;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.Mod;

public class PingSpoof extends Mod
{
    public PingSpoof() {
        super("PingSpoof", Category.PLAYER);
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
    
    @EventTarget
    public void onPacketSend(final EventPacketSend event) {
        if (event.packet instanceof C01PacketPing) {
            final C01PacketPing packet = (C01PacketPing)event.packet;
            packet.clientTime = 0L;
            event.packet = packet;
        }
    }
}
