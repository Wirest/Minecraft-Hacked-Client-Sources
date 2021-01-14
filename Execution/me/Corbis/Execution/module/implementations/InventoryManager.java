package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;

import java.util.concurrent.ThreadLocalRandom;

public class InventoryManager extends Module {
    TimeHelper timer = new TimeHelper();
    public InventoryManager(){
        super("InventoryManager", Keyboard.KEY_NONE, Category.PLAYER);
        Execution.instance.settingsManager.rSetting(new Setting("Delay", this, 30, 20, 1000, true));
        Execution.instance.settingsManager.rSetting(new Setting("SwordSlot", this, 1, 1, 9, true));
        Execution.instance.settingsManager.rSetting(new Setting("Clean", this, true));
        Execution.instance.settingsManager.rSetting(new Setting("CleanBadItems", this, true));
    }

    @EventTarget
    public void onMotionUpdate(EventMotionUpdate event){

        if(Execution.instance.moduleManager.getModule(Aura.class).target != null || Execution.instance.moduleManager.getModule(Stealer.class).silentCurrent != null) return;
        double delay = Math.max(20, Execution.instance.settingsManager.getSettingByName("Delay").getValDouble() + ThreadLocalRandom.current().nextDouble(-20, 20));
        if (mc.currentScreen != null) {
            timer.reset();
            return;
        }
        if (timer.hasReached((long) delay)) {
            int bestSword = this.getBestSword();
            int bestPick = this.getBestPickaxe();
            int bestAxe = this.getBestAxe();
            int bestShovel = this.getBestShovel();

            for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; k++) {
                ItemStack is = mc.thePlayer.inventory.mainInventory[k];
                if (is != null && !(is.getItem() instanceof ItemArmor)) {
                    boolean clean = Execution.instance.settingsManager.getSettingByName("Clean").getValBoolean();
                    if (clean) {
                        if (is.getItem() instanceof ItemSword) {
                            if (bestSword != -1 && bestSword != k) {
                                this.drop(k, is);
                                timer.reset();
                                return;
                            }
                        }
                        if (is.getItem() instanceof ItemPickaxe) {
                            if (bestPick != -1 && bestPick != k) {
                                this.drop(k, is);
                                timer.reset();
                                return;
                            }
                        }

                        if (is.getItem() instanceof ItemAxe) {
                            if (bestAxe != -1 && bestAxe != k) {
                                this.drop(k, is);
                                timer.reset();
                                return;
                            }
                        }

                        if (this.isShovel(is.getItem())) {
                            if (bestShovel != -1 && bestShovel != k) {
                                this.drop(k, is);
                                timer.reset();
                                return;
                            }
                        }
                    }
                    int swordSlot = (int) (Execution.instance.settingsManager.getSettingByName("SwordSlot").getValDouble() - 1);
                    if (bestSword != -1 && bestSword != swordSlot) {
                        for (int i = 0; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
                            Slot s = mc.thePlayer.inventoryContainer.inventorySlots.get(i);
                            if (s.getHasStack() && s.getStack() == mc.thePlayer.inventory.mainInventory[bestSword]) {
                                mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, s.slotNumber, swordSlot, 2, mc.thePlayer);
                                timer.reset();
                                return;
                            }
                        }
                    }
                    if (Execution.instance.settingsManager.getSettingByName("CleanBadItems").getValBoolean() && this.isBad(is.getItem())) {
                        this.drop(k, is);
                        timer.reset();
                        return;
                    }
                }
            }
            timer.reset();
        }
    }

    private int getBestSword() {
        int bestSword = -1;
        float bestDamage = 1F;

        for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; k++) {
            ItemStack is = mc.thePlayer.inventory.mainInventory[k];
            if (is != null && is.getItem() instanceof ItemSword) {
                ItemSword itemSword = (ItemSword) is.getItem();
                float damage = itemSword.getDamageVsEntity();
                damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, is);
                if (damage > bestDamage) {
                    bestDamage = damage;
                    bestSword = k;
                }
            }
        }
        return bestSword;
    }

    private int getBestPickaxe() {
        int bestPick = -1;
        float bestDamage = 1F;

        for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; k++) {
            ItemStack is = mc.thePlayer.inventory.mainInventory[k];
            if (is != null && is.getItem() instanceof ItemPickaxe) {
                ItemPickaxe itemSword = (ItemPickaxe) is.getItem();
                float damage = itemSword.getStrVsBlock(is, Block.getBlockById(4));
                if (damage > bestDamage) {
                    bestDamage = damage;
                    bestPick = k;
                }
            }
        }
        return bestPick;
    }

    private int getBestAxe() {
        int bestPick = -1;
        float bestDamage = 1F;

        for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; k++) {
            ItemStack is = mc.thePlayer.inventory.mainInventory[k];
            if (is != null && is.getItem() instanceof ItemAxe) {
                ItemAxe itemSword = (ItemAxe) is.getItem();
                float damage = itemSword.getStrVsBlock(is, Block.getBlockById(17));
                if (damage > bestDamage) {
                    bestDamage = damage;
                    bestPick = k;
                }
            }
        }
        return bestPick;
    }

    private int getBestShovel() {
        int bestPick = -1;
        float bestDamage = 1F;

        for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; k++) {
            ItemStack is = mc.thePlayer.inventory.mainInventory[k];
            if (is != null && this.isShovel(is.getItem())) {
                ItemTool itemSword = (ItemTool) is.getItem();
                float damage = itemSword.getStrVsBlock(is, Block.getBlockById(3));
                if (damage > bestDamage) {
                    bestDamage = damage;
                    bestPick = k;
                }
            }
        }
        return bestPick;
    }

    private boolean isShovel(Item is) {
        return Item.getItemById(256) == is || Item.getItemById(269) == is || Item.getItemById(273) == is || Item.getItemById(277) == is || Item.getItemById(284) == is;
    }

    private void drop(int slot, ItemStack item) {
        boolean hotbar = false;
        for (int k = 0; k < 9; k++) {
            ItemStack itemK = mc.thePlayer.inventory.getStackInSlot(k);
            if (itemK != null && itemK == item) {
                hotbar = true;
                continue;
            }
        }

        if (hotbar) {
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
            C07PacketPlayerDigging.Action diggingAction = C07PacketPlayerDigging.Action.DROP_ALL_ITEMS;
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(diggingAction, BlockPos.ORIGIN, EnumFacing.DOWN));
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        } else {
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 0, mc.thePlayer);
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, -999, 0, 0, mc.thePlayer);
        }
    }

    private boolean isBad(Item i) {
        return i.getUnlocalizedName().contains("tnt") ||
                i.getUnlocalizedName().contains("stick") ||
                i.getUnlocalizedName().contains("egg") ||
                i.getUnlocalizedName().contains("string") ||
                i.getUnlocalizedName().contains("flint") ||
                i.getUnlocalizedName().contains("bow") ||
                i.getUnlocalizedName().contains("arrow") ||
                i.getUnlocalizedName().contains("bucket") ||
                i.getUnlocalizedName().contains("feather") ||
                i.getUnlocalizedName().contains("snow") ||
                i.getUnlocalizedName().contains("piston") ||
                i instanceof ItemGlassBottle ||
                i.getUnlocalizedName().contains("web") ||
                i.getUnlocalizedName().contains("slime") ||
                i.getUnlocalizedName().contains("trip") ||
                i.getUnlocalizedName().contains("wire") ||
                i.getUnlocalizedName().contains("sugar") ||
                i.getUnlocalizedName().contains("note") ||
                i.getUnlocalizedName().contains("record") ||
                i.getUnlocalizedName().contains("flower") ||
                i.getUnlocalizedName().contains("wheat") ||
                i.getUnlocalizedName().contains("fishing") ||
                i.getUnlocalizedName().contains("boat") ||
                i.getUnlocalizedName().contains("leather") ||
                i.getUnlocalizedName().contains("seeds") ||
                i.getUnlocalizedName().contains("skull") ||
                i.getUnlocalizedName().contains("torch") ||
                i.getUnlocalizedName().contains("anvil") ||
                i.getUnlocalizedName().contains("enchant") ||
                i.getUnlocalizedName().contains("exp") ||
                i.getUnlocalizedName().contains("shears");
    }

    

}
