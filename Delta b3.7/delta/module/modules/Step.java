/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.move.EventMotion
 *  me.xtrm.delta.api.event.events.update.EventUpdate
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  me.xtrm.delta.api.module.Module
 *  me.xtrm.delta.api.setting.Setting
 */
package delta.module.modules;

import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.event.events.update.EventUpdate;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import me.xtrm.delta.api.setting.Setting;

public class Step
extends Module {
    public Step() {
        super("Step", Category.Movement);
        this.setDescription("Monte automatiquement les blocks");
        this.addSetting(new Setting("Height", (IModule)this, 5.0, 1.0, 10.0, true));
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        this.mc.thePlayer.stepHeight = (float)this.getSetting("Height").getSliderValue();
    }

    @EventTarget
    public void onMotion(EventMotion eventMotion) {
        this.mc.thePlayer.stepHeight = (float)this.getSetting("Height").getSliderValue();
    }

    public void onDisable() {
        this.mc.thePlayer.stepHeight = 0.6f;
        super.onDisable();
    }
}

