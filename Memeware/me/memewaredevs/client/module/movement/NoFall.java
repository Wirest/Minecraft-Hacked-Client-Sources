
package me.memewaredevs.client.module.movement;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.packet.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.function.Consumer;

public class NoFall extends Module {
    public NoFall() {
        super("No Fall", 0, Module.Category.MISC);
        this.addModes("Ground Spoof", "Hypixel");
    }
    @Handler
    public Consumer<UpdateEvent> eventConsumer0 = (e) -> {

        if (isMode("Hypixel")) {
            if (mc.thePlayer.fallDistance >= 2F && AntiFall.isBlockUnder()) {
                PacketUtil.sendPacketSilent(new C03PacketPlayer(true));
                mc.thePlayer.fallDistance = 0;
            }
        }
        if(isMode("Ground Spoof")) {
        	if (mc.thePlayer.fallDistance > 2.5F) {
        		e.setGround(true);
        	}
        }
        
    };
}
