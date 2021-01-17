/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.player.EventJump
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  me.xtrm.delta.api.module.Module
 *  me.xtrm.delta.api.setting.Setting
 */
package delta.module.modules;

import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.player.EventJump;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import me.xtrm.delta.api.setting.Setting;

public class HighJump
extends Module {
    public HighJump() {
        super("HighJump", Category.Movement);
        this.setDescription("Fait sauter plus haut");
        this.addSetting(new Setting("Motion", (IModule)this, 2.0, 0.0, 5.0, false));
    }

    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    public void onJump(EventJump eventJump) {
        double d = this.getSetting("Motion").getSliderValue();
        if (this.mc.thePlayer.onGround) {
            this.mc.thePlayer.motionY = d;
        }
    }
}

