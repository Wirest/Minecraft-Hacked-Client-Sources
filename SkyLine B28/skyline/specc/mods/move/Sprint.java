package skyline.specc.mods.move;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.minecraft.MoveEvents.EventMoveRaw;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventPlayerUpdate;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;

public class Sprint extends Module {

	public Sprint(){
		super(new ModData("Sprint", Keyboard.KEY_NONE, new Color(40, 255, 10)), ModType.MOVEMENT);
	}

	
	private boolean canSprint() {
        return !p.isCollidedHorizontally && !p.isSneaking() && p.getFoodStats().getFoodLevel() > 6 && (p.movementInput.moveForward != 0.0f || p.movementInput.moveStrafe != 0.0f);
    }

    @EventListener
    public void onMove(EventMoveRaw event) {
        if (canSprint()) {
            p.setSprinting(true);
        }
    }

    @Override
    public void onDisable() {
        if (p != null) {
            p.setSprinting(false);
        }
    }
}