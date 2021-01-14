package modification.modules.movement;

import modification.enummerates.Category;
import modification.events.EventTick;
import modification.extenders.Module;
import modification.interfaces.Event;
import modification.main.Modification;
import modification.modules.combat.Velocity;
import modification.utilities.RotationUtil;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MathHelper;

import java.util.Objects;

public final class Sprint
        extends Module {
    public Sprint(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventTick)) {
            Velocity localVelocity = (Velocity) Objects.requireNonNull(Modification.MODULE_MANAGER.checkModuleForName("Velocity"));
            if ((RotationUtil.currentRotation != null) && (Math.abs(MathHelper.wrapAngleTo180_float(RotationUtil.currentRotation.yaw - MC.thePlayer.rotationYaw)) >= 40.0F)) {
                MC.thePlayer.setSprinting(false);
                KeyBinding.setKeyBindState(MC.gameSettings.keyBindSprint.getKeyCode(), false);
                return;
            }
            KeyBinding.setKeyBindState(MC.gameSettings.keyBindSprint.getKeyCode(), true);
            if (RotationUtil.currentRotation == null) {
            }
        }
    }

    protected void onDeactivated() {
    }
}




