package com.etb.client.module.modules.combat;

import java.awt.Color;

import org.greenrobot.eventbus.Subscribe;

import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.TimerUtil;
import com.etb.client.utils.value.impl.BooleanValue;
import com.etb.client.utils.value.impl.NumberValue;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Module {
    private BooleanValue drop = new BooleanValue("Drop", true);
    private NumberValue<Integer> delay = new NumberValue("Delay", 250, 1, 3000, 1);
    private TimerUtil timer = new TimerUtil();

    public AutoArmor() {
        super("AutoArmor", Category.COMBAT, new Color(180, 180, 190, 255).getRGB());
        setDescription("Automatically equips armor");
        setRenderlabel("Auto Armor");
        addValues(drop, delay);

    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        setSuffix(delay.getValue().toString());
        if (mc.thePlayer != null && (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory)) {
            int slotID = -1;
            double maxProt = -1.0;
            int switchArmor = -1;
            for (int i = 9; i < 45; ++i) {
                double protValue;
                ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack == null || !canEquip(stack) && (!betterCheck(stack) || canEquip(stack))) continue;
                if (betterCheck(stack) && switchArmor == -1) {
                    switchArmor = betterSwap(stack);
                }
                if ((protValue = getProtectionValue(stack)) < maxProt) continue;
                slotID = i;
                maxProt = protValue;
            }
            if (slotID != -1 && timer.sleep(250)) {
                if (switchArmor != -1) {
                    if (drop.isEnabled()) {
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 4 + switchArmor, 0, 0, mc.thePlayer);
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, -999, 0, 0, mc.thePlayer);
                    } else {
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 4 + switchArmor, 0, 1, mc.thePlayer);

                    }
                }
                mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slotID, 0, 1, mc.thePlayer);
                timer.reset();
            }
        }
    }

    private boolean betterCheck(final ItemStack stack) {
        if (stack.getItem() instanceof ItemArmor) {
            if (mc.thePlayer.getEquipmentInSlot(1) != null && stack.getUnlocalizedName().contains("boots") && this.getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > this.getProtectionValue(mc.thePlayer.getEquipmentInSlot(1)) + ((ItemArmor) mc.thePlayer.getEquipmentInSlot(1).getItem()).damageReduceAmount) {
                return true;
            }
            if (mc.thePlayer.getEquipmentInSlot(2) != null && stack.getUnlocalizedName().contains("leggings") && this.getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > this.getProtectionValue(mc.thePlayer.getEquipmentInSlot(2)) + ((ItemArmor) mc.thePlayer.getEquipmentInSlot(2).getItem()).damageReduceAmount) {
                return true;
            }
            if (mc.thePlayer.getEquipmentInSlot(3) != null && stack.getUnlocalizedName().contains("chestplate") && this.getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > this.getProtectionValue(mc.thePlayer.getEquipmentInSlot(3)) + ((ItemArmor) mc.thePlayer.getEquipmentInSlot(3).getItem()).damageReduceAmount) {
                return true;
            }
            if (mc.thePlayer.getEquipmentInSlot(4) != null && stack.getUnlocalizedName().contains("helmet") && this.getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > this.getProtectionValue(mc.thePlayer.getEquipmentInSlot(4)) + ((ItemArmor) mc.thePlayer.getEquipmentInSlot(4).getItem()).damageReduceAmount) {
                return true;
            }
        }
        return false;
    }

    private int betterSwap(final ItemStack stack) {
        if (stack.getItem() instanceof ItemArmor) {
            if (mc.thePlayer.getEquipmentInSlot(1) != null && stack.getUnlocalizedName().contains("boots") && this.getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > this.getProtectionValue(mc.thePlayer.getEquipmentInSlot(1)) + ((ItemArmor) mc.thePlayer.getEquipmentInSlot(1).getItem()).damageReduceAmount) {
                return 4;
            }
            if (mc.thePlayer.getEquipmentInSlot(2) != null && stack.getUnlocalizedName().contains("leggings") && this.getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > this.getProtectionValue(mc.thePlayer.getEquipmentInSlot(2)) + ((ItemArmor) mc.thePlayer.getEquipmentInSlot(2).getItem()).damageReduceAmount) {
                return 3;
            }
            if (mc.thePlayer.getEquipmentInSlot(3) != null && stack.getUnlocalizedName().contains("chestplate") && this.getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > this.getProtectionValue(mc.thePlayer.getEquipmentInSlot(3)) + ((ItemArmor) mc.thePlayer.getEquipmentInSlot(3).getItem()).damageReduceAmount) {
                return 2;
            }
            if (mc.thePlayer.getEquipmentInSlot(4) != null && stack.getUnlocalizedName().contains("helmet") && this.getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > this.getProtectionValue(mc.thePlayer.getEquipmentInSlot(4)) + ((ItemArmor) mc.thePlayer.getEquipmentInSlot(4).getItem()).damageReduceAmount) {
                return 1;
            }
        }
        return -1;
    }

    private boolean canEquip(ItemStack stack) {
        return mc.thePlayer.getEquipmentInSlot(1) == null && stack.getUnlocalizedName().contains("boots") || mc.thePlayer.getEquipmentInSlot(2) == null && stack.getUnlocalizedName().contains("leggings") || mc.thePlayer.getEquipmentInSlot(3) == null && stack.getUnlocalizedName().contains("chestplate") || mc.thePlayer.getEquipmentInSlot(4) == null && stack.getUnlocalizedName().contains("helmet");
    }

    private double getProtectionValue(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemArmor)) {
            return 0.0;
        }
        return ((ItemArmor) stack.getItem()).damageReduceAmount + (100 - ((ItemArmor) stack.getItem()).damageReduceAmount * 4) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 4 * 0.0075;
    }
}
