package com.etb.client.module.modules.combat;

import java.awt.Color;

import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.input.Mouse;

import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.Printer;
import com.etb.client.utils.TimerUtil;
import com.etb.client.utils.value.impl.BooleanValue;
import com.etb.client.utils.value.impl.NumberValue;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;

public class AutoApple extends Module {
    private boolean eatingApple;
    private int switched = -1;
    public static boolean doingStuff = false;
    private final TimerUtil timer = new TimerUtil();
    private final BooleanValue eatHeads = new BooleanValue("Eatheads", true);
    private final BooleanValue eatApples = new BooleanValue("Eatapples", true);
    private final NumberValue<Integer> health = new NumberValue<>("Health", 10, 1, 20, 1);
    private final NumberValue<Integer> delay = new NumberValue<>("Delay", 750, 100, 2000, 25);

    public AutoApple() {
        super("AutoApple", Category.COMBAT, new Color(255, 255, 0, 255).getRGB());
        setDescription("Automatically eats golden apples / heads when at low health");
        setRenderlabel("Auto Apple");
        addValues(health, delay, eatApples, eatHeads);
    }

    @Override
    public void onEnable() {
        this.eatingApple = doingStuff = false;
        switched = -1;
        timer.reset();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        doingStuff = false;
        if (eatingApple) {
            repairItemPress();
            repairItemSwitch();
        }
        super.onDisable();
    }

    private void repairItemPress() {
        if (mc.gameSettings != null) {
            final KeyBinding keyBindUseItem = mc.gameSettings.keyBindUseItem;
            if (keyBindUseItem != null) keyBindUseItem.pressed = false;
        }
    }


    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer == null) return;
        final InventoryPlayer inventory = mc.thePlayer.inventory;
        if (inventory == null) return;
        doingStuff = false;
        if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1)) {
            final KeyBinding useItem = mc.gameSettings.keyBindUseItem;

            if (!this.timer.reach(delay.getValue())) {
                eatingApple = false;
                repairItemPress();
                repairItemSwitch();
                return;
            }

            if (mc.thePlayer.capabilities.isCreativeMode || mc.thePlayer.isPotionActive(Potion.regeneration) ||mc.thePlayer.getHealth() >= health.getValue()) {
                this.timer.reset();
                if (eatingApple) {
                    eatingApple = false;
                    repairItemPress();
                    repairItemSwitch();
                }
                return;
            }

            for (int i = 0; i < 2; i++) {
                final boolean doEatHeads = i != 0;

                if (doEatHeads) {
                    if (!this.eatHeads.isEnabled()) continue;
                } else {
                    if (!this.eatApples.isEnabled()) {
                        eatingApple = false;
                        repairItemPress();
                        repairItemSwitch();
                        continue;
                    }
                }

                int slot;

                if (doEatHeads) {
                    slot = this.getItemFromHotbar(397);
                } else {
                    slot = this.getItemFromHotbar(322);
                }

                if (slot == -1) continue;

                final int tempSlot = inventory.currentItem;

                doingStuff = true;
                if (doEatHeads) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(inventory.getCurrentItem()));
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(tempSlot));
                    timer.reset();
                } else {
                    inventory.currentItem = slot;
                    useItem.pressed = true;
                    if (eatingApple) continue; // no message spam
                    eatingApple = true;
                    this.switched = tempSlot;
                }

                Printer.print(String.format("Automatically ate a %s", doEatHeads ? "player head" : "golden apple"));
            }
        }
    }

    private void repairItemSwitch() {
        final EntityPlayerSP p = mc.thePlayer;
        if (p == null) return;
        final InventoryPlayer inventory = p.inventory;
        if (inventory == null) return;
        int switched = this.switched;
        if (switched == -1) return;
        inventory.currentItem = switched;
        switched = -1;
        this.switched = switched;
    }

    private int getItemFromHotbar(final int id) {
        for (int i = 0; i < 9; i++) {
            if (mc.thePlayer.inventory.mainInventory[i] != null) {
                final ItemStack is = mc.thePlayer.inventory.mainInventory[i];
                final Item item = is.getItem();
                if (Item.getIdFromItem(item) == id) {
                    return i;
                }
            }
        }
        return -1;
    }
}
