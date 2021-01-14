
package me.memewaredevs.client.module.combat;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.util.misc.Timer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;

import java.util.function.Consumer;

public class AutoSoup extends Module {
    private final Timer time = new Timer();

    public AutoSoup() {
        super("Auto Soup", 0, Module.Category.COMBAT);
    }

    @Handler
    public Consumer<UpdateEvent> update = e -> {
        if (mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth() - 10.0f && time.delay(50.0f)) {
            if (hotbarHasSoups()) {
                useSoup();
            } else {
                getSoupFromInventory();
                time.reset();
            }
        }
    };

    private boolean hotbarHasSoups() {
        for (int index = 36; index < 45; ++index) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (itemStack == null || !(itemStack.getItem() instanceof ItemSoup)) {
                continue;
            }
            return true;
        }
        return false;
    }

    private void getSoupFromInventory() {
        int index;
        ItemStack itemStack;
        int item = -1;
        boolean found = false;
        for (index = 36; index >= 9; --index) {
            itemStack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (itemStack == null || !(itemStack.getItem() instanceof ItemSoup)) {
                continue;
            }
            item = index;
            found = true;
            break;
        }
        if (found) {
            for (index = 0; index < 45; ++index) {
                itemStack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                if (itemStack == null) {
                    continue;
                }
                if (itemStack.getItem() != Items.bowl || index < 36 || index > 44) {
                    break;
                }
                mc.playerController.windowClick(0, index, 0, 0, mc.thePlayer);
                mc.playerController.windowClick(0, -999, 0, 0, mc.thePlayer);
                break;
            }
            mc.playerController.windowClick(0, item, 0, 1, mc.thePlayer);
        }
    }

    private void useSoup() {
        int index;
        ItemStack itemStack;
        int item = -1;
        boolean found = false;
        for (index = 36; index < 45; ++index) {
            itemStack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (itemStack == null || !(itemStack.getItem() instanceof ItemSoup)) {
                continue;
            }
            item = index;
            found = true;
            break;
        }
        if (found) {
            for (index = 0; index < 45; ++index) {
                itemStack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                if (itemStack == null || itemStack.getItem() != Items.bowl || index < 36 || index > 44) {
                    continue;
                }
                mc.playerController.windowClick(0, index, 0, 0, mc.thePlayer);
                mc.playerController.windowClick(0, -999, 0, 0, mc.thePlayer);
            }
            final int slot = mc.thePlayer.inventory.currentItem;
            mc.thePlayer.inventory.currentItem = item - 36;
            mc.playerController.updateController();
            mc.thePlayer.sendQueue
                    .addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
            mc.thePlayer.sendQueue
                    .addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
                    C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            mc.thePlayer.stopUsingItem();
            mc.thePlayer.inventory.currentItem = slot;
        }
    }
}
