package com.etb.client.module.modules.world;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import org.greenrobot.eventbus.Subscribe;

import com.etb.client.event.events.game.TickEvent;
import com.etb.client.module.Module;

import net.minecraft.entity.item.EntityItem;

/**
 * made by Xen for ETB Reloaded
 *
 * @since 6/10/2019
 **/
public class NoRender extends Module {

    public NoRender() {
        super("NoRender", Category.WORLD, new Color(0).getRGB());
        setDescription("Dont render items");
        setRenderlabel("No Render");
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.theWorld != null) {

            List<EntityItem> items = new LinkedList<>();

            for (Object o : mc.theWorld.loadedEntityList) {
                if (o instanceof EntityItem) {
                    items.add((EntityItem) o);
                }
            }

            items.forEach(mc.theWorld::removeEntity);
        }
    }
}
