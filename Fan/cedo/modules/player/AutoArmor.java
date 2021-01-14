package cedo.modules.player;

import cedo.Fan;
import cedo.events.Event;
import cedo.events.listeners.EventUpdate;
import cedo.modules.Module;
import cedo.settings.impl.NumberSetting;
import cedo.util.BypassUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("rawtypes")
public class AutoArmor extends Module {
    NumberSetting speed = new NumberSetting("Delay", 170, 100, 500, 10);

    public AutoArmor() {
        super("AutoArmor", Keyboard.KEY_NONE, Category.PLAYER);
        addSettings(speed);
    }

    public static double getProtectionValue(ItemStack stack) {
        return !(stack.getItem() instanceof ItemArmor) ? 0.0D : (double) ((ItemArmor) stack.getItem()).damageReduceAmount + (double) ((100 - ((ItemArmor) stack.getItem()).damageReduceAmount * 4) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 4) * 0.0075D;
    }

    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            if (mc.thePlayer != null && (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory)) {
                int slotID = -1;
                double maxProt = -1;
                int switchArmor = -1;

                for (int i = 9; i < 45; ++i) {
                    ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if (stack != null && (this.canEquip(stack) || this.betterCheck(stack) && !this.canEquip(stack))) {
                        if (this.betterCheck(stack) && switchArmor == -1) {
                            switchArmor = this.betterSwap(stack);
                        }

                        double protValue = getProtectionValue(stack);
                        if (protValue >= maxProt) {
                            slotID = i;
                            maxProt = protValue;
                        }
                    }
                }

                if (slotID != -1 && Fan.invCooldownElapsed((long) (speed.getValue() + BypassUtil.range(0, 30)))) {
                    if (!(mc.currentScreen instanceof GuiInventory)) {
                        mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                    }
                    if (switchArmor != -1) {
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 4 + switchArmor, 0, 0, mc.thePlayer);
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, -999, 0, 0, mc.thePlayer);
                    }

                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slotID, 0, 1, mc.thePlayer);
                    if (!(mc.currentScreen instanceof GuiInventory)) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(mc.thePlayer.openContainer.windowId));
                    }
                    Fan.invCooldownElapsed(0);
                }
            }
        }

    }

    public boolean betterCheck(ItemStack stack) {
        if (stack.getItem() instanceof ItemArmor) {
            if (mc.thePlayer.getEquipmentInSlot(1) != null && stack.getUnlocalizedName().contains("boots") && getProtectionValue(stack) + (double) ((ItemArmor) stack.getItem()).damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(1)) + (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(1).getItem()).damageReduceAmount) {
                return true;
            }

            if (mc.thePlayer.getEquipmentInSlot(2) != null && stack.getUnlocalizedName().contains("leggings") && getProtectionValue(stack) + (double) ((ItemArmor) stack.getItem()).damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(2)) + (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(2).getItem()).damageReduceAmount) {
                return true;
            }

            if (mc.thePlayer.getEquipmentInSlot(3) != null && stack.getUnlocalizedName().contains("chestplate") && getProtectionValue(stack) + (double) ((ItemArmor) stack.getItem()).damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(3)) + (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(3).getItem()).damageReduceAmount) {
                return true;
            }

            return mc.thePlayer.getEquipmentInSlot(4) != null && stack.getUnlocalizedName().contains("helmet") && getProtectionValue(stack) + (double) ((ItemArmor) stack.getItem()).damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(4)) + (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(4).getItem()).damageReduceAmount;
        }

        return false;
    }

    private int betterSwap(ItemStack stack) {
        if (stack.getItem() instanceof ItemArmor) {
            if (mc.thePlayer.getEquipmentInSlot(4) != null && stack.getUnlocalizedName().contains("helmet") && getProtectionValue(stack) + (double) ((ItemArmor) stack.getItem()).damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(4)) + (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(4).getItem()).damageReduceAmount) {
                return 1;
            }

            if (mc.thePlayer.getEquipmentInSlot(3) != null && stack.getUnlocalizedName().contains("chestplate") && getProtectionValue(stack) + (double) ((ItemArmor) stack.getItem()).damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(3)) + (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(3).getItem()).damageReduceAmount) {
                return 2;
            }

            if (mc.thePlayer.getEquipmentInSlot(2) != null && stack.getUnlocalizedName().contains("leggings") && getProtectionValue(stack) + (double) ((ItemArmor) stack.getItem()).damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(2)) + (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(2).getItem()).damageReduceAmount) {
                return 3;
            }

            if (mc.thePlayer.getEquipmentInSlot(1) != null && stack.getUnlocalizedName().contains("boots") && getProtectionValue(stack) + (double) ((ItemArmor) stack.getItem()).damageReduceAmount > getProtectionValue(mc.thePlayer.getEquipmentInSlot(1)) + (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(1).getItem()).damageReduceAmount) {
                return 4;
            }
        }

        return -1;
    }

    private boolean canEquip(ItemStack stack) {
        return mc.thePlayer.getEquipmentInSlot(1) == null && stack.getUnlocalizedName().contains("boots") || mc.thePlayer.getEquipmentInSlot(2) == null && stack.getUnlocalizedName().contains("leggings") || mc.thePlayer.getEquipmentInSlot(3) == null && stack.getUnlocalizedName().contains("chestplate") || mc.thePlayer.getEquipmentInSlot(4) == null && stack.getUnlocalizedName().contains("helmet");
    }
}