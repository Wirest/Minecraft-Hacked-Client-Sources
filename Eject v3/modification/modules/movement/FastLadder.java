package modification.modules.movement;

import modification.enummerates.Category;
import modification.events.EventPreMotion;
import modification.extenders.Module;
import modification.interfaces.Event;
import net.minecraft.util.BlockPos;

public final class FastLadder
        extends Module {
    public FastLadder(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventPreMotion)) {
            BlockPos localBlockPos = new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY + MC.thePlayer.getEyeHeight(), MC.thePlayer.posZ);
            if (MC.theWorld.isAirBlock(localBlockPos)) {
                return;
            }
            if ((MC.thePlayer.isOnLadder()) && ((MC.thePlayer.moveForward != 0.0F) || (MC.thePlayer.moveStrafing != 0.0F))) {
                MC.thePlayer.motionY = 0.25D;
            }
        }
    }

    protected void onDeactivated() {
    }
}




