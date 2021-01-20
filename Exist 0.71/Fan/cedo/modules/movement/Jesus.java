package cedo.modules.movement;

import cedo.events.Event;
import cedo.events.listeners.EventLiquidBB;
import cedo.events.listeners.EventUpdate;
import cedo.modules.Module;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("rawtypes")
public class Jesus extends Module {

    public Jesus() {
        super("Jesus", "Walk on water", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    public void onEvent(Event e) {
        if (e instanceof EventLiquidBB && !mc.thePlayer.isInWater() && mc.thePlayer.fallDistance < 4 && !mc.thePlayer.isSneaking()) {
            ((EventLiquidBB) e).setAxisAlignedBB((new AxisAlignedBB(((EventLiquidBB) e).getPos().getX(), ((EventLiquidBB) e).getPos().getY(), ((EventLiquidBB) e).getPos().getZ(), ((EventLiquidBB) e).getPos().getX() + 1, ((EventLiquidBB) e).getPos().getY() + 1, ((EventLiquidBB) e).getPos().getZ() + 1)));
        }
        if (e instanceof EventUpdate && mc.thePlayer.isInWater() && !mc.thePlayer.isSneaking()) {
            mc.thePlayer.motionY = 0.2; //A little glitchy, Does it bypass? ¯\_(ツ)_/¯
        }
    }

}
