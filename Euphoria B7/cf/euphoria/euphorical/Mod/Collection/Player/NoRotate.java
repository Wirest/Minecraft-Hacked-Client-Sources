// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import cf.euphoria.euphorical.Events.EventPacketTake;
import com.darkmagician6.eventapi.EventManager;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.Mod;

public class NoRotate extends Mod
{
    public NoRotate() {
        super("NoRotate", Category.PLAYER);
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
    public void onPacketTake(final EventPacketTake event) {
        if (event.packet instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.packet;
            packet.yaw = this.mc.thePlayer.rotationYaw;
            packet.pitch = this.mc.thePlayer.rotationPitch;
        }
    }
}
