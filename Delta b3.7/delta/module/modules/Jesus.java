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

public class Jesus
extends Module {
    public Jesus() {
        super("Jesus", Category.Movement);
        this.setDescription("Permet de marcher sur l'eau");
    }

    @EventTarget
    public void jYuG(EventUpdate eventUpdate) {
        if (this.mc.thePlayer.isInWater()) {
            this.mc.thePlayer.motionY = 0.05;
            this.mc.thePlayer.motionX *= 1.2;
            this.mc.thePlayer.motionZ *= 1.2;
            if (this.mc.thePlayer.isCollidedHorizontally) {
                this.mc.thePlayer.onGround = true;
            }
        }
    }
}

