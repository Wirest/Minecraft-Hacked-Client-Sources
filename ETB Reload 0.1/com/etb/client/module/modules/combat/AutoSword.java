package com.etb.client.module.modules.combat;

import java.awt.Color;

import org.greenrobot.eventbus.Subscribe;

import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.TimerUtil;
import com.etb.client.utils.value.impl.NumberValue;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class AutoSword extends Module {
    private NumberValue<Integer> delay = new NumberValue<>("Delay", 100, 1, 2000, 1);
    private TimerUtil timer = new TimerUtil();
    public static NumberValue<Integer> slot = new NumberValue<>("Slot", 0, 0, 8, 1);

    public AutoSword() {
        super("AutoSword", Category.COMBAT, new Color(153, 204, 255, 255).getRGB());
        setDescription("Automatically equips the strongest sword");
        setRenderlabel("Auto Sword");
        addValues(delay, slot);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (event.isPre() || !timer.reach(delay.getValue()) || (mc.currentScreen != null && !(mc.currentScreen instanceof GuiInventory)))
            return;
        int best = -1;
        float swordDamage = 0;
        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemSword) {
                    float swordD = getItemDamage(is);
                    if (swordD > swordDamage) {
                        swordDamage = swordD;
                        best = i;
                    }
                }
            }
        }
        final ItemStack current = mc.thePlayer.inventoryContainer.getSlot(36 + slot.getValue()).getStack();
        if (best != -1 && (current == null || !(current.getItem() instanceof ItemSword) || swordDamage > getItemDamage(current))) {
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, best, slot.getValue(), 2, mc.thePlayer);
            timer.reset();
        }
    }

    private float getItemDamage(final ItemStack itemStack) {
        float damage = ((ItemSword) itemStack.getItem()).getDamageVsEntity();
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.01f;
        return damage;
    }
}
