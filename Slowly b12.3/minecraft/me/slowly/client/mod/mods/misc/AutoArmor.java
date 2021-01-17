/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.misc;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor
extends Mod {
    private TimeHelper timeHelper = new TimeHelper();
    private Value<Boolean> openInv = new Value<Boolean>("AutoArmor_OnlyInInv", true);
    private Value<Double> delay = new Value<Double>("AutoArmor_Delay", 60.0, 0.0, 1000.0, 10.0);
    private Value<Double> inventoryKeepTimeVal = new Value<Double>("AutoArmor_TimeInInv", 1000.0, 0.0, 10000.0, 100.0);
    private Value<Boolean> insant = new Value<Boolean>("AutoArmor_Insant", false);
    private ArrayList<ItemStack> openList = new ArrayList();
    private ArrayList<ItemStack> closeList = new ArrayList();

    public AutoArmor() {
        super("AutoArmor", Mod.Category.MISCELLANEOUS, Colors.DARKAQUA.c);
    }

    @EventTarget
    public void onEvent(UpdateEvent event) {
        this.showValue = this.delay;
        this.setColor(-16728065);
        this.clearLists();
        this.addCloseList();
        this.changeArmor();
    }

    private void changeArmor() {
        String[] armorType = new String[]{"item.boots", "item.leggings", "item.chestplate", "item.helmet"};
        int i = 0;
        while (i < 4) {
            int bestArmor;
            if ((this.insant.getValueState().booleanValue() || this.timeHelper.isDelayComplete(this.delay.getValueState().longValue())) && (bestArmor = this.getBestArmor(armorType[i])) != -1) {
                if (this.mc.thePlayer.inventory.armorInventory[i] == null) {
                    if (!this.openInv.getValueState().booleanValue() || this.mc.currentScreen instanceof GuiInventory) {
                        this.mc.playerController.windowClick(0, bestArmor, 0, 1, this.mc.thePlayer);
                        this.timeHelper.reset();
                    }
                } else {
                    Item currentArmor;
                    Item pBestArmor = this.getInventoryItem(bestArmor);
                    if (this.isBetter(pBestArmor, currentArmor = this.mc.thePlayer.inventory.armorInventory[i].getItem()) && (!this.openInv.getValueState().booleanValue() || this.mc.currentScreen instanceof GuiInventory)) {
                        this.mc.playerController.windowClick(0, 8 - i, 0, 1, this.mc.thePlayer);
                        this.timeHelper.reset();
                    }
                }
            }
            ++i;
        }
    }

    private int getBestArmor(String armorType) {
        int bestArmorId = -1;
        ItemArmor bestArmor = null;
        int id = 9;
        while (id < 45) {
            Slot currentSlot = this.mc.thePlayer.inventoryContainer.getSlot(id);
            if (currentSlot.getHasStack()) {
                ItemStack currentItem = currentSlot.getStack();
                this.addOpenList(currentItem);
                if (this.closeList.contains(currentItem) && currentItem.getItem().getUnlocalizedName().startsWith(armorType)) {
                    ItemArmor armor = (ItemArmor)currentItem.getItem();
                    if (bestArmor != null) {
                        if (this.isBetter(armor, bestArmor)) {
                            bestArmor = armor;
                            bestArmorId = id;
                        }
                    } else {
                        bestArmor = armor;
                        bestArmorId = id;
                    }
                }
            }
            ++id;
        }
        return bestArmorId;
    }

    private boolean isBetter(Item item1, Item item2) {
        if (item1 instanceof ItemArmor && item2 instanceof ItemArmor) {
            ItemArmor armor1 = (ItemArmor)item1;
            ItemArmor armor2 = (ItemArmor)item2;
            if (armor1.damageReduceAmount > armor2.damageReduceAmount) {
                return true;
            }
            return false;
        }
        return false;
    }

    private Item getInventoryItem(int id) {
        if (this.mc.thePlayer.inventoryContainer.getSlot(id).getStack() == null) {
            return null;
        }
        return this.mc.thePlayer.inventoryContainer.getSlot(id).getStack().getItem();
    }

    private void addOpenList(ItemStack item) {
        if (!(item.getItem() instanceof ItemArmor)) {
            return;
        }
        if (!this.openList.contains(item)) {
            this.openList.add(item);
            for (ItemStack st : this.openList) {
                if (st != item) continue;
                item.timer.reset();
            }
        }
    }

    private void addCloseList() {
        for (ItemStack st : this.openList) {
            if (!st.timer.isDelayComplete(this.inventoryKeepTimeVal.getValueState().intValue())) continue;
            if (!this.closeList.contains(st)) {
                this.closeList.add(st);
            }
            this.openList.remove(st);
        }
    }

    private void clearLists() {
        for (ItemStack st : this.closeList) {
            ItemStack stack = null;
            InventoryPlayer invp = this.mc.thePlayer.inventory;
            int i = 0;
            while (i < 45) {
                ItemStack itemStack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (itemStack != null && st == itemStack) {
                    stack = st;
                }
                ++i;
            }
            if (stack != null) continue;
            this.closeList.remove(st);
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("AutoArmor Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("AutoArmor Enable", ClientNotification.Type.SUCCESS);
    }
}

