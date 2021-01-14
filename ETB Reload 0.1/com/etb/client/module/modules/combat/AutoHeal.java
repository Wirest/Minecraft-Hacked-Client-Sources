package com.etb.client.module.modules.combat;

import java.awt.Color;

import net.minecraft.item.ItemSoup;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.greenrobot.eventbus.Subscribe;

import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.TimerUtil;
import com.etb.client.utils.value.impl.BooleanValue;
import com.etb.client.utils.value.impl.NumberValue;

import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class AutoHeal extends Module {
    private int slot = -1;
    private int currentslot = -1;
    public static boolean healing = false, doSoup;
    private final TimerUtil timer = new TimerUtil();
    private final BooleanValue potions = new BooleanValue("Potions", true);
    private final BooleanValue soup = new BooleanValue("Soup", true);
    private final BooleanValue jumpPot = new BooleanValue("Jump", false);
    private final BooleanValue regenpots = new BooleanValue("RegenPots", false);
    private final NumberValue<Integer> health = new NumberValue("Health", 10, 1, 20, 1);
    private final NumberValue<Integer> delay = new NumberValue("Delay", 5, 5, 20, 1);

    public AutoHeal() {
        super("AutoHeal", Category.COMBAT, new Color(0, 255, 255, 255).getRGB());
        setDescription("Automatically heals for you");
        setRenderlabel("Auto Heal");
        addValues(health, delay, potions, soup, regenpots, jumpPot);
    }

    @Override
    public void onDisable() {
        currentslot = -1;
        doSoup = false;
        healing = false;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (soup.isEnabled()) {
            int soupSlot = this.getSoupSlot();
            if (event.isPre()) {
                if (timer.sleep(delay.getValue()) && mc.thePlayer.getHealth() < health.getValue() && soupSlot != -1) {
                    doSoup = true;
                }
            } else if (doSoup) {
                doSoup = false;
                swap(soupSlot, 5);
                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(5));
                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ALL_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            }
        }
        int eatables = 0;
        if (potions.isEnabled()) {
            eatables += getPotionCount();
            if (regenpots.isEnabled()) eatables += getRegenCount();
        }
        String suf = "";
        StringBuilder suffix = new StringBuilder(suf);
        if (potions.isEnabled()) suffix.append(String.valueOf(eatables));
        if (potions.isEnabled() && soup.isEnabled()) suffix.append(" ");
        if (soup.isEnabled()) suffix.append(getCount());
        setSuffix(suffix.toString());
        if (eatables > 0) {
            if (event.isPre()) {
                update();
                if (!shouldHeal()) {
                    healing = false;
                    return;
                }
                if (slot == -1) {
                    healing = false;
                    return;
                }
                boolean up = jumpPot.getValue() && mc.thePlayer.onGround;
                if (mc.thePlayer.inventory.mainInventory[slot] != null && mc.thePlayer.inventory.mainInventory[slot].getItem() == Items.potionitem) {
                    if (up) mc.thePlayer.jump();
                    event.setPitch(up ? -90 : 90);
                }
                healing = true;
            } else {
                if (slot == -1) return;
                if (!healing) return;
                if (!shouldHeal()) return;
                if (mc.thePlayer.inventory.mainInventory[slot] == null) return;
                int packetSlot = slot;
                if (slot < 9) {
                    currentslot = mc.thePlayer.inventory.currentItem;
                    highlight(slot);
                    mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                    highlight(mc.thePlayer.inventory.currentItem);
                    highlight(currentslot);
                } else {
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(8));
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 8, 2, mc.thePlayer);
                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.mainInventory[slot]));
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, packetSlot, 8, 2, mc.thePlayer);
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                }
                timer.reset();
            }
        } else {
            healing = false;
        }
    }

    private void update() {
        for (int i = 0; i < 36; ++i) {
            if (mc.thePlayer != null && mc.theWorld != null && mc.thePlayer.inventory.mainInventory[i] != null) {
                final ItemStack is = mc.thePlayer.inventory.mainInventory[i];
                final Item item = is.getItem();
                if (item instanceof ItemPotion && potions.isEnabled()) {
                    final ItemPotion potion = (ItemPotion) item;
                    if (potion.getEffects(is) != null) {
                        for (final Object o : potion.getEffects(is)) {
                            final PotionEffect effect = (PotionEffect) o;
                            if (effect.getPotionID() == Potion.heal.id || (effect.getPotionID() == Potion.regeneration.id && regenpots.isEnabled() && !mc.thePlayer.isPotionActive(Potion.regeneration)) && ItemPotion.isSplash(is.getItemDamage())) {
                                slot = i;
                            }
                        }
                    }
                }
            }
        }
    }

    private void swap(int slot, int hotbarSlot) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarSlot, 2, mc.thePlayer);
    }

    private int getCount() {
        int counter = 0;

        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (item instanceof ItemSoup) {
                    ++counter;
                }
            }
        }

        return counter;
    }

    private int getSoupSlot() {
        int itemSlot = -1;

        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (item instanceof ItemSoup) {
                    itemSlot = i;
                }
            }
        }

        return itemSlot;
    }

    private int getPotionCount() {
        int count = 0;
        for (Slot s : mc.thePlayer.inventoryContainer.inventorySlots)
            if (s.getHasStack()) {

                ItemStack is = s.getStack();
                if ((is.getItem() instanceof ItemPotion)) {

                    ItemPotion ip = (ItemPotion) is.getItem();
                    if (ItemPotion.isSplash(is.getMetadata())) {

                        boolean hasHealing = false;
                        for (PotionEffect pe : ip.getEffects(is))
                            if (pe.getPotionID() == Potion.heal.getId()) {

                                hasHealing = true;
                                break;
                            }
                        if (hasHealing) {
                            count++;
                        }
                    }
                }
            }
        return count;
    }

    private int getRegenCount() {
        int count = 0;
        for (Slot s : mc.thePlayer.inventoryContainer.inventorySlots)
            if (s.getHasStack()) {

                ItemStack is = s.getStack();
                if ((is.getItem() instanceof ItemPotion)) {

                    ItemPotion ip = (ItemPotion) is.getItem();
                    if (ItemPotion.isSplash(is.getMetadata())) {

                        boolean hasHealing = false;
                        for (PotionEffect pe : ip.getEffects(is))
                            if (pe.getPotionID() == Potion.regeneration.getId()) {

                                hasHealing = true;
                                break;
                            }
                        if (hasHealing) {
                            count++;
                        }
                    }
                }
            }
        return count;
    }

    private void highlight(int slot) {
        mc.thePlayer.inventory.currentItem = slot;
        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
    }

    private boolean shouldHeal() {
        return mc.thePlayer.getHealth() <= health.getValue() && timer.reach(delay.getValue() * 50);
    }
}
