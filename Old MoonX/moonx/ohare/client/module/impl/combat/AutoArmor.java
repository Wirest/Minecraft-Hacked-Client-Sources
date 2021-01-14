package moonx.ohare.client.module.impl.combat;

import java.awt.Color;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.MathUtils;
import moonx.ohare.client.utils.TimerUtil;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Module {
    private BooleanValue drop = new BooleanValue("Drop","Drop Worse Armor", true);
    private NumberValue<Integer> delay = new NumberValue<>("Delay", 250, 1, 3000, 1);
    private BooleanValue inventoryonly = new BooleanValue("Inventory Only",false);
    private TimerUtil timer = new TimerUtil();

    public AutoArmor() {
        super("AutoArmor", Category.COMBAT, new Color(180, 180, 190, 255).getRGB());
        setDescription("Automatically equips armor");
        setRenderLabel("Auto Armor");
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        setSuffix(delay.getValue().toString());
        if (!(getMc().currentScreen instanceof GuiInventory) && inventoryonly.isEnabled()) {
            return;
        }
        if (getMc().thePlayer != null && (getMc().currentScreen == null || getMc().currentScreen instanceof GuiInventory)) {
            int slotID = -1;
            double maxProt = -1.0;
            int switchArmor = -1;
            for (int i = 9; i < 45; ++i) {
                double protValue;
                ItemStack stack = getMc().thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack == null || !canEquip(stack) && (!betterCheck(stack) || canEquip(stack))) continue;
                if (betterCheck(stack) && switchArmor == -1) {
                    switchArmor = betterSwap(stack);
                }
                if ((protValue = getProtectionValue(stack)) < maxProt) continue;
                slotID = i;
                maxProt = protValue;
            }
            if (slotID != -1) {
                if (timer.sleep(delay.getValue())) {
                    if (switchArmor != -1) {
                        if (drop.isEnabled()) {
                            getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, 4 + switchArmor, 0, 0, getMc().thePlayer);
                            getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, -999, 0, 0, getMc().thePlayer);
                        } else {
                            getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, 4 + switchArmor, 0, 1, getMc().thePlayer);

                        }
                    }
                    getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, slotID, 0, 1, getMc().thePlayer);
                    timer.reset();
                }
            }
            else {
                timer.reset();
            }
        }
    }

    private boolean betterCheck(final ItemStack stack) {
        if (getMc().thePlayer.getEquipmentInSlot(4) != null &&Item.getIdFromItem(getMc().thePlayer.getEquipmentInSlot(4).getItem()) == 397) return true;
        if (stack.getItem() instanceof ItemArmor) {
            if (getMc().thePlayer.getEquipmentInSlot(1) != null  && stack.getUnlocalizedName().contains("boots") && this.getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > this.getProtectionValue(getMc().thePlayer.getEquipmentInSlot(1)) + ((ItemArmor) getMc().thePlayer.getEquipmentInSlot(1).getItem()).damageReduceAmount) {
                return true;
            }
            if (getMc().thePlayer.getEquipmentInSlot(2) != null  && stack.getUnlocalizedName().contains("leggings") && this.getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > this.getProtectionValue(getMc().thePlayer.getEquipmentInSlot(2)) + ((ItemArmor) getMc().thePlayer.getEquipmentInSlot(2).getItem()).damageReduceAmount) {
                return true;
            }
            if (getMc().thePlayer.getEquipmentInSlot(3) != null  && stack.getUnlocalizedName().contains("chestplate") && this.getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > this.getProtectionValue(getMc().thePlayer.getEquipmentInSlot(3)) + ((ItemArmor) getMc().thePlayer.getEquipmentInSlot(3).getItem()).damageReduceAmount) {
                return true;
            }
            if (getMc().thePlayer.getEquipmentInSlot(4) != null  && stack.getUnlocalizedName().contains("helmet") && this.getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > this.getProtectionValue(getMc().thePlayer.getEquipmentInSlot(4)) + ((ItemArmor) getMc().thePlayer.getEquipmentInSlot(4).getItem()).damageReduceAmount) {
                return true;
            }
        }
        return false;
    }

    private int betterSwap(final ItemStack stack) {
        if (getMc().thePlayer.getEquipmentInSlot(4) != null && Item.getIdFromItem(getMc().thePlayer.getEquipmentInSlot(4).getItem()) == 397) return 1;
        if (stack.getItem() instanceof ItemArmor) {
            if (getMc().thePlayer.getEquipmentInSlot(1) != null  && stack.getUnlocalizedName().contains("boots") && this.getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > this.getProtectionValue(getMc().thePlayer.getEquipmentInSlot(1)) + ((ItemArmor) getMc().thePlayer.getEquipmentInSlot(1).getItem()).damageReduceAmount) {
                return 4;
            }
            if (getMc().thePlayer.getEquipmentInSlot(2) != null  && stack.getUnlocalizedName().contains("leggings") && this.getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > this.getProtectionValue(getMc().thePlayer.getEquipmentInSlot(2)) + ((ItemArmor) getMc().thePlayer.getEquipmentInSlot(2).getItem()).damageReduceAmount) {
                return 3;
            }
            if (getMc().thePlayer.getEquipmentInSlot(3) != null  && stack.getUnlocalizedName().contains("chestplate") && this.getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > this.getProtectionValue(getMc().thePlayer.getEquipmentInSlot(3)) + ((ItemArmor) getMc().thePlayer.getEquipmentInSlot(3).getItem()).damageReduceAmount) {
                return 2;
            }
            if (getMc().thePlayer.getEquipmentInSlot(4) != null  && stack.getUnlocalizedName().contains("helmet") && this.getProtectionValue(stack) + ((ItemArmor) stack.getItem()).damageReduceAmount > this.getProtectionValue(getMc().thePlayer.getEquipmentInSlot(4)) + ((ItemArmor) getMc().thePlayer.getEquipmentInSlot(4).getItem()).damageReduceAmount) {
                return 1;
            }
        }
        return -1;
    }

    private boolean canEquip(ItemStack stack) {
        return getMc().thePlayer.getEquipmentInSlot(1) == null  && stack.getUnlocalizedName().contains("boots") || getMc().thePlayer.getEquipmentInSlot(2) == null && stack.getUnlocalizedName().contains("leggings") || getMc().thePlayer.getEquipmentInSlot(3) == null && stack.getUnlocalizedName().contains("chestplate") || getMc().thePlayer.getEquipmentInSlot(4) == null && stack.getUnlocalizedName().contains("helmet");
    }

    private double getProtectionValue(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemArmor)) {
            return 0.0;
        }
        return ((ItemArmor) stack.getItem()).damageReduceAmount + (100 - ((ItemArmor) stack.getItem()).damageReduceAmount * 4) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 4 * 0.0075;
    }
}
