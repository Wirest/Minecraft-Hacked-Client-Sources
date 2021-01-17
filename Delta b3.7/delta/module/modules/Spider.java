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

public class Spider
extends Module {
    public Spider() {
        super("Spider", Category.Movement);
        this.setDescription("Grimpe les blocks");
    }

    @EventTarget
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getType() != Event.State.PRE) {
            return;
        }
        if (this.mc.thePlayer.motionY < 0.0 && this.mc.thePlayer.isCollidedHorizontally) {
            this.mc.thePlayer.jump();
        }
    }
}

