/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.combat;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPacketSent;
import darkmagician6.events.EventPostMotionUpdates;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.module.ModuleManager;
import me.razerboy420.weepcraft.module.modules.misc.FlashBack;
import me.razerboy420.weepcraft.util.Timer;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;

@Module.Mod(category=Module.Category.COMBAT, description="Uses potions when your health is low", key=0, name="AutoPot")
public class NewPot
extends Module {
    public static Value health = new Value("autopot_Health", Float.valueOf(10.0f), Float.valueOf(5.0f), Float.valueOf(19.0f), Float.valueOf(0.1f));
    public static Value delay1 = new Value("autopot_Delay", Float.valueOf(250.0f), Float.valueOf(50.0f), Float.valueOf(500.0f), Float.valueOf(1.0f));
    public boolean isPotting;
    public boolean goagain;
    public int delay;
    public int ticks = 0;
    Timer timer = new Timer();
    ItemStack helditem;

    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        if (this.isHealing()) {
            EntityPlayerSP var10000 = Wrapper.getPlayer();
            var10000.rotationPitch += 9.0E-4f;
            this.isPotting = true;
            event.pitch = 90.0f;
        }
    }

    @EventTarget
    public void onPre(EventPostMotionUpdates event) {
        this.setDisplayName("AutoPot [" + this.getTotalPots() + "]");
        double saved = Wrapper.getPlayer().rotationPitch;
        if (this.delay > 0) {
            this.timer.update();
            if (this.timer.hasPassed(NewPot.delay1.value.longValue())) {
                this.delay = 0;
                this.timer.reset();
            }
        }
        if (this.getTotalPots() > 0 && this.isHealing() && this.delay == 0) {
            if (this.isPotionOnHotBar()) {
                int current = Wrapper.getPlayer().inventory.currentItem;
                int meme = this.getPotionSlot();
                ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(this.getPotionSlot());
                Wrapper.sendPacket(new CPacketHeldItemChange(this.getPotionSlot()));
                Wrapper.mc().playerController.updateController();
                this.ticks = 90;
                if (!ModuleManager.flashback.isToggled()) {
                    Wrapper.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    Wrapper.sendPacket(new CPacketHeldItemChange(current));
                    if (this.getHeldItem() == -1) {
                        this.helditem = null;
                    }
                    if (this.helditem != null) {
                        Wrapper.mc().playerController.windowClick(0, this.getHeldItem(), 0, ClickType.SWAP, Wrapper.getPlayer());
                        this.helditem = null;
                    }
                    this.isPotting = false;
                    this.delay = 9;
                }
            } else {
                this.helditem = Wrapper.getPlayer().getHeldItemMainhand();
                this.getPotFromInv();
            }
        }
    }

    public int getHeldItem() {
        int index = 9;
        while (index < 36) {
            ItemStack stack = Wrapper.getPlayer().inventoryContainer.getSlot(index).getStack();
            if (stack == this.helditem) {
                return index;
            }
            ++index;
        }
        return -1;
    }

    @EventTarget
    public void onPost(EventPostMotionUpdates event) {
        float saved = Wrapper.getPlayer().rotationPitch;
        if (ModuleManager.flashback.isToggled() && this.getTotalPots() > 0 && this.isHealing() && this.delay == 0 && this.isPotionOnHotBar()) {
            int current = Wrapper.getPlayer().inventory.currentItem;
            int meme = this.getPotionSlot();
            ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(this.getPotionSlot());
            Wrapper.sendPacket(new CPacketHeldItemChange(8));
            Wrapper.sendPacket(new CPacketHeldItemChange(this.getPotionSlot()));
            Wrapper.mc().playerController.updateController();
            Wrapper.mc().playerController.windowClick(0, this.getPotionSlot(), 0, ClickType.SWAP, Wrapper.getPlayer());
            Wrapper.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            Wrapper.sendPacket(new CPacketHeldItemChange(current));
            if (this.getHeldItem() == -1) {
                this.helditem = null;
            }
            if (this.helditem != null) {
                Wrapper.mc().playerController.windowClick(0, this.getHeldItem(), 0, ClickType.SWAP, Wrapper.getPlayer());
                this.helditem = null;
            }
            this.ticks = (int)saved;
            this.isPotting = false;
            this.delay = 9;
        }
        this.ticks = (int)saved;
    }

    @EventTarget
    public void onPacket(EventPacketSent event) {
        if (this.isHealing()) {
            this.getTotalPots();
        }
    }

    @Override
    public void onDisable() {
        this.delay = 0;
        this.isPotting = false;
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
                    Wrapper.mc().playerController.windowClick(0, index, 0, ClickType.SWAP, Wrapper.getPlayer());
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
        if (this.isToggled() && Wrapper.getPlayer().getHealth() <= NewPot.health.value.floatValue() && this.getTotalPots() > 0) {
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

