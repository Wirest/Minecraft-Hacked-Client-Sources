package cedo.modules.movement;

import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.NumberSetting;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

public class Phase extends Module {

    BooleanSetting holdDown = new BooleanSetting("Hold down", true);
    NumberSetting strength = new NumberSetting("Strength", 0.4, 0.2, 0.4, 0.01);

    public Phase() {
        super("Phase", Keyboard.KEY_NONE, Category.MOVEMENT);
        addSettings(holdDown, strength);
    }


    public void onDisable() {
        mc.thePlayer.stepHeight = 0.625f;
    }

    public void onEvent(Event e) {
        if (e instanceof EventMotion && e.isPre()) {
            if (holdDown.isEnabled() && !Keyboard.isKeyDown(getKey())) {
                toggle();
            }
            mc.thePlayer.stepHeight = 0;
            double mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
            double mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
            double x = (double) mc.thePlayer.movementInput.moveForward * strength.getValue() * mx + (double) mc.thePlayer.movementInput.moveStrafe * strength.getValue() * mz;
            double z = (double) mc.thePlayer.movementInput.moveForward * strength.getValue() * mz - (double) mc.thePlayer.movementInput.moveStrafe * strength.getValue() * mx;

            if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder()) {
                mc.thePlayer.sendQueue.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
                mc.thePlayer.sendQueue.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3, mc.thePlayer.posZ, false));
                mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
            }
        }
    }
}
