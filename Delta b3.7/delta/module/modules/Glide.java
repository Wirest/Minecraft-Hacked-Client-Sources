/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.Event$State
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.move.EventMotion
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  me.xtrm.delta.api.module.Module
 *  me.xtrm.delta.api.setting.Setting
 */
package delta.module.modules;

import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import me.xtrm.delta.api.setting.Setting;

public class Glide
extends Module {
    @EventTarget
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getType() != Event.State.PRE) {
            return;
        }
        if (this.mc.thePlayer.motionY < 0.0 && !this.mc.thePlayer.onGround) {
            this.mc.thePlayer.motionY *= this.getSetting("Motion").getSliderValue();
        }
    }

    public Glide() {
        super("Glide", Category.Movement);
        this.setDescription("Tombe lentement dans les airs");
        this.addSetting(new Setting("Motion", (IModule)this, 0.75, 0.0, 1.0, false));
    }
}

