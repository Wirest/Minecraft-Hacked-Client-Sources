/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.world;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.List;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class AntiInvis
extends Mod {
    private ArrayList<Entity> entites = new ArrayList();

    public AntiInvis() {
        super("AntiInvis", Mod.Category.WORLD, Colors.GREY.c);
    }

    @Override
    public void onDisable() {
        for (Entity e : this.entites) {
            e.setInvisible(true);
        }
        this.entites.clear();
        super.onDisable();
        ClientUtil.sendClientMessage("AntiInvis Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("AntiInvis Enable", ClientNotification.Type.SUCCESS);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        for (Object o : this.mc.theWorld.loadedEntityList) {
            EntityLivingBase entity;
            if (!(o instanceof EntityLivingBase) || !(entity = (EntityLivingBase)o).isInvisible() || this.entites.contains(entity)) continue;
            this.entites.add(entity);
            entity.setInvisible(false);
        }
    }
}

