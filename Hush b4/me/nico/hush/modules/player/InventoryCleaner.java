// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.player;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemSword;
import java.util.Iterator;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.item.ItemPotion;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import me.nico.hush.events.EventUpdate;
import me.nico.hush.modules.Category;
import me.nico.hush.utils.TimeHelper;
import me.nico.hush.modules.Module;

public class InventoryCleaner extends Module
{
    public TimeHelper time;
    private int currentSlot;
    private double handitemAttackValue;
    
    public InventoryCleaner() {
        super("InvCleaner", "InvCleaner", 16777215, 0, Category.PLAYER);
        this.time = new TimeHelper();
        this.currentSlot = 9;
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (this.isEnabled() && InventoryCleaner.mc.currentScreen instanceof GuiInventory) {
            if (this.currentSlot >= 45) {
                this.toggled = false;
                this.toggle();
            }
            else if (TimeHelper.hasReached(130L)) {
                final Minecraft mc = InventoryCleaner.mc;
                this.handitemAttackValue = getAttackDamage(Minecraft.thePlayer.getHeldItem());
                final Minecraft mc2 = InventoryCleaner.mc;
                final ItemStack itemStack = Minecraft.thePlayer.inventoryContainer.getSlot(this.currentSlot).getStack();
                if (isShit(itemStack) && getAttackDamage(itemStack) <= this.handitemAttackValue) {
                    final ItemStack itemStack2 = itemStack;
                    final Minecraft mc3 = InventoryCleaner.mc;
                    if (itemStack2 != Minecraft.thePlayer.getHeldItem()) {
                        final PlayerControllerMP playerController = InventoryCleaner.mc.playerController;
                        final int windowId = 0;
                        final int currentSlot = this.currentSlot;
                        final int mouseButtonClicked = 1;
                        final int mode = 4;
                        final Minecraft mc4 = InventoryCleaner.mc;
                        playerController.windowClick(windowId, currentSlot, mouseButtonClicked, mode, Minecraft.thePlayer);
                        TimeHelper.reset();
                    }
                }
                ++this.currentSlot;
            }
        }
    }
    
    public static boolean isShit(final ItemStack itemStack) {
        return itemStack.getItem().getUnlocalizedName().contains("flower pot") || itemStack.getItem().getUnlocalizedName().contains("string") || itemStack.getItem().getUnlocalizedName().contains("flint") || itemStack.getItem().getUnlocalizedName().contains("feather") || itemStack.getItem().getUnlocalizedName().contains("chest") || itemStack.getItem().getUnlocalizedName().contains("snow") || itemStack.getItem().getUnlocalizedName().contains("fishing") || itemStack.getItem().getUnlocalizedName().contains("slime") || itemStack.getItem().getUnlocalizedName().contains("ice") || itemStack.getItem().getUnlocalizedName().contains("lapis") || itemStack.getItem().getUnlocalizedName().contains("magma") || (itemStack.getItem().getUnlocalizedName().contains("potion") && isBadPotion(itemStack));
    }
    
    public static boolean isBadPotion(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion)stack.getItem();
            for (final Object o : potion.getEffects(stack)) {
                final PotionEffect effect = (PotionEffect)o;
                if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.harm.getId()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static double getAttackDamage(final ItemStack itemStack) {
        if (itemStack != null && itemStack.getItem() instanceof ItemSword) {
            final ItemSword sword = (ItemSword)itemStack.getItem();
            return EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) + sword.getAttackDamage();
        }
        return 0.0;
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
        this.currentSlot = 9;
        final Minecraft mc = InventoryCleaner.mc;
        this.handitemAttackValue = getAttackDamage(Minecraft.thePlayer.getHeldItem());
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
}
