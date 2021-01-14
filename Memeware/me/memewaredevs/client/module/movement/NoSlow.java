
package me.memewaredevs.client.module.movement;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.function.Consumer;

public class NoSlow extends Module {
    public NoSlow() {
        super("No Slowdown", 0, Module.Category.MOVEMENT);
        this.addModes("AAC", "NCP", "Velt");
    }

    @Handler
    public Consumer<UpdateEvent> eventConsumer0 = e -> {
        if (isMode("NCP")) {
            if (mc.thePlayer.isBlocking()) {
                if (e.isPre()) {
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
                            C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
                } else {
                    mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 71999999);
                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                }
            }
        }
    };

    public static int unusableItemSlot() {
        for (int j = 0; j < 8; ++j) {
            ItemStack stack = mc.thePlayer.inventory.mainInventory[j];
            if (stack != null && stack.getItemUseAction() == EnumAction.NONE) {
                return j;
            }
        }
        return -10;
    }
}
