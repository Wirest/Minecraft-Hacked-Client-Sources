package dev.astroclient.client.feature.impl.miscellaneous;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventType;
import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.property.impl.number.Type;
import dev.astroclient.client.util.ItemUtil;
import dev.astroclient.client.util.Timer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;

@Toggleable(label = "InventoryManager", category = Category.MISC)
public class InventoryManager extends ToggleableFeature {

    public NumberProperty<Integer> delay = new NumberProperty<>("Delay", true, 180, 1, 0, 500, Type.MILLISECONDS);

    private boolean cleaning, equipping;

    private Timer timer = new Timer();

    @Subscribe
    public void onUpdate(EventMotion e) {
        int slotID = -1;
        double maxProt = -1.0D;
        int switchArmor = -1;
        if (e.getEventType() == EventType.PRE && (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory)) {
            for (int i = 9; i < 45; ++i) {
                if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if (ItemUtil.isTrash(is) && timer.hasReached(delay.getValue())) {
                        if (!cleaning) {
                            cleaning = true;
                            mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                        }
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 0, 0, mc.thePlayer);
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, -999, 0, 0, mc.thePlayer);
                        timer.reset();
                        break;
                    }
                }

                if (i == 44 && cleaning) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(0));
                    cleaning = false;
                }


                ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack != null && (this.canEquip(stack) || ItemUtil.isBestArmor(stack) && !this.canEquip(stack))) {
                    if (ItemUtil.isBestArmor(stack) && switchArmor == -1) {
                        switchArmor = ItemUtil.getSlotToSwap(stack);
                    }

                    double protValue = this.getProtectionValue(stack);
                    if (protValue >= maxProt) {
                        slotID = i;
                        maxProt = protValue;
                    }
                }
            }

            if (slotID != -1) {
                if (this.timer.hasReached(delay.getValue())) {
                    if (!this.equipping) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                        this.equipping = true;
                    }

                    if (switchArmor != -1) {
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 4 + switchArmor, 0, 4, mc.thePlayer);
                    } else {
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slotID, 0, 1, mc.thePlayer);
                    }
                    this.timer.reset();
                }
            } else if (this.equipping) {
                mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(0));
                this.equipping = false;
            }
        }

    }

    private boolean canEquip(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemArmor)) return false;

        return mc.thePlayer.getEquipmentInSlot(1) == null && stack.getUnlocalizedName().contains("boots") || mc.thePlayer.getEquipmentInSlot(2) == null && stack.getUnlocalizedName().contains("leggings") || mc.thePlayer.getEquipmentInSlot(3) == null && stack.getUnlocalizedName().contains("chestplate") || mc.thePlayer.getEquipmentInSlot(4) == null && stack.getUnlocalizedName().contains("helmet");
    }

    private double getProtectionValue(ItemStack stack) {
        return stack.getItem() instanceof ItemArmor ? (double) ((ItemArmor) stack.getItem()).damageReduceAmount + (double) ((100 - ((ItemArmor) stack.getItem()).damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075D : 0.0D;
    }
}
