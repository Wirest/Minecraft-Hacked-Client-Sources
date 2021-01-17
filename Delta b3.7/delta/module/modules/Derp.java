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
 */
package delta.module.modules;

import java.util.Random;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import me.xtrm.delta.api.setting.Setting;

public class Derp
extends Module {
    public Derp() {
        super("Derp", Category.Misc);
        this.setDescription("\"jme sens pas bien\"");
        this.addSetting(new Setting("ServerOnly", (IModule)this, true));
    }

    @EventTarget
    public void onMotion(EventMotion eventMotion) {
        Random random = new Random();
        eventMotion.setYaw((float)(random.nextInt(360) - 180));
        eventMotion.setPitch((float)(random.nextInt(180) - 90));
        eventMotion.setSilent(this.getSetting("ServerOnly").getCheckValue());
    }
}

