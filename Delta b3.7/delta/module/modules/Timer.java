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

import delta.utils.PlayerUtils;
import delta.utils.Wrapper;
import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import me.xtrm.delta.api.setting.Setting;

public class Timer
extends Module {
    public Timer() {
        super("Timer", Category.World);
        this.setDescription("Acc\u00e9l\u00e8re ou ralentit le jeu");
        this.addSetting(new Setting("Timer", (IModule)this, 1.25, (double)0.2f, 2.0, false));
    }

    @EventTarget
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getType() != Event.State.PRE) {
            return;
        }
        float f = (float)this.getSetting("Timer").getSliderValue();
        if (f < 0.1f) {
            f = 0.1f;
            this.getSetting("Timer").setSliderValue((double)0.1f);
            PlayerUtils.addChatMessage("Timer d\u00e9bugg\u00e9.");
        }
        Wrapper.timer.timerSpeed = f;
    }

    public void onDisable() {
        Wrapper.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}

