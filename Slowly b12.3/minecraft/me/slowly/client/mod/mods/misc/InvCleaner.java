/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.misc;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Random;
import me.slowly.client.events.EventPreMotion;
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
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class InvCleaner
extends Mod {
    private TimeHelper timer = new TimeHelper();
    private Value<Boolean> openInv = new Value<Boolean>("InvCleaner_OnlyInv", false);
    private Value<Double> delay = new Value<Double>("InvCleaner_Delay", 60.0, 0.0, 1000.0, 10.0);
    private Value<Boolean> insant = new Value<Boolean>("InvCleaner_Insant", false);
    private Value<Boolean> throwAxe = new Value<Boolean>("InvCleaner_ThrowAxe", true);

    public InvCleaner() {
        super("InvCleaner", Mod.Category.MISCELLANEOUS, Colors.DARKMAGENTA.c);
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("InvCleaner Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("InvCleaner Enable", ClientNotification.Type.SUCCESS);
    }
    @EventTarget
    public void onPre(EventPreMotion event) {
        this.setColor(Colors.DARKBLUE.c);
        this.showValue = this.delay;
        String[] armorType = new String[]{"item.boots", "item.leggings", "item.chestplate", "item.helmet"};
        ArrayList<BestItem> blackList = new ArrayList<BestItem>();
        int i = 0;
        while (i < armorType.length) {
            blackList.add(this.getBestArmor(armorType[i], blackList));
            ++i;
        }
        BestItem bestWeapon = this.getBestWeapon();
        if (bestWeapon != null) {
            blackList.add(bestWeapon);
        }
        blackList.add(this.removeDouble(261));
        blackList.add(this.removeDouble(346));
        this.throwItems(blackList);
        this.sortItems(blackList);
    }

    public BestItem removeDouble(int itemId) {
        Slot currentSlot;
        ItemStack currentItem;
        int id = 36;
        while (id <= 44) {
            currentSlot = this.mc.thePlayer.inventoryContainer.getSlot(id);
            if (currentSlot.getHasStack() && Item.getIdFromItem((currentItem = currentSlot.getStack()).getItem()) == itemId && Item.getIdFromItem(currentItem.getItem()) == itemId) {
                return new BestItem(this, currentItem, id);
            }
            ++id;
        }
        id = 35;
        while (id >= 9) {
            currentSlot = this.mc.thePlayer.inventoryContainer.getSlot(id);
            if (currentSlot.getHasStack() && Item.getIdFromItem((currentItem = currentSlot.getStack()).getItem()) == itemId && Item.getIdFromItem(currentItem.getItem()) == itemId) {
                return new BestItem(this, currentItem, id);
            }
            --id;
        }
        return null;
    }

    private void throwItem(int id) {
        if ((this.mc.currentScreen instanceof GuiInventory || !this.openInv.getValueState().booleanValue()) && (this.timer.isDelayComplete(this.delay.getValueState().intValue() + new Random().nextInt(20)) || this.insant.getValueState().booleanValue())) {
            this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, id, this.mc.thePlayer.inventory.currentItem, 4, this.mc.thePlayer);
            this.timer.reset();
        }
    }

    private void changeSlot(int id) {
        if ((this.mc.currentScreen instanceof GuiInventory || !this.openInv.getValueState().booleanValue()) && (this.timer.isDelayComplete(this.delay.getValueState().intValue() + new Random().nextInt(20)) || this.insant.getValueState().booleanValue())) {
            this.mc.playerController.windowClick(0, id, 0, 1, this.mc.thePlayer);
            this.timer.reset();
        }
    }

    private void sort(int slotId, int itemId) {
        Slot currentSlot = this.mc.thePlayer.inventoryContainer.getSlot(slotId);
        if (currentSlot.getHasStack()) {
            this.changeSlot(slotId);
        }
        this.changeSlot(itemId);
    }

    private void sortItems(ArrayList<BestItem> blackList) {
        int weaponSlot = 36;
        int bowSlot = 37;
        int fishingRodSlot = 38;
        for (BestItem item : blackList) {
            if (item == null) continue;
            if ((item.getItem().getItem() instanceof ItemSword || item.getItem().getItem() instanceof ItemAxe && this.throwAxe.getValueState().booleanValue()) && item.invId != weaponSlot) {
                this.sort(weaponSlot, item.invId);
            }
            if (item.getItem().getItem() instanceof ItemBow && (item.invId < weaponSlot || item.invId > bowSlot)) {
                this.sort(bowSlot, item.invId);
            }
            if (!(item.getItem().getItem() instanceof ItemFishingRod) || item.invId >= weaponSlot && item.invId <= fishingRodSlot) continue;
            this.sort(fishingRodSlot, item.invId);
        }
        int nextSlot = this.getNextFreeSlot();
        if (nextSlot != -1) {
            int id = 9;
            while (id < 35) {
                Slot currentSlot = this.mc.thePlayer.inventoryContainer.getSlot(id);
                if (currentSlot.getHasStack() && currentSlot.getStack().getItem() instanceof ItemFood) {
                    this.sort(nextSlot, id);
                }
                ++id;
            }
        }
    }

    private int getNextFreeSlot() {
        int id = 36;
        while (id < 45) {
            Slot currentSlot = this.mc.thePlayer.inventoryContainer.getSlot(id);
            if (!currentSlot.getHasStack()) {
                return id;
            }
            ++id;
        }
        return -1;
    }

    private void throwItems(ArrayList<BestItem> blackList) {
        int id = 9;
        while (id < 45) {
            ItemStack currentItem;
            Slot currentSlot = this.mc.thePlayer.inventoryContainer.getSlot(id);
            if (currentSlot.getHasStack() && !this.isInBlackList(blackList, id) && this.isValid((currentItem = currentSlot.getStack()).getItem())) {
                this.throwItem(id);
            }
            ++id;
        }
    }

    private boolean isValid(Item item) {
        if (!(item instanceof ItemSword || item instanceof ItemAxe && this.throwAxe.getValueState().booleanValue() || item instanceof ItemArmor || item instanceof ItemBow || item instanceof ItemFishingRod)) {
            return false;
        }
        return true;
    }

    private boolean isInBlackList(ArrayList<BestItem> blackList, int id) {
        for (BestItem item : blackList) {
            if (item == null || item.invId != id) continue;
            return true;
        }
        return false;
    }

    private BestItem getBestWeapon() {
        ItemAxe axe;
        ItemSword sword;
        ItemStack currentItem;
        float weaponDmg;
        Slot currentSlot;
        ItemStack bestWeapon = null;
        int invId = -1;
        float weaponDamage = -1.0f;
        int id = 36;
        while (id <= 44) {
            currentSlot = this.mc.thePlayer.inventoryContainer.getSlot(id);
            if (currentSlot.getHasStack() && ((currentItem = currentSlot.getStack()).getItem() instanceof ItemAxe && this.throwAxe.getValueState().booleanValue() || currentItem.getItem() instanceof ItemSword)) {
                if (bestWeapon == null) {
                    bestWeapon = currentItem;
                    invId = id;
                    if (currentItem.getItem() instanceof ItemSword) {
                        sword = (ItemSword)currentItem.getItem();
                        weaponDamage = sword.getDamageVsEntity() + 4.0f;
                    } else {
                        axe = (ItemAxe)currentItem.getItem();
                        weaponDamage = axe.getDamageVsEntity() + 3.0f;
                    }
                } else if (currentItem.getItem() instanceof ItemSword) {
                    sword = (ItemSword)currentItem.getItem();
                    weaponDmg = sword.getDamageVsEntity() + 4.0f;
                    if (weaponDmg > weaponDamage) {
                        bestWeapon = currentItem;
                        invId = id;
                        weaponDamage = weaponDmg;
                    }
                } else {
                    axe = (ItemAxe)currentItem.getItem();
                    weaponDmg = axe.getDamageVsEntity() + 3.0f;
                    if (weaponDmg > weaponDamage) {
                        bestWeapon = currentItem;
                        invId = id;
                        weaponDamage = weaponDmg;
                    }
                }
            }
            ++id;
        }
        id = 35;
        while (id >= 9) {
            currentSlot = this.mc.thePlayer.inventoryContainer.getSlot(id);
            if (currentSlot.getHasStack() && ((currentItem = currentSlot.getStack()).getItem() instanceof ItemAxe && this.throwAxe.getValueState().booleanValue() || currentItem.getItem() instanceof ItemSword)) {
                if (bestWeapon == null) {
                    bestWeapon = currentItem;
                    invId = id;
                    if (currentItem.getItem() instanceof ItemSword) {
                        sword = (ItemSword)currentItem.getItem();
                        weaponDamage = sword.getDamageVsEntity() + 4.0f;
                    } else {
                        axe = (ItemAxe)currentItem.getItem();
                        weaponDamage = axe.getDamageVsEntity() + 3.0f;
                    }
                } else if (currentItem.getItem() instanceof ItemSword) {
                    sword = (ItemSword)currentItem.getItem();
                    weaponDmg = sword.getDamageVsEntity() + 4.0f;
                    if (weaponDmg > weaponDamage) {
                        bestWeapon = currentItem;
                        invId = id;
                        weaponDamage = weaponDmg;
                    }
                } else {
                    axe = (ItemAxe)currentItem.getItem();
                    weaponDmg = axe.getDamageVsEntity() + 3.0f;
                    if (weaponDmg > weaponDamage) {
                        bestWeapon = currentItem;
                        invId = id;
                        weaponDamage = weaponDmg;
                    }
                }
            }
            --id;
        }
        if (bestWeapon != null) {
            return new BestItem(this, bestWeapon, invId);
        }
        return null;
    }

    private BestItem getBestArmor(String armorType, ArrayList<BestItem> blackList) {
        ItemStack bestArmor = null;
        int invId = -1;
        int armorStrength = Integer.MIN_VALUE;
        int id = 0;
        while (id < 45) {
            ItemArmor armor;
            ItemStack currentItem;
            Slot currentSlot = this.mc.thePlayer.inventoryContainer.getSlot(id);
            if (currentSlot.getHasStack() && (currentItem = currentSlot.getStack()).getItem() instanceof ItemArmor && (armor = (ItemArmor)currentItem.getItem()).getUnlocalizedName().startsWith(armorType)) {
                if (bestArmor == null) {
                    bestArmor = currentItem;
                    invId = id;
                    armorStrength = armor.damageReduceAmount;
                } else {
                    ItemArmor currentArmor = (ItemArmor)bestArmor.getItem();
                    if (armor.damageReduceAmount > currentArmor.damageReduceAmount) {
                        bestArmor = currentItem;
                        invId = id;
                        armorStrength = armor.damageReduceAmount;
                    }
                }
            }
            ++id;
        }
        if (bestArmor != null && invId != -1) {
            return new BestItem(this, bestArmor, invId);
        }
        return null;
    }

    private class BestItem {
        private ItemStack item;
        private int invId;
        final /* synthetic */ InvCleaner this$0;

        private BestItem(InvCleaner invCleaner, ItemStack item, int invId) {
            this.this$0 = invCleaner;
            this.item = item;
            this.invId = invId;
        }

        public ItemStack getItem() {
            return this.item;
        }

        public void setItem(ItemStack item) {
            this.item = item;
        }

        public int getInvId() {
            return this.invId;
        }

        public void setInvId(int invId) {
            this.invId = invId;
        }


        }
    }



