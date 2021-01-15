package dev.astroclient.client.feature.impl.miscellaneous;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventType;
import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Mouse;

@Toggleable(label = "AutoTool", category = Category.MISC)
public class AutoTool extends ToggleableFeature {

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
        if (Mouse.isButtonDown(0) && eventMotion.getEventType() == EventType.PRE) {
            if (mc.objectMouseOver != null) {
                BlockPos blockPos = mc.objectMouseOver.getBlockPos();
                if (blockPos != null)
                    switchSlot(blockPos);
            }
        }
    }

    private void switchSlot(BlockPos blockPos) {
        Block block = mc.theWorld.getBlockState(blockPos).getBlock();

        float strength = 1.0F;
        int bestToolSlot = -1;

        for(int i = 36; i < 45; ++i) {
            ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getStrVsBlock(block) > strength) {
                strength = itemStack.getStrVsBlock(block);
                bestToolSlot = i - 36;
            }
        }

        if (bestToolSlot != -1)
            mc.thePlayer.inventory.currentItem = bestToolSlot;
    }

}
