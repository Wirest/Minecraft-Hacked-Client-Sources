package me.memewaredevs.client.module.movement;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.Memeware;
import me.memewaredevs.client.event.events.ClimbSlabEvent;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.function.Consumer;

public class Step extends Module {

    private int timerSlow;
    private float height;

    public Step() {
        super("Step", 0, Module.Category.MOVEMENT);
        this.addModes("Vanilla", "NCP");
        this.addDouble("Height", 1, 1, 10);
    }

    @Override
    public void onDisable() {
        mc.thePlayer.stepHeight = 0.6F;
    }

    @Handler
    public Consumer<UpdateEvent> eventConsumer = (event) -> {
        height = this.getDouble("Height").floatValue();

        if (isMode("Vanilla")) {
            mc.thePlayer.stepHeight = height;
        } else {
            mc.thePlayer.stepHeight = 0.6F;
        }
    };

    @Handler
    public Consumer<ClimbSlabEvent> eventConsumer0 = (event) -> {
        if (isMode("NCP")) {
            double posX = mc.thePlayer.posX;
            double posY = mc.thePlayer.posY;
            double posZ = mc.thePlayer.posZ;
            if (event.isPre() && !Memeware.INSTANCE.getModuleManager().getModule("Speed").isToggled()) {
                event.setStepHeight(1.0F);
            } else {
                double height = mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY;
                if (height > 0.6F && height <= 1.0F) {
                    float[] values = { 0.42F, 0.75F };
                    for (float value : values)
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + value, posZ, false));
                    mc.timer.timerSpeed = 0.4F;
                }
                timerSlow = 2;
            }
        }
    };

    @Handler
    public Consumer<UpdateEvent> eventConsumer1 = (event) -> {
        if (isMode("NCP")) {
            if (timerSlow <= 0 && timerSlow >= -2) {
                mc.timer.timerSpeed = 1.0F;
            }
            timerSlow--;
        }
    };

}
