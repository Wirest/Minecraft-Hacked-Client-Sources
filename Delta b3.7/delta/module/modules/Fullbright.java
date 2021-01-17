/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.Event$State
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.move.EventMotion
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.Module
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 */
package delta.module.modules;

import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.Module;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Fullbright
extends Module {
    @EventTarget
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getType() != Event.State.PRE) {
            return;
        }
        this.mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.id, 860, 10));
    }

    public Fullbright() {
        super("Fullbright", Category.Render);
        this.setDescription("Permet de voir dans le noir");
    }

    public void onDisable() {
        this.mc.thePlayer.removePotionEffectClient(Potion.nightVision.id);
        super.onDisable();
    }
}

