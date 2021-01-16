/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.player;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;

@Module.Mod(category=Module.Category.AUTO, description="Eats for you when needed.", key=0, name="AutoEat")
public class AutoEat
extends Module {
    int savedslot = -1;
    boolean hasdoneit = false;

    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        if (!AutoEat.shouldEat()) {
            if (this.hasdoneit) {
                Wrapper.getSettings().keyBindUseItem.pressed = false;
                this.hasdoneit = false;
            }
            if (this.savedslot != -1) {
                Wrapper.getPlayer().inventory.currentItem = this.savedslot;
                this.savedslot = -1;
            }
        } else {
            if (this.savedslot == -1) {
                this.savedslot = Wrapper.getPlayer().inventory.currentItem;
                Wrapper.getPlayer().inventory.currentItem = AutoEat.getFood();
            }
            Wrapper.getSettings().keyBindUseItem.pressed = true;
            this.hasdoneit = true;
        }
    }

    public static boolean shouldEat() {
        if ((double)Wrapper.getPlayer().getFoodStats().getFoodLevel() < 17.5 && AutoEat.getFood() != -1) {
            return true;
        }
        return false;
    }

    public static int getFood() {
        int food = -1;
        int i = 0;
        while (i < 9) {
            if (Wrapper.getPlayer().inventory.getStackInSlot(i) != null && Wrapper.getPlayer().inventory.getStackInSlot(i).getItem() instanceof ItemFood) {
                food = i;
                break;
            }
            ++i;
        }
        return food;
    }
}

