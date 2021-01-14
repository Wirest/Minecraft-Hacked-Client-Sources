package cedo.modules.render;

import cedo.events.Event;
import cedo.events.listeners.EventRenderWorld;
import cedo.modules.Module;
import cedo.ui.elements.drawLine;
import net.minecraft.entity.Entity;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("rawtypes")
public class Tracers extends Module {

    public Tracers() {
        super("Tracers", Keyboard.KEY_O, Category.RENDER);
    }

    public void onEvent(Event e) {
        if (e instanceof EventRenderWorld) {
            for (int i = 0; i < mc.theWorld.loadedEntityList.size(); i++) {
                Entity entity = mc.theWorld.loadedEntityList.get(i);
                if (entity != null && entity != mc.thePlayer) {
                    //if (entity instanceof EntityPlayer)
                    new drawLine(0, mc.thePlayer.getEyeHeight(), 0, entity.posX - mc.thePlayer.posX, entity.posY - mc.thePlayer.posY, entity.posZ - mc.thePlayer.posZ, 1.5f);
                    //The line should not be drawn from ^ eye height, but instead from the middle of the player's camera. If you want me to do this, just remind me, cos I can't do it rn - Foggy
                }
                //but look at this
            }
        }
    }
}

