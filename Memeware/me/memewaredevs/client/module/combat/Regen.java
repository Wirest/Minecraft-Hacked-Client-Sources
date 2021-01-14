
package me.memewaredevs.client.module.combat;

import me.hippo.api.lwjeb.annotation.Handler;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;

import java.util.function.Consumer;

public class Regen extends Module {
    public Regen() {
        super("Regen", 0, Module.Category.COMBAT);
        this.addModes("ComuGamers", "Vanilla");
    }

    @Handler
    public Consumer<UpdateEvent> update = e -> {
        if (isMode("ComuGamers")) {
            if (this.mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth() && this.mc.thePlayer.isCollidedVertically) {
                for (int i = 0; i < 125; ++i) {
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                            this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-9, this.mc.thePlayer.posZ, true));
                }
            }
        } else {
            if (this.mc.thePlayer.getHealth() <= 17.0f && this.mc.thePlayer.isCollidedVertically) {
                for (int i = 0; i < 15; ++i) {
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                            this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
                }
            }
        }
    };

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }
}
