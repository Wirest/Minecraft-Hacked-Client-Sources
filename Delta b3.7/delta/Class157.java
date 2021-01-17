/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.network.EventPacket
 *  me.xtrm.delta.api.event.events.update.EventUpdate
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  me.xtrm.delta.api.module.Module
 *  me.xtrm.delta.api.setting.Setting
 */
package delta;

import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.network.EventPacket;
import me.xtrm.delta.api.event.events.update.EventUpdate;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import me.xtrm.delta.api.setting.Setting;

public class Class157
extends Module {
    @EventTarget
    public void UTW9(EventPacket eventPacket) {
    }

    public Class157() {
        super("TestModule", Category.Misc);
        this.addSetting(new Setting("Dick length", (IModule)this, 128.0, 0.0, 65535.0, 49 - 71 + 1 - 1 + 23));
    }

    @EventTarget
    public void jytA(EventUpdate eventUpdate) {
    }
}

