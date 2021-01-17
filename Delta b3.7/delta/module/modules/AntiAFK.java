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

import delta.utils.MovementUtils;
import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.Module;

public class AntiAFK
extends Module {
    private int delay = 0;

    @EventTarget
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getType() != Event.State.PRE) {
            return;
        }
        ++this.delay;
        if (this.delay > 100) {
            this.delay = 0;
            this.mc.thePlayer.rotationYaw -= 90.0f;
            MovementUtils.setSpeed(0.5);
            if (this.mc.thePlayer.onGround) {
                this.mc.thePlayer.jump();
            }
        }
    }

    public AntiAFK() {
        super("AntiAFK", Category.Misc);
        this.setDescription("Permet de rester AFK et de ne pas se faire kick");
    }
}

