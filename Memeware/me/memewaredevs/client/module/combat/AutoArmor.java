
package me.memewaredevs.client.module.combat;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.misc.Timer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

public class AutoArmor extends Module {
    private static final int[] HELMET = new int[]{310, 306, 314, 302, 298};
    private static final int[] CHESTPLATE = new int[]{311, 307, 315, 303, 299};
    private static final int[] LEGGINGS = new int[]{312, 308, 316, 304, 300};
    private static final int[] BOOTS = new int[]{313, 309, 317, 305, 301};

    private Timer timer = new Timer();
    private int num = 5;
    private double maxValue = -1.0;
    private double protVal;
    private int item = -1;

    public AutoArmor() {
        super("Auto Armor", 0, Module.Category.COMBAT);
    }


    @Handler
    public Consumer<UpdateEvent> update = e -> {
        if (e.isPost())
            return;
        if (mc.thePlayer.capabilities.isCreativeMode
                || mc.thePlayer.openContainer != null && mc.thePlayer.openContainer.windowId != 0) {
            return;
        }
        if (timer.delay(500L)) {
            maxValue = -1.0;
            item = -1;
            for (int i = 9; i < 45; ++i) {
                if (mc.thePlayer.inventoryContainer.getSlot(i).getStack() == null
                        || canEquip(mc.thePlayer.inventoryContainer.getSlot(i).getStack()) == -1
                        || canEquip(mc.thePlayer.inventoryContainer.getSlot(i).getStack()) != num) {
                    continue;
                }
                change(num, i);
            }
            if (item != -1) {
                if (mc.thePlayer.inventoryContainer.getSlot(item).getStack() != null) {
                    mc.playerController.windowClick(0, num, 0, 1, mc.thePlayer);
                }
                mc.playerController.windowClick(0, item, 0, 1, mc.thePlayer);
            }
            num = num == 8 ? 5 : ++num;
        }
    };

    private int canEquip(ItemStack stack) {
        for (int id : BOOTS) {
            if (Item.getIdFromItem(stack.getItem()) != id) {
                continue;
            }
            return 8;
        }
        for (int id : LEGGINGS) {
            if (Item.getIdFromItem(stack.getItem()) != id) {
                continue;
            }
            return 7;
        }
        for (int id : CHESTPLATE) {
            if (Item.getIdFromItem(stack.getItem()) != id) {
                continue;
            }
            return 6;
        }
        for (int id : HELMET) {
            if (Item.getIdFromItem(stack.getItem()) != id) {
                continue;
            }
            return 5;
        }
        return -1;
    }

    private void change(int armorSlot, int slot) {
        ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(armorSlot).getStack();
        protVal = maxValue == -1.0 ? stack != null
                ? getProtValue(stack)
                : maxValue : maxValue;
        if (protVal <= getProtValue(stack)
                && protVal != getProtValue(stack)) {
            item = slot;
            maxValue = getProtValue(stack);
        }
    }

    private double getProtValue(ItemStack stack) {
        if (stack != null) {
            return ((ItemArmor) stack.getItem()).damageReduceAmount
                    + (100 - ((ItemArmor) stack.getItem()).damageReduceAmount * 4)
                    * EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180310_c.effectId, stack) * 4
                    * 0.0075;
        }
        return 0.0;
    }
}
