package cn.kody.debug.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;

import cn.kody.debug.events.EventPacket;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate extends Mod
{
    
    public NoRotate() {
        super("NoRotate", "No Rotate", Category.PLAYER);
    }
    
    @EventTarget
    public void onPacket(final EventPacket eventPacket) {
        if (eventPacket.packet instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook s08PacketPlayerPosLook = (S08PacketPlayerPosLook)eventPacket.packet;
            s08PacketPlayerPosLook.yaw = this.mc.thePlayer.rotationYaw;
            s08PacketPlayerPosLook.pitch = this.mc.thePlayer.rotationPitch;
        }
    }
}
