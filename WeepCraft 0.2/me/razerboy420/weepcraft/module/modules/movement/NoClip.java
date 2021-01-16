/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.movement;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPrePlayerUpdate;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

@Module.Mod(category=Module.Category.MOVEMENT, description="Walk through walls", key=0, name="NoClip")
public class NoClip
extends Module {
    @EventTarget
    public void onEvent(EventPrePlayerUpdate event) {
        Wrapper.getPlayer().noClip = true;
        if (Wrapper.getSettings().keyBindJump.pressed) {
            Wrapper.getPlayer().motionY = 0.10000000149011612;
        } else if (!Wrapper.getSettings().keyBindSneak.pressed) {
            Wrapper.getPlayer().motionY = 0.0;
        }
    }
}

