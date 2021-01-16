/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.auto;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.InventoryPlayer;

@Module.Mod(category=Module.Category.AUTO, description="Switch items fast", key=0, name="AutoSwitch")
public class AutoSwitch
extends Module {
    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        if (Wrapper.getPlayer().inventory.currentItem >= 8) {
            Wrapper.getPlayer().inventory.currentItem = 0;
        }
        ++Wrapper.getPlayer().inventory.currentItem;
    }
}

