/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.combat;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPostMotionUpdates;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Timer;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;

@Module.Mod(category=Module.Category.COMBAT, description="Uses potions when your health is low", key=0, name="AutoPot")
public class AutoPot
extends Module {
    public static Value health = new Value("autopot_Health", Float.valueOf(10.0f), Float.valueOf(5.0f), Float.valueOf(19.0f), Float.valueOf(0.1f));
    public static Value delay1 = new Value("autopot_Delay", Float.valueOf(250.0f), Float.valueOf(50.0f), Float.valueOf(500.0f), Float.valueOf(1.0f));
    public static ItemStack held;
    Timer timer = new Timer();

    @EventTarget
    public void onPre(EventPreMotionUpdates event) {
        this.timer.update();
        if (this.timer.hasPassed(AutoPot.delay1.value.longValue()) && this.isHealing()) {
            event.pitch = 90.0f;
            if (!this.isPotionOnHotBar()) {
                this.getPotFromInv();
            }
            if (held == null) {
                held = Wrapper.getPlayer().getHeldItemMainhand();
            }
            Wrapper.sendPacket(new CPacketPlayer.Rotation(Wrapper.getPlayer().rotationYaw, 90.0f, Wrapper.getPlayer().onGround));
            int potion = this.getPotionSlot();
            int saved = Wrapper.getPlayer().inventory.currentItem;
            Wrapper.swap(potion, saved);
            Wrapper.getPlayerController().updateController();
            Wrapper.sendPacket(new CPacketHeldItemChange(potion));
            Wrapper.getPlayerController().updateController();
            Wrapper.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            Wrapper.sendPacket(new CPacketHeldItemChange(saved));
            Wrapper.getPlayerController().updateController();
            Wrapper.sendPacket(new CPacketPlayer.Rotation(Wrapper.getPlayer().rotationYaw, Wrapper.getPlayer().rotationPitch, Wrapper.getPlayer().onGround));
            if (held != null) {
                int index = 9;
                while (index < 36) {
                    ItemStack stack = Wrapper.getPlayer().inventoryContainer.getSlot(index).getStack();
                    if (stack != null && stack.equals(held)) {
                        Wrapper.swap(index, Wrapper.getPlayer().inventory.currentItem);
                        Wrapper.getPlayerController().updateController();
                        break;
                    }
                    ++index;
                }
                held = null;
            }
            this.timer.reset();
        }
    }

    @EventTarget
    public void onPost(EventPostMotionUpdates event) {
        if (this.isHealing()) {
            this.timer.hasPassed(AutoPot.delay1.value.longValue());
        }
    }

    public boolean isPotionOnHotBar() {
        int i = 0;
        while (i < 9) {
            if (this.isStackPotion(Wrapper.getPlayer().inventory.getStackInSlot(i), i)) {
                return true;
            }
            ++i;
        }
        return false;
    }

    private void getPotFromInv() {
        int saved = Wrapper.getPlayer().inventory.currentItem;
        if (!(Wrapper.mc().currentScreen instanceof GuiChest)) {
            if (held == null) {
                held = Wrapper.getPlayer().getHeldItemMainhand();
            }
            int index = 9;
            while (index < 36) {
                ItemStack stack = Wrapper.getPlayer().inventoryContainer.getSlot(index).getStack();
                if (this.ishotbarFull()) {
                    Wrapper.getPlayer().inventory.currentItem = 8;
                    Wrapper.mc().playerController.updateController();
                    if (Wrapper.getPlayer().inventory.currentItem == 8) {
                        Wrapper.getPlayer().dropItem(true);
                        Wrapper.getPlayer().inventory.currentItem = saved;
                    }
                }
                if (stack != null && this.isStackPotion(stack, index)) {
                    Wrapper.swap(index, Wrapper.getPlayer().inventory.currentItem);
                    break;
                }
                ++index;
            }
        }
    }

    public boolean ishotbarFull() {
        int i = 0;
        while (i < 9) {
            if (Wrapper.getPlayer().inventory.getStackInSlot(i) == null) {
                return false;
            }
            ++i;
        }
        return true;
    }

    public int getPotionSlot() {
        int i = 0;
        while (i < 9) {
            if (this.isStackPotion(Wrapper.getPlayer().inventory.getStackInSlot(i), i)) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    public boolean isHealing() {
        if (this.isToggled() && Wrapper.getPlayer().getHealth() <= AutoPot.health.value.floatValue() && this.getTotalPots() > 0) {
            return true;
        }
        return false;
    }

    public int getTotalPots() {
        int potions = 0;
        int index = 9;
        while (index < 45) {
            ItemStack stack = Wrapper.getPlayer().inventoryContainer.getSlot(index).getStack();
            if (this.isStackPotion(stack, index)) {
                potions += stack.stackSize;
            }
            ++index;
        }
        return potions;
    }

    private boolean isStackPotion(ItemStack stack, int index) {
        if (stack == null) {
            return false;
        }
        Item item = stack.getItem();
        if (stack != null && stack.getItem() == Items.SPLASH_POTION) {
            for (PotionEffect effect : PotionUtils.getEffectsFromStack(stack)) {
                if (effect.getPotion() != MobEffects.INSTANT_HEALTH) continue;
                return true;
            }
        }
        return false;
    }
}

