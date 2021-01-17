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

import delta.utils.MovementUtils;
import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import me.xtrm.delta.api.setting.Setting;

public class Speed
extends Module {
    public Speed() {
        super("Speed", 50, Category.Movement);
        this.setDescription("Zooooooooommmmmmmmmmmmmm");
        this.addSetting(new Setting("Speed", (IModule)this, 1.0, 0.0, 5.0, false));
        this.addSetting(new Setting("BunnyHop", (IModule)this, false));
    }

    @EventTarget
    public void CFsd(EventMotion eventMotion) {
        if (eventMotion.getType() != Event.State.PRE) {
            return;
        }
        MovementUtils.setSpeed(0.0);
        if (MovementUtils.isMoving()) {
            MovementUtils.setSpeed(this.getSetting("Speed").getSliderValue());
        }
        if (this.getSetting("BunnyHop").getCheckValue() && this.mc.thePlayer.onGround && MovementUtils.isMoving()) {
            this.mc.thePlayer.jump();
        }
    }
}

