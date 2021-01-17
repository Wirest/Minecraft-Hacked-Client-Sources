package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import java.util.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import com.darkmagician6.eventapi.*;

public class InstantPickup extends Module
{
    public InstantPickup() {
        this.setName("InstantPickup");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    public Entity getItems(final double range) {
        Entity tempEntity = null;
        double dist = range;
        for (final Object i : InstantPickup.mc.theWorld.loadedEntityList) {
            final Entity entity = (Entity)i;
            if (InstantPickup.mc.thePlayer.onGround && InstantPickup.mc.thePlayer.isCollidedVertically && entity instanceof EntityItem) {
                final double curDist = InstantPickup.mc.thePlayer.getDistanceToEntity(entity);
                if (curDist > dist) {
                    continue;
                }
                dist = curDist;
                tempEntity = entity;
            }
        }
        return tempEntity;
    }
    
    @EventTarget
    public void onPreTick(final EventPreTick e) {
        if (this.isToggled()) {
            final Entity items = this.getItems(1.0);
            if (items != null) {
                for (int i = 0; i < 200; ++i) {
                    InstantPickup.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
                }
            }
        }
    }
}
