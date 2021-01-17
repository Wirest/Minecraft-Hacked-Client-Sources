/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.Event$State
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.move.EventMotion
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.Module
 *  net.minecraft.client.Minecraft
 */
package delta.module.modules;

import java.lang.reflect.Field;
import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.Module;
import net.minecraft.client.Minecraft;

public class FastPlace
extends Module {
    public FastPlace() {
        super("FastPlace", Category.World);
        this.setDescription("Permet de poser les blocks plus vite");
    }

    @EventTarget
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getType() != Event.State.PRE) {
            return;
        }
        try {
            Field field = Minecraft.getMinecraft().getClass().getDeclaredField("rightClickDelayTimer");
            field.setAccessible(true);
            field.set((Object)Minecraft.getMinecraft(), 0);
        }
        catch (Exception exception) {
            try {
                Field field = Minecraft.getMinecraft().getClass().getDeclaredField("field_71467_ac");
                field.setAccessible(true);
                field.set((Object)Minecraft.getMinecraft(), 0);
            }
            catch (Exception exception2) {
                exception.printStackTrace();
                exception2.printStackTrace();
            }
        }
    }
}

