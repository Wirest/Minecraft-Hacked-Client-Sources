package cedo.modules.movement;

import cedo.events.Event;
import cedo.events.listeners.EventUpdate;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
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

    public void onEvent(Event e) { //TODO for omni, check that the player is moving before sprinting
        if (e instanceof EventUpdate && e.isPre()) {

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