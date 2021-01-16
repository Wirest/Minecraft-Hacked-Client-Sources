/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.combat;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPacketRecieve;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityVelocity;

@Module.Mod(category=Module.Category.COMBAT, description="Reduce your KB", key=0, name="Velocity")
public class Velocity
extends Module {
    public static Value percent = new Value("velocity_Percent", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), Float.valueOf(1.0f));

    @EventTarget
    public void onPacket(EventPacketRecieve packet) {
        this.setDisplayName("Velocity [" + Velocity.percent.value.longValue() + "%]");
        if (packet.getPacket() instanceof SPacketEntityVelocity) {
            SPacketEntityVelocity p = (SPacketEntityVelocity)packet.getPacket();
            if (Velocity.percent.value.floatValue() < 3.0f) {
                packet.setCancelled(true);
                Velocity.percent.value = Float.valueOf(0.0f);
            } else {
                SPacketEntityVelocity packets = (SPacketEntityVelocity)packet.getPacket();
                if (Wrapper.getWorld().getEntityByID(packets.getEntityID()) == Wrapper.getPlayer()) {
                    double mult = Velocity.percent.value.floatValue() / 100.0f;
                    if (mult == 0.0) {
                        packet.setCancelled(true);
                    } else {
                        SPacketEntityVelocity.motionX = (int)((double)packets.getMotionX() * mult);
                        SPacketEntityVelocity.motionY = (int)((double)packets.getMotionY() * mult);
                        SPacketEntityVelocity.motionZ = (int)((double)packets.getMotionZ() * mult);
                    }
                }
            }
        }
    }

    public void runCmd(String s) {
    }
}

