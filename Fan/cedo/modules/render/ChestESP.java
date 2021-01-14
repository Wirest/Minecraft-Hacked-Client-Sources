package cedo.modules.render;

import cedo.events.Event;
import cedo.events.listeners.EventRenderWorld;
import cedo.modules.Module;
import cedo.util.render.EspUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ChestESP extends Module {

    public ChestESP() {
        super("ChestESP", Keyboard.KEY_O, Category.RENDER);
    }

    public void onEvent(Event e) {
        if (e instanceof EventRenderWorld) {
            for (TileEntity tileEntity : mc.theWorld.loadedTileEntityList) {


                if (!(tileEntity instanceof TileEntityChest))
                    continue;

                Color chestColor = new Color(-1);

                EspUtil.chestESPBox(tileEntity, 0, chestColor);
            }
        }

    }

}

