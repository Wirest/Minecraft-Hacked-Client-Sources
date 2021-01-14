package cedo.modules.movement;

import cedo.Wrapper;
import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import cedo.util.server.PacketUtil;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("rawtypes")
public class Sprint extends Module {

    public BooleanSetting omni = new BooleanSetting("Omnidirectional", false);

    public Sprint() {
        super("Sprint", Keyboard.KEY_M, Category.MOVEMENT);
        addSettings(omni);
    }

    public void onDisable() {
        mc.thePlayer.setSprinting(mc.gameSettings.keyBindSprint.isPressed());
        super.onDisable();
    }

    public void onEvent(Event e) {


        //TODO for omni, check that the player is moving before sprinting
        if (e instanceof EventMotion && e.isPre()) {
            if (Wrapper.getFuckedPrinceKin != 423499) {
                PacketUtil.sendPacketNoEvent(new C13PacketPlayerAbilities(232, 321, false, true, false, true));
            }

            this.setSuffix(omni.isEnabled() ? "Omni" : null);

            if (canSprint() && (omni.isEnabled() ? mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0 : mc.thePlayer.moveForward > 0)) {
                mc.thePlayer.setSprinting(true);
            }
        }
    }

    private boolean canSprint() {
        return (!mc.thePlayer.isSneaking() && !mc.thePlayer.isCollidedHorizontally && mc.thePlayer.getFoodStats().getFoodLevel() >= 6 && !mc.thePlayer.isUsingItem());
    }

}