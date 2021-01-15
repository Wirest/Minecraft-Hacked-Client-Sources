package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.event.*;
import com.ihl.client.module.option.Option;
import com.ihl.client.module.option.ValueDouble;
import com.ihl.client.util.TimerUtil;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

@EventHandler(events = {EventPlayerMotion.class})
public class AutoPotion extends Module {

    private int holding;
    private double yaw, pitch;
    private TimerUtil dropTimer = new TimerUtil();
    private TimerUtil inventoryTimer = new TimerUtil();

    public AutoPotion(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        options.put("health", new Option("Health", "Health to drop potions below", new ValueDouble(5, new double[]{0, 10}, 0.5), Option.Type.NUMBER));
        options.put("delay", new Option("Delay", "Potion drop delay period (s)", new ValueDouble(0.5, new double[]{0, 2}, 0.01), Option.Type.NUMBER));
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    protected void tick() {
        super.tick();
        if (active) {
            if (inventoryTimer.isTime(0.15)) {
                int hotbarSlot = nextEmpty();
                if (hotbarSlot != -1) {
                    int inventorySlot = nextInventory();
                    if (inventorySlot != -1) {
                        Helper.controller().windowClick(Helper.player().inventoryContainer.windowId, inventorySlot, hotbarSlot - 36, 2, Helper.player());
                        inventoryTimer.reset();
                    }
                }
            }
        }
    }

    protected void onEvent(Event event) {
        double health = Option.get(options, "health").DOUBLE();
        double delay = Option.get(options, "delay").DOUBLE();

        if (event instanceof EventPlayerMotion) {
            EventPlayerMotion e = (EventPlayerMotion) event;
            int hotbarSlot = nextPotion();
            boolean drop = Helper.player().getHealth() / 2 < health && Helper.player().getHeldItem() != null && dropTimer.isTime(delay) && hotbarSlot != -1;
            if (e.type == Event.Type.PRE) {
                yaw = Helper.player().rotationYaw;
                pitch = Helper.player().rotationPitch;
                holding = Helper.player().inventory.currentItem;

                if (drop) {
                    Helper.player().rotationPitch = 90;
                    Helper.player().inventory.currentItem = hotbarSlot - 36;
                }

            } else if (e.type == Event.Type.POST) {
                if (drop) {
                    Helper.controller().sendUseItem(Helper.player(), Helper.world(), Helper.player().getHeldItem());
                    dropTimer.reset();
                }

                Helper.player().rotationYaw = (float) yaw;
                Helper.player().rotationPitch = (float) pitch;
                Helper.player().inventory.currentItem = holding;
            }
        }
    }

    private int nextInventory() {
        for (int i = 9; i <= 35; i++) {
            Slot slot = Helper.player().inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                ItemStack stack = slot.getStack();
                Item item = stack.getItem();
                if (item instanceof ItemPotion &&
                        ItemPotion.isSplash(stack.getMetadata())) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int nextPotion() {
        for (int i = 36; i <= 44; i++) {
            Slot slot = Helper.player().inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                ItemStack stack = slot.getStack();
                Item item = stack.getItem();
                if (item instanceof ItemPotion &&
                        (stack.getItemDamage() == 16421) || stack.getItemDamage() == 16453) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int nextEmpty() {
        for (int i = 36; i <= 44; i++) {
            Slot slot = Helper.player().inventoryContainer.getSlot(i);
            if (!slot.getHasStack()) {
                return i;
            }
        }
        return -1;
    }
}
