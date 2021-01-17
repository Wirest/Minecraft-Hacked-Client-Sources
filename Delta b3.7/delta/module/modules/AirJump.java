/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.update.EventUpdate
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.Module
 */
package delta.module.modules;

import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.update.EventUpdate;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.Module;

public class AirJump
extends Module {
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        this.mc.thePlayer.onGround = true;
    }

    public AirJump() {
        super("AirJump", Category.Movement);
        this.setDescription("Permet de sauter en l'air");
    }
}

