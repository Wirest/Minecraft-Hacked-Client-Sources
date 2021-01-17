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

public class AutoBreak
extends Module {
    public void onDisable() {
        String[] arrstring = new String[]{"pressed", "field_74513_e"};
        ReflectionHelper.setPrivateValue(KeyBinding.class, (Object)this.mc.gameSettings.keyBindAttack, (Object)false, (String[])arrstring);
        super.onDisable();
    }

    public AutoBreak() {
        super("AutoBreak", Category.Player);
        this.setDescription("Presse toujours clique gauche pour casser automatiquement");
    }

    @EventTarget
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getType() != Event.State.PRE) {
            return;
        }
        String[] arrstring = new String[]{"pressed", "field_74513_e"};
        ReflectionHelper.setPrivateValue(KeyBinding.class, (Object)this.mc.gameSettings.keyBindAttack, (Object)true, (String[])arrstring);
    }
}

