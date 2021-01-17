/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.move.EventMotion
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.Module
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.EnumChatFormatting
 */
package delta.module.modules;

import delta.utils.BoundingBox;
import delta.utils.PlayerUtils;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumChatFormatting;

public class DeathCoords
extends Module {
    private boolean isDead;

    @EventTarget
    public void onMotion(EventMotion eventMotion) {
        boolean dead = this.isDead;
        this.isDead = this.mc.thePlayer.isDead;
        if (dead == this.isDead) {
            return;
        }
        if (this.isDead) {
            PlayerUtils.addChatMessage("Vous \u00eates mort en: " + (Object)EnumChatFormatting.LIGHT_PURPLE + new BoundingBox((Entity)this.mc.thePlayer)._involve());
        }
    }

    public DeathCoords() {
        super("DeathCoords", Category.Misc);
        this.setDescription("Affiche vos coordonn\u00e9es de mort dans le chat");
    }
}

