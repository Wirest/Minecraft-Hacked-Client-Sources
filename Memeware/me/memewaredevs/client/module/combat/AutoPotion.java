
package me.memewaredevs.client.module.combat;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.util.misc.Timer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;

import java.util.function.Consumer;

public class AutoPotion extends Module {
    private static boolean doThrow;
    int slot;
    private final Timer timer = new Timer();

    public AutoPotion() {
        super("Auto Potion", 0, Module.Category.COMBAT);
    }

    @Handler
    public Consumer<UpdateEvent> update = event -> {
        int j;
        ItemStack stack;
        if (mc.thePlayer.getHealth() >= 9.0 || mc.thePlayer.capabilities.isCreativeMode) {
            return;
        }
        if (event.isPost() && doThrow) {
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
            mc.thePlayer.sendQueue.addToSendQueue(
                    new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(slot)));
            mc.thePlayer.sendQueue
                    .addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            doThrow = false;
        }
        if (!timer.delay(500.0f)) {
            return;
        }
        slot = -1;
        boolean hasPots = false;
        for (j = 0; j < 9; ++j) {
            stack = mc.thePlayer.inventory.getStackInSlot(j);
            if (stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemPotion)
                    || !isValidPotion((ItemPotion) stack.getItem(), stack)) {
                continue;
            }
            hasPots = true;
        }
        if (!hasPots) {
            for (j = 9; j < 36; ++j) {
                stack = mc.thePlayer.inventoryContainer.getSlot(j).getStack();
                if (stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemPotion)
                        || !isValidPotion(stack)) {
                    continue;
                }
                mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, j, 1, 2,
                        mc.thePlayer);
                break;
            }
        }
        for (j = 0; j < 9; ++j) {
            stack = mc.thePlayer.inventory.getStackInSlot(j);
            if (stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemPotion)
                    || !isValidPotion((ItemPotion) stack.getItem(), stack)) {
                continue;
            }
            slot = j;
            break;
        }
        if (slot == -1) {
            return;
        }
        timer.reset();
        doThrow = true;
        event.setPitch(90.0f);
    };

    public boolean isValidPotion(final ItemPotion potion, final ItemStack stack) {
        if (ItemPotion.isSplash(stack.getItemDamage())) {
            for (final Object o : potion.getEffects(stack)) {
                if (!(o instanceof PotionEffect) || ((PotionEffect) o).getPotionID() != Potion.heal.id) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    public boolean isValidPotion(final ItemStack stack) {
        return isValidPotion((ItemPotion) stack.getItem(), stack);
    }

    public static boolean isPotting() {
        return doThrow;
    }

}
