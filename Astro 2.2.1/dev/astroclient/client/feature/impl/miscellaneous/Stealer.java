package dev.astroclient.client.feature.impl.miscellaneous;

import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.property.impl.number.Type;
import dev.astroclient.client.util.Timer;
import dev.astroclient.client.util.ItemUtil;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.*;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventType;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;

@Toggleable(label = "Stealer", category = Category.MISC)
public class Stealer extends ToggleableFeature {

    public NumberProperty<Integer> delay = new NumberProperty<>("Delay", true, 150, 1, 1, 250, Type.MILLISECONDS);

    public Timer timer = new Timer();

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
        if (mc.currentScreen instanceof GuiChest && eventMotion.getEventType() == EventType.PRE) {
            GuiChest chest = (GuiChest) mc.currentScreen;
            if (chest.getLowerChestInventory().getDisplayName().getUnformattedText().equalsIgnoreCase("Chest") || chest.getLowerChestInventory().getDisplayName().getUnformattedText().equalsIgnoreCase("LOW")) {
                if (isChestEmpty(chest) || isInventoryFull()) {
                    mc.thePlayer.closeScreen();
                    return;
                }
                for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
                    ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
                    if (stack != null && timer.hasReached(delay.getValue())) {
                        if (!ItemUtil.isTrash(stack)) {
                            mc.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, mc.thePlayer);
                            timer.reset();
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean isChestEmpty(final GuiChest chest) {
        for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
            ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
            if (stack != null)
                if (!ItemUtil.isTrash(stack))
                    return false;
        }
        return true;
    }


    private boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }
        return true;
    }
}
