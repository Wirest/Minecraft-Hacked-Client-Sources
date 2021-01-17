/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.relauncher.ReflectionHelper
 *  me.xtrm.atlaspluginloader.core.event.Event$State
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.move.EventMotion
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.Module
 *  net.minecraft.client.settings.KeyBinding
 */
package delta.module.modules;

import cpw.mods.fml.relauncher.ReflectionHelper;
import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.Module;
import net.minecraft.client.settings.KeyBinding;

public class Twerk
extends Module {
    public Twerk() {
        super("Twerk", Category.Misc);
        this.setDescription("... me demandez pas pourquoi c'est l\u00e0");
    }

    @EventTarget
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getType() != Event.State.POST) {
            return;
        }
        String[] arrstring = new String[]{"pressed", "field_74513_e"};
        ReflectionHelper.setPrivateValue(KeyBinding.class, (Object)this.mc.gameSettings.keyBindSneak, (Object)(this.mc.gameSettings.keyBindSneak.getIsKeyPressed() ? 1 : 0), (String[])arrstring);
    }
}

