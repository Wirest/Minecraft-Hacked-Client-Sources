/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.Event$State
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.move.EventMotion
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.Module
 */
package delta.module.modules;

import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.Module;

public class NoFall
extends Module {
    @EventTarget
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getType() != Event.State.PRE) {
            return;
        }
        if (this.mc.thePlayer.fallDistance >= 2.0f) {
            eventMotion.setOnGround(true);
        }
    }

    public NoFall() {
        super("NoFall", Category.Movement);
        this.setDescription("Permet de ne pas prendre de d\u00e9gats de chute");
    }
}

