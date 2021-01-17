/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.move.EventMotion
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  me.xtrm.delta.api.module.Module
 *  me.xtrm.delta.api.setting.Setting
 *  net.minecraft.util.MathHelper
 */
package delta.module.modules;

import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import me.xtrm.delta.api.setting.Setting;
import net.minecraft.util.MathHelper;

public class Spin
extends Module {
    private int count;

    public Spin() {
        super("Spin", Category.Misc);
        this.setDescription("Beyblade beyblade hyper vitesse!");
        this.addSetting(new Setting("ServerOnly", (IModule)this, true));
    }

    @EventTarget
    public void onMotion(EventMotion eventMotion) {
        this.count += 10;
        if (this.count > 360) {
            this.count = 0;
        }
        eventMotion.setYaw(MathHelper.wrapAngleTo180_float((float)this.count));
        eventMotion.setSilent(this.getSetting("ServerOnly").getCheckValue());
    }
}

