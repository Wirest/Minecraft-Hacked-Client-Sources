package cn.kody.debug.mod.mods.PLAYER;

import cn.kody.debug.events.EventUpdate;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.utils.time.TimeHelper;
import cn.kody.debug.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;

public class AutoArmor
extends Mod {
    private TimeHelper timeHelper = new TimeHelper();
    public TimeHelper timer = new TimeHelper();
    public TimeHelper droptimer = new TimeHelper();
    private Value openInv = new Value<Boolean>("AutoArmor_SortInInv", false);
    private Value delay = new Value<Double>("AutoArmor_Delay", 60.0, 0.0, 1000.0, 10.0);
    private Value inventoryKeepTimeVal = new Value<Double>("AutoArmor_TimeInInv", 1000.0, 0.0, 10000.0, 100.0);
    private Value insant = new Value<Boolean>("AutoArmor_Insant", false);
    private ArrayList<ItemStack> openList = new ArrayList();
    private ArrayList<ItemStack> closeList = new ArrayList();
    public static boolean complete;
    private static boolean openedInventory;
    private int[] itemHelmet = new int[]{298, 302, 306, 310, 314};
    private int[] itemChestplate = new int[]{299, 303, 307, 311, 315};
    private int[] itemLeggings = new int[]{300, 304, 308, 312, 316};
    private int[] itemBoots = new int[]{301, 305, 309, 313, 317};
    private HashMap armorContains = new HashMap();

    public AutoArmor() {
        super("AutoArmor", "Auto Armor", Category.PLAYER);
    }

    @EventTarget
    public void onEvent(EventUpdate event) {
        this.clearLists();
        this.addCloseList();
        this.changeArmor();
    }
    
//    @EventTarget
//    public void onEvent(EventUpdate event) {
//        if (Minecraft.thePlayer == null) {
//            Minecraft.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(Minecraft.thePlayer.inventoryContainer.windowId));
//            return;
//        }
//        if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiInventory)) {
//            if (AutoArmor.openedInventory == false) return;
//            Minecraft.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(Minecraft.thePlayer.inventoryContainer.windowId));
//            AutoArmor.openedInventory = false;
//            return;
//        }
//        wearArmor = (Boolean)this.openInv.getValueState() != false && this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiInventory != false || (Boolean)this.openInv.getValueState() == false;
//        finished = true;
//        var5 = this.getArmors().iterator();
//        {
//            block12 : {
//                block11 : {
//                    if (!var5.hasNext()) {
//                        if (finished == false) return;
//                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(Minecraft.thePlayer.inventoryContainer.windowId));
//                        AutoArmor.openedInventory = false;
//                        return;
//                    }
//                    armorNames = (String)var5.next();
//                    finished = false;
//                    currentSlot = this.getSlotByName(armorNames);
//                    bestArmorSlot = this.getBestInInventory(armorNames);
//                    shouldAdd = true;
//                    if (bestArmorSlot == -1) break block11;
//                    v0 = shouldAdd = this.getValence(Minecraft.thePlayer.inventoryContainer.getSlot(bestArmorSlot).getStack()) > this.getValence(Minecraft.thePlayer.inventoryContainer.getSlot(currentSlot).getStack());
//                    if (!shouldAdd) break block12;
//                }
//                if (bestArmorSlot != -1 && !this.armorContains.containsKey(Minecraft.thePlayer.inventoryContainer.getSlot(bestArmorSlot).getStack())) {
//                    this.armorContains.put(Minecraft.thePlayer.inventoryContainer.getSlot(bestArmorSlot).getStack(), System.currentTimeMillis());
//                }
//            }
//            if (!wearArmor) 
//            if (bestArmorSlot != -1 && Minecraft.thePlayer.inventoryContainer.getSlot(currentSlot).getHasStack() && this.getValence(Minecraft.thePlayer.inventoryContainer.getSlot(bestArmorSlot).getStack()) < this.getValence(Minecraft.thePlayer.inventoryContainer.getSlot(currentSlot).getStack())) {
//                bestArmorSlot = -1;
//            }
//            if (this.timer.isDelayComplete(((Double)this.delay.getValueState()).longValue()) && bestArmorSlot != -1 && this.armorContains.containsKey(Minecraft.thePlayer.inventoryContainer.getSlot(bestArmorSlot).getStack()) && System.currentTimeMillis() - (Long)this.armorContains.get(Minecraft.thePlayer.inventoryContainer.getSlot(bestArmorSlot).getStack()) >= 200L) {
//                this.putOnItem(currentSlot, bestArmorSlot);
//                this.armorContains.remove(Minecraft.thePlayer.inventoryContainer.getSlot(bestArmorSlot).getStack());
//                this.timer.reset();
//            }
//            var10 = this.findArmor(armorNames).iterator();
//            do {
//                if (!var10.hasNext()) continue block0;
//                anotherArmors = (Integer)var10.next();
//                isOldBetter = false;
//                if (currentSlot == -1 || !(isOldBetter = this.getValence(Minecraft.thePlayer.inventoryContainer.getSlot(currentSlot).getStack()) >= this.getValence(Minecraft.thePlayer.inventoryContainer.getSlot(anotherArmors).getStack()))) continue;
//                finished = false;
//                if (!this.droptimer.isDelayComplete(300L)) continue;
//                this.droptimer.reset();
//            } while (true);
//            break;
//        } while (true);
//    }

    private void changeArmor() {
        String[] armorType = new String[]{"boots", "leggings", "chestplate", "helmet"};
        for (int i = 0; i < 4; ++i) {
            if (!((Boolean)this.insant.getValueState()).booleanValue() && !this.timeHelper.isDelayComplete(((Double)this.delay.getValueState()).longValue())) continue;
            int bestArmor = this.getBestArmor(armorType[i]);
            if (bestArmor != -1) {
                Item currentArmor;
                complete = false;
                if (Minecraft.thePlayer.inventory.armorInventory[i] == null) {
                    Minecraft.playerController.windowClick(0, bestArmor, 0, 1, Minecraft.thePlayer);
                    this.timeHelper.reset();
                    continue;
                }
                Item pBestArmor = this.getInventoryItem(bestArmor);
                if (!this.isBetter(pBestArmor, currentArmor = Minecraft.thePlayer.inventory.armorInventory[i].getItem())) continue;
                Minecraft.playerController.windowClick(0, 8 - i, 0, 1, Minecraft.thePlayer);
                this.timeHelper.reset();
                continue;
            }
            complete = true;
        }
    }

    private int getBestArmor(String armorType) {
        return this.getBestInInventory(armorType);
    }

    private boolean isBetter(Item item1, Item item2) {
        if (item1 instanceof ItemArmor && item2 instanceof ItemArmor) {
            ItemArmor armor1 = (ItemArmor)item1;
            ItemArmor armor2 = (ItemArmor)item2;
            return armor1.damageReduceAmount > armor2.damageReduceAmount;
        }
        return false;
    }

    private Item getInventoryItem(int id) {
        return Minecraft.thePlayer.inventoryContainer.getSlot(id).getStack() == null ? null : Minecraft.thePlayer.inventoryContainer.getSlot(id).getStack().getItem();
    }

    private void addOpenList(ItemStack item) {
        if (item.getItem() instanceof ItemArmor && !this.openList.contains(item)) {
            this.openList.add(item);
            for (ItemStack st : this.openList) {
                if (st != item) continue;
                this.timer.reset();
            }
        }
    }

    private void addCloseList() {
        for (ItemStack st : this.openList) {
            if (!this.timer.isDelayComplete(((Double)this.inventoryKeepTimeVal.getValueState()).intValue())) continue;
            if (!this.closeList.contains(st)) {
                this.closeList.add(st);
            }
            this.openList.remove(st);
        }
    }

    private void clearLists() {
        for (ItemStack st : this.closeList) {
            ItemStack stack = null;
            InventoryPlayer invp = Minecraft.thePlayer.inventory;
            for (int i = 0; i < 45; ++i) {
                ItemStack itemStack = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (itemStack == null || st != itemStack) continue;
                stack = st;
            }
            if (stack != null) continue;
            this.closeList.remove(st);
        }
    }

    private void putOnItem(int armorSlot, int slot) {
        if (armorSlot != -1 && Minecraft.thePlayer.inventoryContainer.getSlot(armorSlot).getStack() != null) {
            this.dropOldArmor(armorSlot);
        }
        this.inventoryAction(slot);
    }

    private void dropOldArmor(int slot) {
        Minecraft.thePlayer.inventoryContainer.slotClick(slot, 0, 4, Minecraft.thePlayer);
        Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, slot, 1, 4, Minecraft.thePlayer);
    }

    private void inventoryAction(int click) {
        Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, click, 1, 1, Minecraft.thePlayer);
    }

    private List getArmors() {
        return Arrays.asList("helmet", "leggings", "chestplate", "boots");
    }

    private int[] getIdsByName(String armorName) {
        switch (armorName.hashCode()) {
            case -1220934547: {
                if (!armorName.equals("helmet")) break;
                return this.itemHelmet;
            }
            case 93922241: {
                if (!armorName.equals("boots")) break;
                return this.itemBoots;
            }
            case 1069952181: {
                if (!armorName.equals("chestplate")) break;
                return this.itemChestplate;
            }
            case 1735676010: {
                if (!armorName.equals("leggings")) break;
                return this.itemLeggings;
            }
        }
        return new int[0];
    }

    private List findArmor(String armorName) {
        int[] itemIds = this.getIdsByName(armorName);
        ArrayList<Integer> availableSlots = new ArrayList<Integer>();
        for (int slots = 9; slots < Minecraft.thePlayer.inventoryContainer.getInventory().size(); ++slots) {
            ItemStack itemStack = Minecraft.thePlayer.inventoryContainer.getSlot(slots).getStack();
            if (itemStack == null) continue;
            int itemId = Item.getIdFromItem(itemStack.getItem());
            int[] array = itemIds;
            int length = itemIds.length;
            for (int i = 0; i < length; ++i) {
                int ids = array[i];
                if (itemId != ids) continue;
                availableSlots.add(slots);
            }
        }
        return availableSlots;
    }

    private int getBestInInventory(String armorName) {
        int slot = -1;
        Iterator var4 = this.findArmor(armorName).iterator();
        while (var4.hasNext()) {
            int slots = (Integer)var4.next();
            if (slot == -1) {
                slot = slots;
            }
            if (Minecraft.thePlayer.inventoryContainer.getSlot(slots) == null || !(Minecraft.thePlayer.inventoryContainer.getSlot(slots).getStack().getItem() instanceof ItemArmor) || this.getValence(Minecraft.thePlayer.inventoryContainer.getSlot(slots).getStack()) <= this.getValence(Minecraft.thePlayer.inventoryContainer.getSlot(slot).getStack())) continue;
            slot = slots;
        }
        return slot;
    }

    private int getSlotByName(String armorName) {
        int id = -1;
        switch (armorName.hashCode()) {
            case -1220934547: {
                if (!armorName.equals("helmet")) break;
                id = 5;
                break;
            }
            case 93922241: {
                if (!armorName.equals("boots")) break;
                id = 8;
                break;
            }
            case 1069952181: {
                if (!armorName.equals("chestplate")) break;
                id = 6;
                break;
            }
            case 1735676010: {
                if (!armorName.equals("leggings")) break;
                id = 7;
            }
        }
        return id;
    }

    private double getProtectionValue(ItemStack stack) {
        return !(stack.getItem() instanceof ItemArmor) ? 0.0 : (double)((ItemArmor)stack.getItem()).damageReduceAmount + (double)((100 - ((ItemArmor)stack.getItem()).damageReduceAmount * 4) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 4) * 0.0075 + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, stack) + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack) + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.respiration.effectId, stack) + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack);
    }

    private int getValence(ItemStack itemStack) {
        int valence = 0;
        if (itemStack == null) {
            return 0;
        }
        if (itemStack.getItem() instanceof ItemArmor) {
            valence += ((ItemArmor)itemStack.getItem()).damageReduceAmount;
        }
        if (itemStack != null && itemStack.hasTagCompound()) {
            valence += (int)itemStack.getEnchantmentTagList().getCompoundTagAt(0).getDouble("lvl");
            valence += (int)itemStack.getEnchantmentTagList().getCompoundTagAt(1).getDouble("lvl");
            valence += (int)itemStack.getEnchantmentTagList().getCompoundTagAt(2).getDouble("lvl");
            valence += (int)itemStack.getEnchantmentTagList().getCompoundTagAt(3).getDouble("lvl");
            valence += (int)itemStack.getEnchantmentTagList().getCompoundTagAt(4).getDouble("lvl");
            valence += (int)itemStack.getEnchantmentTagList().getCompoundTagAt(5).getDouble("lvl");
            valence += (int)itemStack.getEnchantmentTagList().getCompoundTagAt(6).getDouble("lvl");
            valence += (int)itemStack.getEnchantmentTagList().getCompoundTagAt(7).getDouble("lvl");
            valence += (int)itemStack.getEnchantmentTagList().getCompoundTagAt(8).getDouble("lvl");
            valence += (int)itemStack.getEnchantmentTagList().getCompoundTagAt(9).getDouble("lvl");
            valence += (int)itemStack.getEnchantmentTagList().getCompoundTagAt(34).getDouble("lvl");
        }
        valence += (int)this.getProtectionValue(itemStack);
        return valence += itemStack.getMaxDamage() - itemStack.getItemDamage();
    }
}

